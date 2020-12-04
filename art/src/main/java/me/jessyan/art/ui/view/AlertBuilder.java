package me.jessyan.art.ui.view;

import android.app.AlertDialog;
import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class AlertBuilder extends AlertDialog.Builder {
    public AlertBuilder(@NonNull Context context) {
        super(context);
    }

    public AlertBuilder(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    private CharSequence getSpanString(CharSequence text, int color, int size, boolean dip) {
        CharSequence realText;
        if (!TextUtils.isEmpty(text)) {
            SpannableString spannableString = new SpannableString(text);
            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(color);
            AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(size, dip);
            spannableString.setSpan(foregroundColorSpan, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(absoluteSizeSpan, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            realText = spannableString;
        } else {
            realText = text;
        }
        return realText;
    }

    public AlertBuilder setTitle(int resId, int color, int size) {
        return (AlertBuilder) super.setTitle(getSpanString(getContext().getString(resId), color, size, false));
    }

    public AlertBuilder setTitle(int resId, int color, int size, boolean dip) {
        return (AlertBuilder) super.setTitle(getSpanString(getContext().getString(resId), color, size, dip));
    }

    public AlertBuilder setTitle(@Nullable CharSequence title, int color, int size) {
        return (AlertBuilder) super.setTitle(getSpanString(title, color, size, false));
    }

    public AlertBuilder setTitle(@Nullable CharSequence title, int color, int size, boolean dip) {
        return (AlertBuilder) super.setTitle(getSpanString(title, color, size, dip));
    }

    public AlertBuilder setMessage(int messageId, int color, int size) {
        return (AlertBuilder) super.setMessage(getSpanString(getContext().getString(messageId), color, size, false));
    }

    public AlertBuilder setMessage(int messageId, int color, int size, boolean dip) {
        return (AlertBuilder) super.setMessage(getSpanString(getContext().getString(messageId), color, size, dip));
    }

    public AlertBuilder setMessage(@Nullable CharSequence message, int color, int size) {
        return (AlertBuilder) super.setMessage(getSpanString(message, color, size, false));
    }

    public AlertBuilder setMessage(@Nullable CharSequence message, int color, int size, boolean dip) {
        return (AlertBuilder) super.setMessage(getSpanString(message, color, size, dip));
    }

}
