package cn.markmjw.platform.login;

/**
 * 登录状态监听器
 *
 * @author markmjw
 * @date 2015-04-07
 */
public interface ILoginListener {
    /** 登录成功. */
    int CODE_SUCCESS = 0x00;
    /** 登录失败. */
    int CODE_FAILED = 0x01;
    /** 登录中. */
    int CODE_LOGIN_ING = 0x02;
    /** 授权成功. */
    int CODE_AUTH_SUCCESS = 0x03;
    /** 授权异常. */
    int CODE_AUTH_EXCEPTION = 0x04;
    /** 取消授权. */
    int CODE_CANCEL_AUTH = 0x05;
    /** 授权失败. */
    int CODE_AUTH_FAILED = 0x06;

    /**
     * 登录回调
     *
     * @param code
     * @param data
     */
    void loginStatus(int code, Object data);
}
