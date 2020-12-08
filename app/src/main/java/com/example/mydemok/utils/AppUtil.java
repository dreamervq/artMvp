package com.example.mydemok.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.mydemok.R;
import com.example.mydemok.application.App;
import com.example.mydemok.application.Constants;
import com.example.mydemok.manager.LoginManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;


/**
 * Created by magic-lee on 15/4/7.
 */
public class AppUtil {

    private static final String TAG = "AppUtil";

    private static String sAndroidId;
    private static Point sPoint;
    private static String sVersion;
    private static String sImei;
    private static String mChannel;
    private static String mReqDeviceId;
    private static String mHash;

    public static void createShortCut() {
        Intent intent = new Intent();
        //intent.setClass(gContext, SplashActivity.class);
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        Intent shortcut = new Intent("taccb.android.launcher.action.INSTALL_SHORTCUT");
        shortcut.putExtra("duplicate", false);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, App.getInstance().getString(R.string.app_name));
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(App.getInstance(), R.mipmap.ic_launcher));
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
//        App.getInstance().sendBroadcast(shortcut);
    }

    /**
     * 系统
     *
     * @return
     */
    public static String getOS() {
        return "Android";
    }

    /**
     * 系统版本号
     *
     * @return
     */
    public static String getOSVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 设备名称
     *
     * @return
     */
    public static String getDevice() {
        return Build.MODEL;
    }

    /**
     * 设备唯一标示
     *
     * @return MD5(IMEI + android_id)
     */
    public static String getReqDeviceId() {
        if (TextUtils.isEmpty(mReqDeviceId)) {
            final StringBuilder gDidSb = new StringBuilder();
            String imei = getIMEI();
            if (!TextUtils.isEmpty(imei)) {
                gDidSb.append(imei);
            }

            String android_id = getAndroidId();
            if (!TextUtils.isEmpty(android_id)) {
                gDidSb.append(android_id);
            }

            mReqDeviceId = encrypt(gDidSb.toString(), "MD5");
        }
        return mReqDeviceId;
    }

    public static String encrypt(String strSrc, String encName) {
        String sign = "";
        try {
            MessageDigest md = MessageDigest.getInstance(encName);
            sign = byte2hex(md.digest(strSrc.getBytes("utf-8")));
        } catch (Exception e) {
            // ignore
        }

        return sign;
    }

    public static String getAppProcessName(Context context) {
        //当前应用pid
        int pid = android.os.Process.myPid();
        //任务管理类
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //遍历所有应用
        String packageName = "";
        if (manager != null) {
            List<ActivityManager.RunningAppProcessInfo> infos = manager.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo info : infos) {
                if (info != null && info.pid == pid) {//得到当前应用
                    packageName = info.processName;//返回包名
                    break;
                }
            }
        }
        return packageName;
    }

    private static String byte2hex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        String temp;
        for (byte b : bytes) {
            temp = (Integer.toHexString(b & 0XFF));
            if (temp.length() == 1) builder.append("0").append(temp);
            else builder.append(temp);
        }
        return builder.toString().toLowerCase(Locale.CHINA);
    }

    /**
     * 获取androidId.
     *
     * @return
     */
    public static String getAndroidId() {
        if (TextUtils.isEmpty(sAndroidId)) {
            sAndroidId = Settings.Secure.getString(App.getInstance().getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        return sAndroidId;
    }

    /**
     * 获取程序版本号.
     *
     * @return empty string if failed
     */
    public static String getAppVersion() {
        if (TextUtils.isEmpty(sVersion)) {
            try {
                Context context = App.getInstance();
                sVersion = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            } catch (Throwable e) {
                sVersion = "1.0";
            }
        }
        return sVersion;
    }

    /**
     * 获取IMEI值.
     *
     * @return empty string if failed
     */
    @SuppressLint("MissingPermission")
    public static String getIMEI() {
        if (TextUtils.isEmpty(sImei)) {
            try {
                Context context = App.getInstance();
                sImei = ((TelephonyManager) (context.getSystemService(Context.TELEPHONY_SERVICE))).getDeviceId();
            } catch (Throwable e) {
                sImei = "";
            }
        }
        return sImei;
    }

    /**
     * 获取本地Mac地址.
     * <p>
     * the context
     *
     * @return empty string if failed
     */
    public static String getMacAddress() {
        String macAdress = null;
        try {
            WifiManager wifi = (WifiManager) App.getInstance().getApplicationContext().getSystemService(Context.WIFI_SERVICE);

            WifiInfo info = wifi.getConnectionInfo();

            macAdress = info.getMacAddress();
        } catch (Throwable e) {
        }
        return macAdress == null ? "" : macAdress;
    }


    /**
     * 语言、国家
     *
     * @return
     */
    public static String getLocale() {
        Locale locale = Locale.getDefault();
        return locale.getLanguage() + "_" + locale.getCountry();
    }


    /**
     * 判断是否使用MIUI
     *
     * @return
     */
    public static boolean isMIUI() {
        String systemProperty = getSystemProperty("ro.miui.ui.version.name");
        return !TextUtils.isEmpty(systemProperty);
    }

    /**
     * 判断是否Flyme os
     *
     * @return
     */
    //    public static boolean isFlyme() {
    //        return Build.BRAND.contains("Meizu") || Build.BRAND.contains("meizu") || Build.BRAND
    //                .contains("MEIZU");
    //    }

    /**
     * 判断是否Flyme os
     *
     * @return
     */
    public static boolean isFlyme() {
        try {
            // Invoke Build.hasSmartBar()
            final Method method = Build.class.getMethod("hasSmartBar");
            return method != null;
        } catch (final Exception e) {
            return false;
        }
    }

    public static String getSystemProperty(String propName) {
        String line;
        BufferedReader input = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + propName);
            input = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
            input.close();
        } catch (IOException ex) {
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                }
            }
        }
        return line;
    }
