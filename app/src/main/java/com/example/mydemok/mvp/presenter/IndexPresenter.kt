package com.example.mydemok.mvp.presenter

import com.example.mydemok.application.Constants
import com.example.mydemok.constant.MyResponse
import com.example.mydemok.mvp.model.ResponseBase
import com.example.mydemok.mvp.model.Version
import com.example.mydemok.mvp.repository.CommonRepository
import me.jessyan.art.di.component.AppComponent
import me.jessyan.art.mvp.BasePresenter
import me.jessyan.art.mvp.Message
import me.jessyan.rxerrorhandler.core.RxErrorHandler

class IndexPresenter(appComponent: AppComponent) : BasePresenter<CommonRepository>(
    appComponent.repositoryManager().createRepository(CommonRepository::class.java)
) {
    private val mErrorHandler: RxErrorHandler = appComponent.rxErrorHandler()

    fun checkVersion(msg: Message) {
        val view = msg.target
        mModel.checkVersion()
            .`as`(view.bindAutoDispose())//                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
            .subscribe(object : MyResponse<ResponseBase<Version?>>(mErrorHandler) {
                override fun onSuccess(response: ResponseBase<Version?>) {
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


}