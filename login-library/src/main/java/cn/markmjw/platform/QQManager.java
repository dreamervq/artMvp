package cn.markmjw.platform;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.open.utils.SystemUtils;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.util.ArrayList;

/**
 * QQ管理器
 *
 * @author leewenbil
 * @date 2015-03-02
 */
public class QQManager {
    public static final String TAG = "QQManager";

    private static final int TYPE_QQ = 0;
    private static final int TYPE_QZONE = 1;

    private static QQManager sInstance;

    private Tencent mTencent;

    private QQManager(Context context) {
        mTencent = Tencent.createInstance(PlatformConfig.getInstance().getQQId(),
                context);
    }

    public synchronized static QQManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new QQManager(context);
        }
        return sInstance;
    }

    /**
     * 是否安装QQ客户端
     *
     * @return
     */
    public static boolean isInstalled(Context context) {
        return SystemUtils.checkMobileQQ(context);
    }

    /**
     * 获取QQ API
     *
     * @return
     */
    public Tencent getTencent() {
        return mTencent;
    }

    /**
     * 分享到qq好友(本地图片，注：如果没有安装qq客户端，有问题，坑啊)
     *
     * @param activity
     * @param title     标题
     * @param summary   分享内容、摘要
     * @param targetUrl 点击跳转的url
     * @param imagePath 图片路径
     */
    public void shareToQQWithLocalImage(Activity activity, String title, String summary,
                                        String targetUrl, String imagePath, IUiListener listener) {
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, summary);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, targetUrl);
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, imagePath);
        mTencent.shareToQQ(activity, params, listener);

    }

    /**
     * 分享到qq好友（网络图片）
     *
     * @param activity
     * @param title     标题
     * @param summary   分享内容、摘要
     * @param targetUrl 点击跳转的url
     * @param imageUrl  图片url
     */
    public void shareToQQWithNetworkImage(Activity activity, String title, String summary,
                                          String targetUrl, String imageUrl) {
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, summary);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, targetUrl);
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imageUrl);
        mTencent.shareToQQ(activity, params, new BaseUiListener(TYPE_QQ));
    }

    public void shareToQQWithNetworkImage(Activity activity, String title, String summary,
                                          String targetUrl, String imageUrl, IUiListener listener) {
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, summary);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, targetUrl);
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imageUrl);
        mTencent.shareToQQ(activity, params, listener);
    }

    /**
     * 分享本地图片到QQ
     *
     * @param activity
     * @param imageUrl
     */
    public void shareToQQWithImage(Activity activity, String imageUrl,IUiListener listener) {
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, imageUrl);
        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_TYPE_APP);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "全景遂宁");
        mTencent.shareToQQ(activity, params, listener);
    }

    /**
     * 分享到qq空间（本地图文模式，注：有问题，坑啊，如果安装了qq客户端，分享不了；如果没有安装qq客户端，分享看不到图片）
     * QzoneShare.SHARE_TO_QQ_IMAGE_LOCAL_URL已废弃，分享本地图片统一使用QzoneShare.SHARE_TO_QQ_IMAGE_URL
     *
     * @param activity
     * @param title      标题
     * @param summary    内容、摘要
     * @param targetUrl  跳转url
     * @param imagePaths 图片路径集合
     */
    public void shareToQzoneWithLocalImages(Activity activity, String title, String summary,
                                            String targetUrl, ArrayList<String> imagePaths) {
        shareToQzoneWithLocalImages(activity, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT, title,
                summary, targetUrl, imagePaths);
    }

    /**
     * 分享到qq空间
     *
     * @param activity
     * @param shareType  分享类型（本地图文、本地图片等，注：如果没有安装qq客户端，有问题，坑啊）
     * @param title      标题
     * @param summary    内容、摘要
     * @param targetUrl  跳转url
     * @param imagePaths 图片路径集合
     */
    public void shareToQzoneWithLocalImages(Activity activity, int shareType, String title,
                                            String summary, String targetUrl,
                                            ArrayList<String> imagePaths) {
        final Bundle params = new Bundle();
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, shareType);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, title);
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, summary);
        if (shareType != QzoneShare.SHARE_TO_QZONE_TYPE_APP) {
            params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, targetUrl);
        }
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, imagePaths);
        mTencent.shareToQzone(activity, params, new BaseUiListener(TYPE_QZONE));
    }

    /**
     * 分享到qq空间（图文模式）
     *
     * @param activity
     * @param title     标题
     * @param summary   内容、摘要
     * @param targetUrl 跳转url
     * @param imageUrls 图片url集合
     */
    public void shareToQzoneWithNetWorkImages(Activity activity, String title, String summary,
                                              String targetUrl, ArrayList<String> imageUrls) {
        shareToQzoneWithNetWorkImages(activity, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT, title,
                summary, targetUrl, imageUrls);
    }

    public void shareToQzoneWithNetWorkImages(Activity activity, String title, String summary,
                                              String targetUrl, ArrayList<String> imageUrls, IUiListener listener) {
        shareToQzoneWithNetWorkImages(activity, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT, title,
                summary, targetUrl, imageUrls, listener);
    }

    /**
     * 分享到qq空间
     *
     * @param activity
     * @param shareType 分享类型（图文、图等）
     * @param title     标题
     * @param summary   内容、摘要
     * @param targetUrl 跳转url
     * @param imageUrls 图片url集合
     */
    public void shareToQzoneWithNetWorkImages(Activity activity, int shareType, String title,
                                              String summary, String targetUrl,
                                              ArrayList<String> imageUrls) {
        final Bundle params = new Bundle();
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, shareType);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, title);
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, summary);
        if (shareType != QzoneShare.SHARE_TO_QZONE_TYPE_APP) {
            params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, targetUrl);
        }
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);
        mTencent.shareToQzone(activity, params, new BaseUiListener(TYPE_QZONE));
    }

    public void shareToQzoneWithNetWorkImages(Activity activity, int shareType, String title,
                                              String summary, String targetUrl,
                                              ArrayList<String> imageUrls, IUiListener listener) {
        final Bundle params = new Bundle();
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, shareType);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, title);
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, summary);
        if (shareType != QzoneShare.SHARE_TO_QZONE_TYPE_APP) {
            params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, targetUrl);
        }
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);
        mTencent.shareToQzone(activity, params, listener);
    }

    private class BaseUiListener implements IUiListener {
        private final int mShareType;

        public BaseUiListener(int shareType) {
            mShareType = shareType;
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onComplete(Object response) {
            switch (mShareType) {
                case TYPE_QQ:

                    break;

                case TYPE_QZONE:

                    break;
            }
        }

        @Override
        public void onError(UiError e) {
            Log.e(TAG, "QQ share error: " + e.errorMessage);
        }
    }
}
