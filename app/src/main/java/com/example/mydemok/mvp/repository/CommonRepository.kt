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
package com.example.mydemok.mvp.repository

import com.example.mydemok.constant.ServiceApi
import com.example.mydemok.mvp.model.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.jessyan.art.mvp.IModel
import me.jessyan.art.mvp.IRepositoryManager

/**
 * ================================================
 * 必须实现 IModel
 * 可以根据不同的业务逻辑划分多个 Repository 类,多个业务逻辑相近的页面可以使用同一个 Repository 类
 * 无需每个页面都创建一个独立的 Repository
 * 通过 [IRepositoryManager.createRepository] 获得的 Repository 实例,为单例对象
 *
 *
 * Created by JessYan on 9/4/16 10:56
 * [Contact me](mailto:jess.yan.effort@gmail.com)
 * [Follow me](https://github.com/JessYanCoding)
 * ================================================
 */
class CommonRepository(private val mManager: IRepositoryManager) : IModel {
    companion object {
        const val USERS_PAGE_SIZE = 20
    }

    fun splashAD(): Observable<SplashAD?> {
        return return addSchedulers(serviceApi().splashAD())
    }

    fun rewardConfig(): Observable<ResponseBase<RewardConfigInfo?>> {
        return addSchedulers(serviceApi().rewardConfig())
    }

    fun appAgreement(): Observable<AppAgreement?> {
        return addSchedulers(serviceApi().appAgreement())
    }

    fun addAdvClick(id: Long): Observable<ResponseBase<*>> {
        return addSchedulers(serviceApi().addAdvClick(id))
    }

    fun checkVersion(): Observable<ResponseBase<Version?>> {
        return addSchedulers(serviceApi().checkVersion())
    }


    private fun serviceApi(): ServiceApi {
        return mManager
            .createRetrofitService(ServiceApi::class.java)
    }

    private fun <K> addSchedulers(observable: Observable<K>): Observable<K> {
        return observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    override fun onDestroy() {}


}