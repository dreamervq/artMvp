package com.example.mydemok.manager

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.text.TextUtils
import androidx.fragment.app.FragmentActivity
import cn.markmjw.platform.QQManager
import cn.markmjw.platform.ShareModel
import cn.markmjw.platform.WechatManager
import cn.markmjw.platform.WeiboManager
import com.bumptech.glide.Glide
import com.example.mydemok.application.Constants
import com.example.mydemok.mvp.ui.dialog.PosterShareDialog
import com.example.mydemok.mvp.ui.listener.OnPlatformClickListener
import com.example.mydemok.mvp.ui.listener.QQUiListener
import com.example.mydemok.utils.AppUtil
import com.example.mylibrary.mvp.uis.activities.BaseActivity
import com.tbruyelle.rxpermissions2.RxPermissions
import me.jessyan.art.base.BaseApplication
import me.jessyan.art.base.Platform
import me.jessyan.art.utils.ArtUtils
import me.jessyan.art.utils.PermissionUtil
import me.jessyan.art.utils.PermissionUtil.RequestPermission
import me.jessyan.rxerrorhandler.core.RxErrorHandler
import java.util.*
import java.util.concurrent.ExecutionException

class ShareManager(private val mActivity: FragmentActivity) {
    private var mShareProvider: IShareContentProvider? = null
    var rxErrorHandler: RxErrorHandler
    private var clipboardManager: ClipboardManager? = null

    init {
        rxErrorHandler =
            ArtUtils.obtainAppComponentFromContext(BaseApplication.getInstance()).rxErrorHandler()
    }
    fun setShareContentProvider(provider: IShareContentProvider?) {
        mShareProvider = provider
    }

    fun shareTo(platform: String?) {
        if (mShareProvider == null) {
            return
        }
        when (platform) {
            Platform.QQ -> shareToQQFriend(mShareProvider!!.qqShareModel)
            Platform.QZONE -> shareToQzone(mShareProvider!!.qzoneShareModel)
            Platform.WEIBO -> shareToWeibo(mShareProvider!!.weiboShareModel)
            Platform.WECHAT -> shareToWeChat(
                mShareProvider!!.weChatShareModel,
                WechatManager.TYPE_WECHAT_FRIEND
            )
            Platform.WECHAT_TIMELINE -> shareToWeChat(
                mShareProvider!!.weChatShareModel,
                WechatManager.TYPE_WECHAT_TIMELINE
            )
            Platform.COPY -> shareCopy(mShareProvider!!.copy())
            Platform.HAIBAO -> sharePoster(mShareProvider!!.generatePoster())
        }
    }

    private fun sharePoster(shareModel: ShareModel) {
        var dialog = PosterShareDialog(mActivity)
        dialog.setOnPlatformClickListener(object : OnPlatformClickListener {
            override fun onPlatformClick(platform: String?) {
                val shareModel1 = ShareModel()
                shareModel1.imageUri = dialog.getImage2file()?.path
                shareModel1.shareUrl = shareModel.shareUrl
                shareModel1.title = shareModel.title
                when (platform) {
                    Platform.QQ -> shareToQQFriend(shareModel1)
                    Platform.QZONE -> shareToQzone(shareModel1)
                    Platform.WEIBO -> shareToWeibo(shareModel1)
                    Platform.WECHAT -> sharePicture(
                        shareModel1.imageUri, WechatManager.TYPE_WECHAT_FRIEND
                    )
                    Platform.WECHAT_TIMELINE -> sharePicture(
                        shareModel1.imageUri, WechatManager.TYPE_WECHAT_TIMELINE
                    )
                }
            }
        })
        dialog.show(shareModel.shareUrl, shareModel.title, shareModel.imageUri)
    }

