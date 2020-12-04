package me.jessyan.art.ui.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.text.TextUtils;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;
import java.io.InputStream;

import me.jessyan.art.R;
import me.jessyan.art.http.imageloader.glide.GlideArt;
import me.jessyan.art.ui.util.glide.GlideBlurformation;
import me.jessyan.art.ui.util.glide.GlideCenterCropRoundTransform;
import me.jessyan.art.ui.util.glide.GlideCircleShadowTransform;
import me.jessyan.art.ui.util.glide.GlideDynamicUrl;
import me.jessyan.art.ui.util.glide.VideoFrameBitmapTransformation;
import okhttp3.OkHttpClient;

import static com.bumptech.glide.load.resource.bitmap.VideoDecoder.FRAME_OPTION;


public class GlideUtils {

    public static int getLoadingRes(boolean ratioLoding) {
        return ratioLoding ? R.drawable.img_loading_1_1 : R.drawable.img_loading_16_9;
    }

    public static void loadDrawable(Context context, String url, boolean ratioLoding, final BitmapReadyCallBack callBack) {
        GlideArt.with(context)
                .load(new GlideDynamicUrl(getNotEmptyUrl(url)))
                .placeholder(getLoadingRes(ratioLoding))
                .dontAnimate()
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        if (callBack != null) {
                            callBack.onExceptoin(e);
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        int w = resource.getIntrinsicWidth();
                        int h = resource.getIntrinsicHeight();
//                        Bitmap.Config config =
//                                resource.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
//                                        : Bitmap.Config.RGB_565;
//                        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
//                        //注意，下面三行代码要用到，否则在View或者SurfaceView里的canvas.drawBitmap会看不到图
//                        Canvas canvas = new Canvas(bitmap);
//                        resource.setBounds(0, 0, w, h);
//                        resource.draw(canvas);
                        if (callBack != null) {
                            callBack.onBitmapReady(resource, w, h);
                        }
                    }
                });
    }

    public static void loadDrawable(Context context, File file, boolean ratioLoding, final BitmapReadyCallBack callBack) {
        GlideArt.with(context)
                .load(file)
                .placeholder(getLoadingRes(ratioLoding))
                .dontAnimate()
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        if (callBack != null) {
                            callBack.onExceptoin(e);
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        int w = resource.getIntrinsicWidth();
                        int h = resource.getIntrinsicHeight();
//                        Bitmap.Config config =
//                                resource.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
//                                        : Bitmap.Config.RGB_565;
//                        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
//                        //注意，下面三行代码要用到，否则在View或者SurfaceView里的canvas.drawBitmap会看不到图
//                        Canvas canvas = new Canvas(bitmap);
//                        resource.setBounds(0, 0, w, h);
//                        resource.draw(canvas);
                        if (callBack != null) {
                            callBack.onBitmapReady(resource, w, h);
                        }
                    }
                });
    }


    /**
     * 加载普通图片（http://或者file://）
     */
    public static void loadImage(Context context, String url, boolean ratioLoding, ImageView imageView) {
//        GlideArt.with(context)
//                .load(url)
//                .placeholder(R.drawable.img_loading)
//                .error(R.drawable.img_loading)
//                .into(imageView);
        if (!TextUtils.isEmpty(url) && url.startsWith("http")) {
            loadImageWithHolderError(context, url, imageView, getLoadingRes(ratioLoding), getLoadingRes(ratioLoding));
        } else {
            loadImageWithHolderError(context, url, imageView, getLoadingRes(ratioLoding), getLoadingRes(ratioLoding));
        }
    }

    public static void loadImageWithHolderError(Context context, String url, ImageView imageView, int holderRes, int errorRes) {
        GlideArt.with(context)
                .load(new GlideDynamicUrl(getNotEmptyUrl(url)))
                .placeholder(holderRes)
                .error(errorRes)
                .dontAnimate()
                .into(imageView);
    }

    public static void loadFrameWithHolderError(Context context, String url, ImageView imageView, int holderRes, int errorRes) {
        GlideArt.with(context)
                .load(new GlideDynamicUrl(getNotEmptyUrl(url)))
                .placeholder(holderRes)
                .error(errorRes)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(imageView);
    }

    /**
     * 加载普通图片（http://或者file://）
     */
    public static void loadAgateImage(Context context, String url, boolean isVideo, boolean ratioLoding, ImageView imageView) {
        if (isVideo) {
            if (!TextUtils.isEmpty(url) && url.startsWith("http")) {
                loadFrameWithHolderError(context,  url, imageView, getLoadingRes(ratioLoding), getLoadingRes(ratioLoding));
            } else {
                loadLocalFrame(context, url, 1, ratioLoding, imageView);
            }
        } else {
            if (!TextUtils.isEmpty(url) && url.startsWith("http") && isVideo(url)) {
                loadImageWithHolderError(context, url, imageView, getLoadingRes(ratioLoding), getLoadingRes(ratioLoding));
            } else {
                loadImageWithHolderError(context, url, imageView, getLoadingRes(ratioLoding), getLoadingRes(ratioLoding));
            }
        }
    }

    /**
     * 加载普通图片（http://或者file://）
     */
    public static void loadAgateDrawable(Context context, String url, boolean isVideo, boolean ratioLoding, BitmapReadyCallBack callBack) {
        if (isVideo) {
            if (!TextUtils.isEmpty(url) && url.startsWith("http")) {
                loadDrawable(context, url, ratioLoding, callBack);
            } else {
                loadLocalFrameDrawable(context, url, 1, ratioLoding, callBack);
            }
        } else {
//            loadDrawable(context, url, callBack);
            if (!TextUtils.isEmpty(url) && url.startsWith("http") && isVideo(url)) {
                loadDrawable(context, url, ratioLoding, callBack);
            } else {
                loadDrawable(context, url, ratioLoding, callBack);
            }
        }
    }

    /**
     * 加载普通图片（http://或者file://）
     */
    public static void loadRoundImage(Context context, String url, boolean ratioLoding, ImageView imageView, int dp) {
        GlideArt.with(context)
                .load(new GlideDynamicUrl(getNotEmptyUrl( url)))
                .apply(RequestOptions.bitmapTransform(new GlideCenterCropRoundTransform(url, dp)))
                .placeholder(getLoadingRes(ratioLoding))
                .error(getLoadingRes(ratioLoding))
                .into(imageView);
    }

    /**
     * 加载普通圆角图片 File
     */
    public static void loadRountImage(Context context, File file, boolean ratioLoding, ImageView imageView, int dp) {
        GlideArt.with(context)
                .load(file)
                .placeholder(getLoadingRes(ratioLoding))
                .apply(RequestOptions.bitmapTransform(new GlideCenterCropRoundTransform(file.getAbsolutePath(), dp)))//圆角半径
                .error(getLoadingRes(ratioLoding))
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(imageView);
    }

    /**
     * 加载普通图片（http://或者file://）高斯模糊
     */
    public static void loadBlurImage(Context context, String url, boolean ratioLoding, ImageView imageView) {
        GlideArt.with(context)
                .load(new GlideDynamicUrl(getNotEmptyUrl( url)))
                .apply(RequestOptions.bitmapTransform(new GlideBlurformation(context)))
                .placeholder(getLoadingRes(ratioLoding))
                .error(getLoadingRes(ratioLoding))
                .into(imageView);
    }

    /**
     * 加载普通图片 File
     */
    public static void loadImage(Context context, File file, boolean ratioLoding, ImageView imageView) {
        GlideArt.with(context)
                .load(file)
                .placeholder(getLoadingRes(ratioLoding))
                .error(getLoadingRes(ratioLoding))
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(imageView);
    }

    /**
     * 加载图片
     */
    public static void loadImage(Context context, int source, boolean ratioLoding, ImageView imageView) {
        GlideArt.with(context)
                .load(source)
                .placeholder(getLoadingRes(ratioLoding))
                .error(getLoadingRes(ratioLoding))
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(imageView);
    }

    /**
     * 加载为圆形图片（一般为头像加载）
     */
    public static void loadCircleImage(Context context, String url, boolean ratioLoding, ImageView imageView) {
        GlideArt.with(context)
                .load(new GlideDynamicUrl(getNotEmptyUrl(url)))
                .placeholder(getLoadingRes(ratioLoding))
                .error(getLoadingRes(ratioLoding))
                .circleCrop()
                .into(imageView);
    }

    /**
     * 加载为圆形图片（一般为头像加载）
     */
    public static void loadAvatarImage(Context context, String url, ImageView imageView) {
        GlideArt.with(context)
                .load(new GlideDynamicUrl(getNotEmptyUrl( url)))
                .placeholder(R.drawable.img_default_avatar)
                .error(R.drawable.img_default_avatar)
                .circleCrop()
                .into(imageView);
    }

    /**
     * 加载为圆形图片附带光晕（一般为头像加载）
     */
    public static void loadAvatarImageShadow(Context context, String url, int shadowColor, int shadowRadius, ImageView imageView) {
        GlideArt.with(context)
                .load(new GlideDynamicUrl(getNotEmptyUrl(url)))
                .apply(RequestOptions.placeholderOf(R.drawable.img_loading_transparent)
                        .error(R.drawable.img_default_avatar)
                        .transform(new GlideCircleShadowTransform(url, shadowColor, shadowRadius)))
//                .thumbnail(loadCircleShadowTransform(context, R.drawable.img_default_avatar, shadowColor, shadowRadius))
//                .thumbnail(loadCircleShadowTransform(context, R.drawable.img_default_avatar, shadowColor, shadowRadius))
                .into(imageView);
    }

    private static RequestBuilder<Drawable> loadCircleShadowTransform(Context context, @DrawableRes int placeholderId, int shadowColor, int shadowRadius) {
        return GlideArt.with(context)
                .load(placeholderId)
                .apply(new RequestOptions().centerCrop()
                        .transform(new GlideCircleShadowTransform(String.valueOf(placeholderId), shadowColor, shadowRadius)));

    }

    /**
     * 加载为圆形图片（一般为头像加载）
     */
    public static void loadAvatarImage(Context context, File file, ImageView imageView) {
        GlideArt.with(context)
                .load(file)
                .placeholder(R.drawable.img_default_avatar)
                .error(R.drawable.img_default_avatar)
                .circleCrop()
                .into(imageView);
    }

    /**
     * 加载为圆形图片附带光晕（一般为头像加载）
     */
    public static void loadAvatarImageShadow(Context context, File file, int shadowColor, int shadowRadius, ImageView imageView) {
        GlideArt.with(context)
                .load(file)
                .apply(RequestOptions.placeholderOf(R.drawable.img_default_avatar)
                        .error(R.drawable.img_default_avatar)
                        .transform(new GlideCircleShadowTransform(file.getAbsolutePath(), shadowColor, shadowRadius)))
                .thumbnail(loadCircleShadowTransform(context, R.drawable.img_default_avatar, shadowColor, shadowRadius))
                .thumbnail(loadCircleShadowTransform(context, R.drawable.img_default_avatar, shadowColor, shadowRadius))
                .into(imageView);
    }

    /**
     * 加载为圆形图片（一般为头像加载）
     */
    public static void loadAvatarImageTransparent(Context context, String url, ImageView imageView) {
        GlideArt.with(context)
                .load(new GlideDynamicUrl(getNotEmptyUrl(url)))
                .placeholder(R.drawable.bg_transparent_avatar)
                .error(R.drawable.bg_transparent_avatar)
                .circleCrop()
                .into(imageView);
    }

    /**
     * 加载本地图片（资源文件）
     */
    public static void loadLocalImage(Context context, int resId, ImageView imageView) {
        GlideArt.with(context)
                .load(resId)
                .into(imageView);
    }

    /**
     * 加载本地图片（资源文件）
     */
    public static void loadLocalCircleImage(Context context, int resId, ImageView imageView) {
        GlideArt.with(context)
                .load(resId)
                .circleCrop()
                .into(imageView);
    }

    /**
     * @param okHttpClient 使用Https时的，Net中的okHttpClient
     */
    public static void registerHttps(Context context, OkHttpClient okHttpClient) {
        GlideArt.get(context)
                .getRegistry()
                .replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory());
    }


