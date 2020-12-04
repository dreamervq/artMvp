package com.example.mylibrary.mvp.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.ViewGroup
import me.jessyan.art.R
import me.jessyan.art.ui.view.LoadingView

class LoadingDialog(context: Context) : Dialog(context, R.style.normal_dialog_loading) {
    private var lvLoading: LoadingView? = null
    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        lvLoading = findViewById(R.id.lv_loading)
    }

    /**
     * loading
     */
    fun showLoading(tip: String?) {
        show()
        lvLoading!!.showLoading()
        lvLoading!!.setText(tip)
        lvLoading!!.setTextVisible(!TextUtils.isEmpty(tip))
    }

    /**
     * loading
     */
    fun showLoading() {
        show()
        lvLoading!!.showLoading()
        lvLoading!!.setTextVisible(false)
    }

    /**
     * 成功
     */
    fun showSuccess() {
        show()
        lvLoading!!.showSuccess()
    }

    /**
     * 失败
     */
    fun showFail() {
        show()
        lvLoading!!.showFail()
    }

    init {
        setContentView(R.layout.dialog_loading)
        val window = window
        window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}