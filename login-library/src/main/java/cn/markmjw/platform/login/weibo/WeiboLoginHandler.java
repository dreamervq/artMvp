package cn.markmjw.platform.login.weibo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cn.markmjw.platform.PlatformConfig;
import cn.markmjw.platform.login.AuthResult;
import cn.markmjw.platform.login.BaseLoginHandler;
import cn.markmjw.platform.login.ILoginListener;
import cn.markmjw.platform.util.GsonUtil;
import cn.markmjw.platform.util.HttpUtil;

/**
 * 微博登录
 *
 * @author markmjw
 * @date 2014-03-27
 */
public class WeiboLoginHandler extends BaseLoginHandler {
    /** 获取用户信息. */
    private static final String URL_GET_USER_INFO = "https://api.weibo.com/2/users/show.json";

    private SsoHandler mSsoHandler;

    public WeiboLoginHandler() {

    }

    public void login(Activity activity, ILoginListener listener) {
        setCallBack(listener);
        PlatformConfig config = PlatformConfig.getInstance();
        AuthInfo weiboAuth = new AuthInfo(activity, config.getWeiboKey(), config.getWeiboCallback(),
                config.getWeiboScope());

        mSsoHandler = new SsoHandler(activity, weiboAuth);
        mSsoHandler.authorize(mAuthListener);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (null != mSsoHandler) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    private void requestUserInfo(final WeiboLoginResult result) {
        Map<String, String> params = new HashMap<>();
        params.put("uid", result.uid);
        params.put("access_token", result.access_token);
        params.put("source", PlatformConfig.getInstance().getWeiboKey());
        String url = HttpUtil.buildUrl(URL_GET_USER_INFO, params);
        Request request = new Request.Builder().url(url).build();
        HttpUtil.enqueue(request, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                // 回调->登录失败
                callBack(ILoginListener.CODE_FAILED, e.getMessage());
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    String data = response.body().string();
                    WeiboUserInfo info = GsonUtil.fromJson(data, WeiboUserInfo.class);
                    // 回调->登录成功
                    callBack(ILoginListener.CODE_SUCCESS, info);
                    System.out.println(response.body().string());
                } else {
                    // 回调->登录失败
                    callBack(ILoginListener.CODE_FAILED, response.message());
                }
            }
        });
    }

    private WeiboAuthListener mAuthListener = new WeiboAuthListener() {

        @Override
        public void onComplete(Bundle values) {
            // 解析授权结果
            WeiboLoginResult result = new WeiboLoginResult();
            result.access_token = values.getString("access_token");
            result.expires_in = values.getString("expires_in");
            result.remind_in = values.getString("remind_in");
            result.uid = values.getString("uid");
            result.userName = values.getString("userName");
            result.refresh_token = values.getString("refresh_token");

            // 转换为统一的授权数据
            AuthResult auth = new AuthResult();
            auth.from = AuthResult.TYPE_WEIBO;
            auth.id = result.uid;
            auth.accessToken = result.access_token;
            auth.refreshToken = result.refresh_token;
            auth.expiresIn = System.currentTimeMillis() + Long.parseLong(result.expires_in) * 1000L;

            log("微博授权成功!" +
                    "\nUid: " + auth.id +
                    "\nAccess token: " + auth.accessToken +
                    "\nExpires in: " + formatDate(auth.expiresIn));

            // 回调->授权成功
            callBack(ILoginListener.CODE_AUTH_SUCCESS, auth);
            if (mRequestInfoEnable) {
                // 回调->登录中
                callBack(ILoginListener.CODE_LOGIN_ING, "");
                // 请求用户信息
                requestUserInfo(result);
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            // 回调->授权异常
            callBack(ILoginListener.CODE_AUTH_EXCEPTION, e);
        }

        @Override
        public void onCancel() {
            // 回调->取消授权
            callBack(ILoginListener.CODE_CANCEL_AUTH, null);
        }
    };
}
