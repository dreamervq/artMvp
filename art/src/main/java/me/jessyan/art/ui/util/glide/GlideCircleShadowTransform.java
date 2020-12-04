package me.jessyan.art.ui.util.glide;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;

import java.security.MessageDigest;

public class GlideCircleShadowTransform extends GlideShadowTransform {
    public GlideCircleShadowTransform(String url, int shadowColor, int shadowRadius) {
        super(url, shadowColor, shadowRadius);
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        return addShadow(pool, TransformationUtils.circleCrop(pool, toTransform, outWidth, outHeight), outWidth, outHeight);
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        try {
            messageDigest.update(String.format("%s_circleShadow_%s_%s", url, shadowRadius, shadowColor).getBytes("utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
