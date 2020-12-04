package com.example.mydemok.constant;

import android.content.Context;

import com.google.gson.GsonBuilder;

import io.reactivex.annotations.NonNull;
import me.jessyan.art.di.module.AppModule;

public class MyGsonModuleConfiguration implements AppModule.GsonConfiguration {

    @Override
    public void configGson(@NonNull Context context, @NonNull GsonBuilder builder) {
        builder
                .serializeNulls()//支持序列化值为 null 的参数
                .enableComplexMapKeySerialization()
//                            .registerTypeAdapter(Integer.class, new IntegerDefaultAdapter())
//                            .registerTypeAdapter(int.class, new IntegerDefaultAdapter())
//                            .registerTypeAdapter(Double.class, new DoubleDefaultAdapter())
//                            .registerTypeAdapter(double.class, new DoubleDefaultAdapter())
//                            .registerTypeAdapter(Long.class, new LongDefault0Adapter())
//                            .registerTypeAdapter(long.class, new LongDefault0Adapter())
//                            .registerTypeAdapter(String.class, new StringNullAdapter())
                .create();
    }
}