package com.example.mydemok.mvp.ui.dialog

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.example.mydemok.R
import kotlinx.android.synthetic.main.dialog_tip.*

class TipsDialog<T>(context: Context?) :
    Dialog(context, R.style.normal_dialog_small) {
    protected var entity: T? = null
    protected var listener: OnDialogClickListener<T>? = null

    constructor(
        context: Context?,
        onOperatClickListener: OnDialogClickListener<T>?
    ) : this(context) {
        this.listener = onOperatClickListener
    }

    fun setOnOperatClickListener(onOperatClickListener: OnDialogClickListener<T>?) {
        this.listener = onOperatClickListener
    }

    protected val layoutRes: Int
        protected get() = R.layout.dialog_tip


    fun onViewClicked(view: View) {
        if (listener != null) {
            when (view) {
                tv_cancel -> listener?.onCancelClick(entity)
                tv_ensure -> listener?.onEnsureClick(entity)
            }
        }
        dismiss()
    }

    fun show(
        tip: CharSequence?,
        ensure: String?,
        cancel: String?,
        params: T
    ) {
        show(tip, params)
        tv_ensure?.setText(ensure)
        tv_cancel?.setText(cancel)
    }

    fun showEnsure(tip: CharSequence?) {
        show(tip, null)
        tv_cancel?.visibility = View.GONE
        tv_ensure?.setText("确定")
    }

    fun showEnsure(tip: CharSequence?, ensure: String?, params: T) {
        show(tip, null)
        tv_cancel?.visibility = View.GONE
        tv_ensure?.text = ensure
    }


    @JvmOverloads
    fun show(tip: CharSequence?, params: T? = null) {
        entity = params
        super.show()
        tv_content?.setText(tip)
    }

    interface OnDialogClickListener<K> {
        fun onEnsureClick(param: K?)
        fun onCancelClick(param: K?)
    }

    init {
        setContentView(layoutRes)
        tv_ensure.setOnClickListener(this::onViewClicked)
        tv_cancel.setOnClickListener(this::onViewClicked)
        val metrics = getContext().resources.displayMetrics
        val screenWidth = (metrics.widthPixels * 0.72f).toInt()
        val window = window
        window?.setLayout(screenWidth, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}