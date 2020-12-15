package com.example.mydemok.application;

import android.os.Environment;

import com.example.mydemok.mvp.model.AppAgreement;

import java.io.File;

public class Constants {
    public static final int KEY_SUCCESS = 1001;
    public static final int KEY_FAILED = 1002;
    public static final String PREF_FIRST = "xinmeng_sharedpreference_first";
    public static final String HAS_SET_NOTIFICATION = "has_set_notification";
    public static final String PREF_UPDATE = "xinmeng_sharedpreference_appupdate";
    public static String SHARE_IMAGE_URL = "";
    public static AppAgreement APP_AGREEMENT = null;
    public static final int ARG_1 = 1;
    public static final int ARG_2 = 2;
    public static final int ARG_3 = 3;
    public static final int ARG_4 = 4;
    public static final int ARG_5 = 5;
    public static final int ARG_6 = 6;
    public static final int ARG_7 = 7;
    public static final int ARG_MAIN = 11;

    private static final int SERVER_ENVIRONMENT_DEV = 1;
    private static final int SERVER_ENVIRONMENT_ZDG_TEST = 2;
    private static final int SERVER_ENVIRONMENT_ZDG_OFFICIAL = 3;
    public static final int SERVER_ENVIRONMENT = SERVER_ENVIRONMENT_ZDG_OFFICIAL;


    private static final String URL_HOST_DEV = "http://app.huashengfm.xinmem.com/appapi/";  //dev站url
    private static final String URL_HOST_TEST = "http://app.test.zdg.sun0769.com/appapi/";  //测试站url
    private static final String URL_HOST_OFFICIAL = "https://api.radiofoshan.com.cn/appapi/"; //正式站url
    public static final String URL_HOST = SERVER_ENVIRONMENT == SERVER_ENVIRONMENT_ZDG_OFFICIAL ? URL_HOST_OFFICIAL :
            (SERVER_ENVIRONMENT == SERVER_ENVIRONMENT_ZDG_TEST ? URL_HOST_TEST : URL_HOST_DEV);

    private static String URL_WEB_HOST_DEV = "http://admin.huashengfm.xinmem.com/web/radiopeanut/content";
    private static String URL_WEB_HOST_TEST = "http://app.test.zdg.sun0769.com/web/";
    private static String URL_WEB_HOST_OFFICIAL = "http://app.radiofoshan.com.cn/web/news/content/";
    public static String URL_WEB_HOST = SERVER_ENVIRONMENT == SERVER_ENVIRONMENT_ZDG_OFFICIAL ? URL_WEB_HOST_OFFICIAL :
            (SERVER_ENVIRONMENT == SERVER_ENVIRONMENT_ZDG_TEST ? URL_WEB_HOST_TEST : URL_WEB_HOST_DEV);

    public static final String PATH_SDCARD = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "test";
    public static final String PATH_IMG = PATH_SDCARD + File.separator + "image" + File.separator;
    /**
     * 鉴权
     */
    public static final String ART_KEY = "huashengfm";
    public static final String ART_SECRET = "b165ZL39eqK1361c6ec0pmE63b22e2B";

}
