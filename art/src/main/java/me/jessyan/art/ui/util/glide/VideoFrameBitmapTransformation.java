package me.jessyan.art.ui.util.glide;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;


public class VideoFrameBitmapTransformation extends BitmapTransformation {

    private long timeMillis;
    private String link;

    public VideoFrameBitmapTransformation(String link, long timeMillis) {
        this.link = link;
        this.timeMillis = timeMillis;
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        return toTransform;
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        try {
            messageDigest.update(String.format("VideoTransform_%s_%s", link, timeMillis).getBytes("utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
