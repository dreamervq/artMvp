package com.example.mydemok.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import java.io.File;

/**
 * Created by GongLi on 2017/9/13.
 * Email：lc824767150@163.com
 */

public class OpenDocumentsUtils {

    private final String[][] MIME_MapTable = {
            {".3gp", "video/3gpp"},
            {".apk", "application/vnd.android.package-archive"},
            {".asf", "video/x-ms-asf"},
            {".avi", "video/x-msvideo"},
            {".bin", "application/octet-stream"},
            {".bmp", "image/bmp"},
            {".c", "text/plain"},
            {".class", "application/octet-stream"},
            {".conf", "text/plain"},
            {".cpp", "text/plain"},
            {".doc", "application/msword"},
            {".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            {".xls", "application/vnd.ms-excel"},
            {".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
            {".exe", "application/octet-stream"},
            {".gif", "image/gif"},
            {".gtar", "application/x-gtar"},
            {".gz", "application/x-gzip"},
            {".h", "text/plain"},
            {".htm", "text/html"},
            {".html", "text/html"},
            {".jar", "application/java-archive"},
            {".java", "text/plain"},
            {".jpeg", "image/jpeg"},
            {".jpg", "image/jpeg"},
            {".js", "application/x-javascript"},
            {".log", "text/plain"},
            {".m3u", "audio/x-mpegurl"},
            {".m4a", "audio/mp4a-latm"},
            {".m4b", "audio/mp4a-latm"},
            {".m4p", "audio/mp4a-latm"},
            {".m4u", "video/vnd.mpegurl"},
            {".m4v", "video/x-m4v"},
            {".mov", "video/quicktime"},
            {".mp2", "audio/x-mpeg"},
            {".mp3", "audio/x-mpeg"},
            {".mp4", "video/mp4"},
            {".mpc", "application/vnd.mpohun.certificate"},
            {".mpe", "video/mpeg"},
            {".mpeg", "video/mpeg"},
            {".mpg", "video/mpeg"},
            {".mpg4", "video/mp4"},
            {".mpga", "audio/mpeg"},
            {".msg", "application/vnd.ms-outlook"},
            {".ogg", "audio/ogg"},
            {".pdf", "application/pdf"},
            {".png", "image/png"},
            {".pps", "application/vnd.ms-powerpoint"},
            {".ppt", "application/vnd.ms-powerpoint"},
            {".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
            {".prop", "text/plain"},
            {".rc", "text/plain"},
            {".rmvb", "audio/x-pn-realaudio"},
            {".rtf", "application/rtf"},
            {".sh", "text/plain"},
            {".tar", "application/x-tar"},
            {".tgz", "application/x-compressed"},
            {".txt", "text/plain"},
            {".wav", "audio/x-wav"},
            {".wma", "audio/x-ms-wma"},
            {".wmv", "audio/x-ms-wmv"},
            {".wps", "application/vnd.ms-works"},
            {".xml", "text/plain"},
            {".z", "application/x-compress"},
            {".zip", "application/x-zip-compressed"},
            {"", "*/*"}
    };

    private static OpenDocumentsUtils openDocumentsUtils;

    private OpenDocumentsUtils() {

    }

    public static OpenDocumentsUtils getInstance() {
        if (openDocumentsUtils == null) {
            openDocumentsUtils = new OpenDocumentsUtils();
        }
        return openDocumentsUtils;
    }

    /**
     * 根据路径获取文件名
     *
     * @param pathandname
     * @return
     */
    public String getFileExtension(String pathandname) {
        if (pathandname == null) {
            return "";
        }
        int start = pathandname.lastIndexOf(".");
        if (start != -1) {
            return pathandname.substring(start);
        } else {
            return "";
        }
    }

    /**
     * @param file
     */
    public void openFile(Context context, File file) {
        openFile(context, file, getFileExtension(file.getName()));
    }

    /**
     * @param file
     */
    public void openFile(Context context, File file, String suffix) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        String type = getMimeTypeByExtension(suffix);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context, String.format("%s.provider", AppUtil.getAppProcessName(context)), file);
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(file);
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setDataAndType(uri, type);
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "打开文档失败", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 根据文件后缀名获得对应的MIME类型。
     */
    public String getMimeTypeByFileName(String pathandname) {
        return getMimeTypeByExtension(getFileExtension(pathandname));
    }

    /**
     * 根据文件后缀名获得对应的MIME类型。
     */
    public String getMimeTypeByExtension(String extension) {
        String type = "*/*";
        for (int i = 0; i < MIME_MapTable.length; i++) {
            if (TextUtils.equals(MIME_MapTable[i][0], extension)) {
                type = MIME_MapTable[i][1];
                break;
            }
        }
        return type;
    }

    /**
     * 根据路径获取文件名
     *
     * @param pathandname
     * @return
     */
    public String getFileSuffix(String pathandname) {
        if (pathandname == null) {
            return "";
        }
        int start = pathandname.lastIndexOf(".");
        if (start != -1) {
            return pathandname.substring(start, pathandname.length());
        } else {
            return "";
        }
    }
}
