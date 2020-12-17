package com.example.mydemok.mvp.ui.activity

import androidx.fragment.app.Fragment
import com.example.mylibrary.listeners.ITabPager
import com.example.mylibrary.mvp.uis.activities.BaseTabActivity
import me.jessyan.art.mvp.IPresenter
import me.jessyan.art.mvp.Message

class test: BaseTabActivity<ITabPager, IPresenter>() {

    override fun getData() {
        TODO("Not yet implemented")
    }

    override fun getTitleText(): String? {
        TODO("Not yet implemented")
    }

    override fun getContentViewId(): Int {
        TODO("Not yet implemented")
    }

    override fun obtainPresenter(): IPresenter? {
        TODO("Not yet implemented")
    }

    override fun handleMessage(message: Message) {
        TODO("Not yet implemented")
    }

    override fun getContent(pos: Int): Fragment? {
        TODO("Not yet implemented")
    }


}