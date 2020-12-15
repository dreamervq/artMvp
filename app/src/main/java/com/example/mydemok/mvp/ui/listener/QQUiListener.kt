package com.example.mydemok.mvp.ui.listener

import android.util.Log
import com.example.mydemok.constant.EventBusTags
import com.tencent.tauth.IUiListener
import com.tencent.tauth.UiError
import me.jessyan.art.base.BaseApplication
import me.jessyan.art.integration.EventBusManager
import me.jessyan.art.utils.ArtUtils

/**
 * Created by yj on 2019/1/24.
 */
class QQUiListener : IUiListener {
    private val mShareType = 0


    override fun onComplete(o: Any) {
        when (mShareType) {
            TYPE_QQ -> {
            }
            TYPE_QZONE -> {
            }
        }
        EventBusManager.getInstance()
            .post(EventBusTags.EVENT_SHARE_SUCCESS, EventBusTags.EVENT_SHARE_SUCCESS)
    }

    override fun onError(uiError: UiError) {
        Log.e("share", "QQ share error: " + uiError.errorMessage)
        ArtUtils.makeText(BaseApplication.getInstance().applicationContext, "分享失败")
    }

    override fun onCancel() {
        ArtUtils.makeText(BaseApplication.getInstance().applicationContext, "已取消分享")
    }

    companion object {
        const val TYPE_QQ = 0
        const val TYPE_QZONE = 1
        var stance: QQUiListener? = null
            get() {
                if (field == null) {
                    field = QQUiListener()
                }
                return field
            }
            private set
    }
}