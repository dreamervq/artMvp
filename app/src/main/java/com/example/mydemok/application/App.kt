package com.example.mydemok.application

import android.app.ActivityManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.os.Environment
import android.os.Looper
import android.os.Process
import androidx.multidex.BuildConfig
import androidx.multidex.MultiDex
import com.example.mydemok.utils.CrashReportingTree
import me.jessyan.art.base.BaseApplication
import timber.log.Timber
import timber.log.Timber.DebugTree
import java.io.File

class App : BaseApplication() {
    override fun onCreate() {
        super.onCreate()
        init()
        initShareSdk()
        initLogger()
        initUMeng()
        initJGuang()
        initDownLoader()
        //        closeAndroidPDialog();
    }

    private fun initLogger() {
//        Logger.addLogAdapter(new AndroidLogAdapter() {
//            @Override
//            public boolean isLoggable(int priority, String tag) {
//                return isDebug();
//            }
//        });
    }

    val isDebug: Boolean
        get() = applicationInfo != null && applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0

    private fun init() {
        //   Net.getInstance().clear();
    }

    private fun initDownLoader() {
        //FileDownloader.setupOnApplicationOnCreate(this);
    }

    private fun initUMeng() {
//        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
//        MobclickAgent.openActivityDurationTrack(false);
//        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, null);
//        UMConfigure.setLogEnabled(isDebug());
//        UMConfigure.setEncryptEnabled(true);
    }

    private fun initShareSdk() {
//        MobSDK.init(this);
    }

    private fun initJGuang() {
//        JPushInterface.setDebugMode(isDebug());
//        JPushInterface.init(this);
//        DataSharedPreferences.setJPushId(JPushInterface.getRegistrationID(this));
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    private fun getExternalFilesDirEx(
        context: Context,
        type: String
    ): File {
        if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            val ef = context.getExternalFilesDir(type)
            if (ef != null && ef.isDirectory) {
                return ef
            }
        }
        return File(Environment.getExternalStorageDirectory(), type)
    }

    /**
     * 解决androidP 第一次打开程序出现莫名弹窗
     * 弹窗内容“detected problems with api ”
     */
    private fun closeAndroidPDialog() {
        try {
            val aClass =
                Class.forName("android.content.pm.PackageParser\$Package")
            val declaredConstructor =
                aClass.getDeclaredConstructor(
                    String::class.java
                )
            declaredConstructor.isAccessible = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        try {
            val cls = Class.forName("android.app.ActivityThread")
            val declaredMethod =
                cls.getDeclaredMethod("currentActivityThread")
            declaredMethod.isAccessible = true
            val activityThread = declaredMethod.invoke(null)
            val mHiddenApiWarningShown =
                cls.getDeclaredField("mHiddenApiWarningShown")
            mHiddenApiWarningShown.isAccessible = true
            mHiddenApiWarningShown.setBoolean(activityThread, true)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * @return 是否是主线程
     */
    private val isMainThread: Boolean
        private get() = Looper.getMainLooper() == Looper.myLooper()

    /**
     * 判断是否是主进程
     *
     * @param context Context
     * @return 是否是主进程
     */
    private fun isMainProcess(context: Context): Boolean {
        return isCurrentProcess(context.packageName)
    }

    /**
     * 判断是否是当前进程
     *
     * @param processName 进程名称
     * @return 是否是当前进程
     */
    private fun isCurrentProcess(processName: String?): Boolean {
        return processName != null && processName == currentProcessName
    }

    /**
     * 获取当前进程名
     */
    private val currentProcessName: String
        private get() {
            val pid = Process.myPid()
            var processName = ""
            val manager =
                applicationContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            for (process in manager.runningAppProcesses) {
                if (process.pid == pid) {
                    processName = process.processName
                }
            }
            return processName
        }
}