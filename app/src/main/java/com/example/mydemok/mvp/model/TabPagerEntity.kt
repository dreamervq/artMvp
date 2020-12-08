package com.example.mydemok.mvp.model

import com.example.mylibrary.listeners.ITabPager

class TabPagerEntity : ITabPager {
    override var title: CharSequence
    var type: String? = null

    constructor(title: CharSequence, type: String?) {
        this.title = title
        this.type = type
    }

    constructor(title: CharSequence) {
        this.title = title
    }

}