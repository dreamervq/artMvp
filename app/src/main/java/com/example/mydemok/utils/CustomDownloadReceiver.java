package com.example.mydemok.utils;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

public class CustomDownloadReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (TextUtils.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE, intent.getAction())) {
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (DownloadApkUtil.getInstance(context).getDownloadId() == id) {
                DownloadApkUtil.getInstance(context).installApk(context, DownloadApkUtil.getInstance(context).getApkPath(context));
            }
        }

    }

}