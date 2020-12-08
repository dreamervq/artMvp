package com.example.mydemok.mvp.model


data class ResponseBase<T>(
    var error_code: Int,
    var error_msg: String,
    var data: T,
    var msg: String,
    var max_id: Int,
    var max: Int
)