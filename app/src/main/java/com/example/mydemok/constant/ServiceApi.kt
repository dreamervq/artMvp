package com.example.mydemok.constant

import com.example.mydemok.mvp.model.*
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * Created by magic-lee on 15/4/7.
 */
interface ServiceApi {

    /**
     * 广告
     */
    @POST("startup/get")
    fun splashAD(): Observable<SplashAD?>

    /**
     * 打赏配置
     */
    @POST("reward/config")
    fun rewardConfig(): Observable<ResponseBase<RewardConfigInfo?>>

    /**
     * 获取app协议
     */
    @POST("user/agreementSetting")
    fun appAgreement(): Observable<AppAgreement?>

    /**
     * 广告点击
     */
    @FormUrlEncoded
    @POST("adv/advViewAdd")
    fun addAdvClick(@Field("id") id: Long): Observable<ResponseBase<*>>

    @POST("version/get")
    fun checkVersion(): Observable<ResponseBase<Version?>>
}