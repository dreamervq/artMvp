package com.example.mylibrary.mvp.uis.activities

import android.os.Bundle
import me.jessyan.art.R
import me.jessyan.art.mvp.IPresenter
import me.jessyan.art.ui.view.StateLayout
import me.jessyan.art.ui.view.emptyprovider.FadeViewAnimProvider

abstract class BaseStateLoadingActivity<P : IPresenter> : BaseSwipeBackActivity<P>() {
    protected var stateLayout: StateLayout? = null
    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        stateLayout = findViewById(R.id.stateLayout)
        initStateLayout()
    }

    protected fun initStateLayout() {
        stateLayout!!.setViewSwitchAnimProvider(FadeViewAnimProvider())
        stateLayout!!.setErrorAndEmptyAction { autoLoad() }
        stateLayout!!.showProgressView(getString(R.string.loading))
        stateLayout!!.postDelayed({ loadData() }, 200)
    }

    fun autoLoad() {
        stateLayout!!.showProgressView(getString(R.string.loading))
        loadData()
    }

    protected abstract fun loadData()
    protected fun loadingComplete(state: Int) {
        when (state) {
            STATE_CONTENT -> stateLayout!!.showContentView()
            STATE_EMPTY -> stateLayout!!.showEmptyView(emptyTip)
            STATE_PROGRESS -> stateLayout!!.showProgressView(
                getString(R.string.loading)
            )
            STATE_ERROR -> stateLayout!!.showErrorView(getString(R.string.error))
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