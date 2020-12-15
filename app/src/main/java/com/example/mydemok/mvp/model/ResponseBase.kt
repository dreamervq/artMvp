package com.example.mydemok.mvp.model


class ResponseBase<T> {
    var error_code = 0
    var error_msg: String? = null
    var data: T? = null
    var msg: String? = null
    var max_id = 0
    var max = 0
    var count: String? = null
    constructor()
}
