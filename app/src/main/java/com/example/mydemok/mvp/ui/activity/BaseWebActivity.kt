package com.example.mydemok.mvp.ui.activity

import android.annotation.TargetApi
import android.app.Activity
import android.content.ClipData
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import com.alipay.sdk.app.H5PayCallback
import com.alipay.sdk.app.PayTask
import com.alipay.sdk.util.H5PayResultModel
import com.example.mydemok.R
import com.example.mydemok.application.Constants
import com.example.mydemok.application.Schema
import com.example.mydemok.manager.LocateManager
import com.example.mydemok.manager.LoginManager
import com.example.mydemok.mvp.ui.dialog.ShareDialog
import com.example.mydemok.utils.AppUtil
import com.example.mydemok.utils.CommonUtil
import com.example.mydemok.utils.FileUtil
import com.example.mylibrary.mvp.uis.activities.BaseHeaderActivity
import com.tbruyelle.rxpermissions2.RxPermissions
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.tencent.smtt.export.external.interfaces.SslError
import com.tencent.smtt.export.external.interfaces.SslErrorHandler
import com.tencent.smtt.export.external.interfaces.WebResourceRequest
import com.tencent.smtt.export.external.interfaces.WebResourceResponse
import com.tencent.smtt.sdk.*
import me.jessyan.art.base.Platform
import me.jessyan.art.mvp.IPresenter
import me.jessyan.art.utils.ArtUtils
import me.jessyan.art.utils.NetworkUtil
import me.jessyan.art.utils.PermissionUtil
import me.jessyan.art.utils.PermissionUtil.RequestPermission
import me.jessyan.art.utils.TinyPref
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.net.URISyntaxException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.set
import android.content.Intent as Intent1

