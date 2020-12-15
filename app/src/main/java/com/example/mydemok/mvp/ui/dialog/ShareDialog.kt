package com.example.mydemok.mvp.ui.dialog

import android.content.Context
import android.view.View
import com.example.mydemok.R
import com.example.mydemok.mvp.ui.listener.OnPlatformClickListener
import com.example.mylibrary.mvp.dialogs.BaseDialog
import kotlinx.android.synthetic.main.dialog_share.*
import kotlinx.android.synthetic.main.layout_share_header.view.*
import me.jessyan.art.base.Platform

class ShareDialog(context: Context, mListener: OnPlatformClickListener? = null) :
    BaseDialog(context) {
    private var mListener: OnPlatformClickListener? = null

    override fun initView() {
        ll_head.ll_wx_line.setOnClickListener(this::onPlatFormClick)
        ll_head.ll_wx.setOnClickListener(this::onPlatFormClick)
        ll_head.ll_sina.setOnClickListener(this::onPlatFormClick)
        ll_head.ll_qq.setOnClickListener(this::onPlatFormClick)
        ll_head.ll_qzone.setOnClickListener(this::onPlatFormClick)
        ll_copy.setOnClickListener(this::onPlatFormClick)
        ll_sport.setOnClickListener(this::onPlatFormClick)
        tv_close.setOnClickListener(this::onPlatFormClick)
    }

    override fun getLayoutRes(): Int {
        return R.layout.dialog_share
    }

    private fun onPlatFormClick(view: View) {
        mListener?.let {
            when (view) {
                ll_head.ll_wx_line -> {
                    it.onPlatformClick(Platform.WECHAT_TIMELINE)
                }
                ll_head.ll_wx -> {
                    it.onPlatformClick(Platform.WECHAT)
                }
                ll_head.ll_sina -> {
                    it.onPlatformClick(Platform.WEIBO)
                }
                ll_head.ll_qq -> {
                    it.onPlatformClick(Platform.QQ)
                }
                ll_head.ll_qzone -> {
                    it.onPlatformClick(Platform.QZONE)
                }
                ll_copy -> {
                    it.onPlatformClick(Platform.COPY)
                }
                ll_sport -> {
                    it.onPlatformClick(Platform.HAIBAO)
                }
                else -> {
                }
            }
        }
        dismiss()
    }

    fun setOnPlatformClickListener(mListener: OnPlatformClickListener) {
        this.mListener = mListener
    }

    init {
        this.mListener = mListener
    }
}