package com.example.mydemok.constant;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;


import com.example.mydemok.utils.AppUtil;

import me.jessyan.art.di.module.ClientModule;
import me.jessyan.art.http.SSLSocketClient;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkhttpConfiguration implements ClientModule.OkhttpConfiguration {
    @Override
    public void configOkhttp(@NonNull Context context, @NonNull OkHttpClient.Builder builder) {
        builder.sslSocketFactory(SSLSocketClient.getSSLSocketFactory());
        builder.hostnameVerifier(SSLSocketClient.getHostnameVerifier());
        builder.addInterceptor(chain -> {
            Request request;
            Request.Builder requestBuilder = chain.request().newBuilder();
            String deviceId = AppUtil.getReqDeviceId();
            if (!TextUtils.isEmpty(deviceId)) {
                requestBuilder.addHeader("devID", deviceId);
            }
            request = requestBuilder.build();
            Response response = chain.proceed(request);
            return response;
        });
    }
}