    private fun shareToWeibo(shareModel: ShareModel) {

        // 检查WeiBo是否已安装
        if (!AppUtil.isAppInstalled("com.sina.weibo")) {
            makeToast("您似乎没安装微博呢")
            return
        }
        if (TextUtils.isEmpty(shareModel.imageUri) && !TextUtils.isEmpty(Constants.SHARE_IMAGE_URL)) {
            shareModel.imageUri = Constants.SHARE_IMAGE_URL
        }
        val imageUrl = shareModel.imageUri
        //        String imageUrl = AppConstent.APP_SHARE_IMAGE_URL;
        if (!TextUtils.isEmpty(imageUrl)) {
            PermissionUtil.externalStorage(object : RequestPermission {
                override fun onRequestPermissionSuccess() {
                    Thread(Runnable {
                        try {
                            val target =
                                Glide.with(BaseApplication.getInstance())
                                    .asFile()
                                    .load(imageUrl)
                                    .submit()
                            val imageFile = target.get()
                            shareModel.imageUri = imageFile.absolutePath
                        } catch (e: InterruptedException) {
                        } catch (e: ExecutionException) {
                        }
                        mActivity.runOnUiThread { //                                    mActivity.dismissProgressDialog();
                            WeiboManager.getInstance(mActivity)
                                .sendMessage(mActivity, shareModel)
                        }
                    }).start()
                }

                override fun onRequestPermissionFailure(permissions: List<String>) {}
                override fun onRequestPermissionFailureWithAskNeverAgain(permissions: List<String>) {
                    makeToast("请到手机“设置-应用管理-权限管理”开启读写存储权限后再分享")
                }
            }, RxPermissions(mActivity), rxErrorHandler)
        } else {
            WeiboManager.getInstance(mActivity).sendMessage(mActivity, shareModel)
        }
    }

    private fun shareToWeChat(shareModel: ShareModel, type: Int) {
        // 检查微信是否已安装
        if (!WechatManager.getInstance(mActivity).isInstalled) {
            makeToast("您似乎没安装微信呢")
            return
        }

        // 检查是否支持分享朋友圈
        if (WechatManager.TYPE_WECHAT_TIMELINE == type && !WechatManager.getInstance(mActivity)
                .isSupported
        ) {
            makeToast("您的微信版本不支持分享到朋友圈")
            return
        }
        if (TextUtils.isEmpty(shareModel.imageUri) && !TextUtils.isEmpty(Constants.SHARE_IMAGE_URL)) {
            shareModel.imageUri = Constants.SHARE_IMAGE_URL
        }
        val imageUrl = shareModel.imageUri
        //        String imageUrl = Constants.APP_SHARE_IMAGE_URL;
        if (!TextUtils.isEmpty(imageUrl)) {
            PermissionUtil.externalStorage(object : RequestPermission {
                override fun onRequestPermissionSuccess() {
                    Thread(Runnable {
                        val target =
                            Glide.with(BaseApplication.getInstance())
                                .asBitmap()
                                .load(imageUrl)
                                .submit()
                        try {
                            val imageFile = target.get()
                            shareModel.thumbnail = imageFile
                        } catch (e: InterruptedException) {
                            e.printStackTrace()
                        } catch (e: ExecutionException) {
                            e.printStackTrace()
                        }
                        var share_type: Int = WechatManager.SHARE_TYPE_H5
                        if (shareModel.isIsmini()) {
                            share_type = WechatManager.SHARE_TYPE_MINI
                        }
                        val bitmap = WechatManager.getInstance(mActivity)
                            .zoomOut(shareModel.thumbnail, share_type)
                        shareModel.thumbnail = bitmap
                        mActivity.runOnUiThread {
                            if (WechatManager.TYPE_WECHAT_FRIEND == type) {
                                shareWeChatFriend(shareModel)
                            } else if (WechatManager.TYPE_WECHAT_TIMELINE == type) {
                                shareWeChatTimeLine(shareModel)
                            }

//                                    mActivity.dismissProgressDialog();
                        }
                    }).start()
                }

                override fun onRequestPermissionFailure(permissions: List<String>) {}
                override fun onRequestPermissionFailureWithAskNeverAgain(permissions: List<String>) {
                    makeToast("没有开启权限，无发使用分享功能")
                }
            }, RxPermissions(mActivity), rxErrorHandler)
        } else {
            if (WechatManager.TYPE_WECHAT_FRIEND == type) {
                shareWeChatFriend(shareModel)
            } else if (WechatManager.TYPE_WECHAT_TIMELINE == type) {
                shareWeChatTimeLine(shareModel)
            }
        }
    }