//
//    /**
//     * 是否为竖屏
//     *
//     * @param context
//     * @return
//     */
//    public static boolean isPortrait(Context context) {
//        Configuration configuration = context.getResources().getConfiguration();
//        int ori = configuration.orientation;
//
//        if (ori == Configuration.ORIENTATION_PORTRAIT) {
//            return true;
//        } else if (ori == Configuration.ORIENTATION_LANDSCAPE) {
//            return false;
//        } else {
//            return true;
//        }
//    }

    public static String getPhoneMsg() {
        StringBuffer sb = new StringBuffer();
        sb.append("{");
        sb.append("device:" + getDevice());
        sb.append(",os:" + getOS());
        sb.append(",os_version:" + getOSVersion());
        sb.append(",locale:" + getLocale());
        sb.append(",app_version:" + getAppVersion());
        sb.append("}");
        String result = sb.toString();
        try {
            result = java.net.URLEncoder.encode(result, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // ignore
        }
        return result;
    }

    /**
     * 获取渠道号
     *
     * @return
     */
    public static String getChannel() {
        if (TextUtils.isEmpty(mChannel)) {
            try {
                mChannel = getMetaData("UMENG_CHANNEL");
            } catch (Exception e) {
            }
        }
        return mChannel;
    }

    private static String getMetaData(String key) throws PackageManager.NameNotFoundException {
        PackageManager pm = App.getInstance().getPackageManager();
        if (null != pm) {
            ApplicationInfo ai = pm.getApplicationInfo(App.getInstance().getPackageName(), PackageManager.GET_META_DATA);
            if (null != ai.metaData) {
                Object value = ai.metaData.get(key);
                if (value != null) {
                    return value.toString();
                }
            }
        }
        return null;
    }

    public static boolean isAppInstalled(String packageName) {
        try {
            PackageInfo packageInfo = App.getInstance().getPackageManager().getPackageInfo(packageName, 0);
            return true;
        } catch (Exception e) {

        }
        return false;
    }

    /**
     * 显示软键盘
     *
     * @param view the view
     */
    public static void showSoftInput(final View view) {
        if (null != view) {
            view.post(new Runnable() {
                @Override
                public void run() {
                    InputMethodManager imm = (InputMethodManager) App.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(view, InputMethodManager.HIDE_NOT_ALWAYS);
                }
            });
        }
    }

    /**
     * 隐藏软键盘
     *
     * @param view the view
     */
    public static void hideSoftInput(final View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) App.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void openKeybord(EditText mEditText, FragmentActivity activity) {
        if (activity != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        }
    }

    public static void toggleSoftInput() {
        InputMethodManager imm = (InputMethodManager) App.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 将分享地址复制到剪切板
     *
     * @param str
     */
    public static void share2copy(String str) {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) App.getInstance().getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(str.trim());
    }

    public static boolean ishasSimCard(Context context) {
        TelephonyManager telMgr = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);
        int simState = telMgr.getSimState();
        boolean result = true;
        switch (simState) {
            case TelephonyManager.SIM_STATE_ABSENT:
                result = false; // 没有SIM卡
                break;
            case TelephonyManager.SIM_STATE_UNKNOWN:
                result = false;
                break;
        }
        return result;
    }

