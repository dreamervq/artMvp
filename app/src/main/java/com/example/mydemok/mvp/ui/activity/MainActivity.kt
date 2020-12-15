package com.example.mydemok.mvp.ui.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.Settings
import android.text.TextUtils
import androidx.core.app.NotificationManagerCompat
import cn.markmjw.platform.util.GsonUtil
import com.example.mydemok.R
import com.example.mydemok.application.Constants
import com.example.mydemok.mvp.model.Version
import com.example.mydemok.mvp.presenter.IndexPresenter
import com.example.mydemok.mvp.ui.dialog.TipsDialog
import com.example.mydemok.mvp.ui.fragment.*
import com.example.mydemok.mvp.ui.listener.TypeEnum
import com.example.mydemok.utils.AppUtil
import com.example.mydemok.utils.CommonUtil
import com.example.mydemok.utils.DownloadApkUtil
import com.example.mylibrary.mvp.uis.activities.BaseActivity
import com.example.mylibrary.mvp.uis.fragment.BaseNewFragment
import kotlinx.android.synthetic.main.activity_main.*
import me.jessyan.art.mvp.Message
import me.jessyan.art.ui.adapters.BaseAdapter
import me.jessyan.art.ui.view.TabStripView
import me.jessyan.art.utils.ArtUtils
import me.jessyan.art.utils.TinyPref

class MainActivity : BaseActivity<IndexPresenter>(), TabStripView.OnTabSelectedListener {
    val GET_UNKNOWN_APP_SOURCES = 0x123
    private lateinit var currentTag: String
    private var currentFragment: BaseNewFragment<*>? = null
    private var logTime: Long = 0
    override fun getContentViewId(): Int {
        return R.layout.activity_main
    }

    override fun init(savedInstanceState: Bundle?) {
        currentTag = TypeEnum.MainTab.Homepage
        initTab(savedInstanceState)
        initNotification()
        getPresenter().checkVersion(Message.obtain(this, Constants.ARG_MAIN))
    }

    private fun initNotification() {
        if (!isNotificationEnabled(this)) {
            val hasSetNotification =
                TinyPref.getInstance().getBoolean(Constants.HAS_SET_NOTIFICATION, false)
            if (!hasSetNotification) {
                var tipDialog: TipsDialog<String> = TipsDialog(this)
                tipDialog.setOnOperatClickListener(object :
                    TipsDialog.OnDialogClickListener<String> {
                    override fun onEnsureClick(param: String?) {
                        gotoSet()
                    }

                    override fun onCancelClick(param: String?) {
                    }
                })
                tipDialog.show("一般不烦你，有事才@？", "开~", "忽略", "")
                TinyPref.getInstance().putBoolean(Constants.HAS_SET_NOTIFICATION, true)
            }
        }
    }

    private fun gotoSet() {
        try {
            var intent = Intent()
            if (Build.VERSION.SDK_INT >= 26) {
                // android 8.0引导
                intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
                intent.putExtra("android.provider.extra.APP_PACKAGE", packageName)
            } else if (Build.VERSION.SDK_INT >= 21) {
                // android 5.0-7.0
                intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
                intent.putExtra("app_package", packageName)
                intent.putExtra("app_uid", applicationInfo.uid)
            } else {
                // 其他
                intent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
                intent.data = Uri.fromParts("package", packageName, null)
            }
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        } catch (e: Exception) {
        }
    }

    private fun initTab(savedInstanceState: Bundle?) {
        tabBar.addTab(
            FirstFragment::class.java,
            TabStripView.TabParam(
                R.color.white_normal,
                R.mipmap.icon_homepage_nor,
                R.mipmap.icon_homepage_ser,
                TypeEnum.MainTab.Homepage
            )
        )
        tabBar.addTab(
            SecondFragment::class.java,
            TabStripView.TabParam(
                R.color.white_normal,
                R.mipmap.icon_homepage_nor,
                R.mipmap.icon_homepage_ser,
                TypeEnum.MainTab.Second
            )
        )
        tabBar.addTab(
            ThirdFragment::class.java,
            TabStripView.TabParam(
                R.color.white_normal,
                R.mipmap.icon_homepage_nor,
                R.mipmap.icon_homepage_ser,
                TypeEnum.MainTab.Third
            )
        )
        tabBar.addTab(
            FourFragment::class.java,
            TabStripView.TabParam(
                R.color.white_normal,
                R.mipmap.icon_homepage_nor,
                R.mipmap.icon_homepage_ser,
                TypeEnum.MainTab.Four
            )
        )
        tabBar.addTab(
            FiveFragment::class.java,
            TabStripView.TabParam(
                R.color.white_normal,
                R.mipmap.icon_homepage_nor,
                R.mipmap.icon_homepage_ser,
                TypeEnum.MainTab.Mine
            )
        )
        tabBar.onRestoreInstanceState(savedInstanceState)
        tabBar.setTabSelectListener(this)
    }

