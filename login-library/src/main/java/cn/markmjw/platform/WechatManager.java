package cn.markmjw.platform;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMiniProgramObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.File;
import java.util.ArrayList;

import cn.markmjw.platform.util.ImageUtil;

/**
 * 微信分享管理器
 *
 * @author markmjw
 * @date 2014-09-28
 */
public class WechatManager {
    public static final int TYPE_WECHAT_FRIEND = 0;
    public static final int TYPE_WECHAT_TIMELINE = 1;
    public static final int SHARE_TYPE_H5=0;
    public static final int SHARE_TYPE_MINI=1;

    /**
     * Min supported version.
     */
    private static final int MIN_SUPPORTED_VERSION = 0x21020001;

    private static final int MAX_IMAGE_LENGTH = 32 * 1024;
    private static final int DEFAULT_MAX_SIZE = 150;

    /**
     * 小程序 分享
     */
    private static final int MAX_IMAGE_LENGTH_MINI = 128 * 1024;
    private static final int DEFAULT_MAX_SIZE_MINI = 400;

    private static WechatManager sInstance;

    private Context mContext;

    private IWXAPI mApi;

    private WechatManager(Context context) {
        mContext = context.getApplicationContext();
        mApi = WXAPIFactory.createWXAPI(mContext, PlatformConfig.getInstance().getWechatId());
        mApi.registerApp(PlatformConfig.getInstance().getWechatId());
    }

