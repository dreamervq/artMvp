/*
 * Copyright 2017 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.jessyan.art.di.module;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import me.jessyan.art.base.BaseApplication;
import me.jessyan.art.http.GlobalHttpHandler;
import me.jessyan.art.http.log.RequestInterceptor;
import me.jessyan.art.http.persistentcookiejar.PersistentCookieJar;
import me.jessyan.art.http.persistentcookiejar.cache.SetCookieCache;
import me.jessyan.art.http.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import me.jessyan.art.utils.NetworkUtil;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.listener.ResponseErrorListener;
import okhttp3.CacheControl;
import okhttp3.Dispatcher;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * ================================================
 * 提供一些三方库客户端实例的 {@link Module}
 * <p>
 * Created by JessYan on 2016/3/14.
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
@Module
public abstract class ClientModule {
    private static final int TIME_OUT = 10;

    /**
     * 提供 {@link Retrofit}
     *
     * @param application   {@link Application}
     * @param configuration {@link RetrofitConfiguration}
     * @param builder       {@link Retrofit.Builder}
     * @param client        {@link OkHttpClient}
     * @param httpUrl       {@link HttpUrl}
     * @param gson          {@link Gson}
     * @return {@link Retrofit}
     */
    @Singleton
    @Provides
    static Retrofit provideRetrofit(Application application, @Nullable RetrofitConfiguration configuration, Retrofit.Builder builder, OkHttpClient client
            , HttpUrl httpUrl, Gson gson) {
        builder
                .baseUrl(httpUrl)//域名
                .client(client);//设置 OkHttp

        if (configuration != null)
            configuration.configRetrofit(application, builder);

        builder
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//使用 RxJava
                .addConverterFactory(GsonConverterFactory.create(gson));//使用 Gson
        return builder.build();
    }


    /**
     * 提供 {@link OkHttpClient}
     *
     * @param application     {@link Application}
     * @param configuration   {@link OkhttpConfiguration}
     * @param builder         {@link OkHttpClient.Builder}
     * @param intercept       {@link Interceptor}
     * @param interceptors    {@link List<Interceptor>}
     * @param handler         {@link GlobalHttpHandler}
     * @param executorService {@link ExecutorService}
     * @return {@link OkHttpClient}
     */
    @Singleton
    @Provides
    static OkHttpClient provideClient(Application application, @Nullable OkhttpConfiguration configuration, OkHttpClient.Builder builder, Interceptor intercept
            , @Nullable List<Interceptor> interceptors, @Nullable GlobalHttpHandler handler, ExecutorService executorService) {
        builder
                .cookieJar( new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(BaseApplication.getInstance())))
                .connectTimeout(TIME_OUT*3, TimeUnit.SECONDS)
                .readTimeout(TIME_OUT*3, TimeUnit.SECONDS)
                .addNetworkInterceptor(intercept);

        Interceptor cacheInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                //有网的时候,读接口上的@Headers里的注解配置
                String cacheControl = request.cacheControl().toString();
                //没有网络并且添加了注解,才使用缓存.
                if (!NetworkUtil.isConnected()&&!TextUtils.isEmpty(cacheControl)){
                    //重置请求体;
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_CACHE)
                            .build();
                }

                //如果没有添加注解,则不缓存
                if (TextUtils.isEmpty(cacheControl) || "no-store" .contains(cacheControl)) {
                    //响应头设置成无缓存
                    cacheControl = "no-store";
                } else if (NetworkUtil.isConnected()) {
                    //如果有网络,则将缓存的过期事件,设置为0,获取最新数据
                    cacheControl = "public, max-age=" + 0;
                }else {
                    //...如果无网络,则根据@headers注解的设置进行缓存.
                }
                Response response = chain.proceed(request);
                return response.newBuilder()
                        .header("Cache-Control", cacheControl)
                        .removeHeader("Pragma")
                        .build();
            }
        };

        if (handler != null)
            builder.addNetworkInterceptor(cacheInterceptor);
        builder.addInterceptor(cacheInterceptor);
            builder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    return chain.proceed(handler.onHttpRequestBefore(chain, chain.request()));
                }
            });

        //如果外部提供了 Interceptor 的集合则遍历添加
        if (interceptors != null) {
            for (Interceptor interceptor : interceptors) {
                builder.addInterceptor(interceptor);
            }
        }

        //为 OkHttp 设置默认的线程池
        builder.dispatcher(new Dispatcher(executorService));

        if (configuration != null)
            configuration.configOkhttp(application, builder);
        return builder.build();
    }

    @Singleton
    @Provides
    static Retrofit.Builder provideRetrofitBuilder() {
        return new Retrofit.Builder();
    }

    @Singleton
    @Provides
    static OkHttpClient.Builder provideClientBuilder() {
        return new OkHttpClient.Builder();
    }

    @Binds
    abstract Interceptor bindInterceptor(RequestInterceptor interceptor);




    /**
     * 提供处理 RxJava 错误的管理器
     *
     * @param application {@link Application}
     * @param listener    {@link ResponseErrorListener}
     * @return {@link RxErrorHandler}
     */
    @Singleton
    @Provides
    static RxErrorHandler proRxErrorHandler(Application application, ResponseErrorListener listener) {
        return RxErrorHandler
                .builder()
                .with(application)
                .responseErrorListener(listener)
                .build();
    }

    /**
     * {@link Retrofit} 自定义配置接口
     */
    public interface RetrofitConfiguration {
        void configRetrofit(@NonNull Context context, @NonNull Retrofit.Builder builder);
    }

    /**
     * {@link OkHttpClient} 自定义配置接口
     */
    public interface OkhttpConfiguration {
        void configOkhttp(@NonNull Context context, @NonNull OkHttpClient.Builder builder);
    }


}