    private fun sharePicture(bitmap: String, shareType: Int) {
        val manager = WechatManager.getInstance(mActivity)
        if (shareType == WechatManager.TYPE_WECHAT_FRIEND) {
            manager.sendFriend(bitmap, ByteArray(0))
        } else {
            manager.sendTimeLine(bitmap, ByteArray(0))
        }
    }

    private fun shareWeChatFriend(shareModel: ShareModel) {
        val manager = WechatManager.getInstance(mActivity)
        if (shareModel.isIsmini()) {
            manager.sendFriend(
                shareModel.title,
                shareModel.description,
                shareModel.shareUrl,
                shareModel.thumbnail,
                shareModel.getMiniusername(),
                shareModel.getMinipath()
            )
        } else {
            manager.sendFriend(
                shareModel.title,
                shareModel.description,
                shareModel.shareUrl,
                shareModel.thumbnail
            )
        }
    }

    private fun shareWeChatTimeLine(shareModel: ShareModel) {
        val manager = WechatManager.getInstance(mActivity)
        manager.sendTimeLine(
            shareModel.title,
            shareModel.description,
            shareModel.shareUrl,
            shareModel.thumbnail
        )
    }

    private fun shareToQQFriend(model: ShareModel) {
        // 检查QQ是否已安装
        if (!QQManager.isInstalled(mActivity)) {
            makeToast("您似乎没安装QQ呢")
            return
        }
        if (TextUtils.isEmpty(model.imageUri) && !TextUtils.isEmpty(Constants.SHARE_IMAGE_URL)) {
            model.imageUri = Constants.SHARE_IMAGE_URL
        }
        val instance = QQManager.getInstance(mActivity)
        when (model.imageType) {
            ShareModel.IMAGE_FILE -> instance.shareToQQWithImage(
                mActivity,
                model.imageUri,
                QQUiListener.stance
            )
            else ->                 //instance.shareToQQWithNetworkImage(mActivity, model.getTitle(), model.getDescription(), model.getShareUrl(), model.getImageUri());
                instance.shareToQQWithNetworkImage(
                    mActivity,
                    model.title,
                    model.description,
                    model.shareUrl,
                    model.imageUri,
                    QQUiListener.stance
                )
        }
    }

    private fun shareToQzone(model: ShareModel) {
        // 检查QQ是否已安装
        if (!QQManager.isInstalled(mActivity)) {
            makeToast("您似乎没安装QQ呢")
            return
        }
        val images = ArrayList<String>()
        if (TextUtils.isEmpty(model.imageUri) && !TextUtils.isEmpty(Constants.SHARE_IMAGE_URL)) {
            model.imageUri = Constants.SHARE_IMAGE_URL
        }
        //        String imageUri = Constants.APP_SHARE_IMAGE_URL;
        val imageUri = model.imageUri
        if (!TextUtils.isEmpty(imageUri)) {
            images.add(imageUri)
        } else {
//            images.add(Constants.APP_SHARE_IMAGE_URL);
        }
        QQManager.getInstance(mActivity).shareToQzoneWithNetWorkImages(
            mActivity, model.title, model.description, model.shareUrl,
            images, QQUiListener.stance
        )
    }

    private fun makeToast(str: String) {
        try {
            ArtUtils.makeText(mActivity, str)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun shareCopy(str: String) {
        if (clipboardManager == null) clipboardManager =
            mActivity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        //创建ClipData对象
        val clipData = ClipData.newPlainText("copystr", str)
        //添加ClipData对象到剪切板中
        clipboardManager!!.primaryClip = clipData
        makeToast("复制成功")
    }
}