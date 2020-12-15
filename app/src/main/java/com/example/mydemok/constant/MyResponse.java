package com.example.mydemok.constant;


import android.content.Intent;

import com.alibaba.fastjson.JSON;
import com.example.mydemok.manager.LoginManager;
import com.example.mydemok.mvp.ui.dialog.TipsDialog;
import com.example.mydemok.utils.GsonUtil;

import org.json.JSONException;
import org.json.JSONObject;

import me.jessyan.art.base.BaseApplication;
import me.jessyan.art.integration.AppManager;
import me.jessyan.art.integration.EventBusManager;
import me.jessyan.art.utils.ArtUtils;
import me.jessyan.art.utils.MsgException;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

public abstract class MyResponse<T> extends ErrorHandleSubscriber<T> {
    public MyResponse(RxErrorHandler rxErrorHandler) {
        super(rxErrorHandler);
    }

    @Override
    public void onNext(T t) {
        try {
            String s = GsonUtil.toJson(t);
            JSONObject obj = null;
            obj = new JSONObject(s);
            AppManager appManager = AppManager.getAppManager();
            if (obj.optInt("error_code") == 0 ) {
                try{
                    onSuccess(t);
                }catch (Exception e){
                    onError(e);
                }
            }

            if (obj.optInt("error_code") == 220){
                LoginManager.getInstance().logout();

//                TipsDialog tipsDialog = new TipsDialog.Builder(appManager.getCurrentActivity()).setContent(obj.optString("error_msg"))
//                        .setConfirmText("取消").setCancleText("登录").build();
//                tipsDialog.setOnDialogClick(new TipsDialog.OnDialogClickListener() {
//                    @Override
//                    public void onSure() {
//                        LoginManager.getInstance().logout();
//                        EventBusManager.getInstance().post(EventBusTags.EVENT_LOGOUT,EventBusTags.EVENT_LOGOUT);
//                    }
//                    @Override
//                    public void onCancle() {
//                        LoginManager.getInstance().logout();
//                        EventBusManager.getInstance().post(EventBusTags.EVENT_LOGOUT,EventBusTags.EVENT_LOGOUT);
//                        appManager.getCurrentActivity().startActivity(new Intent(appManager.getCurrentActivity(), LoginActivity.class));
//                    }
//                });
//                tipsDialog.show();
                return;
            }else if (obj.optInt("error_code")==1001){
                if (LoginManager.getInstance().isLoginValid()) {
                    LoginManager.getInstance().logout();
                    EventBusManager.getInstance().post(EventBusTags.EVENT_LOGOUT,EventBusTags.EVENT_LOGOUT);
                    onError(new MsgException(obj.optString("error_msg")));
                } else {
                    onError(new MsgException(obj.optString("error_msg")));
                }
                return;
            }else if ( obj.optInt("error_code") != 0){
                onError(new MsgException(obj.optString("error_msg"),obj.optInt("error_code")));
                return;
            }
        } catch (JSONException e) {
            ArtUtils.makeText(BaseApplication.getInstance(), "解析数据错误");
            onError(new MsgException("解析数据错误"));
        }

    }
    public abstract void onSuccess(final T response);

    @Override
    public void onError(Throwable t) {
        super.onError(t);
//        if (t instanceof  MsgException){
//            if (!TextUtils.isEmpty(t.getMessage()))
//                ArtUtils.makeText(BaseApplication.getInstance(),t.getMessage());
//        }
    }
}
