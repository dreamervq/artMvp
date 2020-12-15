package com.example.mydemok.manager

import android.content.Context
import com.example.mydemok.mvp.model.AdvLiveData
import com.example.mydemok.mvp.ui.listener.TypeEnum

object CardManager {
    fun launchCardClick(
        activity: Context?,
        type: Int,
        id: Long,
        url: String?,
        title: String?
    ) {
        when (type) {
            TypeEnum.BaseCard.MODEL_SPECIAL.value -> {
            }
            TypeEnum.BaseCard.TYPE_VOTE.value -> {
            }
            TypeEnum.BaseCard.MODEL_ARTICLE.value -> {
            }
            TypeEnum.BaseCard.MODEL_VIDEO.value -> {
            }
            TypeEnum.BaseCard.MODEL_ATLAS.value -> {
            }
            TypeEnum.BaseCard.TYPE_ACTIVITY.value -> {
            }
            TypeEnum.BaseCard.MODEL_LIVES.value -> {
            }
            TypeEnum.BaseCard.MODEL_POSTER.value -> {
            }
            TypeEnum.BaseCard.MODEL_SHORT_VIDEO.value -> {
            }
            else -> {
                //   if (!TextUtils.isEmpty(url)) ShopWebActivity.launch(activity, url, title, false)
            }
        }
    }

    fun launchAdvlick(
        type: Int,
        advtype: Int,
        activity: Context,
        id: Long,
        share_url: String?,
        title: String?,
        liveType: Int,
        itemInfo: AdvLiveData?
    ) {
        //advtype广告模块：1资讯 2拍客 3晒照 4爆料 5我型我秀 7问吧8启动页
        if (advtype == 1) {
            launchCardClick(activity,type, id, share_url, title)
        } else if (advtype == 2) {
        } else if (advtype == 3) {
        }
    }
}