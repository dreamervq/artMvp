package com.example.mylibrary.mvp.uis.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.InflateException
import android.view.View
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import com.example.mylibrary.mvp.dialogs.LoadingDialog
import com.gyf.immersionbar.ImmersionBar
import com.tbruyelle.rxpermissions2.RxPermissions
import com.uber.autodispose.AutoDispose
import com.uber.autodispose.AutoDisposeConverter
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import io.reactivex.subjects.BehaviorSubject
import me.jessyan.art.R
import me.jessyan.art.base.delegate.IActivity
import me.jessyan.art.integration.cache.Cache
import me.jessyan.art.integration.cache.CacheType
import me.jessyan.art.mvp.IPresenter
import me.jessyan.art.mvp.IView
import me.jessyan.art.ui.util.RxCheckLifeCycleTransformer.LifeCycleEvent
import me.jessyan.art.utils.ArtUtils
import me.jessyan.art.utils.PermissionUtil
import me.jessyan.art.utils.PermissionUtil.RequestPermission
import me.jessyan.rxerrorhandler.core.RxErrorHandler
import java.util.*
import javax.inject.Inject


@Suppress("UNCHECKED_CAST")
abstract class BaseActivity<P : IPresenter> : AppCompatActivity(), IActivity<P>, IView {

    protected var mImmersionBar: ImmersionBar? = null
    protected var mLifecycleSubject: BehaviorSubject<LifeCycleEvent> = BehaviorSubject.create()
    private var loadingDialog: LoadingDialog? = null
    private var mLastClickTime: Long = 0
    protected var rxErrorHandler: RxErrorHandler? = null
    protected var rxPermissions: RxPermissions? = null
    private var mCache: Cache<String, Any>? = null

    /**
     * 权限回调接口
     */
    private lateinit var mListener: CheckPermListener

    @Inject
    protected var mPresenter: P? = null

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appComponent = ArtUtils.obtainAppComponentFromContext(this)
        rxErrorHandler = appComponent.rxErrorHandler()
        rxPermissions = RxPermissions(this)
        try {
            setContentView(getContentViewId())
            mImmersionBar = ImmersionBar.with(this)
            initImmersionBar()
            init(savedInstanceState)
            mLifecycleSubject.onNext(LifeCycleEvent.CREATE)
        } catch (e: Exception) {
            if (e is InflateException) throw e
            e.printStackTrace()
        }
    }

    override fun setPresenter(presenter: P?) {
        this.mPresenter = presenter
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        if (mPresenter == null) mPresenter = obtainPresenter()
    }

    override fun initView(savedInstanceState: Bundle?): Int {
        TODO("Not yet implemented")
    }

    protected fun initImmersionBar() {
        if (isImmersionBlack()) {
            if (ImmersionBar.isSupportStatusBarDarkFont()) { //判断当前设备支不支持状态栏字体变色
                mImmersionBar?.statusBarDarkFont(true)?.navigationBarColor(R.color.black)
            } else {
                mImmersionBar?.statusBarDarkFont(true, 0.3f)?.navigationBarColor(R.color.black)
            }
        } else {
            mImmersionBar?.statusBarDarkFont(false)?.navigationBarColor(R.color.black)
        }
        if (needImmersionBar()) mImmersionBar?.statusBarColor(R.color.white_normal)
        mImmersionBar?.fitsSystemWindows(needImmersionBar())?.init()
    }

    protected open fun isImmersionBlack(): Boolean {
        return false
    }

    protected open fun needImmersionBar(): Boolean {
        return false
    }

    override fun initData(savedInstanceState: Bundle?) {
    }

    fun transparentStatusBar() {
        if (Build.VERSION.SDK_INT >= 21) {
            val decorView = window.decorView
            val option = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
            decorView.systemUiVisibility = option
            window.statusBarColor = Color.TRANSPARENT
        }
        val actionBar = supportActionBar
        actionBar?.hide()
    }

    fun shortToast(message: String) {
        ArtUtils.makeText(this, message)
    }

    fun shortToast(messageResId: Int) {
        shortToast(getString(messageResId))
    }


    override fun onBackPressed() {
        finish()
        //        overridePendingTransition(0, R.anim.af_right_out);
//        overridePendingTransition(R.anim.fade_in, R.anim.af_right_out);
    }


    /**
     * 用户权限处理,
     * 如果全部获取, 则直接过.
     * 如果权限缺失, 则提示Dialog.
     *
     * @param requestCode  请求码
     * @param permissions  权限
     * @param grantResults 结果
     */

    @FunctionalInterface
    interface CheckPermListener {
        //权限通过后的回调方法
        fun agreeAllPermission()
        fun agreePermissionFail()
    }

    protected open fun checkPermission(permission: String?): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission!!
        ) == PackageManager.PERMISSION_GRANTED
    }

    protected open fun checkMorePermissions(permissions: Array<String>): List<String>? {
        val permissionList: MutableList<String> = ArrayList()
        for (i in permissions.indices) {
            if (!checkPermission(permissions[i])) permissionList.add(permissions[i])
        }
        return permissionList
    }

    open fun getPermissions(
        listener: CheckPermListener?,
        resString: String?,
        vararg mPerms: String?
    ) {
        PermissionUtil.requestPermission(object : RequestPermission {
            override fun onRequestPermissionSuccess() {
                listener?.agreeAllPermission()
            }

            override fun onRequestPermissionFailure(permissions: List<String>) {
                if (permissions.size < 3) {
                }
                listener?.agreePermissionFail()
            }

            override fun onRequestPermissionFailureWithAskNeverAgain(permissions: List<String>) {
                listener?.agreePermissionFail()
            }
        }, rxPermissions, rxErrorHandler, *mPerms)
    }

