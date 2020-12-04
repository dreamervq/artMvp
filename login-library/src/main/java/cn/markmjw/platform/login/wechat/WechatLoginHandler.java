package cn.markmjw.platform.login.wechat;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

import cn.markmjw.platform.PlatformConfig;
import cn.markmjw.platform.WechatManager;
import cn.markmjw.platform.login.AuthResult;
import cn.markmjw.platform.login.BaseLoginHandler;
import cn.markmjw.platform.login.ILoginListener;
import cn.markmjw.platform.util.GsonUtil;
import cn.markmjw.platform.util.HttpUtil;

/**
 * 微信登录
 *
 * @author markmjw
 * @date 2015-04-21
 */
public class WechatLoginHandler extends BaseLoginHandler {
    /** Get token url. */
    private static final String URL_TOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token";
    /** Get user info url. */
    private static final String URL_WECHAT_USER = "https://api.weixin.qq.com/sns/userinfo";

    private WechatManager mManager;

    public WechatLoginHandler(Context context) {
        mManager = WechatManager.getInstance(context);
    }

    public void login(ILoginListener listener) {
        setCallBack(listener);
        SendAuth.Req request = new SendAuth.Req();
        request.scope = PlatformConfig.getInstance().getWechatScope();
        request.state = PlatformConfig.getInstance().getWechatState();
        mManager.getAPI().sendReq(request);
        Log.e("tag", "----------------------------"+PlatformConfig.getInstance().getWechatId()+"   "+mManager.isInstalled());
    }

    public void handleResponse(SendAuth.Resp response) {
        switch (response.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                String code = response.code;
                if (!TextUtils.isEmpty(code)) {
                    // 回调->登录中
                    callBack(ILoginListener.CODE_LOGIN_ING, "");
                    requestToken(code);
                } else {
                    // 回调->授权失败
                    callBack(ILoginListener.CODE_AUTH_FAILED, "");
                }
                break;

            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                // 回调->授权失败
                callBack(ILoginListener.CODE_AUTH_FAILED, "");
                break;

            case BaseResp.ErrCode.ERR_USER_CANCEL:
                // 回调->取消授权
                callBack(ILoginListener.CODE_CANCEL_AUTH, "");
                break;
        }
    }

    private void requestToken(String code) {
        Map<String, String> params = new HashMap<>();
        params.put("appid", PlatformConfig.getInstance().getWechatId());
        params.put("secret", PlatformConfig.getInstance().getWechatSecret());
        params.put("code", code);
        params.put("grant_type", "authorization_code");
        String url = HttpUtil.buildUrl(URL_TOKEN, params);
        final Request request = new Request.Builder().url(url).build();
        HttpUtil.enqueue(request, new Callback() {

            @Override
            public void onFailure(Request request, IOException e) {
                // 回调->授权失败
                callBack(ILoginListener.CODE_AUTH_FAILED, e.getMessage());
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    String data = response.body().string();
                    // 授权成功
                    WechatLoginResult result = GsonUtil.fromJson(data, WechatLoginResult.class);
                    AuthResult auth = new AuthResult();
                    auth.from = AuthResult.TYPE_WECHAT;
                    auth.id = result.openid;
                    auth.accessToken = result.accessToken;
                    auth.expiresIn = result.expiresIn;
                    auth.refreshToken = result.refreshToken;

                    log("微信授权成功!" +
                            "\nOpenId: " + auth.id +
                            "\nAccess token: " + auth.accessToken +
                            "\nExpires in: " + formatDate(auth.expiresIn));

                    // 回调->授权成功
                    callBack(ILoginListener.CODE_AUTH_SUCCESS, auth);
                    if (mRequestInfoEnable) {
                        // 回调->登录中
                        callBack(ILoginListener.CODE_LOGIN_ING, "");
                        // 请求用户信息
                        requestUserInfo(auth.id, auth.accessToken);
                    }
                } else {
                    // 回调->授权失败
                    callBack(ILoginListener.CODE_AUTH_FAILED, response.message());
                }
            }
        });
    }

    private void requestUserInfo(String openId, String accessToken) {
        Map<String, String> params = new HashMap<>();
        params.put("access_token", accessToken);
        params.put("openid", openId);
        String url = HttpUtil.buildUrl(URL_WECHAT_USER, params);
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
                    WechatUserInfo info = GsonUtil.fromJson(data, WechatUserInfo.class);
                    // 回调->登录成功
                    callBack(ILoginListener.CODE_SUCCESS, info);
                } else {
                    // 回调->登录失败
                    callBack(ILoginListener.CODE_FAILED, response.message());
                }
            }
        });
    }
}
