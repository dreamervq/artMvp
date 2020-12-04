package com.example.mylibrary.mvp.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import me.jessyan.art.R

abstract class BaseDialog : Dialog {
    protected var mContext: Context? = null
    private var mLastButterKnifeClickTime: Long = 0

    constructor(context: Context) : super(context, R.style.PXDialog) {
        init(context)
    }

    constructor(context: Context, defStyle: Int) : super(context, defStyle) {
        init(context)
    }

    private fun init(context: Context) {
        mContext = context
        setContentView(layoutRes)
        val window = window
        if (window != null) {
            window.setGravity(Gravity.BOTTOM)
            val layoutParams = window.attributes
            layoutParams.width =
                if (isWithWrapContent()) ViewGroup.LayoutParams.WRAP_CONTENT else ViewGroup.LayoutParams.MATCH_PARENT
            val d = setWithPercent()
            if (d > 0) {
                layoutParams.width =
                    (getContext().resources.displayMetrics.widthPixels * d).toInt()
            }
            layoutParams.height =
                if (isHeightWrapContent()) ViewGroup.LayoutParams.WRAP_CONTENT else ViewGroup.LayoutParams.MATCH_PARENT
            val h = setHeightPercent()
            if (h > 0) {
                layoutParams.height =
                    (getContext().resources.displayMetrics.heightPixels * h).toInt()
            }
            if (isGravityRight()) {
                layoutParams.gravity = Gravity.RIGHT
            } else {
                layoutParams.gravity = if (isGravityCenter()) Gravity.CENTER else Gravity.BOTTOM
            }
            window.attributes = layoutParams
        }
    }

    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        initView()
    }

    fun beFastClick(): Boolean {
        val currentClickTime = System.currentTimeMillis()
        val flag = currentClickTime - mLastButterKnifeClickTime < 400L
        mLastButterKnifeClickTime = currentClickTime
        return flag
    }

    abstract fun initView()
    abstract val layoutRes: Int
    fun isGravityCenter(): Boolean {
        return false
    }

    fun isWithWrapContent(): Boolean {
        return false
    }

    fun isGravityRight(): Boolean {
        return false
    }

    fun setWithPercent(): Double {
        return -0.1
    }

    fun setHeightPercent(): Double {
        return -0.1
    }

    fun isHeightWrapContent(): Boolean {
        return true
    }
}