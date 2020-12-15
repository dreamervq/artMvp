package com.example.mylibrary.mvp.uis.activities

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.LinearLayout
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.SimpleOnPageChangeListener
import com.example.mylibrary.listeners.ITabContent
import com.example.mylibrary.listeners.ITabPager
import com.scwang.smart.refresh.layout.util.SmartUtil.dp2px
import me.jessyan.art.R
import me.jessyan.art.mvp.IPresenter
import me.jessyan.art.ui.adapters.TabPagerAdapter
import net.lucode.hackware.magicindicator.FragmentContainerHelper
import net.lucode.hackware.magicindicator.MagicIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView
import java.util.*

abstract class BaseMagicTabActivity<T : ITabPager?,P:IPresenter> : BaseSwipeBackActivity<P>(),ITabContent {
    protected var magicIndicator: MagicIndicator? = null
    protected var mPager: ViewPager? = null
    protected var mItems: List<T>? = ArrayList()
    protected var mAdapter: PagerAdapter? = null
    protected var commonNavigator: CommonNavigator? = null
    override fun initViews(savedInstanceState: Bundle?) {
        magicIndicator = findViewById(R.id.pre_tab_layout)
        mPager = findViewById(R.id.pre_pager)
        getData()
    }

    protected fun initPager() {
        if (mItems == null || mItems?.size == 0) {
            return
        }
        mAdapter = pagerAdapter
        mPager?.adapter = mAdapter
        commonNavigator = CommonNavigator(this)
        val navigatorAdapter: CommonNavigatorAdapter = object : CommonNavigatorAdapter() {
            override fun getCount(): Int {
                return mItems?.size ?: 0
            }

            override fun getTitleView(
                context: Context,
                index: Int
            ): IPagerTitleView {
                val currentPos = mPager?.currentItem
                val simplePagerTitleView: SimplePagerTitleView = ColorTransitionPagerTitleView(context)
                setTabTextColor(simplePagerTitleView)
                simplePagerTitleView.text = mItems?.get(index)?.title
                if (index == currentPos) {
                    setSelectedTextSize(simplePagerTitleView)
                } else {
                    setNormalTextSize(simplePagerTitleView)
                }
                simplePagerTitleView.setOnClickListener { v: View? ->
                    mPager?.currentItem = index
                }
                simplePagerTitleView.setPadding(dp2px(12f), 0, dp2px(12f), 0)
                return simplePagerTitleView
            }

            override fun getIndicator(context: Context): IPagerIndicator {
                return getMyIndicator(context)
            }

            override fun getTitleWeight(context: Context, index: Int): Float {
                return 1f
            }
        }
        commonNavigator?.adapter = navigatorAdapter
        magicIndicator?.navigator = commonNavigator
        val titleContainer =
            commonNavigator?.titleContainer // must after setNavigator
        titleContainer?.showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
        val fragmentContainerHelper = FragmentContainerHelper(magicIndicator)
        fragmentContainerHelper.setInterpolator(OvershootInterpolator(2.0f))
        fragmentContainerHelper.setDuration(300)
        mPager?.addOnPageChangeListener(object : SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                fragmentContainerHelper.handlePageSelected(position)
                navigatorAdapter.notifyDataSetChanged()
            }
        })
    }

    protected fun getMyIndicator(context: Context?): IPagerIndicator {
        val linePagerIndicator = LinePagerIndicator(context)
        linePagerIndicator.mode = LinePagerIndicator.MODE_EXACTLY
        linePagerIndicator.lineWidth = dp2px(36f).toFloat()
        linePagerIndicator.setColors(resources.getColor(R.color.black))
        linePagerIndicator.lineHeight = dp2px(4f).toFloat()
        linePagerIndicator.roundRadius = dp2px(2f).toFloat()
        return linePagerIndicator
    }

    protected val pagerAdapter: PagerAdapter
        protected get() = TabPagerAdapter(supportFragmentManager, mItems, this)

    protected fun setTabTextColor(simplePagerTitleView: SimplePagerTitleView) {
        simplePagerTitleView.normalColor = -0xddddde
        simplePagerTitleView.selectedColor = -0xddddde
    }

    protected fun setSelectedTextSize(simplePagerTitleView: SimplePagerTitleView) {
        simplePagerTitleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f)
        simplePagerTitleView.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
    }

    protected fun setNormalTextSize(simplePagerTitleView: SimplePagerTitleView) {
        simplePagerTitleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        simplePagerTitleView.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
    }

    /**
     * 获取完数据后回调设置pager
     */
    protected abstract fun getData(): Unit
}