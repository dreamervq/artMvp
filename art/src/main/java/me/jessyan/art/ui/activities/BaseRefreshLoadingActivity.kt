package com.example.mylibrary.mvp.uis.activities

import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration
import jp.wasabeef.recyclerview.adapters.AnimationAdapter
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter
import me.jessyan.art.R
import me.jessyan.art.mvp.IPresenter
import me.jessyan.art.ui.adapters.MultiItemTypeAdapter
import me.jessyan.art.ui.view.WrapContentLinearLayoutManager
import java.util.*

abstract class BaseRefreshLoadingActivity<T, P : IPresenter> : BaseSwipeBackActivity<P>(),
    OnRefreshListener, OnLoadMoreListener,
    MultiItemTypeAdapter.OnItemClickListener<T>, MultiItemTypeAdapter.OnItemLongClickListener<T> {
    protected val FIRST_PAGE = 1
    protected var mLayoutRefresh: SmartRefreshLayout? = null
    protected var mRecyclerView: RecyclerView? = null
    protected var mItems = ArrayList<T>()
    protected var mAdapter: RecyclerView.Adapter<*>? = null
    protected var mLayoutManager: RecyclerView.LayoutManager? = null
    protected var mCurrPage = FIRST_PAGE
    private var mLastItemClickTime: Long = 0
    override fun initViews(savedInstanceState: Bundle?) {
        mLayoutRefresh = findViewById(R.id.pre_refresh)
        mRecyclerView = findViewById(R.id.pre_recycler_view)
        setupRecyclerView()
        setupRefreshAndLoadMore()
    }

    protected fun setupRecyclerView() {
        mRecyclerView!!.layoutManager = initLayoutManager()
        mRecyclerView!!.adapter = initAdapter()
        if (isShowDivider) setDivider()
    }

    protected fun setDivider() {
        mRecyclerView!!.addItemDecoration(
            HorizontalDividerItemDecoration.Builder(this)
                .colorResId(R.color.black_divider)
                .sizeResId(R.dimen.spacing_divider)
                .build()
        )
    }

    protected val isShowDivider: Boolean
        protected get() = true

    protected fun initLayoutManager(): RecyclerView.LayoutManager? {
        mLayoutManager = layoutManager
        return mLayoutManager
    }

    protected val layoutManager: RecyclerView.LayoutManager
        protected get() = WrapContentLinearLayoutManager(this)

    protected fun initAdapter(): RecyclerView.Adapter<*>? {
        mAdapter = getAdapter()
        (mAdapter as MultiItemTypeAdapter<*>).setOnItemClickListener(this)
        (mAdapter as MultiItemTypeAdapter<*>).setOnItemLongClickListener(this)
        return dealAnimationAdapter(mAdapter)
    }

    protected abstract fun getAdapter(): MultiItemTypeAdapter<T>?

    protected fun dealAnimationAdapter(recyclerAdapterWithHF: RecyclerView.Adapter<*>?): RecyclerView.Adapter<*> {
        val adapter: AnimationAdapter = ScaleInAnimationAdapter(recyclerAdapterWithHF)
        adapter.setFirstOnly(true)
        adapter.setDuration(500)
        adapter.setInterpolator(OvershootInterpolator(.5f))
        return adapter
    }

    protected fun setupRefreshAndLoadMore() {
        if (isAutoRefresh) {
            autoRefresh()
        }
    }

    protected fun autoRefresh() {
        mRecyclerView!!.postDelayed({ mLayoutRefresh!!.autoRefresh() }, 100)
    }

    override fun onRefresh(@NonNull refreshLayout: RefreshLayout) {
        onRefresh()
    }

    fun onRefresh() {
        mCurrPage = FIRST_PAGE
        loadData(mCurrPage)
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        loadData(++mCurrPage)
    }

    protected open fun refreshComplete(loadSuccess: Boolean) {
        if (!loadSuccess && mCurrPage > FIRST_PAGE) {
            mCurrPage--
        }
        mAdapter!!.notifyDataSetChanged()
        mLayoutRefresh!!.finishRefresh()
        mLayoutRefresh!!.setEnableLoadMore(loadSuccess && mItems.size >= 15)
        if (mCurrPage > FIRST_PAGE) {
            mLayoutRefresh!!.finishLoadMore(true)
        }
    }

    protected fun refreshComplete(loadSuccess: Boolean, totalPage: Int) {
        if (!loadSuccess && mCurrPage > FIRST_PAGE) {
            mCurrPage--
        }
        mAdapter!!.notifyDataSetChanged()
        mLayoutRefresh!!.finishRefresh()
        mLayoutRefresh!!.setEnableLoadMore(loadSuccess && mCurrPage < totalPage)
        mLayoutRefresh!!.finishLoadMore(true)
    }

    override fun onItemClick(view: View, holder: RecyclerView.ViewHolder, item: T, position: Int) {
        // Logger.d(position)
    }

    protected abstract fun loadData(page: Int)
    val isAutoRefresh: Boolean
        get() = true

    override fun onItemLongClick(
        view: View,
        holder: RecyclerView.ViewHolder,
        item: T,
        position: Int
    ): Boolean {
        return false
    }

    fun beFastItemClick(): Boolean {
        val currentClickTime = System.currentTimeMillis()
        val flag = currentClickTime - mLastItemClickTime < 400L
        mLastItemClickTime = currentClickTime
        return flag
    }
}