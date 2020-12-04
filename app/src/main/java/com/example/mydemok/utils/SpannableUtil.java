package com.example.mydemok.utils;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.ScaleXSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.TypefaceSpan;
import android.text.style.UnderlineSpan;

/**
 * Created by magic-lee on 9/19/15.
 */
public class SpannableUtil {

    /**
     * 设置字体大小，用px
     *
     * @param str
     *         目标字符串
     * @param start
     *         开始位置
     * @param end
     *         结束位置
     * @param pxSize
     *         像素大小
     */
    public static SpannableString getSizeSpanUsePx(String str, int start, int end, int pxSize) {
        SpannableString ss = new SpannableString(str);
        ss.setSpan(new AbsoluteSizeSpan(pxSize), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    /**
     * 设置字体大小，用dip
     *
     * @param str
     *         目标字符串
     * @param start
     *         开始位置
     * @param end
     *         结束位置
     * @param dipSize
     *         像素大小
     */
    public static SpannableString getSizeSpanUseDip(String str, int start, int end, int dipSize) {
        SpannableString ss = new SpannableString(str);
        ss.setSpan(new AbsoluteSizeSpan(dipSize, true), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    /**
     * 设置字体大小，用sp
     *
     * @param str
     *         目标字符串
     * @param start
     *         开始位置
     * @param end
     *         结束位置
     * @param spSize
     *         sp大小
     */
    public static SpannableString getSizeSpanSpToPx(String str, int start, int end, int spSize) {
        SpannableString ss = new SpannableString(str);
//        ss.setSpan(new AbsoluteSizeSpan(PixelUtil.sp2px(spSize)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    /**
     * 设置字体大小，用sp
     *
     * @param str
     *         目标字符串
     * @param start
     *         开始位置
     * @param end
     *         结束位置
     */
    public static SpannableString getSizeSpanSpToPx(SpannableString str, int start, int end, int spSize) {
//        str.setSpan(new AbsoluteSizeSpan(PixelUtil.sp2px(spSize)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return str;
    }

    /**
     * 设置字体相对大小
     *
     * @param str
     *         目标字符串
     * @param start
     *         开始位置
     * @param end
     *         结束位置
     * @param relativeSize
     *         相对大小 如：0.5f，2.0f
     * @return
     */
    public static SpannableString getRelativeSizeSpan(String str, int start, int end, float relativeSize) {
        SpannableString ss = new SpannableString(str);
        ss.setSpan(new RelativeSizeSpan(relativeSize), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }
    /**
     * 设置字体相对大小以及位置
     */
    public static SpannableString getAbsoluteSizeSpan(String str, int start, int end){
        SpannableString ss = new SpannableString(str);
        SuperscriptSpan superScriptTextSpan = new SuperscriptSpan();
        ss.setSpan(superScriptTextSpan, start, end, SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
        AbsoluteSizeSpan sizeSpan =new AbsoluteSizeSpan(18,true);
        ss.setSpan(sizeSpan, start, end, SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
        return ss;
    }
    /**
     * 设置字体
     *
     * @param str
     *         目标字符串
     * @param start
     *         开始位置
     * @param end
     *         结束位置
     * @param typeface
     *         字体类型 如：default，efault-bold,monospace,serif,sans-serif
     */
    public static SpannableString getTypeFaceSpan(String str, int start, int end, String typeface) {
        SpannableString ss = new SpannableString(str);
        ss.setSpan(new TypefaceSpan(typeface), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    /**
     * 设置字体形体
     *
     * @param str
     *         目标字符串
     * @param start
     *         开始位置
     * @param end
     *         结束位置
     * @param style
     *         字体类型 如： Typeface.NORMAL正常 Typeface.BOLD粗体 Typeface.ITALIC斜体 Typeface.BOLD_ITALIC粗斜体
     */
    public static SpannableString getStyleSpan(String str, int start, int end, int style) {
        SpannableString ss = new SpannableString(str);
        ss.setSpan(new StyleSpan(style), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    /**
     * 设置字体形体
     *
     * @param str
     *         目标字符串
     * @param start
     *         开始位置
     * @param end
     *         结束位置
     * @param style
     *         字体类型 如： Typeface.NORMAL正常 Typeface.BOLD粗体 Typeface.ITALIC斜体 Typeface.BOLD_ITALIC粗斜体
     */
    public static SpannableString getStyleSpan(SpannableString str, int start, int end, int style) {
        str.setSpan(new StyleSpan(style), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return str;
    }

    /**
     * 设置字体下划线
     *
     * @param str
     *         目标字符串
     * @param start
     *         开始位置
     * @param end
     *         结束位置
     */
    public static SpannableString getUnderLineSpan(String str, int start, int end) {
        SpannableString ss = new SpannableString(str);
        ss.setSpan(new UnderlineSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    /**
     * 设置字体删除线
     *
     * @param str
     *         目标字符串
     * @param start
     *         开始位置
     * @param end
     *         结束位置
     */
    public static SpannableString getDeleteLineSpan(String str, int start, int end) {
        SpannableString ss = new SpannableString(str);
        ss.setSpan(new StrikethroughSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    /**
     * 设置上标
     *
     * @param str
     *         目标字符串
     * @param start
     *         开始位置
     * @param end
     *         结束位置
     */
    public static SpannableString getSuperscriptSpan(String str, int start, int end) {
        SpannableString ss = new SpannableString(str);
        ss.setSpan(new SuperscriptSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    /**
     * 设置放大系数
     *
     * @param str
     *         目标字符串
     * @param start
     *         开始位置
     * @param end
     *         结束位置
     * @param scale
     *         放大多少倍，x轴方向，y轴不变 如：0.5f， 2.0f
     */
    public static SpannableString getScaleSpan(String str, int start, int end, float scale) {
        SpannableString ss = new SpannableString(str);
        ss.setSpan(new ScaleXSpan(scale), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    /**
     * 设置下标
     *
     * @param str
     *         目标字符串
     * @param start
     *         开始位置
     * @param end
     *         结束位置
     */
    public static SpannableString getSubscriptSpan(String str, int start, int end) {
        SpannableString ss = new SpannableString(str);
        ss.setSpan(new SubscriptSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    /**
     * 设置背景色
     *
     * @param str
     *         目标字符串
     * @param start
     *         开始位置
     * @param end
     *         结束位置
     * @param color
     *         颜色值 如Color.BLACK
     */
    public static SpannableString getBackGroundColorSpan(String str, int start, int end, int color) {
        SpannableString ss = new SpannableString(str);
        ss.setSpan(new BackgroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    /**
     * 设置背景色
     *
     * @param str
     *         目标字符串
     * @param start
     *         开始位置
     * @param end
     *         结束位置
     * @param color
     *         颜色值 如：#CCCCCC
     */
    public static SpannableString getBackGroundColorSpan(String str, int start, int end, String color) {
        SpannableString ss = new SpannableString(str);
        ss.setSpan(new BackgroundColorSpan(Color.parseColor(color)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    /**
     * 设置字体颜色
     *
     * @param str
     *         目标字符串
     * @param start
     *         开始位置
     * @param end
     *         结束位置
     * @param color
     *         颜色值 如Color.BLACK
     */
    public static SpannableString getForegroundColorSpan(String str, int start, int end, int color) {
        SpannableString ss = new SpannableString(str);
        ss.setSpan(new ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    /**
     * 设置字体颜色
     *
     * @param str
     *         目标字符串
     * @param start
     *         开始位置
     * @param end
     *         结束位置
     * @param color
     *         颜色值 如：#CCCCCC
     */
    public static SpannableString getForegroundColorSpan(String str, int start, int end, String color) {
        SpannableString ss = new SpannableString(str);
        ss.setSpan(new ForegroundColorSpan(Color.parseColor(color)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    public static SpannableString getForegroundColorSpan(String str, String key, String color) {
        SpannableString style = new SpannableString(str);
        if (TextUtils.isEmpty(key)) {
            return style;
        }
        style.setSpan(new ForegroundColorSpan(Color.parseColor(color)), key.length(), str.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE );
        return style;
    }
    public static SpannableString getForegroundColorSpan1(String str, String key, String color) {
        if (TextUtils.isEmpty(str))
            return  null;
        if (TextUtils.isEmpty(key))
            return new SpannableString(str);
        SpannableString ss = new SpannableString(str);
        int start = 0;
        key = key.toUpperCase();
        while (str.indexOf(key, start) >= 0) {
            int indexStart = str.indexOf(key, start);
            if (indexStart >= 0) {
                start = indexStart + key.length();
                ss.setSpan(new ForegroundColorSpan(Color.parseColor(color)),
                        indexStart, indexStart + key.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                break;
            }
        }
        return ss;
    }
    public static SpannableString getForegroundColorSpan(String str, String key, SpannableString ss, int color) {
        int start = 0;
        while (str.indexOf(key, start) >= 0) {
            int indexStart = str.indexOf(key, start);
            if (indexStart >= 0) {
                start = indexStart + key.length();
                ss.setSpan(new ForegroundColorSpan(color),
                        indexStart, indexStart + key.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                break;
            }
        }
        return ss;
    }
}