//    /**
//     * 加载普通图片（http://或者file://）
//     */
//    public static void loadImage(Context context, String url, ImageView imageView) {
//        Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.SOURCE).placeholder(R.drawable.img_loading).error(R.drawable.img_loading).into(imageView);
//    }
//
//    /**
//     * 加载为圆形图片（一般为头像加载）
//     */
//    public static void loadCircleImage(Context context, String url, ImageView imageView) {
//        Glide.with(context).load(url).placeholder(R.drawable.img_default_avatar).error(R.drawable.img_default_avatar).
//                transform(new GlideCircleTransform(context)).into(imageView);
//    }
//
//    /**
//     * 加载本地图片（资源文件）
//     */
//    public static void loadLocalImage(Context context, int resId, ImageView imageView) {
//        Glide.with(context).load(resId).into(imageView);
//    }
//
//    /**
//     * 加载本地图片（资源文件）
//     */
//    public static void loadLocalCircleImage(Context context, int resId, ImageView imageView) {
//        Glide.with(context).load(resId).transform(new GlideCircleTransform(context)).into(imageView);
//    }
//
//    /**
//     * @param okHttpClient 使用Https时的，Net中的okHttpClient
//     */
//    public static void registerHttps(Context context, OkHttpClient okHttpClient) {
//        Glide.get(context).register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(okHttpClient));
//    }

    public interface BitmapReadyCallBack {
        void onExceptoin(Exception e);

        void onBitmapReady(Drawable drawable, int width, int height);
    }

    public static void loadLocalFrameDrawable(final Context context, String url, long timeMillis, boolean ratioLoding, final BitmapReadyCallBack callBack) {
        GlideArt.with(context)
                .load(new GlideDynamicUrl(getNotEmptyUrl(url)))
                .apply(RequestOptions.frameOf(timeMillis)
                        .set(FRAME_OPTION, MediaMetadataRetriever.OPTION_CLOSEST)
                        .transform(new VideoFrameBitmapTransformation(url, timeMillis)))
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .placeholder(getLoadingRes(ratioLoding))
                .error(getLoadingRes(ratioLoding))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        if (callBack != null) {
                            callBack.onExceptoin(e);
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        int w = resource.getIntrinsicWidth();
                        int h = resource.getIntrinsicHeight();
                        if (callBack != null) {
                            callBack.onBitmapReady(resource, w, h);
                        }
                    }
                });
    }

    public static void loadLocalFrame(final Context context, String url, long timeMillis, boolean ratioLoding, ImageView imageView) {
        GlideArt.with(context)
                .load(new GlideDynamicUrl(getNotEmptyUrl(url)))
                .apply(RequestOptions.frameOf(timeMillis)
                        .set(FRAME_OPTION, MediaMetadataRetriever.OPTION_CLOSEST)
                        .transform(new VideoFrameBitmapTransformation(url, timeMillis)))
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .placeholder(getLoadingRes(ratioLoding))
                .error(getLoadingRes(ratioLoding))
                .dontAnimate()
                .into(imageView);
    }

    public static void loadLocalFrame(final Context context, File file, long timeMillis, boolean ratioLoding, ImageView imageView) {
        GlideArt.with(context)
                .load(file)
                .apply(RequestOptions.frameOf(timeMillis)
                        .set(FRAME_OPTION, MediaMetadataRetriever.OPTION_CLOSEST)
                        .transform(new VideoFrameBitmapTransformation(file.getAbsolutePath(), timeMillis)))
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .placeholder(getLoadingRes(ratioLoding))
                .error(getLoadingRes(ratioLoding))
                .dontAnimate()
                .into(imageView);
    }

    final static String[] videoSuffix = new String[]{".avi", ".wmv", ".mpeg", ".mp4", ".mov", ".moov", ".mkv", ".flv", ".f4v", ".m4v", ".rmvb", ".rm", ".3gp", ".dat", ".ts", ".mts", ".vob"};

    public static boolean isVideo(String imgLink) {
        boolean result = false;
        if (!TextUtils.isEmpty(imgLink) && imgLink.contains(".")) {
            String suffix = imgLink.substring(imgLink.lastIndexOf("."));
            if (!TextUtils.isEmpty(suffix)) {
                for (int i = 0; i < videoSuffix.length; i++) {
                    if (TextUtils.equals(videoSuffix[i], suffix)) {
                        result = true;
                        break;
                    }
                }
            }
        }
        return result;
    }

    public static String getNotEmptyUrl(String url) {
        return TextUtils.isEmpty(url) ? "empty url" : url;
    }

    public static String getQiniuVideoFrame(String url, long time) {
        return String.format("%s?vframe/jpg/offset/%s", url, time);
    }

//    public static String getOssVideoFrame(String url, long time) {
//        if (url == null) {
//            url = "";
//        }
//        String result;
//        if (url.contains("?")) {
//            result = String.format("%s&x-oss-process=video/snapshot,t_%s,f_png,w_0,h_0,m_fast", url, time);
//        } else {
//            result = String.format("%s?x-oss-process=video/snapshot,t_%s,f_png,w_0,h_0,m_fast", url, time);
//        }
//        return result;
//    }
}
