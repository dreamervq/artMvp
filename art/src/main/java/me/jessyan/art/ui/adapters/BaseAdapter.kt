package me.jessyan.art.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import com.example.mylibrary.mvp.uis.adapters.holders.CommonHolder

abstract class BaseAdapter<T>(
    context: Context,
    layoutId: Int,
    datas: List<T>
) : MultiItemTypeAdapter<T>(context, datas) {
    protected var mLayoutId: Int
    protected var mInflater: LayoutInflater
    protected abstract fun convert(holder: CommonHolder?, item: T, position: Int)

    init {
        mContext = context
        mInflater = LayoutInflater.from(context)
        mLayoutId = layoutId
        mItems = datas
        addItemViewDelegate(object : ItemViewDelegate<T> {
            override fun getItemViewLayoutId(): Int {
                return layoutId
            }

            override fun isForViewType(item: T, position: Int): Boolean {
                return true
            }

            override fun convert(holder: CommonHolder, t: T, position: Int) {
                this@BaseAdapter.convert(holder, t, position)
            }
        })
    }
}