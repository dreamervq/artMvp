package com.example.mylibrary.mvp.uis.activities

import android.os.Bundle
import android.view.View
import android.widget.TextView
import me.jessyan.art.R
import me.jessyan.art.mvp.IPresenter
import me.jessyan.art.ui.events.FinishToolEvent
import org.simple.eventbus.EventBus

/**
 * activity必须包含af_layout_header.xml文件,或者使用id:pre_tv_title和pre_v_back
 */
abstract class BaseHeaderActivity<P : IPresenter> : BaseActivity<P>(), View.OnClickListener {
    protected var tvTitle: TextView? = null
    protected var vBack: View? = null
    protected var vClose: View? = null
    override fun init(savedInstanceState: Bundle?) {
        initHeader()
        initViews(savedInstanceState)
    }

    protected fun initHeader() {
        try {
            tvTitle = findViewById(R.id.pre_tv_title)
            tvTitle?.text = getTitleText()
            tvTitle?.setOnClickListener(this)
        } catch (e: Exception) {
//            Logger.w("未设置标题id~")
        }
        try {
            vClose = findViewById(R.id.pre_v_close)
            vClose?.setOnClickListener(this)
        } catch (e: Exception) {
//            Logger.w("未设置返回图标id~")
        }
        try {
            vBack = findViewById(R.id.pre_v_back)
            vBack?.setOnClickListener(this)
        } catch (e: Exception) {
//            Logger.w("未设置返回图标id~")
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.pre_tv_title -> {
                onTitleClicked(view)
            }
            R.id.pre_v_close -> {
                EventBus.getDefault().post(FinishToolEvent())
            }
            R.id.pre_v_back -> {
                onBackPressed()
            }
            else -> {
            }
        }
    }

    override fun initData(savedInstanceState: Bundle?) {
    }

    protected abstract fun getTitleText(): String?
    protected fun onTitleClicked(view: View?) {}
    protected abstract fun initViews(savedInstanceState: Bundle?)
}