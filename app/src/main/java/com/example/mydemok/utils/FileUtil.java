package com.example.mydemok.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;

import com.example.mydemok.application.Constants;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

import me.jessyan.art.base.BaseApplication;
import timber.log.Timber;

/**
 * File Util <p> Created by magic-lee on 15/4/7.
 */
public class FileUtil {
    public static final String EXTERNAL_STORAGE = Environment.getExternalStorageDirectory().toString();

    public static String DIR_HOME = EXTERNAL_STORAGE + "/foshan";
    private static String DIR_LOG = DIR_HOME + "/log";
    private static String DIR_IMAGE = DIR_HOME + "/image";
    private static String DIR_DOWNLOAD_APP = DIR_HOME + "/app";
    private static String DIR_WEBVIEW_CACHE = DIR_HOME + "/cache";
    public static String DIR_VIDEO = DIR_HOME + "/fm/video";
    public static String DIR_VOICE = DIR_HOME + "/fm/voice";

    public static final int DIR_TYPE_LOG = 0x01 << 1;
    public static final int DIR_TYPE_COPY_DB = DIR_TYPE_LOG << 1;
    public static final int DIR_TYPE_IMAGE = DIR_TYPE_COPY_DB << 1;
    public static final int DIR_TYPE_APP_DOWNLOAD = DIR_TYPE_IMAGE << 1;
    public static final int DIR_TYPE_WEBVIEW_CACHE = DIR_TYPE_APP_DOWNLOAD << 1;
    public static final int DIR_TYPE_MUSIC_CACHE = DIR_TYPE_WEBVIEW_CACHE << 1;
    public static final int DIR_TYPE_VIDEO_CACHE = DIR_TYPE_MUSIC_CACHE << 1;

    /**
     * default  size of space
     */
    public static final long MIN_SPACE = 10 * 1024 * 1024;

    /**
     * hidden the app images
     */
    private static final String DIR_NO_MEDIA_FILE = DIR_HOME + "/.nomedia";


    /**
     * Get file path by type
     */
    public static String getPathByType(int type) {
        String dir;
        File file;
        switch (type) {
            case DIR_TYPE_LOG:
                dir = DIR_LOG;
                break;
            case DIR_TYPE_COPY_DB:
                File cacheDir = BaseApplication.getInstance().getExternalCacheDir();

                if (cacheDir != null) {
                    dir = cacheDir.getAbsolutePath() + "/db";
                } else {
                    dir = DIR_HOME + "/db";
                }
                break;
            case DIR_TYPE_IMAGE:
                dir = DIR_IMAGE;
                file = new File(dir);
                if (file.exists())
                    break;
                if (!new File(dir).getParentFile().exists())
                    new File(dir).mkdirs();
                break;
            case DIR_TYPE_APP_DOWNLOAD:
                dir = DIR_DOWNLOAD_APP;
                file = new File(dir);
                if (file.exists())
                    break;
                if (!new File(dir).getParentFile().exists())
                    new File(dir).mkdirs();
                break;
            case DIR_TYPE_WEBVIEW_CACHE:
                dir = DIR_WEBVIEW_CACHE;
                file = new File(dir);
                if (file.exists())
                    break;
                if (!new File(dir).getParentFile().exists())
                    new File(dir).mkdirs();
                break;
            case DIR_TYPE_VIDEO_CACHE:
                dir = DIR_VIDEO;
                file = new File(dir);
                if (file.exists())
                    break;
                if (!new File(dir).getParentFile().exists())
                    new File(dir).mkdirs();
                break;
            case DIR_TYPE_MUSIC_CACHE:
                dir = DIR_VOICE;
                file = new File(dir);
                if (file.exists())
                    break;
                if (!new File(dir).getParentFile().exists())
                    new File(dir).mkdirs();
                break;
            default:
                dir = "";
                break;
        }
        dir = dir + "/";

        Timber.e("dir" + dir + "type:" + type);

        try {
            checkFolder(dir);
        } catch (Exception e) {
            // do nothing
        }
        return dir;
    }

