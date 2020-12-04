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
package com.example.mydemok.mvp.repository;


import com.example.mydemok.constant.ServiceApi;
import com.example.mydemok.model.main.RewardConfigInfo;
import com.example.mydemok.model.main.SplashAD;
import com.example.mydemok.model.main.ResponseBase;

import io.reactivex.Observable;
import me.jessyan.art.mvp.IModel;
import me.jessyan.art.mvp.IRepositoryManager;


/**
 * ================================================
 * 必须实现 IModel
 * 可以根据不同的业务逻辑划分多个 Repository 类,多个业务逻辑相近的页面可以使用同一个 Repository 类
 * 无需每个页面都创建一个独立的 Repository
 * 通过 {@link IRepositoryManager#createRepository(Class)} 获得的 Repository 实例,为单例对象
 * <p>
 * Created by JessYan on 9/4/16 10:56
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */

public class CommonRepository implements IModel {

    private IRepositoryManager mManager;
    public static final int USERS_PAGE_SIZE = 20;

    /**
     * 必须含有一个接收IRepositoryManager接口的构造函数,否则会报错
     *
     * @param manager
     */
    public CommonRepository(IRepositoryManager manager) {
        this.mManager = manager;
    }

    public Observable<SplashAD> getSplashAD() {
        return mManager
                .createRetrofitService(ServiceApi.class)
                .getSplashAD();

    }

    public Observable<ResponseBase<RewardConfigInfo>> getRewardConfig() {
        return mManager
                .createRetrofitService(ServiceApi.class)
                .getRewardConfig();
    }

    @Override
    public void onDestroy() {

    }
}