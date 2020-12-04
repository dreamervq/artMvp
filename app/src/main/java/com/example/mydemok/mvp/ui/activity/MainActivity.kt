package com.example.mydemok.mvp.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.example.mydemok.R
import com.example.mydemok.mvp.ui.dialog.TipsDialog
import com.example.mydemok.mvp.ui.dialog.TipsDialog.OnDialogClickListener
import com.example.mylibrary.mvp.uis.activities.BaseSwipeBackActivity
import com.example.mylibrary.mvp.uis.adapters.holders.CommonHolder
import kotlinx.android.synthetic.main.activity_main.*
import me.jessyan.art.mvp.IPresenter
import me.jessyan.art.mvp.Message
import me.jessyan.art.ui.adapters.BaseAdapter

class MainActivity : BaseSwipeBackActivity<IPresenter>() {
    override fun getTitleText(): String? {
        TODO("Not yet implemented")
    }

    @SuppressLint("SetTextI18n")
    override fun initViews(savedInstanceState: Bundle?) {
        tv_title.text = "anko"
        tv_title.setOnClickListener(this::showDialog)
    }

    fun showDialog(v: View) {
        object : BaseAdapter<String>(this, R.layout.activity_main, mutableListOf()) {
            override fun convert(holder: CommonHolder?, item: String, position: Int) {
                TODO("Not yet implemented")
            }
        }

        val tipDialog: TipsDialog<String> = TipsDialog<String>(this)

        tipDialog.setOnOperatClickListener(object : OnDialogClickListener<String> {
            override fun onEnsureClick(param: String?) {
                shortToast("yes")
            }

            override fun onCancelClick(param: String?) {
                shortToast("no")
            }
        })
        tipDialog.show("是否取消绑定？", "确认", "取消", "")

    }

    override fun getContentViewId(): Int {
        return R.layout.activity_main
    }

    override fun obtainPresenter(): IPresenter? {
        return null
    }

    override fun handleMessage(message: Message) {
        TODO("Not yet implemented")
    }

    override fun isImmersionBlack(): Boolean {
        return true
    }

    override fun needImmersionBar(): Boolean {
        return true
    }
}