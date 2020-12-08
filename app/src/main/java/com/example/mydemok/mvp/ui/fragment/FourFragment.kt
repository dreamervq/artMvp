package com.example.mydemok.mvp.ui.fragment

import com.example.mydemok.R
import com.example.mylibrary.mvp.uis.fragment.BaseNewFragment
import kotlinx.android.synthetic.main.fragment_first.*
import me.jessyan.art.mvp.IPresenter
import me.jessyan.art.mvp.Message

class FourFragment : BaseNewFragment<IPresenter>() {
    override fun getContentViewId(): Int {
       return R.layout.fragment_first
    }

    override fun obtainPresenter(): IPresenter? {
        return null
    }

    override fun handleMessage(message: Message) {
    }

    override fun init() {
        tv_title.text = this::class.java.simpleName
    }
}