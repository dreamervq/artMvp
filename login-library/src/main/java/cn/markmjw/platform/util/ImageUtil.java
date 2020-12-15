package cn.markmjw.platform.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * ImageUtil
 *
 * @author markmjw
 * @date 2015-08-16
 */
public class ImageUtil {
    /**
     * Bitmap转换为byte[]
     *
     * @param bitmap
     * @param needRecycle
     * @return
     */
    public static byte[] bitmapToBytes(Bitmap bitmap, boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bitmap.recycle();
        }
        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            Log.e("ImageUtil", e.getMessage());
        }
        return result;
    }


    public static Bitmap zoomForWxMini(Bitmap bitmap,int w,int h){
        if (null == bitmap) {
            return null;
        }
       try{
           Bitmap mBitmap=Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
           Canvas mCanvas=new Canvas(mBitmap);
           mCanvas.drawColor(Color.WHITE);
           Paint paint=new Paint();
           mCanvas.drawBitmap(mBitmap,0.0f,0.0f,paint);
           int width_main=bitmap.getWidth();
           int height_main=bitmap.getHeight();
           if(width_main>height_main){
               mCanvas.drawBitmap(bitmap,0,(h-height_main)/2,paint);
           }else{
               mCanvas.drawBitmap(bitmap,(w-width_main)/2,0,paint);
           }
           mCanvas.save();
           mCanvas.restore();
           return mBitmap;
       }catch (OutOfMemoryError ex){
           return null;
       }
    }

    /**
     * 缩放图片
     *
     * @param bitmap 原图
     * @param w      宽
     * @param h      高
     */
    public static Bitmap zoom(Bitmap bitmap, int w, int h) {
        if (null == bitmap) {
            return null;
        }

        try {

            float scaleWidth = w * 1.0f / bitmap.getWidth();
            float scaleHeight = h * 1.0f / bitmap.getHeight();

            // 重新生成一个放大/缩小后图片
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight);
            Bitmap result = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(result);

            canvas.drawBitmap(bitmap, matrix, null);

            return result;
        } catch (OutOfMemoryError e) {
            return null;
        }
    }

    public static Bitmap getBitmapFromFile(String path) {
        Bitmap bitmap = null;
        try {
            File file = new File(path);
            if (file.exists()) {
                bitmap = BitmapFactory.decodeFile(path);
            }
        } catch (OutOfMemoryError e) {
            Log.e("ImageUtil", e.getMessage());
        }
        return bitmap;
    }

    /**
     * 将Drawable转化为Bitmap
     *
     * @param drawable
     * @return
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, drawable
                .getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }

    public static String readPictureDegree(String path) {
        if (TextUtils.isEmpty(path)) {
            return path;
        }
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
                default:
                    Bitmap compressPhoto = getCompressPhoto(path);
                    path = savePhotoToSD(compressPhoto);
                    return path;
            }
        } catch (Exception e) {
        }
        Bitmap compressPhoto = getCompressPhoto(path);
        Bitmap bitmap = rotaingImageView(degree, compressPhoto);
        path = savePhotoToSD(bitmap);
        return path;
    }
    public static String savePhotoToSD(Bitmap mbitmap) {
        FileOutputStream outStream = null;

        File fileName = new File(Environment.getExternalStorageDirectory().toString()+File.separator+"zhidongguan"+File.separator+"image"+File.separator
        +System.currentTimeMillis()+".jpg") ;
        if (!fileName.exists()){
            fileName.getParentFile().mkdirs();
        }
        try {
            outStream = new FileOutputStream(fileName.getPath());
            // 把数据写入文件，100表示不压缩
            mbitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            return fileName.getPath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (outStream != null) {
                    // 记得要关闭流！
                    outStream.close();
                }
                if (mbitmap != null) {
                    mbitmap.recycle();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        if (angle == 0)
            return bitmap;
        Bitmap returnBm = null;
        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bitmap;
        }
        if (bitmap != returnBm) {
            bitmap.recycle();
        }
        return returnBm;
    }
    public static Bitmap getCompressPhoto(String path) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;//只读边,不读内容
        BitmapFactory.decodeFile(path, newOpts);
        newOpts.inJustDecodeBounds = false;
        int width = newOpts.outWidth;
        int height = newOpts.outHeight;
        float maxSize = 1024;
        int be = 1;
        if (width >= height && width > maxSize) {//缩放比,用高或者宽其中较大的一个数据进行计算
            be = (int) (newOpts.outWidth / maxSize);
            be++;
        } else if (width < height && height > maxSize) {
            be = (int) (newOpts.outHeight / maxSize);
            be++;
        }
        newOpts.inSampleSize = be;//设置采样率
        newOpts.inPreferredConfig = Bitmap.Config.ARGB_8888;//该模式是默认的,可不设
        newOpts.inPurgeable = true;// 同时设置才会有效
        newOpts.inInputShareable = true;//。当系统内存不够时候图片自动被回收
        Bitmap bmp = BitmapFactory.decodeFile(path, newOpts);
        newOpts = null;
        return bmp;
    }

    public  static String getCompressImg(String path){
        Bitmap compressPhoto = getCompressPhoto(path);
        String s = savePhotoToSD(compressPhoto);
        return s;
    }
}
