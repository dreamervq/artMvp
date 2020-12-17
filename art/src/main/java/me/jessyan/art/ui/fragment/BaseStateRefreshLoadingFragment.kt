package com.example.mylibrary.mvp.uis.fragment

import me.jessyan.art.R
import me.jessyan.art.mvp.IPresenter
import me.jessyan.art.ui.view.StateLayout
import me.jessyan.art.ui.view.emptyprovider.FadeViewAnimProvider

abstract class BaseStateRefreshLoadingFragment<T, P : IPresenter> :
    BaseRefreshLoadingFragment<T, P>() {
    protected var stateLayout: StateLayout? = null
    override fun initData() {
        super.initData()
        stateLayout = getView<StateLayout>(R.id.stateLayout)
        if (stateLayout == null)
            throw NullPointerException("missing key views")
        initStateLayout()
    }

    protected fun initStateLayout() {
        stateLayout?.let {
            it.setViewSwitchAnimProvider(FadeViewAnimProvider())
            it.setErrorAndEmptyAction { autoLoading() }
            it.showProgressView(getString(R.string.loading))
        }
    }

    fun autoLoading() {
        stateLayout!!.showProgressView(getString(R.string.loading))
        onRefresh()
    }

    override fun refreshComplete(loadSuccess: Boolean) {
        initState(loadSuccess)
        super.refreshComplete(loadSuccess)
    }

    protected fun loadingComplete(loadSuccess: Boolean, more: Boolean) {
        initState(loadSuccess)
        super.refreshComplete(loadSuccess, more)
    }

    protected fun loadingComplete(loadSuccess: Boolean) {
        initState(loadSuccess)
        super.refreshComplete(loadSuccess, false)
    }

    protected fun initState(loadSuccess: Boolean) {
        if (loadSuccess) {
            if (mCurrPage == FIRST_PAGE && mItems.size == 0) {
                stateLayout!!.showEmptyView(emptyTip)
            } else {
                stateLayout!!.showContentView()
            }
        } else {
            if (mCurrPage == FIRST_PAGE) {
                stateLayout!!.showErrorView(getString(R.string.error))
            }
        }
    }

    protected fun showState(state: Int) {
        when (state) {
            STATE_CONTENT -> stateLayout!!.showContentView()
            STATE_EMPTY -> stateLayout!!.showEmptyView(
                emptyTip
            )
            STATE_PROGRESS -> stateLayout!!.showProgressView(
                getString(R.string.loading)
            )
            STATE_ERROR -> stateLayout!!.showErrorView(
                getString(R.string.error)
            )
        }
    }

    protected val emptyTip: String
        protected get() = getString(R.string.empty)

    companion object {
        protected const val STATE_CONTENT = 0
        protected const val STATE_EMPTY = 1
        protected const val STATE_PROGRESS = 2
        protected const val STATE_ERROR = 3
    }
}