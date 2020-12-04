package cn.markmjw.platform.login.qq;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.tencent.connect.UserInfo;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import cn.markmjw.platform.QQManager;
import cn.markmjw.platform.login.AuthResult;
import cn.markmjw.platform.login.BaseLoginHandler;
import cn.markmjw.platform.login.ILoginListener;
import cn.markmjw.platform.util.GsonUtil;

/**
 * QQ登录
 *
 * @author markmjw
 * @date 2015-04-07
 */
public class QQLoginHandler extends BaseLoginHandler implements IUiListener {
    private QQManager mManager;
    private Context mContext;

    public QQLoginHandler(Context context) {
        mContext = context.getApplicationContext();
        mManager = QQManager.getInstance(context);
    }

    public void login(Activity activity, ILoginListener listener) {
        setCallBack(listener);
        mManager.getTencent().login(activity, "all", mAuthListener);
    }

    public void logout(Activity activity) {
        mManager.getTencent().logout(activity);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mManager.getTencent().onActivityResult(requestCode, resultCode, data);
        Tencent.handleResultData(data, this);
    }

    private IUiListener mAuthListener = new IUiListener() {
        @Override
        public void onComplete(Object response) {
            if (null == response) {
                callBack(ILoginListener.CODE_AUTH_FAILED, "");
                // 释放资源
                mManager.getTencent().releaseResource();
                return;
            }

            // 登录成功
            QQLoginResult result = GsonUtil.fromJson(response + "", QQLoginResult.class);
            AuthResult auth = new AuthResult();
            auth.from = AuthResult.TYPE_QQ;
            auth.id = result.openid;
            auth.accessToken = result.access_token;
            auth.expiresIn = result.expires_in;

            mManager.getTencent().setAccessToken(auth.accessToken, auth.expiresIn + "");
            mManager.getTencent().setOpenId(auth.id);
            long expiresIn = mManager.getTencent().getExpiresIn();
            log("QQ授权成功!" +
                    "\nOpenId: " + auth.id +
                    "\nAccess token: " + auth.accessToken +
                    "\nExpires in: " + formatDate(expiresIn));

            // 回调->授权成功
            callBack(ILoginListener.CODE_AUTH_SUCCESS, auth);
            if (mRequestInfoEnable) {
                // 回调->登录中
                callBack(ILoginListener.CODE_LOGIN_ING, "");
                // 请求用户信息
                new UserInfo(mContext, mManager.getTencent().getQQToken())
                        .getUserInfo(mGetInfoListener);
            }
        }

        @Override
        public void onError(UiError e) {
            // 回调->授权异常
            callBack(ILoginListener.CODE_AUTH_EXCEPTION, e.errorMessage);
            // 释放资源
            mManager.getTencent().releaseResource();
        }

        @Override
        public void onCancel() {
            // 回调->取消授权
            callBack(ILoginListener.CODE_CANCEL_AUTH, null);
            // 释放资源
            mManager.getTencent().releaseResource();
        }
    };

    private IUiListener mGetInfoListener = new IUiListener() {
        @Override
        public void onComplete(Object response) {
            if (null == response) {
                callBack(ILoginListener.CODE_AUTH_FAILED, "");
                // 释放资源
                mManager.getTencent().releaseResource();
                return;
            }

            // 获取个人信息成功
            QQUserInfo info = GsonUtil.fromJson(response + "", QQUserInfo.class);
            // 回调->登录成功
            callBack(ILoginListener.CODE_SUCCESS, info);

            // 释放资源
            mManager.getTencent().releaseResource();
        }

        @Override
        public void onError(UiError e) {
            // 回调->登录成功
            callBack(ILoginListener.CODE_FAILED, e.errorMessage);
            // 释放资源
            mManager.getTencent().releaseResource();
        }

        @Override
        public void onCancel() {
            // 回调->取消登录
            callBack(ILoginListener.CODE_CANCEL_AUTH, null);
            // 释放资源
            mManager.getTencent().releaseResource();
        }
    };

    @Override
    public void onComplete(Object o) {

    }

    @Override
    public void onError(UiError uiError) {

    }

    @Override
    public void onCancel() {

    }
}
