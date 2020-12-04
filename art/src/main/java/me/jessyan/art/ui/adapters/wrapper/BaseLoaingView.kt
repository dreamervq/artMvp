package com.example.mylibrary.mvp.uis.adapters.wrapper

import android.content.Context
import android.util.AttributeSet
import android.view.View

abstract class BaseLoaingView : View, BaseLoading {
    private val wrapperState = WrapperState.LOADING

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    fun startLoading() {
        showLoaing()
    }

    fun showFaild() {
        showFailed()
    }

    fun showNoData() {
        showEmpty()
    }
}