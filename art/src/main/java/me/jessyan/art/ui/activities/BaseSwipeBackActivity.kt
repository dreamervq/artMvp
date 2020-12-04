package com.example.mylibrary.mvp.uis.activities

import android.os.Bundle
import me.jessyan.art.mvp.IPresenter
import me.jessyan.art.ui.util.Utils
import me.jessyan.art.ui.util.swipe.SwipeBackLayout
import me.jessyan.art.ui.util.swipe.app.SwipeBackActivityBase
import me.jessyan.art.ui.util.swipe.app.SwipeBackActivityHelper

abstract class BaseSwipeBackActivity<P : IPresenter> : BaseHeaderActivity<P>(),
    SwipeBackActivityBase {
    protected lateinit var mHelper: SwipeBackActivityHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mHelper = SwipeBackActivityHelper(this)
        mHelper.onActivityCreate()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        mHelper.onPostCreate()
    }

    override fun getSwipeBackLayout(): SwipeBackLayout {
        return mHelper.swipeBackLayout
    }

    override fun setSwipeBackEnable(enable: Boolean) {
        swipeBackLayout.setEnableGesture(enable)
    }

    override fun scrollToFinishActivity() {
        Utils.convertActivityToTranslucent(this)
        swipeBackLayout.scrollToFinishActivity()
    }

    override fun onBackPressed() {
        scrollToFinishActivity()
    }
}