//    public override fun onActivityResult(
//        requestCode: Int,
//        resultCode: Int,
//        data: Intent?
//    ) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == EasyPermissions.SETTINGS_REQ_CODE) {
//            backFromSetting()
//        }
//    }


    protected fun backFromSetting() {}

    @JvmOverloads
    fun showProgressDialog(message: String?, allowBackCancel: Boolean = false) {
        if (loadingDialog == null) {
            loadingDialog = LoadingDialog(this)
            loadingDialog?.setCancelable(allowBackCancel)
            loadingDialog?.setCanceledOnTouchOutside(false)
        }
        if (!loadingDialog!!.isShowing()) {
            loadingDialog?.showLoading(message)
        }
    }


    override fun showLoading(msg: String?) {
        showProgressDialog(msg)
    }

    override fun showLoading() {
        showProgressDialog("加载中...")
    }

    override fun hideLoading() {
        loadingDialog?.dismiss()
    }

    abstract fun getContentViewId(): Int
    abstract fun init(savedInstanceState: Bundle?)


    override fun onStart() {
        super.onStart()
        mLifecycleSubject.onNext(LifeCycleEvent.START)
    }

    open fun getPresenter(): P {
        if (mPresenter == null) {
            mPresenter = obtainPresenter()
        }
        return mPresenter!!
    }


    /**
     * 把Observable的生命周期与Activity绑定
     *
     * @param <D>
     * @return
    </D> */
    override fun <T> bindAutoDispose(): AutoDisposeConverter<T?>? {
        return AutoDispose.autoDisposable(
            AndroidLifecycleScopeProvider
                .from(this, Lifecycle.Event.ON_DESTROY)
        )
    }

    override fun onResume() {
        super.onResume()
        mobResume()
    }

    protected fun mobResume() {
    }

    override fun onPause() {
        super.onPause()
        mobPause()
    }

    protected fun mobPause() {
    }

    override fun onDestroy() {
        super.onDestroy()

        mPresenter?.onDestroy() //释放资源
        mPresenter = null
        mLifecycleSubject.onNext(LifeCycleEvent.DESTROY)
    }

    fun beFastClick(): Boolean {
        val currentClickTime = System.currentTimeMillis()
        val flag = currentClickTime - mLastClickTime < 400L
        mLastClickTime = currentClickTime
        return flag
    }

    @Synchronized
    override fun provideCache(): Cache<String, Any> {
        if (mCache == null) {
            mCache = ArtUtils.obtainAppComponentFromContext(this).cacheFactory()
                .build(CacheType.ACTIVITY_CACHE) as Cache<String, Any>
        }
        return mCache as Cache<String, Any>
    }

    override fun useEventBus(): Boolean {
        return false
    }

    override fun useFragment(): Boolean {
        return false
    }

    inline fun <reified T : Activity> Context.startActivity(bundle: Bundle? = null) {
        val intent = Intent(this, T::class.java)
        bundle?.let { intent.putExtras(it) }
        startActivity(intent)
        overridePendingTransition(R.anim.ui_right_in, R.anim.fade_out)
    }

    inline fun <reified T : Activity> Context.startActivityForResult(
        requestCode: Int,
        bundle: Bundle? = null
    ) {
        val intent = Intent(this, T::class.java)
        bundle?.let { intent.putExtras(it) }
        startActivityForResult(intent, requestCode)
        overridePendingTransition(R.anim.ui_right_in, R.anim.fade_out)
    }

    override fun stateError() {}
    override fun showMessage(message: String) {}
}