package me.jessyan.art.ui.activities

import android.os.Bundle
import com.example.mylibrary.mvp.uis.activities.BaseRefreshActivity
import me.jessyan.art.R
import me.jessyan.art.mvp.IPresenter
import me.jessyan.art.ui.view.StateLayout
import me.jessyan.art.ui.view.emptyprovider.FadeViewAnimProvider

abstract class BaseStateRefreshingActivity<P:IPresenter> : BaseRefreshActivity<P>() {
    protected var stateLayout: StateLayout? = null
    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        stateLayout = findViewById(R.id.stateLayout)
        initStateLayout()
    }

    private fun initStateLayout() {
        stateLayout!!.setViewSwitchAnimProvider(FadeViewAnimProvider())
        stateLayout!!.setErrorAndEmptyAction { autoLoading() }
        stateLayout!!.showProgressView(getString(R.string.loading))
        stateLayout!!.postDelayed({ onRefresh() }, 200)
    }

    protected fun autoLoading() {
        stateLayout!!.showProgressView(getString(R.string.loading))
        onRefresh()
    }

    protected fun loadingComplete(state: Int) {
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
        refreshComplete()
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