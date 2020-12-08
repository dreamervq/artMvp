package com.example.mydemok.utils;

import android.app.ActivityManager;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;


import androidx.core.content.FileProvider;

import com.example.mydemok.R;

import java.io.File;
import java.util.List;


public class DownloadApkUtil {
    private int versionCode;
    private long downloadId;

    private DownloadApkUtil(Context context) {
        versionCode = AppUtil.getVersionCode(context);
    }

    private static DownloadApkUtil downloadApkUtil;

    public static DownloadApkUtil getInstance(Context context) {
        if (downloadApkUtil == null) {
            downloadApkUtil = new DownloadApkUtil(context);
        }
        return downloadApkUtil;
    }

    public String getApkPath(Context context) {
        return context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + File.separator + "update.apk";
    }

    // String getDownloadPath(Context context) {
    //return getApkPath(context) + File.separator + getApplicationName(context) + "_" + newVersionName + ".apk";
    // }
    public void downloadNewApkBySystem(final Context context, String url, String versionName) {

        //获取DownloadManager
        DownloadManager systemDownloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        if (systemDownloadManager == null) {
            return;
        }

        //创建下载任务
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        //移动网络情况下是否允许漫游
        request.setAllowedOverRoaming(false);
        //在通知栏中显示，默认就是显示的
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        String fileName = String.format("%sV%s.apk", context.getString(R.string.app_name), versionName);
        request.setTitle(fileName);
        request.setDescription(String.format("%s下载中...", fileName));
        request.setMimeType("application/vnd.android.package-archive");
        request.setVisibleInDownloadsUi(true);

        //设置下载的路径
        File saveFile = new File(getApkPath(context));
        if (saveFile.exists()) {
            saveFile.delete();
        }
        request.setDestinationUri(Uri.fromFile(saveFile));
        if (downloadId != 0) {//清除上一个下载任务
            try {
                systemDownloadManager.remove(downloadId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        downloadId = systemDownloadManager.enqueue(request);//将下载请求加入下载队列，加入下载队列后会给该任务返回一个long型的id，通过该id可以取消任务，重启任务、获取下载的文件等等
    }

    long getDownloadId() {
        return downloadId;
    }


    public void installApk(Context context, String path) {
        File apkFile = new File(path);
        if (apkFile.exists()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            //判断是否是AndroidN以及更高的版本
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                Uri contentUri = FileProvider.getUriForFile(context, getAppProcessName(context) + ".provider", apkFile);
                intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
            } else {
                intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
            }
            context.startActivity(intent);
        }
        downloadId = 0;
    }

    public static String getAppProcessName(Context context) {
        //当前应用pid
        int pid = android.os.Process.myPid();
        //任务管理类
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //遍历所有应用
        String packageName = "";
        if (manager != null) {
            List<ActivityManager.RunningAppProcessInfo> infos = manager.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo info : infos) {
                if (info != null && info.pid == pid) {//得到当前应用
                    packageName = info.processName;//返回包名
                    break;
                }
            }
        }
        return packageName;
    }

//    public boolean clear(Context context) {
//        File file = new File(getApkPath(context));
//        boolean flag;
//        if (file.exists()) {
//            if (file.isDirectory()) {
//                flag = deleteFilesInDir(file);
//            } else if (file.isFile()) {
//                flag = file.delete();
//            } else {
//                return false;
//            }
//        } else {
//            return false;
//        }
//        return flag;
//    }

    private boolean deleteFilesInDir(final File dir) {
        if (dir == null) return false;
        // 目录不存在返回true
        if (!dir.exists()) return true;
        // 不是目录返回false
        if (!dir.isDirectory()) return false;
        // 现在文件存在且是文件夹
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (file.isFile()) {
                    if (!file.delete()) return false;
                } else if (file.isDirectory()) {
                    if (!deleteDir(file)) return false;
                }
            }
        }
        return true;
    }

    private boolean deleteDir(final File dir) {
        if (dir == null) return false;
        // 目录不存在返回true
        if (!dir.exists()) return true;
        // 不是目录返回false
        if (!dir.isDirectory()) return false;
        // 现在文件存在且是文件夹
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (file.isFile()) {
                    if (!file.delete()) return false;
                } else if (file.isDirectory()) {
                    if (!deleteDir(file)) return false;
                }
            }
        }
        return dir.delete();
    }


    public interface ApkFileClearListener {
        void deleted(boolean success);
    }


    /**
     * 获取应用名称
     */
    private String getApplicationName(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            ApplicationInfo info = manager.getApplicationInfo(context.getPackageName(), 0);
            return (String) manager.getApplicationLabel(info);
        } catch (Exception e) {
            e.printStackTrace();
            return "" + System.currentTimeMillis();
        }
    }
}
