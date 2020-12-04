package cn.markmjw.platform.login;

import java.io.Serializable;

/**
 * 通用的第三方授权Result
 *
 * @author markmjw
 * @date 2015-05-05
 */
public class AuthResult implements Serializable {
    /** 未登录 */
    public static final int TYPE_NONE = 0;
    /** 微博登录 */
    public static final int TYPE_WEIBO = 1;
    /** 微信登录 */
    public static final int TYPE_WECHAT = 2;
    /** QQ登录 */
    public static final int TYPE_QQ = 3;

    public int from = TYPE_NONE;
    public String id;
    public String accessToken;
    public long expiresIn;
    public String refreshToken = "";

    public void copy(AuthResult result) {
        if (null != result) {
            from = result.from;
            id = result.id;
            accessToken = result.accessToken;
            refreshToken = result.refreshToken;
            expiresIn = result.expiresIn;
        }
    }

    @Override
    public String toString() {
        return "AuthResult{" +
                "from=" + from +
                ", id='" + id + '\'' +
                ", accessToken='" + accessToken + '\'' +
                ", expiresIn=" + expiresIn +
                ", refreshToken='" + refreshToken + '\'' +
                '}';
    }
}
