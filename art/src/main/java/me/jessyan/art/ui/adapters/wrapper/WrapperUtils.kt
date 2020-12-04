package com.example.mylibrary.mvp.uis.adapters.wrapper

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

object WrapperUtils {
    fun onAttachedToRecyclerView(
        innerAdapter: RecyclerView.Adapter<*>,
        recyclerView: RecyclerView,
        callback: SpanSizeCallback
    ) {
        innerAdapter.onAttachedToRecyclerView(recyclerView)
        val layoutManager = recyclerView.layoutManager
        if (layoutManager is GridLayoutManager) {
            val gridLayoutManager = layoutManager
            val spanSizeLookup = gridLayoutManager.spanSizeLookup
            gridLayoutManager.spanSizeLookup = object : SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return callback.getSpanSize(gridLayoutManager, spanSizeLookup, position)
                }
            }
            gridLayoutManager.spanCount = gridLayoutManager.spanCount
        }
    }

    fun setFullSpan(holder: RecyclerView.ViewHolder) {
        val lp = holder.itemView.layoutParams
        if (lp != null
            && lp is StaggeredGridLayoutManager.LayoutParams
        ) {
            lp.isFullSpan = true
        }
    }

    interface SpanSizeCallback {
        fun getSpanSize(
            layoutManager: GridLayoutManager?,
            oldLookup: SpanSizeLookup?,
            position: Int
        ): Int
    }
}