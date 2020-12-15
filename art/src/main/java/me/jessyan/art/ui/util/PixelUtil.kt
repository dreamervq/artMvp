package me.jessyan.art.ui.util

import android.content.Context
import android.content.res.Resources
import android.view.WindowManager
import me.jessyan.art.R

/**
 * Pixel Util
 *
 *
 * Created by magic-lee on 15/4/7.
 */
object PixelUtil {
    private val APPCOMPAT_CHECK_ATTRS = intArrayOf(R.attr.colorPrimary)

    /**
     * dp to px.
     *
     * @param value the value
     */
    @JvmStatic
    fun dp2px(value: Float): Int {
        val scale =
            Resources.getSystem().displayMetrics.density
        return (value * scale + 0.5f).toInt()
    }

    @JvmStatic
    fun dp2px(value: Int): Int {
        return dp2px(value.toFloat())
    }

    /**
     * px to dp.
     *
     * @param value the value
     */
    @JvmStatic
    fun px2dp(value: Float): Int {
        val scale =
            Resources.getSystem().displayMetrics.density
        return (value / scale + 0.5f).toInt()
    }

    /**
     * sp to px.
     *
     * @param value the value
     */
    @JvmStatic
    fun sp2px(value: Float): Int {
        val scale =
            Resources.getSystem().displayMetrics.scaledDensity
        return (value * scale + 0.5f).toInt()
    }

    /**
     * px to sp.
     *
     * @param value the value
     */
    @JvmStatic
    fun px2sp(value: Float): Int {
        val scale =
            Resources.getSystem().displayMetrics.scaledDensity
        return (value / scale + 0.5f).toInt()
    }

    @JvmStatic
    fun getScreenWidth(context: Context): Int {
        val wm =
            context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        return wm.defaultDisplay.width
    }

    @JvmStatic
    fun getScreenHeight(context: Context): Int {
        val wm =
            context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        return wm.defaultDisplay.height
    }

    /**
     * 根据传入的宽度获取直播图片的高度，比例为 335：188
     *
     * @param width 图片宽度
     */
    @JvmStatic
    fun getLiveImageHeight(width: Int): Int {
        return (188f * width / 335 + 0.5f).toInt()
    }

    @JvmStatic
    fun getPoliticsSingleImageWidth(context: Context): Int {
        return getScreenWidth(context) - dp2px(40f)
    }

    @JvmStatic
    fun getPoliticsSingleImageHeight(context: Context): Int {
        return (151f * getPoliticsSingleImageWidth(context) / 268 + 0.5f).toInt()
    }

    @JvmStatic
    fun checkAppCompatTheme(context: Context) {
        val typedArray =
            context.obtainStyledAttributes(PixelUtil.APPCOMPAT_CHECK_ATTRS)
        val failed = !typedArray!!.hasValue(0)
        typedArray?.recycle()
        require(!failed) { "You need to use a Theme.AppCompat theme (or descendant) with the design library." }
    }
}