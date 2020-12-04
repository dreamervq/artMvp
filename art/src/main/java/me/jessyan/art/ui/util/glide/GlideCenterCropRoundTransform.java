package me.jessyan.art.ui.util.glide;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;

import java.security.MessageDigest;

import me.jessyan.art.ui.util.PixelUtil;

public class GlideCenterCropRoundTransform extends CenterCrop {

    private float radius;
    private String url;

    public GlideCenterCropRoundTransform(String url, int dp) {
        super();
        this.url = url;
        radius = PixelUtil.dp2px(dp);
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return roundCrop(pool, super.transform(pool, toTransform, outWidth, outHeight));
    }

    private Bitmap roundCrop(BitmapPool pool, Bitmap source) {
        if (source == null)
            return null;
        Bitmap result = pool.get(source.getWidth(), source.getHeight(),
                Bitmap.Config.ARGB_8888);
//        if (result == null) {
//            result = Bitmap.createBitmap(source.getWidth(), source.getHeight(),
//                    Bitmap.Config.ARGB_8888);
//        }
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP,
                BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        RectF rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight());
        canvas.drawRoundRect(rectF, radius, radius, paint);
        return result;
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        try {
            messageDigest.update(String.format("%s_round_%s", url, radius).getBytes("utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
