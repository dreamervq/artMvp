package com.example.mydemok.constant;

import android.content.Context;

import java.util.concurrent.TimeUnit;

import io.reactivex.annotations.NonNull;
import me.jessyan.art.di.module.ClientModule;
import okhttp3.OkHttpClient;

public class MyOkhttpConfiguration implements ClientModule.OkhttpConfiguration {
    @Override
    public void configOkhttp(@NonNull Context context, @NonNull OkHttpClient.Builder builder) {
        builder.writeTimeout(10, TimeUnit.SECONDS);
    }
}
