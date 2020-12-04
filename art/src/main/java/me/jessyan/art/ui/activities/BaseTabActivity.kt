package com.example.mylibrary.mvp.uis.activities

import android.os.Bundle
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.example.mylibrary.listeners.ITabContent
import com.example.mylibrary.listeners.ITabPager
import me.jessyan.art.R
import me.jessyan.art.mvp.IPresenter
import me.jessyan.art.ui.adapters.TabPagerAdapter
import me.jessyan.art.ui.view.PagerSlidingTabStrip
import java.util.*

abstract class BaseTabActivity<T : ITabPager?, P : IPresenter> : BaseSwipeBackActivity<P>(),
    OnPageChangeListener, ITabContent {
    protected var mTabLayout: PagerSlidingTabStrip? = null
    protected var mPager: ViewPager? = null
    protected var mItems: List<T>? = ArrayList()
    protected var mAdapter: PagerAdapter? = null
    protected var selectedIndex = -1

    override fun initViews(savedInstanceState: Bundle?) {
        mTabLayout = findViewById(R.id.pre_tab_layout)
        mPager = findViewById(R.id.pre_pager)
        initTabView()
        getData()
    }

    protected fun initTabView() {
//        mTabLayout.setTextColor(getTabTextColor());
//        mTabLayout.setSelectedTextColorResource(getSelectedTabTextColor());
//        mTabLayout.setIndicatorColorResource(getSelectedTabTextColor());
//        mTabLayout.setIndicatorHeight(ScreenUtil.dp2px(2));
//        mTabLayout.setUnderlineColor(getResources().getColor(R.color.black_divider));
//        mTabLayout.setUnderlineHeight(ScreenUtil.dp2px(getUnderLineHeight()));
//        mTabLayout.setDrawDivider(isDrawDivider());
    }

    protected val underLineHeight: Float
        protected get() = 0f

    protected fun initPager() {
        if (mItems == null || mItems!!.size == 0) {
            return
        }
        mAdapter = pagerAdapter
        mPager!!.adapter = mAdapter
        mTabLayout!!.tabAddWay = itemAddWay
        mTabLayout!!.setViewPager(mPager)
        mTabLayout!!.setOnPageChangeListener(this)
    }

    protected val pagerAdapter: PagerAdapter
        protected get() = TabPagerAdapter(supportFragmentManager, mItems, this)

    protected val isDrawDivider: Boolean
        protected get() = false

    protected val selectedTabTextColor: Int
        protected get() = R.color.colorAccent

    protected val tabTextColor: Int
        protected get() = R.color.black_normal

    //        if (mItems.size() <= 4) {
    //        }
//        return PagerSlidingTabStrip.TabAddWay.ITEM_WARP;
    protected val itemAddWay: PagerSlidingTabStrip.TabAddWay
        protected get() =//        if (mItems.size() <= 4) {
            PagerSlidingTabStrip.TabAddWay.ITEM_MATCH
    //        }
//        return PagerSlidingTabStrip.TabAddWay.ITEM_WARP;

    override fun onPageScrolled(
        position: Int,
        positionOffset: Float,
        positionOffsetPixels: Int
    ) {
    }

    override fun onPageScrollStateChanged(state: Int) {}
    override fun onPageSelected(position: Int) {
        selectedIndex = position
    }

    /**
     * 获取完数据后回调设置pager
     */
    protected abstract fun getData(): Unit
}