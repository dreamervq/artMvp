package cn.markmjw.platform.login;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 登录抽象类
 *
 * @author markmjw
 * @date 2015-04-08
 */
public abstract class BaseLoginHandler {
    protected static final String TAG = "LoginHandler";

    protected boolean mRequestInfoEnable = true;
    protected boolean mLogEnable = true;

    private ILoginListener mLoginListener;

    public BaseLoginHandler() {

    }

    public void setRequestUserInfo(boolean requestEnable) {
        mRequestInfoEnable = requestEnable;
    }

    public void setLogEnable(boolean enable) {
        mRequestInfoEnable = mLogEnable;
    }

    /**
     * 设置登录状态监听器
     *
     * @param listener
     */
    protected void setCallBack(ILoginListener listener) {
        mLoginListener = listener;
    }

    /**
     * 登录回调
     *
     * @param statusCode
     * @param data
     */
    protected synchronized void callBack(int statusCode, Object data) {
        if (null != mLoginListener) {
            mLoginListener.loginStatus(statusCode, data);
        }
    }

    protected String formatDate(long time) {
        return new SimpleDateFormat("yyyy-MM-dd hh:MM:ss", Locale.CHINA).format(new Date(time));
    }

    protected void log(String message) {
        if (mLogEnable) {
            Log.i(TAG, message);
        }
    }
}
