package com.example.mydemok.model

import cn.markmjw.platform.login.AuthResult
import java.io.Serializable

data class LoginInfo(
    var user: UserEntity,
    var error_code: Int = 0,
    var error_msg: String,
    var task_url: String,
    var duiba_url: String,
    var auth: AuthResult? = null
) : Serializable {

}