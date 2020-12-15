package com.example.mydemok.mvp.ui.dialog

import android.content.Context
import android.graphics.Bitmap
import android.view.View
import com.example.mydemok.R
import com.example.mydemok.application.Constants
import com.example.mydemok.mvp.ui.listener.OnPlatformClickListener
import com.example.mydemok.utils.ImageUtils
import com.example.mydemok.utils.QRCodeUtil
import com.example.mylibrary.mvp.dialogs.BaseDialog
import kotlinx.android.synthetic.main.dialog_share_poster.*
import kotlinx.android.synthetic.main.layout_share_header.view.*
import me.jessyan.art.base.Platform
import me.jessyan.art.ui.util.GlideUtils
import me.jessyan.art.ui.util.PixelUtil
import java.io.File

class PosterShareDialog(context: Context, mListener: OnPlatformClickListener? = null) :
    BaseDialog(context, R.style.dialog_match_parent) {
    private var mListener: OnPlatformClickListener? = null
    private var file: File? = null
    override fun initView() {
        ll_selector.ll_wx_line.setOnClickListener(this::onPlatFormClick)
        ll_selector.ll_wx.setOnClickListener(this::onPlatFormClick)
        ll_selector.ll_sina.setOnClickListener(this::onPlatFormClick)
        ll_selector.ll_qq.setOnClickListener(this::onPlatFormClick)
        ll_selector.ll_qzone.setOnClickListener(this::onPlatFormClick)
        tv_close.setOnClickListener(this::onPlatFormClick)
    }

    override fun getLayoutRes(): Int {
        return R.layout.dialog_share_poster
    }

    fun show(mShareUrl: String, tittle: String, imgUrl: String) {
        super.show()
        val bitmap1: Bitmap? = QRCodeUtil.createQRCodeBitmap(
            mShareUrl,
            PixelUtil.dp2px(100), null, "0", 0.28f
        )
        img_qr.setImageBitmap(bitmap1)
        tv_title.text = tittle
        if (!imgUrl.isNullOrBlank()) {
            GlideUtils.loadImage(mContext, imgUrl, false, img_post)
        } else {
            img_post.setImageResource(R.mipmap.app_poster_logo)
        }
    }

    private fun onPlatFormClick(view: View) {
        mListener?.let {
            when (view) {
                ll_selector.ll_wx_line -> {
                    it.onPlatformClick(Platform.WECHAT_TIMELINE)
                }
                ll_selector.ll_wx -> {
                    it.onPlatformClick(Platform.WECHAT)
                }
                ll_selector.ll_sina -> {
                    it.onPlatformClick(Platform.WEIBO)
                }
                ll_selector.ll_qq -> {
                    it.onPlatformClick(Platform.QQ)
                }
                ll_selector.ll_qzone -> {
                    it.onPlatformClick(Platform.QZONE)
                }
                else -> {
                }
            }
        }
        dismiss()
    }

    fun getImage2file(): File? {
        if (file != null && file?.length()!! > 0) return file
        val viewBitmap: Bitmap = ImageUtils.getViewBitmap(ll_post_card)
        val path: String = Constants.PATH_IMG
        val imgName = "fenxiangerweima_" + System.currentTimeMillis() + ".temp" //增加时间戳
        file = File(path + File.separator + imgName)
        file?.let {
            if (it.exists() && it.length() > 0) {
                return it
            }
            if (!it.exists()) {
                it.getParentFile().mkdirs()
            }
        }
        ImageUtils.saveBitmap2fileTemp(
            viewBitmap, path + File.separator + imgName, 90,
            Bitmap.CompressFormat.JPEG, imgName
        )
        return file
    }

    fun setOnPlatformClickListener(mListener: OnPlatformClickListener) {
        this.mListener = mListener
    }

    init {
        this.mListener = mListener
    }
}