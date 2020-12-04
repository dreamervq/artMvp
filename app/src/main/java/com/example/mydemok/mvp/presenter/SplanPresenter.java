package com.example.mydemok.mvp.presenter;


import com.example.mydemok.application.Constants;
import com.example.mydemok.constant.MyResponse;
import com.example.mydemok.model.main.ResponseBase;
import com.example.mydemok.model.main.RewardConfigInfo;
import com.example.mydemok.model.main.SplashAD;
import com.example.mydemok.mvp.repository.CommonRepository;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.art.di.component.AppComponent;
import me.jessyan.art.mvp.BasePresenter;
import me.jessyan.art.mvp.IView;
import me.jessyan.art.mvp.Message;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

public class SplanPresenter extends BasePresenter<CommonRepository> {
    public SplanPresenter(AppComponent appComponent) {
        super(appComponent.repositoryManager().createRepository(CommonRepository.class));
        this.mErrorHandler = appComponent.rxErrorHandler();

    }

    private RxErrorHandler mErrorHandler;

    public void getSplashAD(Message msg) {
        IView view = msg.getTarget();
        mModel.getSplashAD()
                .subscribeOn(Schedulers.io())
//                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .observeOn(AndroidSchedulers.mainThread())
                .as(view.bindAutoDispose())
                .subscribe(new ErrorHandleSubscriber<SplashAD>(mErrorHandler) {
                    @Override
                    public void onNext(SplashAD splashAD) {
                        msg.what = Constants.KEY_SUCCESS;
                        if (splashAD != null) {
                            msg.obj = splashAD;
                            msg.handleMessageToTarget();
                        } else {
                            msg.recycle();
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        msg.what = Constants.KEY_FAILED;
                        msg.handleMessageToTarget();
                    }
                });
    }

    public void getRewardConfig(Message msg) {
        IView view = msg.getTarget();
        mModel.getRewardConfig()
                .subscribeOn(Schedulers.io())
//                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .observeOn(AndroidSchedulers.mainThread())
                .as(view.bindAutoDispose())
                .subscribe(new MyResponse<ResponseBase<RewardConfigInfo>>(mErrorHandler) {

                    @Override
                    public void onSuccess(ResponseBase<RewardConfigInfo> response) {
                        msg.what = Constants.KEY_SUCCESS;
                        msg.obj = response;
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        msg.what = Constants.KEY_FAILED;
                        msg.handleMessageToTarget();
                    }
                });
    }
}
