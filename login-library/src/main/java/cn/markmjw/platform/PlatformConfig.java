package cn.markmjw.platform;

/**
 * PlatformConfig.
 *
 * @author markmjw
 * @date 2015-08-15
 */
public class PlatformConfig {
    private static PlatformConfig mInstance;

    private String mWeiboKey;
    private String mWeiboSecret;
    private String mWeiboCallback;
    private String mWeiboScope;

    private String mWechatId;
    private String mWechatSecret;
    private String mWechatScope;
    private String mWechatState;

    private String mQQId;
    private String mQQSecret;

    private PlatformConfig() {
    }

    public static PlatformConfig getInstance() {
        if (mInstance == null) {
            mInstance = new PlatformConfig();
        }
        return mInstance;
    }

    /**
     * init weibo config
     *
     * @param key
     * @param secret
     */
    public PlatformConfig initWeibo(String key, String secret, String callback, String scope) {
        mWeiboKey = key;
        mWeiboSecret = secret;
        mWeiboCallback = callback;
        mWeiboScope = scope;
        return this;
    }

    /**
     * init wechat config
     *
     * @param key
     * @param secret
     * @param scope
     * @param state
     */
    public PlatformConfig initWechat(String key, String secret, String scope, String state) {
        mWechatId = key;
        mWechatSecret = secret;
        mWechatScope = scope;
        mWechatState = state;
        return this;
    }

    /**
     * init QQ config
     *
     * @param key
     */
    public PlatformConfig initQQ(String key, String secret) {
        mQQId = key;
        mQQSecret = secret;
        return this;
    }

    public String getWechatId() {
        return mWechatId;
    }

    public String getWechatSecret() {
        return mWechatSecret;
    }

    public String getWeiboKey() {
        return mWeiboKey;
    }

    public String getWeiboSecret() {
        return mWeiboSecret;
    }

    public String getWeiboCallback() {
        return mWeiboCallback;
    }

    public String getWeiboScope() {
        return mWeiboScope;
    }

    public String getQQId() {
        return mQQId;
    }

    public String getQQSecret() {
        return mQQSecret;
    }

    public String getWechatScope() {
        return mWechatScope;
    }

    public String getWechatState() {
        return mWechatState;
    }

    public void setWeiboKey(String mWeiboKey) {
        this.mWeiboKey = mWeiboKey;
    }

    public void setWeiboSecret(String mWeiboSecret) {
        this.mWeiboSecret = mWeiboSecret;
    }

    public void setWeiboCallback(String mWeiboCallback) {
        this.mWeiboCallback = mWeiboCallback;
    }

    public void setWeiboScope(String mWeiboScope) {
        this.mWeiboScope = mWeiboScope;
    }

    public void setWechatId(String mWechatId) {
        this.mWechatId = mWechatId;
    }

    public void setWechatSecret(String mWechatSecret) {
        this.mWechatSecret = mWechatSecret;
    }

    public void setWechatScope(String mWechatScope) {
        this.mWechatScope = mWechatScope;
    }

    public void setWechatState(String mWechatState) {
        this.mWechatState = mWechatState;
    }

    public void setQQId(String mQQId) {
        this.mQQId = mQQId;
    }

    public void setQQSecret(String mQQSecret) {
        this.mQQSecret = mQQSecret;
    }
}
