package com.example.mydemok.mvp.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.mydemok.R
import com.example.mydemok.mvp.presenter.IndexPresenter
import com.example.mydemok.utils.CommonUtil
import kotlinx.android.synthetic.main.activity_shop_web.*
import me.jessyan.art.mvp.Message

class ShopWebActivity : BaseWebActivity<IndexPresenter>() {
    private var showBrowser = true

    companion object {
        fun launch(context: Context, url: String, title: String, showBrowser: Boolean) {
            var intent = Intent(context, ShopWebActivity.javaClass)
            intent.putExtras(Bundle().apply {
                putString(CommonUtil.KEY_VALUE_1, url)
                putString(CommonUtil.KEY_VALUE_2, title)
                putBoolean(CommonUtil.KEY_VALUE_2, showBrowser)
            })
            context.startActivity(intent)
        }
    }

    override fun getTitleText(): String? {
        return ""
    }

    override fun initViews(savedInstanceState: Bundle?) {
        ll_bottom.visibility = if (showBrowser) View.VISIBLE else View.GONE
    }

    override fun getContentViewId(): Int {
        showBrowser = intent.getBooleanExtra(CommonUtil.KEY_VALUE_3, true)
        return R.layout.activity_shop_web
    }

    override fun obtainPresenter(): IndexPresenter? {
        TODO("Not yet implemented")
    }

    override fun handleMessage(message: Message) {
        TODO("Not yet implemented")
    }


    override fun onWebPageFinished(view: View, url: String) {
        super.onWebPageFinished(view, url)
        mWebView?.run {
            goahead.setColorFilter(resources.getColor(if (canGoForward()) R.color.text_black else R.color.text_gray))
            goback.setColorFilter(resources.getColor(if (canGoBack()) R.color.text_black else R.color.text_gray))
        }

    }

    override fun isImmersionBlack(): Boolean {
        return true
    }

    override fun needImmersionBar(): Boolean {
        return true
    }
}