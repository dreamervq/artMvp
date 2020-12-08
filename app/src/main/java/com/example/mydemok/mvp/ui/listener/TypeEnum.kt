package com.example.mydemok.mvp.ui.listener

interface TypeEnum {


    enum class BaseCard(val value: Int) {
        //phoneCode:登录 bindCode:手机号绑定
        MODEL_ARTICLE(1),
        MODEL_ATLAS(2),
        MODEL_VIDEO(4),
        MODEL_SPECIAL(5),
        MODEL_LIVES(6),
        MODEL_POSTER(14),
        MODEL_SHORT_VIDEO(18),
        TYPE_VOTE(117),
        TYPE_ACTIVITY(118)
    }

     interface MainTab {
        companion object {
            const val Homepage = "首页"
            const val Second = "第二"
            const val Third = "第三"
            const val Four = "第四"
            const val Mine = "我的"
        }
    }
}