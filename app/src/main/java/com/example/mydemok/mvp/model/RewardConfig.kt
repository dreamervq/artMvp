package com.example.mydemok.mvp.model

data class RewardConfig(
    var on //是否开启打赏
    : Int,
    var score //是否能打赏积分
    : Int,
    var coin //是否能打赏金币
    : Int
)