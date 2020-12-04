package com.example.mylibrary.mvp.uis.fragment

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

abstract class BaseTabFragment<T : ITabPager?, P : IPresenter> : BasesFragment<P>(),OnPageChangeListener,ITabContent {
    protected var mTabLayout: PagerSlidingTabStrip? = null
    protected var mPager: ViewPager? = null
    protected var mItems: List<T>? = ArrayList()
    protected var mAdapter: PagerAdapter? = null
    protected var selectedIndex = -1
    override fun initData() {
        super.initData()
        mTabLayout = getView<PagerSlidingTabStrip>(R.id.pre_tab_layout)
        mPager = getView<ViewPager>(R.id.pre_pager)
        initTabView()
        data
    }

    protected fun initTabView() {
//        mTabLayout.setTextColor(getTabTextColor());
//        mTabLayout.setSelectedTextColorResource(getSelectedTabTextColor());
//        mTabLayout.setIndicatorColorResource(getSelectedTabTextColor());
//        mTabLayout.setIndicatorHeight(PixelUtil.dp2px(2));
//        mTabLayout.setUnderlineColor(getResources().getColor(R.color.black_divider));
//        mTabLayout.setUnderlineHeight(PixelUtil.dp2px(getUnderLineHeight()));
//        mTabLayout.setDrawDivider(isDrawDivider());
//        mTabLayout.setTabAddWay(getItemAddWay());
    }

    protected val underLineHeight: Float
        protected get() = 1f

    protected fun initPager() {
        if (mItems == null || mItems!!.size == 0) {
            return
        }
        mAdapter = pagerAdapter
        mPager?.adapter = mAdapter
        mPager?.offscreenPageLimit = mItems?.size ?: 0
        mTabLayout?.tabAddWay = itemAddWay
        mTabLayout?.setViewPager(mPager)
        mTabLayout?.setOnPageChangeListener(this)
    }

    protected val pagerAdapter: PagerAdapter
        protected get() = TabPagerAdapter(childFragmentManager, mItems, this)

    protected val isDrawDivider: Boolean
        protected get() = false

    protected val selectedTabTextColor: Int
        protected get() = R.color.colorPrimary

    protected val tabTextColor: Int
        protected get() = R.color.black_normal

    protected val itemAddWay: PagerSlidingTabStrip.TabAddWay
        protected get() = PagerSlidingTabStrip.TabAddWay.ITEM_WARP

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
    protected abstract val data: Unit
}