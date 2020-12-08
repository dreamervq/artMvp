package com.example.mydemok.mvp.ui.fragment

import android.widget.TextView
import com.example.mydemok.R
import com.example.mylibrary.mvp.uis.fragment.BaseNewFragment
import kotlinx.android.synthetic.main.fragment_first.*
import me.jessyan.art.mvp.IPresenter
import me.jessyan.art.mvp.Message
import timber.log.Timber

class FirstFragment : BaseNewFragment<IPresenter>() {
    override fun getContentViewId(): Int {
        return R.layout.fragment_first
    }

    override fun obtainPresenter(): IPresenter? {
        return null
    }

    override fun handleMessage(message: Message) {
    }

    override fun init() {
        val b1 = tv_title == null
        val b2 = rootView == null
        Timber.e("b1=$b1,b2=$b2")
        rootView.findViewById<TextView>(R.id.tv_title).text = this::class.java.simpleName
    }

}