    override fun obtainPresenter(): IndexPresenter? {
        return IndexPresenter(ArtUtils.obtainAppComponentFromContext(this))
    }

    override fun handleMessage(message: Message) {
        if (message.what == Constants.KEY_SUCCESS) {
            when (message.arg1) {
                Constants.ARG_MAIN -> {
                    val version: Version? = message.obj as Version
                    if (version != null &&
                        !TextUtils.isEmpty(version.version) &&
                        CommonUtil.compareVersion(AppUtil.getAppVersion(), version.version!!) < 0
                    ) {
                        if (version.force) {
                            TinyPref.getInstance()
                                .putString(Constants.PREF_UPDATE, GsonUtil.toJson(version))
                            showUpdateDialog(version, false)
                        } else {
                            val lastShowVersion =
                                TinyPref.getInstance().getString("show_update_version")
                            if (TextUtils.isEmpty(lastShowVersion) || lastShowVersion != version.version) {
                                TinyPref.getInstance().putString(Constants.PREF_UPDATE, "")
                                showUpdateDialog(version, true)
                                TinyPref.getInstance()
                                    .putString("show_update_version", version.version)
                            }
                        }


                    } else {
                        TinyPref.getInstance().putString(Constants.PREF_UPDATE, "")
                    }
                }
                else -> {
                }
            }
        } else {
            hideLoading()
        }
    }

    private fun showUpdateDialog(version: Version, showCancel: Boolean) {
        var tipDialog: TipsDialog<String> = TipsDialog(this)
        tipDialog.setOnOperatClickListener(object :
            TipsDialog.OnDialogClickListener<String> {
            override fun onEnsureClick(param: String?) {
                startUpdateApp(version.url!!, version.version!!)
            }

            override fun onCancelClick(param: String?) {
            }
        })
        tipDialog.show(version.msg, "去升级", if (showCancel) "再看看" else "", "")
    }

    private fun startUpdateApp(url: String, versionName: String) {
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(url.trim { it <= ' ' })) {
            return
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && !packageManager.canRequestPackageInstalls()) { // api > 26
            val intent = Intent()
            //获取当前apk包URI，并设置到intent中（这一步设置，可让“未知应用权限设置界面”只显示当前应用的设置项）
            val packageURI = Uri.parse("package:$packageName")
            intent.data = packageURI
            //设置不同版本跳转未知应用的动作
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //intent = new Intent(android.provider.Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,packageURI);
                intent.action = Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES
            } else {
                intent.action = Settings.ACTION_SECURITY_SETTINGS
            }
            startActivityForResult(intent, GET_UNKNOWN_APP_SOURCES)
            shortToast("请先打开安装应用权限")
            return
        }
        getPermissions(
            object : CheckPermListener {
                override fun agreeAllPermission() {
                    DownloadApkUtil.getInstance(applicationContext)
                        .downloadNewApkBySystem(applicationContext, url, versionName)
                }

                override fun agreePermissionFail() {

                }

            }, "请打开下载需要权限", Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }

    override fun onTabSelected(holder: TabStripView.ViewHolder?) {
        currentTag = holder!!.tag
        currentFragment = tabBar.getCurrentFragment(currentTag)
        if (TextUtils.equals(TypeEnum.MainTab.Mine, holder?.tag)) {
        }
    }

    private fun isNotificationEnabled(context: Context?): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val notificationManagerCompat =
                NotificationManagerCompat.from(context!!)
            return notificationManagerCompat.areNotificationsEnabled()
        }
        return true
    }

    override fun isImmersionBlack(): Boolean {
        return true
    }

    override fun useFragment(): Boolean {
        return true
    }

    override fun onResume() {
        super.onResume()
        if (currentFragment == null) {
            currentFragment = tabBar.getCurrentFragment(currentTag)
        }
        currentFragment?.userVisibleHint = true
    }

    override fun onPause() {
        super.onPause()
        if (currentFragment == null) {
            currentFragment = tabBar.getCurrentFragment(currentTag)
        }
    }

    override fun showMessage(message: String) {
        // shortToast(message)
    }

    override fun onRestoreInstanceState(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?
    ) {
        super.onRestoreInstanceState(savedInstanceState, persistentState)
        if (savedInstanceState != null) {
            val pos = savedInstanceState.getInt("position")
            tabBar.currentSelectedTab = pos
        }
    }

    override fun onBackPressed() {
        if (tabBar.currentSelectedTab != 0) {
            tabBar.currentSelectedTab = 0
            return
        }
        val currentTime = System.currentTimeMillis()
        if (currentTime - logTime < 2000) {
            finish()
        } else {
            shortToast("再按一次退出应用")
            logTime = currentTime
        }
    }
}