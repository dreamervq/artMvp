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
package me.jessyan.art.base;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;

import androidx.annotation.NonNull;

import cn.markmjw.platform.PlatformConfig;
import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;
import me.jessyan.art.base.delegate.AppDelegate;
import me.jessyan.art.base.delegate.AppLifecycles;
import me.jessyan.art.di.component.AppComponent;
import me.jessyan.art.utils.ArtUtils;
import me.jessyan.art.utils.Preconditions;
import me.jessyan.art.utils.TinyPref;
import timber.log.Timber;

/**
 * ================================================
 * MVPArt 是一个新的 MVP 架构, 适合中小型项目, 旨在解决传统 MVP 类和接口太多, 并且 Presenter 和 View
 * 通过接口通信过于繁琐, 重用 Presenter 代价太大等问题
 *
 * @see <a href="https://github.com/JessYanCoding/MVPArms/wiki">请配合官方 Wiki 文档学习本框架 (Arms 的文档除了 MVP 部分, 其他的文档内容 Art 和 Arms 都可以共用)</a>
 * @see <a href="https://github.com/JessYanCoding/MVPArms/wiki/Issues">常见 Issues, 踩坑必看!</a>
 * Created by JessYan on 22/03/2016
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public class BaseApplication extends Application implements App {
    private AppLifecycles mAppDelegate;
    private Typeface DINtypeface;
    private static BaseApplication instance;

    public static synchronized BaseApplication getInstance() {
        return instance;
    }


    /**
     * 这里会在 {@link BaseApplication#onCreate} 之前被调用,可以做一些较早的初始化
     * 常用于 MultiDex 以及插件化框架的初始化
     *
     * @param base
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        if (mAppDelegate == null)
            this.mAppDelegate = new AppDelegate(base);
        this.mAppDelegate.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        TinyPref.getInstance().init(this);
        if (mAppDelegate != null)
            this.mAppDelegate.onCreate(this);
    //    DINtypeface = Typeface.createFromAsset(getAssets(), "fonts/DINAlternateBold.ttf");
        PlatformConfig.getInstance().initWechat(Platform.KEY_WECHAT, Platform.SECRET_WECHAT, "snsapi_userinfo", "snsapi_userinfo");
        PlatformConfig.getInstance().initWeibo(Platform.KEY_WEIBO, Platform.SECRET_WEIBO, "https://api.weibo.com/oauth2/default.html", "");
        PlatformConfig.getInstance().initQQ(Platform.KEY_QQ, Platform.SECRET_QQ);
        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable e) {
                Timber.e("----------------------" + e.getMessage());
            }
        });
    }

    /**
     * 在模拟环境中程序终止时会被调用
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
        if (mAppDelegate != null)
            this.mAppDelegate.onTerminate(this);
    }

    /**
     * 将 {@link AppComponent} 返回出去, 供其它地方使用, {@link AppComponent} 接口中声明的方法所返回的实例, 在 {@link #getAppComponent()} 拿到对象后都可以直接使用
     *
     * @return AppComponent
     * @see ArtUtils#obtainAppComponentFromContext(Context) 可直接获取 {@link AppComponent}
     */
    @NonNull
    @Override
    public AppComponent getAppComponent() {
        Preconditions.checkNotNull(mAppDelegate, "%s cannot be null", AppDelegate.class.getName());
        Preconditions.checkState(mAppDelegate instanceof App, "%s must be implements %s", mAppDelegate.getClass().getName(), App.class.getName());
        return ((App) mAppDelegate).getAppComponent();
    }

    public Typeface getDINTypeface() {
        return DINtypeface;
    }

}