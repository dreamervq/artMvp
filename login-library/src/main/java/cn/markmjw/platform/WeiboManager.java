package cn.markmjw.platform;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.sina.weibo.sdk.ApiUtils;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMessage;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.call.WeiboNotInstalledException;
import com.sina.weibo.sdk.call.WeiboPageUtils;
import com.sina.weibo.sdk.utils.Utility;

import cn.markmjw.platform.util.ImageUtil;

/**
 * 使用微博相关功能管理器
 *
 * @author markmjw
 * @date 2015-03-05
 */
public class WeiboManager {
    /**
     * 分享微博最大字数
     */
    public static final int TEXT_MAX_LENGTH = 140;

    private static final String TAG = "WeiboManager";

    private static WeiboManager sInstance;

    private final IWeiboShareAPI mWeiboShareAPI;

    private Context mContext;

    private WeiboManager(Context context) {
        mContext = context;
        // 创建微博分享接口实例
        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(context.getApplicationContext(),
                PlatformConfig.getInstance().getWeiboKey());
        mWeiboShareAPI.registerApp();
    }

    /**
     * 获取实例
     *
     * @return
     */
    public synchronized static WeiboManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new WeiboManager(context);
        }
        return sInstance;
    }

    /**
     * 去用户Page【客户端】
     *
     * @param context
     * @param uid
     */
    public static void goUserPageByApp(Context context, String uid) {
        try {
            WeiboPageUtils.viewUserInfo(context, uid + "", "", null);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    /**
     * 去用户Page【浏览器】
     *
     * @param context
     * @param uid
     */
    public static void goUserPageByBrowser(Context context, String uid) {
        try {
            Uri uri = Uri.parse("http://m.weibo.cn/u/" + uid);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    /**
     * 去微博详情page【客户端】
     *
     * @param context
     * @param uid
     * @param weiboId
     */
    public static void goWeiboDetailByApp(Context context, String uid, long weiboId) {
        try {
            WeiboPageUtils.weiboDetail(context, weiboId + "", null);
        } catch (WeiboNotInstalledException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    /**
     * 去微博详情page【浏览器】
     *
     * @param context
     * @param uid
     * @param weiboId
     */
    public static void goWeiboDetailByBrowser(Context context, String uid, long weiboId) {
        try {
            Uri uri = Uri.parse("http://m.weibo.cn/" + uid + "/" + weiboId);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    /**
     * 截取分享微博内容
     *
     * @param text
     * @param length
     * @return
     */
    public static String getContent(String text, int length) {
        // 其实这里有一个很大的缺陷， 我们将所有非 ASCII 码中的字符全部按照 1 的中国汉字大小计算了
        StringBuffer sb = new StringBuffer();
        int total = 0;
        boolean isEmptyText = true;
        char ch = 0;
        for (int i = 0, len = text.length(); i < len; i++) {
            ch = (char) text.codePointAt(i);
            // 文本含有不是空格和回车的字符
            if (isEmptyText && ch != ' ' && ch != '\n') {
                isEmptyText = false;
            }
            total += ch > 255 ? 2 : 1;
            if ((int) Math.ceil(total / 2.) <= length) {
                sb.append(ch);
            } else {
                break;
            }
        }
        if (isEmptyText) {
            // 文本只含有空格和回车
            return null;
        }
        return sb.toString();
    }

    /**
     * 计算微博字数
     *
     * @param text
     * @return
     */
    public static int getContentLength(String text) {
        // 其实这里有一个很大的缺陷， 我们将所有非 ASCII 码中的字符全部按照 1 的中国汉字大小计算了
        int total = 0;
        boolean isEmptyText = true;
        char ch = 0;
        for (int i = 0, len = text.length(); i < len; i++) {
            ch = (char) text.codePointAt(i);
            // 文本含有不是空格和回车的字符
            if (isEmptyText && ch != ' ' && ch != '\n') {
                isEmptyText = false;
            }
            total += ch > 255 ? 2 : 1;
        }
        if (isEmptyText) {
            // 文本只含有空格和回车
            return 0;
        }
        return (int) Math.ceil(total / 2.);
    }

    /**
     * 是否支持此API
     *
     * @return
     */
    public boolean isInstalled() {
        return mWeiboShareAPI.isWeiboAppInstalled();
    }

    /**
     * 是否支持此API
     *
     * @return
     */
    public boolean isSupportAPI() {
        return mWeiboShareAPI.isWeiboAppSupportAPI();
    }

    /**
     * 处理分享结果
     *
     * @param intent
     * @param response
     */
    public void handleResponse(Intent intent, IWeiboHandler.Response response) {
        mWeiboShareAPI.handleWeiboResponse(intent, response);
    }

    /**
     * 去用户Page
     *
     * @param context
     * @param uid
     */
    public void goUserPage(Context context, String uid) {
        if (!TextUtils.isEmpty(uid)) {
            if (isInstalled()) {
                goUserPageByApp(context, uid);
            } else {
                goUserPageByBrowser(context, uid);
            }
        }
    }

    /**
     * 去微博详情page
     *
     * @param context
     * @param uid
     * @param weiboId
     */
    public void goWeiboDetail(Context context, String uid, long weiboId) {
        if (!TextUtils.isEmpty(uid)) {
            if (isInstalled()) {
                goWeiboDetailByApp(context, uid, weiboId);
            } else {
                goWeiboDetailByBrowser(context, uid, weiboId);
            }
        }
    }

    /**
     * 第三方应用发送请求消息到微博，唤起微博分享界面。
     *
     * @param activity
     * @param model    分享实体
     * @see {@link #sendMultiMessage} 或者 {@link #sendSingleMessage}
     */
    public void sendMessage(Activity activity, ShareModel model) {
        int supportApi = mWeiboShareAPI.getWeiboAppSupportAPI();
        if (supportApi >= ApiUtils.BUILD_INT_VER_2_2) {
            sendMultiMessage(activity, model);
        } else {
            sendSingleMessage(activity, model);
        }
    }

    /**
     * 第三方应用发送请求消息到微博，唤起微博分享界面。
     * 注意：当 {@link IWeiboShareAPI#getWeiboAppSupportAPI()} >= 10351
     * 时，支持同时分享多条消息， 同时可以分享文本、图片以及其它媒体资源（网页、音乐、视频、声音中的一种）。
     *
     * @param activity
     * @param model    分享实体
     */
    private void sendMultiMessage(Activity activity, ShareModel model) {
        // 1. 初始化微博的分享消息
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        String content = model.getText();
        if (!TextUtils.isEmpty(content)) {
            weiboMessage.textObject = getTextObject(content);
        }
        String imagePath = model.getImageUri();
        if (!TextUtils.isEmpty(imagePath) && ShareModel.IMAGE_FILE == model.getImageType()) {
            weiboMessage.imageObject = getImageObject(imagePath);
        }
        if (!TextUtils.isEmpty(model.getShareUrl()) && null != model.getThumbnail()) {
            weiboMessage.mediaObject = getWebPageObject(model);
        }

        // 2. 初始化从第三方到微博的消息请求
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        // 用transaction唯一标识一个请求
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.multiMessage = weiboMessage;
        // 3. 发送请求消息到微博，唤起微博分享界面
        boolean send = mWeiboShareAPI.sendRequest(activity, request);
        Log.d(TAG, "sendMultiMessage:" + send);
    }

    /**
     * 第三方应用发送请求消息到微博，唤起微博分享界面。
     * 当{@link IWeiboShareAPI#getWeiboAppSupportAPI()} < 10351 时，只支持分享单条消息，
     * 即 文本、图片、网页、音乐、视频中的一种，不支持Voice消息。
     *
     * @param activity
     * @param model    分享实体
     */
    private void sendSingleMessage(Activity activity, ShareModel model) {
        // 1. 初始化微博的分享消息
        WeiboMessage weiboMessage = new WeiboMessage();
        String content = model.getText();
        if (!TextUtils.isEmpty(content)) {
            weiboMessage.mediaObject = getTextObject(content);
        }

        String imagePath = model.getImageUri();
        if (!TextUtils.isEmpty(imagePath) && ShareModel.IMAGE_FILE == model.getImageType()) {
            weiboMessage.mediaObject = getImageObject(imagePath);
        }

        if (!TextUtils.isEmpty(model.getShareUrl())) {
            weiboMessage.mediaObject = getWebPageObject(model);
        }

        // 2. 初始化从第三方到微博的消息请求
        SendMessageToWeiboRequest request = new SendMessageToWeiboRequest();
        // 用transaction唯一标识一个请求
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.message = weiboMessage;

        // 3. 发送请求消息到微博，唤起微博分享界面
        mWeiboShareAPI.sendRequest(activity, request);
    }

    /**
     * 创建文本消息对象
     *
     * @param content
     * @return 文本消息对象
     */
    private TextObject getTextObject(String content) {
        TextObject text = new TextObject();
        text.text = content;
        return text;
    }

    /**
     * 创建图片消息对象
     *
     * @param imagePath
     * @return 图片消息对象
     */
    private ImageObject getImageObject(String imagePath) {
        ImageObject image = new ImageObject();
        Bitmap bitmap = ImageUtil.getBitmapFromFile(imagePath);
        if (bitmap != null) {
            image.setImageObject(bitmap);
        }
        return image;
    }

    /**
     * 创建多媒体（网页）消息对象
     *
     * @param model
     * @return 多媒体（网页）消息对象
     */
    private WebpageObject getWebPageObject(ShareModel model) {
        WebpageObject web = new WebpageObject();
        web.identify = Utility.generateGUID();
        web.title = model.getTitle();
        web.description = model.getDescription();

        // 设置 Bitmap 类型的图片到视频对象里
        Bitmap bitmap = WechatManager.getInstance(mContext).zoomOut(model.getThumbnail());
        web.setThumbImage(bitmap);
        web.actionUrl = model.getShareUrl();
        web.defaultText = model.getTitle();
        return web;
    }
}