    public static boolean isSDCardExist() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * Is enough space size by default {@link FileUtil#MIN_SPACE}
     */
    public static boolean hasEnoughSpace() {
        return hasEnoughSpace(MIN_SPACE);
    }

    /**
     * Is enough space size
     */
    public static boolean hasEnoughSpace(float needSize) {
        if (isSDCardExist()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs sf = new StatFs(path.getPath());
            long blockSize = sf.getBlockSize();
            long availCount = sf.getAvailableBlocks();
            long restSize = availCount * blockSize;
            if (restSize > needSize) {
                return true;
            }
        }
        return false;
    }

    /**
     * create file path
     *
     * @param dirType  file type
     * @param fileName file name
     */
    public static String createPath(int dirType, String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            fileName = "temp";
        }
        String filePath = getPathByType(dirType) + File.separator + createName(fileName);
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }

        return filePath;
    }

    /**
     * create file
     *
     * @param dirType  file type
     * @param fileName file name
     */
    public static File createFile(int dirType, String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            fileName = "temp";
        }

        String filePath = getPathByType(dirType) + File.separator + createName(fileName);
        File file = new File(filePath);
        try {
            if (file.exists() && file.isFile()) {
                file.delete();
            }
            file.createNewFile();
        } catch (IOException e) {
            // ignore
            return null;
        }

        return file;
    }

    /**
     * Delete file or folder by file path.
     */
    public static boolean deleteFile(String filePath) {
        if (!TextUtils.isEmpty(filePath)) {
            File file = new File(filePath);
            if (file.exists()) {
                if (file.isDirectory()) {
                    File files[] = file.listFiles();
                    if (null != files && files.length > 0) {
                        for (File f : files) {
                            deleteFile(f.getAbsolutePath());
                        }
                    }
                }
                return file.delete();
            }
        }
        return false;
    }

    /**
     * Delete file or folder by file.
     */
    public static boolean deleteFile(File deleteFile) {
        if (deleteFile != null) {
            if (!deleteFile.exists()) {
                return true;
            }

            if (deleteFile.isDirectory()) {
                // process folder
                File[] files = deleteFile.listFiles();
                if (null != files && files.length > 0) {
                    for (File file : files) {
                        deleteFile(file.getAbsolutePath());
                    }
                }
            }
            return deleteFile.delete();
        }

        return false;
    }

    /**
     * avoid images into library of image
     */
    public static void hideMediaFile() {
        File file = new File(DIR_NO_MEDIA_FILE);
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
        } catch (IOException e) {
            Timber.e(e);
        }
//        PermissionUtil.checkPermission(new PermissionListener() {
//            @Override
//            public void onPermissionGranted(PermissionGrantedResponse response) {
//
//            }
//
//            @Override
//            public void onPermissionDenied(PermissionDeniedResponse response) {
//            }
//
//            @Override
//            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
//                token.continuePermissionRequest();
//            }
//        }, "android.permission.WRITE_EXTERNAL_STORAGE");

    }

    /**
     * 文件夹检查，不存在则新建
     *
     * @param folderPath 文件夹检查，不存在则新建
     * @return true，存在或新建成功，false，不存在或新建失败
     */
    public static boolean checkFolder(String folderPath) {
        if (folderPath == null) {
            return false;
        }
        return checkFolder(new File(folderPath));
    }

    /**
     * 文件夹检查，不存在则新建
     *
     * @param folder 文件夹检查，不存在则新建
     * @return true，存在或新建成功，false，不存在或新建失败
     */
    public static boolean checkFolder(File folder) {
        if (folder == null) {
            return false;
        }

        if (folder.isDirectory()) {
            return true;
        }

        return folder.mkdirs();
    }

    static void closeSilently(Closeable c) {
        if (c == null) {
            return;
        }
        try {
            c.close();
        } catch (Throwable t) {
            // do nothing
        }
    }


    public static byte[] getStreamData(InputStream in) throws IOException {
        ByteArrayOutputStream out = null;
        try {
            out = new ByteArrayOutputStream();
            byte[] buffer = new byte[8 * 1024];
            int len = -1;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.flush();
            return out.toByteArray();
        } finally {
            closeSilently(out);
        }
    }


