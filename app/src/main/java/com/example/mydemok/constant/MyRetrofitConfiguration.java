package com.example.mydemok.constant;

import android.content.Context;


import com.example.mydemok.utils.FastJsonConverterFactory;

import io.reactivex.annotations.NonNull;
import me.jessyan.art.di.module.ClientModule;
import retrofit2.Retrofit;

public class MyRetrofitConfiguration implements ClientModule.RetrofitConfiguration {
    @Override
    public void configRetrofit(@NonNull Context context, @NonNull Retrofit.Builder builder) {
        builder.addConverterFactory(new FastJsonConverterFactory());//比如使用 FastJson 替代 Gson
    }
}
