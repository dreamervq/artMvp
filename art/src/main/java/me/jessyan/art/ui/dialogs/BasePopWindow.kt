package com.example.mylibrary.mvp.dialogs

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import me.jessyan.art.ui.util.PixelUtil

abstract class BasePopWindow : PopupWindow {
    protected lateinit var context: Context

    private lateinit var mPopView: View
    constructor(context: Context) {
        init(context)
    }

    private fun init(context: Context) {
        this.context = context
        val inflater = LayoutInflater.from(context) //绑定布局
        mPopView = inflater.inflate(getContentViewId(), null)
        contentView = mPopView
        softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
        isFocusable = true
        inputMethodMode = INPUT_METHOD_NEEDED
        isOutsideTouchable = true
        //设置宽与高
        val width: Int = PixelUtil.getScreenWidth(context) - PixelUtil.dp2px(24f)
        setWidth(width)
        val height: Int = PixelUtil.getScreenHeight(context) * 2 / 3
        setHeight(height)
    }


    protected abstract fun getContentViewId(): Int
}