//    public static String getHash() {
//        if (TextUtils.isEmpty(mHash)) {
//            String md5FirstTime = StringUtil.encrypt(Constants.ART_SECRET, "MD5");
//            //String subSecondTime = md5FirstTime.substring(8, md5FirstTime.length()) + "108287";
//            String subSecondTime = md5FirstTime.substring(8, md5FirstTime.length()) + LoginManager.getInstance().getUser().id;
//            mHash = StringUtil.encrypt(subSecondTime, "MD5");
//        }
//        return mHash;
//    }

    public final static int DRAWABLE_LEFT = 0;
    public final static int DRAWABLE_RIGHT = 1;
    public final static int DRAWABLE_TOP = 2;
    public final static int DRAWABLE_DOWN = 3;

    public static void addTextIcon(int resId, TextView tv, int direction) {
        Drawable drawable = App.getInstance().getResources().getDrawable(resId);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        if (direction == DRAWABLE_LEFT) {
            tv.setCompoundDrawables(drawable, null, null, null);
        } else if (direction == DRAWABLE_TOP) {
            tv.setCompoundDrawables(null, drawable, null, null);
        } else if (direction == DRAWABLE_RIGHT) {
            tv.setCompoundDrawables(null, null, drawable, null);
        } else if (direction == DRAWABLE_DOWN) {
            tv.setCompoundDrawables(null, null, null, drawable);
        }
    }

    /**
     * 电话号码的基本判断
     *
     * @param str
     * @return
     */
    public static Boolean isMobileNo(String str) {
        try {
            String regExp = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
            Pattern p = Pattern.compile(regExp);
            Matcher m = p.matcher(str);
            return m.matches();
        } catch (PatternSyntaxException e) {
            return false;
        }
    }

    public static void showImg(boolean isOk, ImageView imageView) {
        if (isOk) {
            ColorMatrix cm = new ColorMatrix();
            cm.setSaturation(0); // 设置饱和度
            ColorMatrixColorFilter grayColorFilter = new ColorMatrixColorFilter(cm);
            imageView.setColorFilter(grayColorFilter);
        } else {
            imageView.setColorFilter(null);
        }
    }

    public static boolean isLocationServicesAvailable(Context context) {
        int locationMode = 0;
        String locationProviders;
        boolean isAvailable = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
            isAvailable = (locationMode != Settings.Secure.LOCATION_MODE_OFF);
        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            isAvailable = !TextUtils.isEmpty(locationProviders);
        }
        boolean coarsePermissionCheck = (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED);
        boolean finePermissionCheck = (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);
        return isAvailable && (coarsePermissionCheck || finePermissionCheck);
    }

    public static String getHash() {
        if (TextUtils.isEmpty(mHash)) {
            String md5FirstTime = encrypt(Constants.ART_SECRET, "MD5");
            String subSecondTime = md5FirstTime.substring(8, md5FirstTime.length()) + LoginManager.getInstance().getUser().getId();
            mHash = encrypt(subSecondTime, "MD5");
        }
        return mHash;
    }

    public static long getTimeMorning(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis() / 1000;
    }


    public static Activity findActivity(Context context) {
        if (context instanceof Activity) {
            return (Activity) context;
        }
        if (context instanceof ContextWrapper) {
            ContextWrapper wrapper = (ContextWrapper) context;
            return findActivity(wrapper.getBaseContext());
        } else {
            return null;
        }
    }

    /**
     * 获取版本号
     */
    public static int getVersionCode(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }
}
