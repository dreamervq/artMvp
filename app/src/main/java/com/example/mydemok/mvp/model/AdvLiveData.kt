package com.example.mydemok.mvp.model

data class AdvLiveData(
    var lor :Int, //1直播，2回顾
    var host_uid: String,
    var cover: String,
    var name: String,
    var videoId: String,
    var play_url: String,
    var play_url_flv: String,
    var createTime: Long,
    var fileSize: Long,
    var duration: String,
    var memsize:Int,
    var thumbup :Int,
    var kai_time: String,
    var title: String,
    var share_url: String,
    var id :Int,
    var notice_content: String,
    var background: String,
    var start_time: Long,
    var content: String,
    var roomnum: String,
    var user: LiveHostInfo,
    var live_type:Int,
    var file_type :Int
)