//    public static String saveImage(Bitmap bitmap, String url) {
//        String path = getPathByType(DIR_TYPE_IMAGE) + StringUtil.md5(url) + ".jpg";
//        if (new File(path).exists()){
//            return path;
//        }
//        String s = ImageUtils.saveBitmap2file(bitmap, path, 58, Bitmap.CompressFormat.JPEG, StringUtil.md5(url) + ".jpg");
//        return s;
//    }

    public static String createName(String name) {
        if (TextUtils.isEmpty(name)) {
            return name;
        }
        String fileName = name.replace('/', '_').replace(':', '_').replace("?", "_");
        return fileName.length() > 58 ? fileName.substring(0, 58) : fileName;
    }

    public static String getCacheSize() {
        long size = 0;
        size += getCacheSize(new File(getPathByType(DIR_TYPE_APP_DOWNLOAD)));
        size += getCacheSize(new File(getPathByType(DIR_TYPE_LOG)));
        size += getCacheSize(new File(getPathByType(DIR_TYPE_WEBVIEW_CACHE)));
        size += getCacheSize(new File(getPathByType(DIR_TYPE_IMAGE)));

        size += getCacheSize(BaseApplication.getInstance().getCacheDir());
        return formetFileSize(size);
    }

    public static long getCacheSize(File file) {
        if (file == null) {
            return 0;
        }
        long size = 0;
        File[] fileList;
        if (file.isDirectory()) {
            fileList = file.listFiles();
        } else {
            fileList = new File[1];
            fileList[0] = file;
        }
        if (fileList == null) {
            return 0;
        }
        for (File childFile : fileList) {
            if (childFile.isDirectory()) {
                size = size + getCacheSize(childFile);
            } else {
                size = size + file.length();
            }
        }

        return size;
    }

    public static void deleteCache() {
        deleteCache(new File(getPathByType(DIR_TYPE_APP_DOWNLOAD)));
        deleteCache(new File(getPathByType(DIR_TYPE_LOG)));
        deleteCache(new File(getPathByType(DIR_TYPE_WEBVIEW_CACHE)));
        deleteCache(BaseApplication.getInstance().getCacheDir());
        deleteCache(new File(getPathByType(DIR_TYPE_IMAGE)));
    }

    public static void deleteCache(File file) {
        if (file == null) {
            return;
        }
        File[] fileList;
        if (file.isDirectory()) {
            fileList = file.listFiles();
        } else {
            fileList = new File[1];
            fileList[0] = file;
        }
        if (fileList == null) {
            return;
        }
        for (File childFile : fileList) {
            if (childFile.isDirectory()) {
                deleteCache(childFile);
            } else {
                childFile.delete();
            }
        }
    }

    public static String formetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("0.00");
        String fileSizeString = "0.0MB";
        if (fileS >= 1024 && fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    private final static byte[] gSyncCode = new byte[0];

    public static boolean fileRename(String fromName, String toName) {
        synchronized (gSyncCode) {
            // TODO: 根据文件名判断是否属于同一挂载点
            File fromFile = new File(fromName);
            File toFile = new File(toName);
            if (!fromFile.exists()) {
                return false;
            }
            boolean result = fromFile.renameTo(toFile);
            if (result) {
            }
            return result;
        }

    }

    public static String saveBitmap(Context context, Bitmap mBitmap) {
        File filePic;
        try {
            filePic = new File(Constants.PATH_IMG + System.currentTimeMillis() + ".jpg");
            if (!filePic.exists()) {
                filePic.getParentFile().mkdirs();
                filePic.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filePic);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return filePic.getAbsolutePath();
    }

    public static boolean exists(String filePath) {
        if (new File(filePath).exists()) {
            return true;
        }
        return false;
    }

    public static String saveImage(Bitmap bitmap, String url) {
        String path = getPathByType(DIR_TYPE_IMAGE) + CommonUtil.md5(url) + ".jpg";
        if (new File(path).exists()) {
            return path;
        }
        String s = ImageUtils.saveBitmap2file(bitmap, path, 58, Bitmap.CompressFormat.JPEG, CommonUtil.md5(url) + ".jpg");
        return s;
    }
}
