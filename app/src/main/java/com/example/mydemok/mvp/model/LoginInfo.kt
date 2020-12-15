package com.example.mydemok.mvp.model

import cn.markmjw.platform.login.AuthResult
import java.io.Serializable

class LoginInfo : Serializable {
    var user: UserEntity? = null
    var auth: AuthResult? = null
    var error_code = 0
    var error_msg: String? = null

    constructor()
}