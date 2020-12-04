package me.jessyan.art.ui.util.glide;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;

import com.bumptech.glide.load.model.GlideUrl;

public class GlideDynamicUrl extends GlideUrl {
    private String mDynamicUrl;

    public GlideDynamicUrl(String url) {
        super(url);
        mDynamicUrl = url;
    }

    @Override
    public String getCacheKey() {
        return (mDynamicUrl != null && Patterns.WEB_URL.matcher(mDynamicUrl).matches()) ? getDynamicUrlKey(mDynamicUrl) : super.getCacheKey();
    }

    private String getDynamicUrlKey(String url) {
        String result;
        Uri uri = Uri.parse(url);
        if (!TextUtils.isEmpty(url)) {
            String info = uri.getHost() != null ? uri.getHost() : "";
            String path = uri.getPath() != null ? uri.getPath() : "";
            String scheme = uri.getScheme() != null ? uri.getScheme() : "";
            result = (TextUtils.isEmpty(info) || TextUtils.isEmpty(path)) ? url : (scheme + info + path);
        } else {
            result = url;
        }
        Log.i("GlideDynamicUrl", String.format("url：%s\ncacheKey：%s", url, result));
        return uri.getScheme() + result;
    }
}
