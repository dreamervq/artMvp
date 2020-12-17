package com.example.mylibrary.mvp.uis.fragment

import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import me.jessyan.art.R
import me.jessyan.art.mvp.IPresenter

abstract class BaseRefreshFragment<P : IPresenter> : BaseNewFragment<P>(),
    OnRefreshListener {
    protected var smartRefreshLayout: SmartRefreshLayout? = null
    override fun initData() {
        super.initData()
        smartRefreshLayout = getView(R.id.pre_refresh)
        if (smartRefreshLayout == null)
            throw NullPointerException("missing key views")
        smartRefreshLayout?.setOnRefreshListener(this)
        smartRefreshLayout?.setEnableLoadMore(false)
    }

    fun autoRefresh() {
        smartRefreshLayout?.postDelayed({
            smartRefreshLayout?.autoRefresh()
            onRefresh()
        }, 100)
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        onRefresh()
    }

    abstract fun onRefresh()
    protected fun refreshComplete() {
        smartRefreshLayout?.finishRefresh()
    }
}