package com.example.mydemok.mvp.model

import android.nfc.Tag

/**
 * id : 26
 * modelid : 0
 * module : 8
 * live_type : 0
 * adv_data : {}
 * adv_type : 1
 * title :
 * image : http://upload.dev.zdg.xinmem.com/1/2020/0521/1590031648399091_1125x2058.jpg
 * adv_image : http://upload.dev.zdg.xinmem.com/1/2020/0521/1590031648399091_1125x2058.jpg
 * status : 1
 * cid_type : 2
 * cid : 20
 * url : http://app.dev.zdg.xinmem.com/web/space/content/20
 * share_url : http://app.dev.zdg.xinmem.com/web/space/content/20?share=1
 * desc :
 * views : 1
 * video_url :
 * video_duration :
 * video_image :
 * money : 0.00
 * keep : 11
 * image_width : 0
 * image_height : 0
 * show_height : 0
 * tags : [{"name":"广告","font_color":"#000000","border_color":"#000000"}]
 * error_code : 0
 * error_msg :
 */
data class SplashAD(
    var keep: Int,
    var id: Long,
    var modelid: Int,
    var module: Int,
    var adv_data: AdvLiveData,
    var live_type: Int,
    var adv_type: Int,
    var title: String,
    var image: List<String>,
    var adv_image: List<String>,
    var status: Int,
    var cid_type: Int,
    var cid: Long,
    var url: String,
    var share_url: String,
    var desc: String,
    var views: Int,
    var video_url: String,
    var video_duration: String,
    var video_image: String,
    var money: String,
    var image_width: Int,
    var image_height: Int,
    var show_height: Int,
    var error_code: Int,
    var error_msg: String,
    var tags: List<Tag>,
    var content_id: String,
    var bs_play_url: String,
    var bs_type: String,
    var bs_id: String
)