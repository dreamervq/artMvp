package com.example.mydemok.mvp.presenter

import com.example.mydemok.application.Constants
import com.example.mydemok.constant.MyResponse
import com.example.mydemok.mvp.model.AppAgreement
import com.example.mydemok.mvp.model.ResponseBase
import com.example.mydemok.mvp.model.RewardConfigInfo
import com.example.mydemok.mvp.model.SplashAD
import com.example.mydemok.mvp.repository.CommonRepository
import me.jessyan.art.di.component.AppComponent
import me.jessyan.art.mvp.BasePresenter
import me.jessyan.art.mvp.Message
import me.jessyan.rxerrorhandler.core.RxErrorHandler
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber

class SplanPresenter(appComponent: AppComponent) : BasePresenter<CommonRepository>(
    appComponent.repositoryManager().createRepository(CommonRepository::class.java)
) {
    private val mErrorHandler: RxErrorHandler = appComponent.rxErrorHandler()


    fun getSplashAD(msg: Message) {
        val view = msg.target
        mModel.splashAD()
            .`as`(view.bindAutoDispose())
            .subscribe(object : ErrorHandleSubscriber<SplashAD?>(mErrorHandler) {
                override fun onNext(splashAD: SplashAD) {
                    msg.what = Constants.KEY_SUCCESS
                    msg.obj = splashAD
                    msg.handleMessageToTarget()
                }

                override fun onError(t: Throwable) {
                    super.onError(t)
                    msg.what = Constants.KEY_FAILED
                    msg.handleMessageToTarget()
                }
            })
    }

    fun getAppAgreement(msg: Message) {
        val view = msg.target
        mModel.appAgreement()
            .`as`(view.bindAutoDispose())
            .subscribe(object : ErrorHandleSubscriber<AppAgreement?>(mErrorHandler) {
                override fun onNext(appAgreement: AppAgreement) {
                    msg.what = Constants.KEY_SUCCESS
                    Constants.APP_AGREEMENT = appAgreement
                }

                override fun onError(t: Throwable) {
                    super.onError(t)
                    msg.what = Constants.KEY_FAILED
                }
            })
    }

    fun getRewardConfig(msg: Message) {
        val view = msg.target
        mModel.rewardConfig()
            .`as`(view.bindAutoDispose())//                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
            .subscribe(object : MyResponse<ResponseBase<RewardConfigInfo?>>(mErrorHandler) {
                override fun onSuccess(response: ResponseBase<RewardConfigInfo?>) {
                    msg.what = Constants.KEY_SUCCESS
                    msg.obj = response.data
                    msg.handleMessageToTarget()
                }

                override fun onError(t: Throwable) {
                    super.onError(t)
                    msg.what = Constants.KEY_FAILED
                    msg.handleMessageToTarget()
                }
            })
    }

    fun addAdvClick(msg: Message, id: Long) {
        val view = msg.target
        mModel.addAdvClick(id)
            .`as`(view.bindAutoDispose())//                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
            .subscribe(object : MyResponse<ResponseBase<*>>(mErrorHandler) {
                override fun onSuccess(response: ResponseBase<*>) {
                    msg.what = Constants.KEY_SUCCESS
                    msg.obj = response
                    msg.handleMessageToTarget()
                }

                override fun onError(t: Throwable) {
                    super.onError(t)
                    msg.what = Constants.KEY_FAILED
                    msg.handleMessageToTarget()
                }
            })
    }

}