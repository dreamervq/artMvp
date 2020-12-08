package com.example.mydemok.mvp.model

import com.alibaba.fastjson.annotation.JSONField

class LiveHostInfo {
    var nickname: String? = null
    var image: String? = null
    var level: String? = null
    var userid: String? = null
    var gender: String? = null
    var is_follow = 0

    @JSONField(name = "fans_number", alternateNames = ["fans"])
    var fans_number = 0
    var follow_number = 0
    var fans = 0
    val isMale: Boolean
        get() = "m" == gender

    val isFemale: Boolean
        get() = "f" == gender
}