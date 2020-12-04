package com.example.mydemok.manager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.text.TextUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.example.mydemok.model.LoginInfo;
import com.example.mydemok.model.UserEntity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cn.markmjw.platform.login.AuthResult;
import cn.markmjw.platform.util.GsonUtil;
import me.jessyan.art.base.BaseApplication;
import timber.log.Timber;


/**
 * 用户管理器
 *
 * @author magic-lee
 * @date 2015-04-13
 */
public class LoginManager {
    /**
     * 登录成功Action
     */
    public static final String ACTION_LOGIN = "com.xinmeng.radiopeanut.LoginManager.LOGIN";
    /**
     * 退出登录Action
     */
    public static final String ACTION_LOGOUT = "com.xinmeng.radiopeanut.LoginManager.LOGOUT";

    private static final String PREF_NAME = "user_prefs";
    private static final String KEY_LOGIN_INFO = "login_info";
    private static final String KEY_USER_INFO = "user_info";
    private static final String KEY_FORUM_USER_INFO = "forum_login_info";

    private static LoginManager sInstance;

    private SharedPreferences mPref;

    private LoginInfo mLoginInfo;

//    public LoginDialog.LoginStatusListener mLoginListener;

    private LoginManager(Context context) {
        mPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    /**
     * 获取实例
     *
     */
    public synchronized static LoginManager getInstance() {
        if (sInstance == null) {
            sInstance = new LoginManager(BaseApplication.getInstance());
        }
        return sInstance;
    }

    /**
     * 初始化
     */
    public void init() {
        readLoginInfo();
    }

    /**
     * 登录成功
     *
     */
    public void login(LoginInfo info) {
        if (null != info) {
            mLoginInfo = info;
            // 保持登录信息
            saveLoginInfo(info);

            // 发送登录的事件
            Intent intent = new Intent();
            intent.setAction(ACTION_LOGIN);
//            AppBroadcastManager.sendBroadcast(intent);
        }
    }

    /**
     * 注销登录
     */
    public void logout() {
        if (isLoginValid()) {
            // 清理登录信息
            // 清除cookies
            clearCookies();
            //   TinyPref.getInstance().remove(Constants.KEY_CONTENT_VIEWS);
            clearLoginInfo();
            // 发送退出登录的事件
//            Intent intent = new Intent();
//            intent.setAction(ACTION_LOGOUT);
//            AppBroadcastManager.sendBroadcast(intent);
        }
    }

    /**
     * 获取当前登录用户信息
     */
    public LoginInfo getLoginInfo() {
        if (null == mLoginInfo) {
            readLoginInfo();
        }

        return mLoginInfo;
    }

    /**
     * 是否微博用户
     *
     */
    public boolean isWeiboUser() {
        if (null != mLoginInfo && null != mLoginInfo.getAuth()) {
            return mLoginInfo.getAuth().from == AuthResult.TYPE_WEIBO;
        }
        return false;
    }

    /**
     * 登录是否有效
     *
     */
    public boolean isLoginValid() {
        if (getLoginInfo() == null) return false;
        getLoginInfo().getUser();
        return true;
    }

    /**
     * 获取当前登录用户信息
     *
     */
    public UserEntity getUser() {
        if (null == mLoginInfo) {
            readLoginInfo();
        }

        return null != mLoginInfo ? mLoginInfo.getUser() : null;
    }

    /**
     * 获取当前登录用户信息授权信息
     *
     */
    public AuthResult getAuth() {
        if (null == mLoginInfo) {
            readLoginInfo();
        }

        return null != mLoginInfo ? mLoginInfo.getAuth() : null;
    }

    /**
     * 判断Token是否有效，针对第三方登录有效
     *
     * @return boolean
     */
    public boolean isTokenValid() {
        if (null != mLoginInfo) {
            AuthResult result = mLoginInfo.getAuth();
            if (null != result && !TextUtils.isEmpty(result.accessToken)) {
                long expiresTime = result.expiresIn;
                String date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINA).
                        format(new Date(expiresTime));
                Timber.d("-----------------------授权有效期：" + date);

                if (System.currentTimeMillis() < expiresTime) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 从配置文件中读取用户登录信息
     */
    private void readLoginInfo() {
        if (mLoginInfo == null || mLoginInfo.getUser() == null || TextUtils.isEmpty(mLoginInfo.getUser().getSessionid())) {
            try {
                String content = mPref.getString(KEY_LOGIN_INFO, "");
                if (!TextUtils.isEmpty(content)) {
                    mLoginInfo = GsonUtil.fromJson(content, LoginInfo.class);
                }
            } catch (Exception e) {
                Timber.e(e);
            }
        }

    }

    /**
     * 保存用户登录信息
     *
     */
    public void saveLoginInfo(LoginInfo info) {
        if (info != null) {
            Editor editor = mPref.edit();
            editor.putString(KEY_LOGIN_INFO, GsonUtil.toJson(info));
            editor.apply();
        }
    }

    /**
     * 保存用户登录信息
     *
     */
    public void saveLoginInfo(UserEntity user) {
        if (user != null) {
            if (null == mLoginInfo) {
                readLoginInfo();
            }
            mLoginInfo.setUser(user);
            saveLoginInfo(mLoginInfo);
        }
    }

    public void saveCurLoginInfo() {
        if (mLoginInfo != null) {
            Editor editor = mPref.edit();
            editor.putString(KEY_LOGIN_INFO, GsonUtil.toJson(mLoginInfo));
            editor.apply();
        }
    }

    public void syncSessionId(String url) {
        if (isLoginValid()) {
            String sessionId = mLoginInfo.getUser().getSessionid();
            if (TextUtils.isEmpty(sessionId)) {
                return;
            }
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeSessionCookie();
            cookieManager.setCookie(url, "PHPSEESSID=" + sessionId);
            if (Build.VERSION.SDK_INT >= 21) {
                cookieManager.flush();
            } else {
                CookieSyncManager.getInstance().sync();
            }
        }
    }

    /**
     * 清除用户登录信息
     */
    private void clearLoginInfo() {
        Editor editor = mPref.edit();
        editor.clear();
        editor.apply();
        mLoginInfo = null;
    }

    private void clearCookies() {
        CookieManager cookieManager = CookieManager.getInstance();
        if (Build.VERSION.SDK_INT >= 21) {
            cookieManager.removeAllCookies(null);
        } else {
            cookieManager.removeAllCookie();
        }
    }

    //用户信息
    public void saveUserInfo(UserEntity user) {
        if (user != null) {
            Editor editor = mPref.edit();
            editor.putString(KEY_USER_INFO, GsonUtil.toJson(user));
            editor.apply();
        }
    }

    public UserEntity getUserInfo() {
        UserEntity user = null;
        String content = mPref.getString(KEY_USER_INFO, "");
        Timber.e("------------------------" + content);
        if (!TextUtils.isEmpty(content)) {
            user = GsonUtil.fromJson(content, UserEntity.class);
        }
        return user;
    }

    public String getUserSession() {
        UserEntity user = getUser();
        if (user != null)
            return user.getSessionid();
        else
            return "";
    }

    private DefaultListener defaultListener;

    public DefaultListener getDefaultListener() {
        return defaultListener;
    }

    public void setDefaultListener(DefaultListener defaultListener) {
        this.defaultListener = defaultListener;
    }

    public interface LoginStatusListener {
        void onSuccess(Context context);

        void onFailed();
    }

    public static abstract class DefaultListener implements LoginStatusListener {
        @Override
        public void onFailed() {

        }
    }
}