    public synchronized static WechatManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new WechatManager(context);
        }
        return sInstance;
    }

    /**
     * 微信是否已安装
     *
     * @return
     */
    public boolean isInstalled() {
        return mApi.isWXAppInstalled();
    }

    /**
     * 是否支持发送朋友圈
     *
     * @return
     */
    public boolean isSupported() {
        return true;
    }

    /**
     * 是否支持发送朋友圈
     *
     * @return
     */
    public boolean isSupportedTimeline() {
        return mApi.getWXAppSupportAPI() >= MIN_SUPPORTED_VERSION;
    }

    /**
     * 处理分享结果
     *
     * @param intent
     * @param handler
     */
    public void handleResponse(Intent intent, IWXAPIEventHandler handler) {
        mApi.handleIntent(intent, handler);
    }

    /**
     * 获取微信API
     *
     * @return
     */
    public IWXAPI getAPI() {
        return mApi;
    }


    public void sendFriend(String title, String des, String url, Bitmap image,String miniusername,String minipath) {
        WXMiniProgramObject page=new WXMiniProgramObject();
        page.webpageUrl=url;
        page.miniprogramType=WXMiniProgramObject.MINIPTOGRAM_TYPE_RELEASE;
        page.userName=miniusername;
        page.path=minipath;
        WXMediaMessage msg = new WXMediaMessage(page);
        msg.title = title;
        msg.description = des;
        if (null != image) {
            // 缩略图的二进制数据
            msg.thumbData = ImageUtil.bitmapToBytes(image, true);
        }

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        // 用分享的时间来标识唯一的请求
        req.transaction = TYPE_WECHAT_FRIEND + String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;
        boolean send = mApi.sendReq(req);
    }
    /**
     * 分享给微信朋友
     *
     * @param title
     * @param des
     * @param url
     * @param image
     */
    public void sendFriend(String title, String des, String url, Bitmap image) {
          WXWebpageObject page = new WXWebpageObject();
          page.webpageUrl = url;

        WXMediaMessage msg = new WXMediaMessage(page);
        msg.title = title;
        msg.description = des;
        if (null != image) {
            // 缩略图的二进制数据
            msg.thumbData = ImageUtil.bitmapToBytes(image, true);
        }

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        // 用分享的时间来标识唯一的请求
        req.transaction = TYPE_WECHAT_FRIEND + String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;
        boolean send = mApi.sendReq(req);
    }

    /**
     * 分享给微信朋友
     *
     * @param title
     * @param des
     * @param url
     * @param image
     */
    public void sendFriend(String title, String des, String url, byte[] image) {
        WXWebpageObject page = new WXWebpageObject();
        page.webpageUrl = url;

        WXMediaMessage msg = new WXMediaMessage(page);
        msg.title = title;
        msg.description = des;
        if (null != image) {
            // 缩略图的二进制数据
            msg.thumbData = image;
        }

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        // 用分享的时间来标识唯一的请求
        req.transaction = TYPE_WECHAT_FRIEND + String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;
        mApi.sendReq(req);
    }

    /**
     * 分享给微信朋友
     *
     * @param filePath
     * @param image
     */
    public void sendFriend(String filePath, byte[] image) {
        WXImageObject imgObj = new WXImageObject();
        imgObj.setImagePath(filePath);

        final WXMediaMessage msg = new WXMediaMessage();
        if (null != image && image.length > 0) {
            // 缩略图的二进制数据
            msg.thumbData = image;
        }
        msg.mediaObject = imgObj;

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = TYPE_WECHAT_FRIEND + String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;
        mApi.sendReq(req);
    }

    /**
     * 分享给微信朋友
     *
     * @param filePath
     * @param image
     */
    public void sendTimeLine(String filePath, byte[] image) {
        WXImageObject imgObj = new WXImageObject();
        imgObj.setImagePath(filePath);

        final WXMediaMessage msg = new WXMediaMessage();
        if (null != image) {
            // 缩略图的二进制数据
            msg.thumbData = image;
        }
        msg.mediaObject = imgObj;

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = TYPE_WECHAT_TIMELINE + String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        mApi.sendReq(req);
    }

    /**
     * 分享到微信朋友圈
     *
     * @param title
     * @param des
     * @param url
     * @param image
     */
    public void sendTimeLine(String title, String des, String url, Bitmap image) {
        WXWebpageObject page = new WXWebpageObject();
        page.webpageUrl = url;

        WXMediaMessage msg = new WXMediaMessage(page);
        msg.title = title;
        msg.description = des + ">> " + url;
        if (null != image) {
            // 缩略图的二进制数据
            msg.thumbData = ImageUtil.bitmapToBytes(image, true);
        }

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        // 用分享的时间来标识唯一的请求
        req.transaction = TYPE_WECHAT_TIMELINE + String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        boolean send = mApi.sendReq(req);
    }

    /**
     * 分享到微信朋友圈
     *
     * @param title
     * @param des
     * @param url
     * @param image
     */
    public void sendTimeLine(String title, String des, String url, byte[] image) {
        WXWebpageObject page = new WXWebpageObject();
        page.webpageUrl = url;

        WXMediaMessage msg = new WXMediaMessage(page);
        msg.title = title;
        msg.description = des + ">> " + url;
        if (null != image) {
            // 缩略图的二进制数据
            msg.thumbData = image;
        }

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        // 用分享的时间来标识唯一的请求
        req.transaction = TYPE_WECHAT_TIMELINE + String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        mApi.sendReq(req);
    }

    /**
     * 分享给微信好友.[不建议使用]
     *
     * @param text
     * @param file
     */
    public void sendFriend(String text, File file) {
        Intent intent = new Intent();
        ComponentName comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI");
        intent.setComponent(comp);
        intent.setAction("android.intent.action.SEND");

        intent.setType("text/*");
        intent.putExtra(Intent.EXTRA_TEXT, text);

        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));

        mContext.startActivity(intent);
    }

    /**
     * 分享到朋友圈.[不建议使用]
     *
     * @param text
     * @param file
     */
    public void sendTimeLine(String text, File file) {
        Intent intent = new Intent();
        ComponentName comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI");
        intent.setComponent(comp);
        intent.setAction("android.intent.action.SEND");

        intent.setType("text/*");
        intent.putExtra(Intent.EXTRA_TEXT, text);

        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));

        mContext.startActivity(intent);
    }

    /**
     * 分享多图到朋友圈. [不建议使用]
     *
     * @param text
     * @param files
     */
    public void sendImagesToTimeLine(String text, File... files) {
        Intent intent = new Intent();
        ComponentName comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI");
        intent.setComponent(comp);
        intent.setAction(Intent.ACTION_SEND_MULTIPLE);

        intent.setType("text/*");
        intent.putExtra(Intent.EXTRA_TEXT, text);

        ArrayList<Uri> imageUris = new ArrayList<>();
        for (File f : files) {
            imageUris.add(Uri.fromFile(f));
        }
        intent.setType("image/*");
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);

        mContext.startActivity(intent);
    }

    /**
     * 获取合适大小的缩略图字节数组
     *
     * @param bitmap
     * @return
     */
    public byte[] getResizeThumbBytes(Bitmap bitmap) {
        byte[] data = null;
        if (null != bitmap) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();

            if (width <= 0 || height <= 0) return null;

            int w, h;
            float scale = height * 1.0f / width;
            if (width > height) {
                w = DEFAULT_MAX_SIZE;
                h = (int) (w * scale);
            } else {
                h = DEFAULT_MAX_SIZE;
                w = (int) (h / scale);
            }

            data = ImageUtil.bitmapToBytes(ImageUtil.zoom(bitmap, w, h), true);
            while (data.length > MAX_IMAGE_LENGTH) {
                w -= 10;
                h = (int) (w * scale);
                data = ImageUtil.bitmapToBytes(ImageUtil.zoom(bitmap, w, h), true);
            }
        }
        return data;
    }


    public Bitmap zoomOut(Bitmap bitmap,int shareType){
        int max_size=0,max_image_length=0;
        if(shareType==SHARE_TYPE_MINI){
            max_size=DEFAULT_MAX_SIZE_MINI;
            max_image_length=MAX_IMAGE_LENGTH_MINI;
        }else{
            max_size=DEFAULT_MAX_SIZE;
            max_image_length=MAX_IMAGE_LENGTH;
        }
        Bitmap dstBitmap = null;
        if (null != bitmap) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            if (width <= 0 || height <= 0) return null;
            int w, h;
            float scale = height * 1.0f / width;
            if (width > height) {
                w = max_size;
                h = (int) (w * scale);
            } else {
                h = max_size;
                w = (int) (h / scale);
            }
            dstBitmap = ImageUtil.zoom(bitmap, w, h);
            //小程序生成5:4的分享图比例
            if(shareType==SHARE_TYPE_MINI) {
                if (width > height) {
                    dstBitmap = ImageUtil.zoomForWxMini(dstBitmap, dstBitmap.getWidth(), dstBitmap.getWidth() * 4 / 5);
                } else {
                    dstBitmap = ImageUtil.zoomForWxMini(dstBitmap, dstBitmap.getHeight() * 5 / 4, dstBitmap.getHeight());
                }
            }

            byte[] data = ImageUtil.bitmapToBytes(dstBitmap, false);
            while (data.length > max_image_length) {
                dstBitmap.recycle();
                w -= 10;
                h = (int) (w * scale);
                dstBitmap = ImageUtil.zoom(bitmap, w, h);
                data = ImageUtil.bitmapToBytes(dstBitmap, false);
            }
        }
        return dstBitmap;
    }

    public Bitmap zoomOut(Bitmap bitmap) {
        return this.zoomOut(bitmap,0);
    }
}
