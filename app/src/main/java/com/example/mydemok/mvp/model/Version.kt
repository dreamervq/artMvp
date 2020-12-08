package com.example.mydemok.mvp.model

/**
 * Created by steven.li on 9/12/16.
 */
data class Version(
    var version: String,
    var force: Boolean,
    var msg: String,
    var url: String
)