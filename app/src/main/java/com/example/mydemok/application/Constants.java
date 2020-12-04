package com.example.mydemok.application;

import android.os.Environment;

import java.io.File;

public class Constants {
    public static final int KEY_SUCCESS = 1001;
    public static final int KEY_FAILED = 1002;



    private static final int SERVER_ENVIRONMENT_DEV = 1;
    private static final int SERVER_ENVIRONMENT_ZDG_TEST = 2;
    private static final int SERVER_ENVIRONMENT_ZDG_OFFICIAL = 3;
    public static final int SERVER_ENVIRONMENT = SERVER_ENVIRONMENT_ZDG_OFFICIAL;


    private static final String URL_HOST_DEV = "http://app.huashengfm.xinmem.com/appapi/";  //dev站url
    private static final String URL_HOST_TEST = "http://app.test.zdg.sun0769.com/appapi/";  //测试站url
    private static final String URL_HOST_OFFICIAL = "https://api.radiofoshan.com.cn/appapi/"; //正式站url
    public static final String URL_HOST = SERVER_ENVIRONMENT == SERVER_ENVIRONMENT_ZDG_OFFICIAL ? URL_HOST_OFFICIAL :
            (SERVER_ENVIRONMENT == SERVER_ENVIRONMENT_ZDG_TEST ? URL_HOST_TEST : URL_HOST_DEV);

    public static final String PATH_SDCARD = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "test";
    public static final String PATH_IMG = PATH_SDCARD + File.separator + "image" + File.separator;
    /**
     * 鉴权
     */
    public static final String ART_KEY = "huashengfm";
    public static final String ART_SECRET = "b165ZL39eqK1361c6ec0pmE63b22e2B";
}
