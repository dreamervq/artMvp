package com.example.mydemok.manager;


import cn.markmjw.platform.ShareModel;


public interface IShareContentProvider {

    ShareModel getWeiboShareModel();

    ShareModel getWeChatShareModel();

    ShareModel getQQShareModel();

    ShareModel getQzoneShareModel();

    String copy();

    ShareModel generatePoster();

}
