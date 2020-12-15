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
class SplashAD {
    var keep = 0
    var id = 0L
    var modelid = 0
    var module = 0
    var live_type = 0
    var adv_data: AdvLiveData? = null
    var adv_type = 0
    var title: String? = null
    var image: String? = null
    var adv_image: String? = null
    var status = 0
    var cid_type = 0
    var cid = 0L
    var url: String? = null
    var share_url: String? = null
    var desc: String? = null
    var views = 0
    var video_url: String? = null
    var video_duration: String? = null
    var video_image: String? = null
    var money: String? = null
    var image_width = 0
    var image_height = 0
    var show_height = 0
    var error_code = 0
    var error_msg: String? = null
    var tags: List<Tag>? = null

    constructor()
}