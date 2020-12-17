package com.example.mylibrary.mvp.uis.fragment

import me.jessyan.art.R
import me.jessyan.art.mvp.IPresenter
import me.jessyan.art.ui.view.StateLayout
import me.jessyan.art.ui.view.emptyprovider.FadeViewAnimProvider
import java.lang.NullPointerException

abstract class BaseStateLoadingFragment<P : IPresenter> : BaseNewFragment<P>() {
    protected var stateLayout: StateLayout? = null
    override fun initData() {
        super.initData()
        stateLayout = getView<StateLayout>(R.id.stateLayout)
        if (stateLayout==null)
            throw NullPointerException("missing key views")
        initStateLayout()
    }

    protected fun initStateLayout() {
        stateLayout?.let {
            it.setViewSwitchAnimProvider(FadeViewAnimProvider())
            it.setErrorAndEmptyAction { autoLoading() }
            it.showProgressView("数据加载中")
            it.postDelayed({ loadData() }, 200)
        }
    }

    protected fun autoLoading() {
        stateLayout!!.showProgressView("数据加载中")
        loadData()
    }

    abstract fun loadData()
    protected fun loadingComplete(state: Int) {
        when (state) {
            STATE_CONTENT -> stateLayout!!.showContentView()
            STATE_EMPTY -> stateLayout!!.showEmptyView(emptyTip)
            STATE_PROGRESS -> stateLayout!!.showProgressView("数据加载中")
            STATE_ERROR -> stateLayout!!.showErrorView("加载失败")
        }
    }

    protected val emptyTip: String
        protected get() = "暂无数据"

    companion object {
        protected const val STATE_CONTENT = 0
        protected const val STATE_EMPTY = 1
        protected const val STATE_PROGRESS = 2
        protected const val STATE_ERROR = 3
    }
}