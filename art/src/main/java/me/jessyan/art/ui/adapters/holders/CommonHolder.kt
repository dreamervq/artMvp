package com.example.mylibrary.mvp.uis.adapters.holders

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StrikethroughSpan
import android.text.style.UnderlineSpan
import android.text.util.Linkify
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import me.jessyan.art.ui.util.GlideUtils
import java.io.File

class CommonHolder(private val mContext: Context, val convertView: View) :
    RecyclerView.ViewHolder(convertView) {
    private val mViews: SparseArray<View?>
    var videoPath: String? = null

    /**
     * 通过viewId获取控件
     */
    fun <T : View?> getView(viewId: Int): T? {
        var view = mViews[viewId]
        if (view == null) {
            view = convertView.findViewById(viewId)
            mViews.put(viewId, view)
        }
        return view as T?
    }

    /****以下为辅助方法 */
    /**
     * 设置TextView的值
     */
    fun setTextWithTag(
        viewId: Int,
        text: String?,
        tags: List<String?>?,
        color: Int
    ): CommonHolder {
        val tv = getView<TextView>(viewId)!!
        if (tags != null && tags.size >= 0) {
            val stringBuilder = StringBuilder()
            for (tag in tags) {
                stringBuilder.append("#").append(tag ?: "").append("# ")
            }
            val s =
                SpannableString(stringBuilder.toString() + (text ?: ""))
            val f = ForegroundColorSpan(color)
            s.setSpan(f, 0, stringBuilder.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            tv.text = s
        } else {
            tv.text = text ?: ""
        }
        return this
    }

    fun setTextWithTagObj(
        viewId: Int,
        text: String?,
        tags: List<*>?,
        color: Int
    ): CommonHolder {
        val tv = getView<TextView>(viewId)!!
        if (tags != null && tags.size >= 0) {
            val stringBuilder = StringBuilder()
            for (tag in tags) {
                stringBuilder.append("#").append(tag?.toString() ?: "")
                    .append("# ")
            }
            val s =
                SpannableString(stringBuilder.toString() + (text ?: ""))
            val f = ForegroundColorSpan(color)
            s.setSpan(f, 0, stringBuilder.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            tv.text = s
        } else {
            tv.text = text ?: ""
        }
        return this
    }

    /**
     * 设置TextView的值
     */
    fun setText(viewId: Int, text: String?): CommonHolder {
        val tv = getView<TextView>(viewId)!!
        if (TextUtils.isEmpty(text)) {
            setVisible(viewId, false)
        } else {
            setVisible(viewId, true)
            tv.text = text
        }
        return this
    }

    /**
     * 设置TextView的值
     */
    fun setTextUnderLine(viewId: Int, text: String): CommonHolder {
        val tv = getView<TextView>(viewId)!!
        if (TextUtils.isEmpty(text)) {
            setVisible(viewId, false)
        } else {
            setVisible(viewId, true)
            val ss = SpannableString(text)
            val underlineSpan = UnderlineSpan()
            ss.setSpan(underlineSpan, 0, text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            tv.text = ss
        }
        return this
    }

    /**
     * 设置TextView的值
     */
    fun setPriceDeleteLine(
        viewId: Int,
        text: String,
        deleteLine: Boolean
    ): CommonHolder {
        val tv = getView<TextView>(viewId)!!
        if (TextUtils.isEmpty(text)) {
            tv.text = ""
        } else {
            val ss = SpannableString(String.format("￥%s", text))
            if (deleteLine) {
                ss.setSpan(StrikethroughSpan(), 0, text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            tv.text = ss
        }
        return this
    }

    /**
     * 设置TextView的值
     */
    fun setText(viewId: Int, text: CharSequence?): CommonHolder {
        val tv = getView<TextView>(viewId)!!
        if (TextUtils.isEmpty(text)) {
            setVisible(viewId, false)
        } else {
            setVisible(viewId, true)
            tv.text = text
        }
        return this
    }

    fun setTextNotHide(viewId: Int, text: CharSequence?): CommonHolder {
        val tv = getView<TextView>(viewId)!!
        tv.text = text ?: ""
        return this
    }

    fun setTextSize(viewId: Int, size: Int): CommonHolder {
        val tv = getView<TextView>(viewId)!!
        tv.textSize = size.toFloat()
        return this
    }

    fun setWeight(viewId: Int, weight: Float): CommonHolder {
        val view = getView<View>(viewId)!!
        val layoutParams = view.layoutParams
        if (layoutParams != null && layoutParams is LinearLayout.LayoutParams) {
            layoutParams.weight = if (weight > 0) weight else 0f
            view.layoutParams = layoutParams
        }
        return this
    }

    fun setPriceNotHide(viewId: Int, text: CharSequence?): CommonHolder {
        val tv = getView<TextView>(viewId)!!
        tv.text = String.format("￥%s", text ?: "")
        return this
    }

    fun setOrderItemPrice(viewId: Int, twoFloat: String): CommonHolder {
        val tv = getView<TextView>(viewId)!!
        if (!TextUtils.isEmpty(twoFloat)) {
            if (twoFloat.contains(".")) {
                val spannableString = SpannableString(twoFloat)
                spannableString.setSpan(
                    AbsoluteSizeSpan(12, true),
                    twoFloat.indexOf("."),
                    twoFloat.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                tv.text = spannableString
            } else {
                tv.text = twoFloat
            }
        } else {
            tv.text = ""
        }
        return this
    }

    fun setSelected(viewId: Int, selected: Boolean): CommonHolder {
        val v = getView<View>(viewId)!!
        v.isSelected = selected
        return this
    }

    fun setEnabled(viewId: Int, enabled: Boolean): CommonHolder {
        val v = getView<View>(viewId)!!
        v.isEnabled = enabled
        return this
    }

    fun setTextDrawableTop(viewId: Int, resId: Int): CommonHolder {
        val tv = getView<TextView>(viewId)!!
        val drawable = getDrawable(resId)
        tv.setCompoundDrawables(null, drawable, null, null)
        return this
    }

    fun setTextDrawableRight(viewId: Int, resId: Int): CommonHolder {
        val tv = getView<TextView>(viewId)!!
        tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, resId, 0)
        return this
    }

    fun setTextDrawableLeft(viewId: Int, resId: Int): CommonHolder {
        val tv = getView<TextView>(viewId)!!
        tv.setCompoundDrawablesWithIntrinsicBounds(resId, 0, 0, 0)
        return this
    }

    private fun getDrawable(resId: Int): Drawable {
        val drawable = mContext.resources.getDrawable(resId)
        drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
        return drawable
    }

    //    public CommonHolder setImage(int viewId, String imgUrl) {
    //        ImageView view = getView(viewId);
    //        GlideUtils.loadImage(mContext, imgUrl, view);
    //        return this;
    //    }
    //
    //    public CommonHolder setVideoImage(int viewId, String imgUrl) {
    //        ImageView view = getView(viewId);
    //        GlideUtils.loadVideoFrame(mContext, imgUrl, view);
    //        return this;
    //    }
    fun setAgateImage(
        viewId: Int,
        url: String?,
        isVideo: Boolean,
        ratioLoding: Boolean
    ): CommonHolder {
        val view = getView<ImageView>(viewId)!!
        GlideUtils.loadAgateImage(mContext, url, isVideo, ratioLoding, view)
        return this
    }

    fun setImage(viewId: Int, url: String?, ratioLoding: Boolean): CommonHolder {
        val view = getView<ImageView>(viewId)!!
        GlideUtils.loadImage(mContext, url, ratioLoding, view)
        return this
    }

    fun setImage(viewId: Int, sourceId: Int, ratioLoding: Boolean): CommonHolder {
        val view = getView<ImageView>(viewId)!!
        GlideUtils.loadImage(mContext, sourceId, ratioLoding, view)
        return this
    }

    fun setVideoImage(viewId: Int, url: String?, ratioLoding: Boolean): CommonHolder {
        val view = getView<ImageView>(viewId)!!
        GlideUtils.loadAgateImage(mContext, url, true, ratioLoding, view)
        return this
    }

    fun setLocalImage(viewId: Int, imgRes: Int): CommonHolder {
        val view = getView<ImageView>(viewId)!!
        GlideUtils.loadLocalImage(mContext, imgRes, view)
        return this
    }

    fun setCircleImage(
        viewId: Int,
        imgUrl: String?,
        ratioLoding: Boolean
    ): CommonHolder {
        val view = getView<ImageView>(viewId)!!
        GlideUtils.loadCircleImage(mContext, imgUrl, ratioLoding, view)
        return this
    }

    fun setRoundImage(
        viewId: Int,
        imgUrl: String?,
        ratioLoding: Boolean
    ): CommonHolder {
        val view = getView<ImageView>(viewId)!!
        GlideUtils.loadRoundImage(mContext, imgUrl, ratioLoding, view, 4)
        return this
    }

    fun setRoundImage(
        viewId: Int,
        imgUrl: String?,
        dp: Int,
        ratioLoding: Boolean
    ): CommonHolder {
        val view = getView<ImageView>(viewId)!!
        GlideUtils.loadRoundImage(mContext, imgUrl, ratioLoding, view, dp)
        return this
    }

    fun setAvatarImage(viewId: Int, imgUrl: String?): CommonHolder {
        val view = getView<ImageView>(viewId)!!
        GlideUtils.loadAvatarImage(mContext, imgUrl, view)
        return this
    }

    fun setAvatarImage(viewId: Int, file: File?): CommonHolder {
        val view = getView<ImageView>(viewId)!!
        GlideUtils.loadAvatarImage(mContext, file, view)
        return this
    }

    fun setImageResource(viewId: Int, resId: Int): CommonHolder {
        val view = getView<ImageView>(viewId)!!
        view.setImageResource(resId)
        return this
    }

    fun setImageBitmap(viewId: Int, bitmap: Bitmap?): CommonHolder {
        val view = getView<ImageView>(viewId)!!
        view.setImageBitmap(bitmap)
        return this
    }

    fun setImageDrawable(viewId: Int, drawable: Drawable?): CommonHolder {
        val view = getView<ImageView>(viewId)!!
        view.setImageDrawable(drawable)
        return this
    }

    fun setBackgroundColor(viewId: Int, color: Int): CommonHolder {
        val view = getView<View>(viewId)!!
        view.setBackgroundColor(color)
        return this
    }

    fun setBackgroundRes(viewId: Int, backgroundRes: Int): CommonHolder {
        val view = getView<View>(viewId)!!
        view.setBackgroundResource(backgroundRes)
        return this
    }

    fun setBackgroundDrawable(viewId: Int, drawable: Drawable?): CommonHolder {
        val view = getView<View>(viewId)!!
        view.background = drawable
        return this
    }

    fun setTextColor(viewId: Int, textColor: Int): CommonHolder {
        val view = getView<TextView>(viewId)!!
        view.setTextColor(textColor)
        return this
    }

    fun setTextColorRes(viewId: Int, textColorRes: Int): CommonHolder {
        val view = getView<TextView>(viewId)!!
        view.setTextColor(mContext.resources.getColor(textColorRes))
        return this
    }

    @SuppressLint("NewApi")
    fun setAlpha(viewId: Int, value: Float): CommonHolder {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getView<View>(viewId)!!.alpha = value
        } else {
            // Pre-honeycomb hack to set Alpha value
            val alpha = AlphaAnimation(value, value)
            alpha.duration = 0
            alpha.fillAfter = true
            getView<View>(viewId)!!.startAnimation(alpha)
        }
        return this
    }

    fun setVisible(viewId: Int, visible: Boolean): CommonHolder {
        val view = getView<View>(viewId)!!
        view.visibility = if (visible) View.VISIBLE else View.GONE
        return this
    }

    fun setVisibleFake(viewId: Int, visible: Boolean): CommonHolder {
        val view = getView<View>(viewId)!!
        view.visibility = if (visible) View.VISIBLE else View.INVISIBLE
        return this
    }

    fun linkify(viewId: Int): CommonHolder {
        val view = getView<TextView>(viewId)!!
        Linkify.addLinks(view, Linkify.ALL)
        return this
    }

    fun setTypeface(typeface: Typeface?, vararg viewIds: Int): CommonHolder {
        for (viewId in viewIds) {
            val view = getView<TextView>(viewId)!!
            view.typeface = typeface
            view.paintFlags = view.paintFlags or Paint.SUBPIXEL_TEXT_FLAG
        }
        return this
    }

    fun setProgress(viewId: Int, progress: Int): CommonHolder {
        val view = getView<ProgressBar>(viewId)!!
        view.progress = progress
        return this
    }

    fun setProgress(viewId: Int, progress: Int, max: Int): CommonHolder {
        val view = getView<ProgressBar>(viewId)!!
        view.max = max
        view.progress = progress
        return this
    }

    fun setMax(viewId: Int, max: Int): CommonHolder {
        val view = getView<ProgressBar>(viewId)!!
        view.max = max
        return this
    }

    fun setRating(viewId: Int, rating: Float): CommonHolder {
        val view = getView<RatingBar>(viewId)!!
        view.rating = rating
        return this
    }

    fun setRating(viewId: Int, rating: Float, max: Int): CommonHolder {
        val view = getView<RatingBar>(viewId)!!
        view.max = max
        view.rating = rating
        return this
    }

    fun setTag(viewId: Int, tag: Any?): CommonHolder {
        val view = getView<View>(viewId)!!
        view.tag = tag
        return this
    }

    fun setTag(viewId: Int, key: Int, tag: Any?): CommonHolder {
        val view = getView<View>(viewId)!!
        view.setTag(key, tag)
        return this
    }

    fun setChecked(viewId: Int, checked: Boolean): CommonHolder {
        val view = getView<View>(viewId) as Checkable
        view.isChecked = checked
        return this
    }

    /**
     * 关于事件的
     */
    fun setOnClickListener(
        viewId: Int,
        listener: View.OnClickListener?
    ): CommonHolder {
        val view = getView<View>(viewId)!!
        view.setOnClickListener(listener)
        return this
    }

    fun setOnTouchListener(
        viewId: Int,
        listener: OnTouchListener?
    ): CommonHolder {
        val view = getView<View>(viewId)!!
        view.setOnTouchListener(listener)
        return this
    }

    fun setOnLongClickListener(
        viewId: Int,
        listener: OnLongClickListener?
    ): CommonHolder {
        val view = getView<View>(viewId)!!
        view.setOnLongClickListener(listener)
        return this
    }

    companion object {
        @JvmStatic
        fun createViewHolder(
            context: Context,
            itemView: View
        ): CommonHolder {
            return CommonHolder(context, itemView)
        }

        @JvmStatic
        fun createViewHolder(
            context: Context,
            parent: ViewGroup?, layoutId: Int
        ): CommonHolder {
            val itemView = LayoutInflater.from(context).inflate(
                layoutId, parent,
                false
            )
            return CommonHolder(context, itemView)
        }
    }

    init {
        mViews = SparseArray()
    }
}