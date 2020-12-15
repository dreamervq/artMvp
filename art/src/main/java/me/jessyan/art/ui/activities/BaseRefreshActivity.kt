package com.example.mylibrary.mvp.uis.activities

import android.os.Bundle
import androidx.annotation.NonNull
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import me.jessyan.art.R
import me.jessyan.art.mvp.IPresenter

abstract class BaseRefreshActivity<P : IPresenter> : BaseSwipeBackActivity<P>(), OnRefreshListener {
    protected var smartRefreshLayout: SmartRefreshLayout? = null
    override fun initViews(savedInstanceState: Bundle?) {
        smartRefreshLayout = findViewById(R.id.pre_refresh)
        smartRefreshLayout?.setOnRefreshListener(this)
    }

    override fun onRefresh(@NonNull refreshLayout: RefreshLayout) {
        onRefresh()
    }

    fun autoRefresh() {
        smartRefreshLayout?.postDelayed({
            smartRefreshLayout?.autoRefresh()
            onRefresh()
        }, 100)
    }

    protected abstract fun onRefresh()
    protected fun refreshComplete() {
        smartRefreshLayout?.finishRefresh()
    }
}