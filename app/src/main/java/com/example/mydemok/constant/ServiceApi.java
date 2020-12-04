package com.example.mydemok.constant;


import com.example.mydemok.model.main.ResponseBase;
import com.example.mydemok.model.main.RewardConfigInfo;
import com.example.mydemok.model.main.SplashAD;

import io.reactivex.Observable;
import retrofit2.http.POST;

/**
 * Created by magic-lee on 15/4/7.
 */
public interface ServiceApi {


    @POST("startup/get")
    Observable<SplashAD> getSplashAD();

    /**
     * 打赏配置
     */
    @POST("reward/config")
    Observable<ResponseBase<RewardConfigInfo>> getRewardConfig();
}



