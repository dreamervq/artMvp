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
        stateLayout?.let {
            it.setViewSwitchAnimProvider(FadeViewAnimProvider())
            it.setErrorAndEmptyAction { autoLoading() }
            it.showProgressView(getString(R.string.loading))
            it.postDelayed({ onRefresh() }, 200)
        }

    }

    protected fun autoLoading() {
        stateLayout!!.showProgressView(getString(R.string.loading))
        onRefresh()
    }

    protected fun loadingComplete(state: Int) {
        stateLayout?.let {
            when (state) {
                STATE_CONTENT -> it.showContentView()
                STATE_EMPTY -> it.showEmptyView(
                    emptyTip
                )
                STATE_PROGRESS -> it.showProgressView(
                    getString(R.string.loading)
                )
                STATE_ERROR -> it.showErrorView(
                    getString(R.string.error)
                )
            }
        }

        refreshComplete()
    }

    protected fun showState(state: Int) {
        stateLayout?.let {
            when (state) {
                STATE_CONTENT -> it.showContentView()
                STATE_EMPTY -> it.showEmptyView(
                    emptyTip
                )
                STATE_PROGRESS -> it.showProgressView(
                    getString(R.string.loading)
                )
                STATE_ERROR -> it.showErrorView(
                    getString(R.string.error)
                )
            }
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