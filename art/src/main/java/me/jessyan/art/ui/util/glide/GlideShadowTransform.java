package me.jessyan.art.ui.util.glide;

import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

public class GlideShadowTransform extends BitmapTransformation {
    String url;
    int shadowColor;
    int shadowRadius;

    public GlideShadowTransform(String url, int shadowColor, int shadowRadius) {
        this.url = url;
        this.shadowColor = shadowColor;
        this.shadowRadius = shadowRadius;
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        return addShadow(pool, toTransform, outWidth, outHeight);
    }

    Bitmap addShadow(BitmapPool pool, Bitmap source, int outWidth, int outHeight) {
        if (source == null) return null;
        int mBitmapWidth = source.getWidth();
        int mBitmapHeight = source.getHeight();
        Bitmap result = pool.get(mBitmapWidth + shadowRadius * 2, mBitmapHeight + shadowRadius * 2, Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(mBitmapWidth + shadowRadius * 2, mBitmapHeight + shadowRadius * 2, Bitmap.Config.ARGB_8888);
        }

        Canvas mCanvas = new Canvas(result);
        //设置抗锯齿
        mCanvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        Paint mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setFilterBitmap(true);
        mPaint.setColor(shadowColor);
        //外发光
        mPaint.setMaskFilter(new BlurMaskFilter(shadowRadius, BlurMaskFilter.Blur.OUTER));
        //从原位图中提取只包含alpha的位图
        Bitmap alphaBitmap = source.extractAlpha();
        //在画布上（mHaloBitmap）绘制alpha位图
        mCanvas.drawBitmap(alphaBitmap, shadowRadius, shadowRadius, mPaint);
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setFilterBitmap(true);
        mCanvas.drawBitmap(source, null, new Rect(shadowRadius + 1, shadowRadius + 1, shadowRadius + mBitmapWidth - 1, shadowRadius + mBitmapHeight - 1), null);
        return result;
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        try {
            messageDigest.update(String.format("%s_shadow_%s_%s", url, shadowRadius, shadowColor).getBytes("utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
