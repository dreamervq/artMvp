package cn.markmjw.platform.util;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * HttpUtil.
 *
 * @author markmjw
 * @date 2015-08-19
 */
public class HttpUtil {
    private static final OkHttpClient mOkHttpClient = new OkHttpClient();

    static {
        mOkHttpClient.setConnectTimeout(15, TimeUnit.SECONDS);
        mOkHttpClient.setReadTimeout(20, TimeUnit.SECONDS);
    }

    /**
     * Request async.
     *
     * @param request
     * @param callback
     */
    public static void enqueue(Request request, Callback callback) {
        mOkHttpClient.newCall(request).enqueue(callback);
    }

    /**
     * encode param with UTF-8
     *
     * @param params
     * @return
     */
    public static String encodeParams(Map<String, String> params) {
        StringBuilder encodedParams = new StringBuilder();
        String paramsEncoding = "UTF-8";
        try {
            Set<Map.Entry<String, String>> entrySet = params.entrySet();
            int size = entrySet.size();
            int index = 0;
            for (Map.Entry<String, String> entry : entrySet) {
                encodedParams.append(URLEncoder.encode(entry.getKey(), paramsEncoding));
                encodedParams.append('=');
                encodedParams.append(URLEncoder.encode(entry.getValue(), paramsEncoding));
                if (++index < size) {
                    encodedParams.append('&');
                }
            }
            return encodedParams.toString();
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Encoding not supported: " + paramsEncoding, uee);
        }
    }

    /**
     * Build url with params, which can auto encode params.
     *
     * @param url
     * @param params
     * @return
     */
    public static String buildUrl(String url, Map<String, String> params) {
        String urlStr = url;
        if (!urlStr.contains("?")) {
            urlStr += '?';
        }
        urlStr += encodeParams(params);
        return urlStr;
    }
}