abstract class BaseWebActivity<P : IPresenter> : BaseHeaderActivity<P>() {
    private val REQUEST_CODE_FILE_CHOOSE = 10002
    private val REQUEST_CODE_LOLIPOP = 10003
    private val REQUEST_CODE_VIDEO = 10004
    private val FILE_CHOOSER_RESULT_CODE = 10001
    private var mHeader: HashMap<String, String>? = null
    private var api: IWXAPI? = null
    protected var mWebView: WebView? = null
    private var progressBar: ProgressBar? = null
    private var rootView: ViewGroup? = null
    private var mUrl: String? = null
    private var mTitle: String? = null
    private val mShopCarNum: TextView? = null
    private var mShareDialog: ShareDialog? = null
    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        if (parseIntent()) {
            api = WXAPIFactory.createWXAPI(this, Platform.KEY_WECHAT)
            api?.registerApp(Platform.KEY_WECHAT)
            mWebView = findViewById(R.id.pre_web_view)
            progressBar = findViewById(R.id.pre_web_progress)
            rootView = findViewById(R.id.pre_web_root)
            initWebView()
            if (NetworkUtil.isConnected()) {
                loadData(mUrl!!)
            }
        } else
            finish()

    }

    protected open fun initWebView() {
        LoginManager.getInstance().syncSessionId(mUrl)
        mWebView?.let {
            var webSetting = it.settings
            webSetting.loadWithOverviewMode = true
            webSetting.setGeolocationEnabled(true)
            webSetting.textZoom = 100
            webSetting.pluginState = WebSettings.PluginState.ON
            webSetting.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
            webSetting.javaScriptEnabled = true
            it.addJavascriptInterface(InJavaScriptLocalObj(), "app")
            webSetting.setSupportZoom(false)
            webSetting.useWideViewPort = true
            webSetting.javaScriptCanOpenWindowsAutomatically = true
            webSetting.userAgentString =
                it.settings.userAgentString + "/fs/" + AppUtil.getAppVersion() + "/android/" + AppUtil.getOSVersion()
            it.webViewClient = MyWebViewClient()
            it.webChromeClient = MyWebChromeClient()
            webSetting.setAppCacheEnabled(false)
            webSetting.domStorageEnabled = true
            webSetting.databaseEnabled = true
            webSetting.blockNetworkImage = false
            webSetting.cacheMode = WebSettings.LOAD_NO_CACHE
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                webSetting.mixedContentMode = MIXED_CONTENT_ALWAYS_ALLOW
            }
            webSetting.allowFileAccess = false
            webSetting.setAllowFileAccessFromFileURLs(false)
            webSetting.setAllowUniversalAccessFromFileURLs(false)
            webSetting.setAppCacheEnabled(true)
        }

    }


    protected open fun loadData(url: String) {
        val loginManager: LoginManager = LoginManager.getInstance()
        if (mHeader == null) {
            mHeader = HashMap()
        }
        if (loginManager.isLoginValid) {
            mHeader?.let {
                it["uid"] = loginManager.user.id ?: ""
                it["telephone"] = loginManager.user.phone ?: ""
                it["nickname"] = loginManager.user.nickname ?: ""
                it["sessionid"] = loginManager.user.sessionid ?: ""
                if (url.contains("activity.m.duiba.com.cn"))
                    it["Referer"] = "https://activity.m.duiba.com.cn"
                if (AppUtil.isLocationServicesAvailable(this)) {
                    if (TinyPref.getInstance()
                            .getBoolean("is_located") && LocateManager.instance?.city != null
                    ) {
                        var city = LocateManager.instance?.city
                        it["lat"] = "" + city?.lat
                        it["lon"] = "" + city?.lng
                        it["locationStatus"] = "1"
                    } else {
                        it["locationStatus"] = "2"
                    }
                } else {
                    it["locationStatus"] = "3"
                }

            }


            if (url.startsWith("http")) {
                mWebView?.loadUrl(url, mHeader)
            } else {
                mWebView?.loadData(url, "text/html; charset=UTF-8", null)
            }
        } else {
            mWebView?.loadUrl(url, mHeader)
        }
    }

    protected fun parseIntent(): Boolean {
        val intent = intent
        if (intent != null) {
            val bundle = intent.extras
            if (null != bundle) {
                mUrl = bundle.getString(CommonUtil.KEY_VALUE_1)
                mTitle = bundle.getString(CommonUtil.KEY_VALUE_2)
            }
        }
        return !(TextUtils.isEmpty(mUrl) || !mUrl?.startsWith("http")!!)
    }


    inner class InJavaScriptLocalObj {
        @JavascriptInterface
        fun showShareView(
            title: String?,
            desc: String?,
            imageURL: String?,
            url: String?
        ) {
            runOnUiThread { showShareDialog(title, imageURL, desc, url) }
        }

        @JavascriptInterface
        fun showShareButton(
            title: String?,
            desc: String?,
            imageURL: String?,
            url: String?
        ) {
            runOnUiThread { showShareDialog(title, imageURL, desc, url) }
        }

        @JavascriptInterface
        fun updateShopCart(num: Int) {
            runOnUiThread(Runnable {
                if (mShopCarNum == null) return@Runnable
                if (num == 0) {
                    mShopCarNum.visibility = View.GONE
                } else {
                    mShopCarNum.visibility = View.VISIBLE
                    mShopCarNum.text = num.toString()
                }
            })
        }

        @JavascriptInterface
        fun delete_order() {
            mWebView?.loadUrl(Constants.URL_WEB_HOST.toString() + "shop/myOrders")
        }

        @JavascriptInterface
        fun pop() {
            shortToast("举报成功")
            finish()
        }

        @JavascriptInterface
        fun returnMedia() {
            finish()
        }

        @JavascriptInterface
        fun wxpay(str: String) {
            try {
                println("payTask:::$str")
                val json = JSONObject(str)
                val data = json.getJSONObject("responseData")
                val response = data.getJSONObject("app_response")
                if (null != response && !response.has("retcode")) {
                    val req = PayReq()
                    req.appId = response.getString("appid")
                    req.partnerId = response.getString("partnerid")
                    req.prepayId = response.getString("prepayid")
                    req.nonceStr = response.getString("noncestr")
                    req.timeStamp = response.getString("timestamp")
                    req.packageValue = response.getString("package")
                    req.sign = response.getString("sign")
                    req.extData = "app data" // optional
                    // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                    api?.sendReq(req)
                } else {
                }
            } catch (e: Exception) {
            }
        }

        @JavascriptInterface
        fun alipay(str: String) {
            val s = str.replace("&amp;".toRegex(), "&")
            //   val task = PayTask(this@BaseWebActivity)
            if (!TextUtils.isEmpty(str)) {
                Thread(Runnable {
                    println("payTask:::$s")
                }).start()
            }
        }
    }

    @Synchronized
    protected open fun showShareDialog(
        title: String?,
        image: String?,
        desc: String?,
        shareUrl: String?
    ) {
        if (mShareDialog == null) {
            mShareDialog = ShareDialog(this)
        }
        CommonUtil.showShareDialog(title, image, desc, shareUrl, mShareDialog, this)
    }

    open fun onWebPageFinished(view: View, url: String) {}
    protected inner class MyWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(
            view: WebView,
            url: String
        ): Boolean {
            val params: Array<String>
            if (url.endsWith("xlsx") || url.endsWith("docx") || url.endsWith("pptx")) {
                mWebView?.loadUrl("https://view.officeapps.live.com/op/view.aspx?src=$url")
                return true
            }
            if (url.contains("https://test-geo.720yun.com") || url.contains("https://pano-geo.720yun.com")) {
                val intent = Intent1(Intent1.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
                return true
            }
            if (url.startsWith("tel://")) {
                val phoneNum = url.substring("tel//".length)
                val intent = Intent1(Intent1.ACTION_DIAL)
                val data = Uri.parse("tel:$phoneNum")
                intent.data = data
                startActivity(intent)
                return true
            }
            if (url.startsWith("baidumap")) {
                if (!CommonUtil.isInstallByread("com.baidu.BaiduMap")) {
                    shortToast("您未安装百度地图")
                    return true
                }
                val uri = Uri.parse(url)
                var intent: Intent1? = null
                try {
                    intent = Intent1.getIntent(
                        "intent://map/place/detail?uid=" + uri.getQueryParameter("uid") + "&src" +
                                "=thirdapp.detail.yourCompanyName.tutengjiudian#Intent;scheme=bdapp;package=com" + ".baidu.BaiduMap;end"
                    )
                } catch (e: URISyntaxException) {
                    e.printStackTrace()
                }
                startActivity(intent)
                return true
            }
            if (url.startsWith("androidamap")) {
                if (CommonUtil.isInstallByread("com.autonavi.minimap")) {
                    val uri1 = Uri.parse(url)
                    val p = uri1.getQueryParameter("p")
                    val split = p.split(",".toRegex()).toTypedArray()
                    val uri = Uri.parse(
                        "amapuri://route/plan/?dlat=" + split[1] + "&dlon=" + split[2] + "&dname=" + split[3] + "&dev=0&t=0"
                    )
                    startActivity(Intent1(Intent1.ACTION_VIEW, uri))
                } else {
                    shortToast("您未安装高德地图")
                }
                return true
            }
            if (url.startsWith(Schema.SCHEMA_PREFIX)) {
                var uri = Uri.parse(url)
                var paramsUrl: String
                val end = url.indexOf('?')
                if (end > -1) {
                    paramsUrl = url.substring(Schema.SCHEMA_PREFIX.length, end)
                } else {
                    paramsUrl = url.substring(Schema.SCHEMA_PREFIX.length)
                }
                if (url.startsWith(Schema.SCHEMA_LIFE_PAGE)) {
                    /*params = paramsUrl.split("/");
                    LifePageActivity.launch(NewsPageActivity.this, Long.valueOf(params[1]), java.net.URLDecoder.decode(url.substring(url.indexOf
                            ('=') + 1)));
                    return true;*/
                } else if (url.startsWith(Schema.SCHEMA_IMAGE_PAGE)) {
                    /*params = paramsUrl.split("/");
                    ImagePageActivity.launch(NewsPageActivity.this, Long.valueOf(params[1]), Cate.Images.id);
                    return true;*/
                } else if (url.startsWith(Schema.SCHEMA_VIDEO_PAGE)) {
                    /*params = paramsUrl.split("/");
                    VideoPageActivity.launch(NewsPageActivity.this, Long.valueOf(params[1]), Cate.Videos.id);
                    return true;*/
                } else if (url.startsWith(Schema.SCHEMA_GOTOINDEX)) {
//                    if (loginManager.isLoginValid()){
//                        params = paramsUrl.split("/");
//                        String userId = params[params.length-1];
//                        if (userId.equals(loginManager.getUser().id)) {
//                            PersonalHomePageActivity.launch(NewsPageActivity.this, PersonalHomePageActivity.TYPE_USER, userId);
//                        } else {
//                            PersonalHomePageActivity.launch(NewsPageActivity.this, PersonalHomePageActivity.TYPE_OTHER, userId);
//                        }
//                    }else {
//                        LoginActivity.launch(NewsPageActivity.this);
//                    }
                    return true
                } else if (url.startsWith(Schema.SCHEMA_REPLYCOMMENT)) {
                    var nickNmae = ""
                    if (url.contains("?")) {
                        val name = url.substring(end + 1)
                        val split =
                            name.split("=".toRegex()).toTypedArray()
                        nickNmae = split[1]
                    }
                    paramsUrl = url.substring(Schema.SCHEMA_REPLYCOMMENT.length, end)
                    params = paramsUrl.split("/".toRegex()).toTypedArray()
                    //
//                    Comment comment = new Comment();
//                    comment.id = Integer.valueOf(params[1]) ;
//                    comment.user = new CommentUser();
//                    comment.user.name = java.net.URLDecoder.decode(nickNmae);
//                    showCommentView(comment);
                    return true
                } else if (url.startsWith(Schema.SCHEMA_COMMENT_PAGE)) {
                    params = paramsUrl.split("/".toRegex()).toTypedArray()
                    //                    CommentListActivity.launch(NewsPageActivity.this, Long.valueOf(params[1]), Constants.NEWS_TYPE, String.valueOf(mNewsDetail.user.id), 0);
                    return true
                } else if (url.startsWith(Schema.SCHEMA_BROWSER_URL)) {
                    /*CusWebViewActivity.launch(NewsPageActivity.this, java.net.URLDecoder.decode(url.substring(url.indexOf('=') + 1)), "");
                    return true;*/
                } else if (url.startsWith(Schema.SCHEMA_PHONE)) {
                    params = paramsUrl.split("/".toRegex()).toTypedArray()
                    uri = Uri.parse("tel:" + params[1])
                    val intent = Intent1(Intent1.ACTION_DIAL, uri)
                    intent.addFlags(Intent1.FLAG_ACTIVITY_REORDER_TO_FRONT)
                    try {
                        startActivity(intent)
                    } catch (e: java.lang.Exception) {
//                        shortToast(R.string.no_call_app);
                    }
                    return true
                } else if (url.startsWith(Schema.SCHEMA_MAP)) {
                    params = paramsUrl.split("/".toRegex()).toTypedArray()
                    //                    if (params.length >= 4) {
//                        Position position = new Position();
//                        position.setLat(Double.valueOf(params[1]));
//                        position.setLon(Double.valueOf(params[2]));
//                        position.setName(java.net.URLDecoder.decode(params[3]));
//                        position.setAddress(java.net.URLDecoder.decode(params[4]));
//                        new MainMapEnterActivity.Builder(NewsPageActivity.this, MainMapEnterActivity.TYPE_SINGLE_POI).setPosition(position).launch();
//                    } else {
//                        shortToast(R.string.poi_is_null);
//                    }
                    return true
                } else if (url.startsWith(Schema.SCHEMA_REPORT_COMMENT)) {
                    paramsUrl = url.substring(Schema.SCHEMA_REPORT_COMMENT.length)
                    //                    if (LoginManager.getInstance().isLoginValid()){
//                        ReportActivity.launch(NewsPageActivity.this,6,paramsUrl);
//                    }else {
//                        LoginActivity.launch(NewsPageActivity.this);
//                    }
                    return true
                } else if (url.startsWith(Schema.SCHEMA_ADD_LOVE)) {
//                    addLove();
                    return true
                } else if (url.startsWith(Schema.SCHEMA_DEL_LOVE)) {
//                    delLove();
                    return true
                } else if (url.startsWith(Schema.SCHEMA_SHOP)) {
                    /*params = paramsUrl.split("/");
                    ShopActivity.launch(NewsPageActivity.this, Long.valueOf(params[1]));
                    return true;*/
                }
                if (url.startsWith(Schema.SCHEMA_NEWS_COMMENT)) {
                    params = paramsUrl.split("/".toRegex()).toTypedArray()
                    var pid = ""
                    if (params.size > 3) {
                        pid = params[3]
                    }
                    val username = uri.getQueryParameter("username")
                    val pId = pid
                    return if (url.startsWith(Schema.SCHEMA_GET_USER)) {
                        true
                    } else true
                }
                if (url.startsWith(Schema.SCHEMA_WX_MINI)) {
                    val uriObj = Uri.parse(url)
                    val appid = uriObj.getQueryParameter("appid")
                    val username = uriObj.getQueryParameter("username")
                    val path = uriObj.getQueryParameter("path")
                    val api = WXAPIFactory.createWXAPI(
                        this@BaseWebActivity,
                        Platform.KEY_WECHAT
                    )
                    val req = WXLaunchMiniProgram.Req()
                    req.userName = username // 填小程序原始id
                    req.path = path //拉起小程序页面的可带参路径，不填默认拉起小程序首页
                    req.miniprogramType =
                        WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE // 可选打开 开发版，体验版和正式版
                    api.sendReq(req)
                    return true
                }
            }
            val task = PayTask(this@BaseWebActivity)
            val isIntercepted: Boolean =
                task.payInterceptorWithUrl(url, true, object : H5PayCallback {
                    override fun onPayResult(result: H5PayResultModel) {
                        // 支付结果返回
                        val url: String = result.returnUrl
                        if (!TextUtils.isEmpty(url)) {
                            this@BaseWebActivity.runOnUiThread { view.loadUrl(url) }
                        }
                    }
                })
            /**
             * 判断是否成功拦截
             * 若成功拦截，则无需继续加载该URL；否则继续加载
             */
            if (!isIntercepted) {
                return if (url.startsWith("weixin://wap/pay?")) {
                    val intent = Intent1()
                    intent.action = Intent1.ACTION_VIEW
                    intent.data = Uri.parse(url)
                    startActivity(intent)
                    true
                } else {
                    if (url.startsWith("http")) {
                        super.shouldOverrideUrlLoading(view, url)
                    } else {
                        true
                    }
                }
            }
            return if (url.startsWith("http")) {
                super.shouldOverrideUrlLoading(view, url)
            } else {
                true
            }
        }

        override fun onPageStarted(
            view: WebView,
            url: String,
            favicon: Bitmap
        ) {
            super.onPageStarted(view, url, favicon)
        }

        override fun onPageFinished(
            view: WebView,
            url: String
        ) {
            super.onPageFinished(view, url)
            onWebPageFinished(view, url)
        }

        @TargetApi(21)
        override fun shouldInterceptRequest(
            view: WebView,
            request: WebResourceRequest
        ): WebResourceResponse {
            return shouldInterceptRequest(view, request.url.toString())
        }

        override fun onReceivedError(
            view: WebView,
            errorCode: Int,
            description: String,
            failingUrl: String
        ) {
            super.onReceivedError(view, errorCode, description, failingUrl)
        }

        override fun onReceivedSslError(
            webView: WebView,
            sslErrorHandler: SslErrorHandler,
            sslError: SslError
        ) {
            sslErrorHandler.proceed()
        }
    }

    lateinit var uploadMessage: ValueCallback<Uri>
    var uploadMessageAboveL: ValueCallback<Array<Uri>>? = null

    private fun openImageChooserActivity(accetype: String) {
        val i = Intent1(Intent1.ACTION_GET_CONTENT)
        i.addCategory(Intent1.CATEGORY_OPENABLE)
        if (!TextUtils.isEmpty(accetype)) {
            if (accetype.contains("image")) {
                i.type = "image/*"
            } else if (accetype.contains("video")) {
                i.type = "video/*"
            } else {
                i.type = "*/*"
            }
        } else {
            i.type = "*/*"
        }
        startActivityForResult(Intent1.createChooser(i, "Image Chooser"), FILE_CHOOSER_RESULT_CODE)
    }

    /**
     * 自定义的WebChromeClient
     */
    protected inner class MyWebChromeClient : WebChromeClient() {
        override fun onReceivedTitle(
            view: WebView,
            title: String
        ) {
            super.onReceivedTitle(view, title)
            if (!TextUtils.isEmpty(this@BaseWebActivity.getTitleText()) &&
                !title.contains("html") &&
                !title.contains("/") &&
                !title.contains("?")
            ) {
//                titleBar.setTitle(title);
            }
        }

        override fun onProgressChanged(
            view: WebView,
            newProgress: Int
        ) {
            progressBar?.visibility = View.VISIBLE
            progressBar?.progress = newProgress
            if (newProgress == 100) {
                progressBar?.visibility = View.GONE
                progressBar?.progress = 0
                //                if (isPlay) {
//                    webView.loadUrl("javascript:audio.cutoff(" + true + ")");//暂停播放
//                }
            }
            super.onProgressChanged(view, newProgress)
        }

        //For Android API < 11 (3.0 OS)
        fun openFileChooser(valueCallback: ValueCallback<Uri>) {
            uploadMessage = valueCallback
            openImageChooserActivity("")
        }

        //For Android API >= 11 (3.0 OS)
        override fun openFileChooser(
            valueCallback: ValueCallback<Uri>,
            acceptType: String,
            capture: String
        ) {
            uploadMessage = valueCallback
            openImageChooserActivity(acceptType)
        }

        //For Android API >= 21 (5.0 OS)
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        override fun onShowFileChooser(
            webView: WebView,
            filePathCallback: ValueCallback<Array<Uri>>,
            fileChooserParams: FileChooserParams
        ): Boolean {
            uploadMessageAboveL = filePathCallback
            openFileChooserImplForAndroid5(
                filePathCallback,
                fileChooserParams.acceptTypes[0]
            )
            return true
        }
    }

    private var mUploadMessageLolipop: ValueCallback<Array<Uri>>? = null
    private var mCameraPhotoPath: String? = null
    private var mCameraPhotoPath1: String? = null
    private fun openFileChooserImplForAndroid5(
        uploadMsg: ValueCallback<Array<Uri>>?,
        accetype: String
    ) {
        val permissions: MutableList<String> =
            ArrayList()
        if (!checkPermission("android.permission.CAMERA")) {
            permissions.add("android.permission.CAMERA")
        }
        if (!checkPermission("android.permission.READ_EXTERNAL_STORAGE")) {
            permissions.add("android.permission.READ_EXTERNAL_STORAGE")
        }
        if (permissions.size > 0) {
            mUploadMessageLolipop?.onReceiveValue(null)
            uploadMsg?.onReceiveValue(null)
            val permissionArray =
                arrayOfNulls<String>(permissions.size)
            for (i in permissions.indices) {
                permissionArray[i] = permissions[i]
            }
            PermissionUtil.requestPermission(
                object : RequestPermission {
                    override fun onRequestPermissionSuccess() {}
                    override fun onRequestPermissionFailure(permissions: List<String>) {}
                    override fun onRequestPermissionFailureWithAskNeverAgain(permissions: List<String>) {}
                },
                RxPermissions(this),
                ArtUtils.obtainAppComponentFromContext(this).rxErrorHandler(),
                *permissionArray
            )
            return
        }
        mUploadMessageLolipop?.onReceiveValue(null)
        mUploadMessageLolipop = uploadMsg
        var takePictureIntent: Intent1? = Intent1(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent!!.resolveActivity(this.packageManager) != null) {
            // Create the File where the photo should go
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
                takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath)
            } catch (ex: IOException) {
                // Error occurred while creating the File
            }

            // Continue only if the File was successfully created
            if (photoFile != null) {
                mCameraPhotoPath = "file:" + photoFile.absolutePath
                val cramuri: Uri
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    cramuri = FileProvider.getUriForFile(
                        this@BaseWebActivity,
                        "com.juntian.radiopeanut.fileprovider",
                        photoFile
                    )
                    takePictureIntent.addFlags(Intent1.FLAG_GRANT_READ_URI_PERMISSION or Intent1.FLAG_GRANT_WRITE_URI_PERMISSION)
                } else {
                    cramuri = Uri.fromFile(photoFile)
                }
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, cramuri)
            } else {
                takePictureIntent = null
            }
        }
        var videoIntent: Intent1? = Intent1(MediaStore.ACTION_VIDEO_CAPTURE)
        if (videoIntent!!.resolveActivity(this.packageManager) != null) {
            // Create the File where the photo should go
            var photoFile: File? = null
            try {
                photoFile = createVideoFile()
                videoIntent.putExtra("videoPath", mCameraPhotoPath1)
            } catch (ex: IOException) {
            }
            if (photoFile != null) {
                val cramuri: Uri
                mCameraPhotoPath1 = "file:" + photoFile.absolutePath
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    cramuri = FileProvider.getUriForFile(
                        this@BaseWebActivity,
                        "com.juntian.radiopeanut.fileprovider",
                        photoFile
                    )
                    videoIntent.addFlags(Intent1.FLAG_GRANT_READ_URI_PERMISSION or Intent1.FLAG_GRANT_WRITE_URI_PERMISSION)
                } else {
                    cramuri = Uri.fromFile(photoFile)
                }
                videoIntent.putExtra(MediaStore.EXTRA_OUTPUT, cramuri)
            } else {
                videoIntent = null
            }
        }
        videoIntent!!.putExtra(
            MediaStore.EXTRA_VIDEO_QUALITY,
            1
        ) // set the video image quality to high
        val contentSelectionIntent1 = Intent1(Intent1.ACTION_GET_CONTENT)
        contentSelectionIntent1.addCategory(Intent1.CATEGORY_OPENABLE)
        contentSelectionIntent1.type = "video/*"
        var intentArray1: Array<Intent1>
        intentArray1 = videoIntent.let { arrayOf(it) }
        val chooserIntent1 = Intent1(Intent1.ACTION_CHOOSER)
        chooserIntent1.putExtra(Intent1.EXTRA_INTENT, contentSelectionIntent1)
        chooserIntent1.putExtra(Intent1.EXTRA_TITLE, "Vidoe Chooser")
        chooserIntent1.putExtra(Intent1.EXTRA_INITIAL_INTENTS, intentArray1)
        val contentSelectionIntent = Intent1(Intent1.ACTION_GET_CONTENT)
        contentSelectionIntent.addCategory(Intent1.CATEGORY_OPENABLE)
        contentSelectionIntent.type = "image/*"
        var intentArray: Array<Intent1?>
        intentArray = takePictureIntent.let { arrayOf(it) }
        val chooserIntent = Intent1(Intent1.ACTION_CHOOSER)
        chooserIntent.putExtra(Intent1.EXTRA_INTENT, contentSelectionIntent)
        chooserIntent.putExtra(Intent1.EXTRA_TITLE, "Image Chooser")
        chooserIntent.putExtra(Intent1.EXTRA_INITIAL_INTENTS, intentArray)
        val i = Intent1(Intent1.ACTION_GET_CONTENT)
        i.addCategory(Intent1.CATEGORY_OPENABLE)
        if (!TextUtils.isEmpty(accetype)) {
            if (accetype.contains("image")) {
                startActivityForResult(chooserIntent, REQUEST_CODE_LOLIPOP)
            } else if (accetype.contains("video")) {
                startActivityForResult(chooserIntent1, REQUEST_CODE_VIDEO)
            } else {
                startActivityForResult(chooserIntent, REQUEST_CODE_LOLIPOP)
            }
        } else {
            startActivityForResult(chooserIntent, REQUEST_CODE_LOLIPOP)
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File? {
        // Create an image file name
        val timeStamp =
            SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = File(FileUtil.getPathByType(FileUtil.DIR_TYPE_IMAGE))
        return File.createTempFile(
            imageFileName,  /* prefix */
            ".jpg",  /* suffix */
            storageDir /* directory */
        )
    }

    @Throws(IOException::class)
    private fun createVideoFile(): File? {
        // Create an image file name
        val timeStamp =
            SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "MP4_" + timeStamp + "_"
        val storageDir = File(FileUtil.getPathByType(FileUtil.DIR_TYPE_IMAGE))
        return File.createTempFile(
            imageFileName,  /* prefix */
            ".mp4",  /* suffix */
            storageDir /* directory */
        )
    }

    private var mUploadMessage: ValueCallback<Uri>? = null
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: android.content.Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        var results: Array<Uri?>? = null

        when (requestCode) {
            REQUEST_CODE_FILE_CHOOSE -> {
                var uri: Uri? = null
                if (intent != null) {
                    uri = intent.data
                }
                mUploadMessage?.onReceiveValue(uri)
                mUploadMessage = null
            }
            REQUEST_CODE_LOLIPOP -> {
                // Check that the response is a good one
                if (resultCode == Activity.RESULT_OK) {
                    if (intent == null) {
                        // If there is not data, then we may have taken a photo
                        if (mCameraPhotoPath != null) {
                            results =
                                arrayOf(Uri.parse(mCameraPhotoPath))
                        }
                    } else {
                        val dataString = intent.dataString
                        var clipData: ClipData? = null
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            clipData = intent.clipData
                        }
                        if (clipData != null) {
                            results = arrayOfNulls(clipData.itemCount)
                            var i = 0
                            while (i < clipData.itemCount) {
                                val item = clipData.getItemAt(i)
                                results[i] = item.uri
                                i++
                            }
                        }
                        if (dataString != null) {
                            results = arrayOf(Uri.parse(dataString))
                        }
                    }
                }

                results?.let {
                    mUploadMessageLolipop?.onReceiveValue(arrayOf(it[0] ?: Uri.EMPTY))
                }
                mUploadMessageLolipop = null
            }
            REQUEST_CODE_VIDEO -> {
                // Check that the response is a good one
                if (resultCode == Activity.RESULT_OK) {
                    if (intent == null) {
                        // If there is not data, then we may have taken a photo
                        if (mCameraPhotoPath1 != null) {
                            results =
                                arrayOf(Uri.parse(mCameraPhotoPath1))
                        }
                    } else {
                        val dataString = intent.dataString
                        var clipData: ClipData? = null
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            clipData = intent.clipData
                        }
                        if (clipData != null) {
                            results = arrayOfNulls(clipData.itemCount)
                            var i = 0
                            while (i < clipData.itemCount) {
                                val item = clipData.getItemAt(i)
                                results[i] = item.uri
                                i++
                            }
                        }
                        if (dataString != null) {
                            results = arrayOf(Uri.parse(dataString))
                        }
                    }
                }
                //  mUploadMessageLolipop?.onReceiveValue(results)
                mUploadMessageLolipop = null
            }
        }
    }

    override fun onDestroy() {
        mWebView?.let {
            it.clearHistory()
            it.removeAllViews()
            it.clearFormData()
            it.clearCache(true)
            CookieSyncManager.createInstance(it.context)
            it.destroy()
        }
        super.onDestroy()
    }
}