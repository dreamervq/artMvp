package com.example.mydemok.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public class CommonUtil {
    public static final int RC_READ_PHONE_STATE = 3001;
    public static final int RC_SETTING = 3000;
    public static final String KEY_LENGTH = "length";
    public static final String KEY_VALUE_1 = "value_1";
    public static final String KEY_VALUE_2 = "value_2";
    public static final String KEY_VALUE_3 = "value_3";
    public static final String KEY_VALUE_4 = "value_4";
    public static final String KEY_VALUE_5 = "value_5";
    public static final String KEY_VALUE_6 = "value_6";
    public static final String KEY_VALUE_7 = "value_7";
    public static final int REQ_CODE_1 = 4001;
    public static final int REQ_CODE_2 = 4002;
    public static final int REQ_CODE_3 = 4003;
    public static final int REQ_CODE_4 = 4004;
    public static final int REQ_CODE_5 = 4005;
    public static final int REQ_CODE_6 = 4006;

    public static final int DEFULT_AREA_ID = 0;


    public static String getEditText(EditText editText) {
        return editText.getText().toString().trim();
    }

    public static String getEditText(TextView editText) {
        return editText.getText().toString().trim();
    }

    public static boolean editTextIsEmpty(TextView editText) {
        return TextUtils.isEmpty(editText.getText().toString().trim());
    }

    public static String getOneDigits(double num) {
        DecimalFormat df = new DecimalFormat("#0");
        return df.format(num);
    }

    public static String getTwoDigits(double num) {
        DecimalFormat df = new DecimalFormat("#00");
        return df.format(num);
    }

    public static String getFourDigits(double num) {
        DecimalFormat df = new DecimalFormat("#0000");
        return df.format(num);
    }

    public static String getShareDigits(double num) {
        DecimalFormat df = new DecimalFormat("#0000000000000");
        return df.format(num);
    }


    public static String getFormatUserId(long userId) {
        DecimalFormat df = new DecimalFormat("#000000");
        return df.format(userId);
    }

    public static String getOneFloat(double num) {
        DecimalFormat df = new DecimalFormat("#0.0");
        return df.format(num);
    }

    public static String getMayOneFloat(double num) {
        DecimalFormat df = new DecimalFormat("#0.#");
        return df.format(num);
    }

    public static String getMayTwoFloat(double num) {
        DecimalFormat df = new DecimalFormat("#0.##");
        return df.format(num);
    }

    public static String getMayTwoMoney(long num) {
        DecimalFormat df = new DecimalFormat("#0.##");
        return df.format(num / 100D);
    }

    public static String getTwoFloat(double num) {
        DecimalFormat df = new DecimalFormat("#0.00");
        return df.format(num);
    }

    public static String getTwoMoney(long num) {
        DecimalFormat df = new DecimalFormat("#0.00");
        return df.format(num / 100D);
    }

    public static String getMoneyNoDecimal(long num) {
        DecimalFormat df = new DecimalFormat("#0");
        return df.format(num / 100D);
    }

    public static String getMoneyFormat(double num) {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        return df.format(num);
    }

    /**
     * 获取版本号
     */
    public static String getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return "" + version;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    public static long calculatePercent(double x, double total) {
        return Math.round(x / total * 100);
    }

    public static String getHideMobile(String phone) {
        String result;
        if (phone != null && phone.length() > 6) {
            result = phone.substring(0, 3) + "****" + phone.substring(phone.length() - 4);
        } else {
            result = phone != null ? phone : "";
        }
        return result;
    }

    public static String getHideIdCard(String idCard) {
        String result;
        if (idCard != null && idCard.length() > 9) {
            result = idCard.substring(0, 6) + "********" + idCard.substring(idCard.length() - 4);
        } else {
            result = idCard != null ? idCard : "";
        }
        return result;
    }

    public static String getBankCardNum(String phone) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < phone.length(); i++) {
            if (i < 4 && i > phone.length() - 4) {
                sb.append(phone.charAt(i));
            } else {
                String result = "*" + (((i + 1) % 4 == 0) ? " " : "");
                sb.append(result);
            }
        }
        return sb.toString();
    }

    private static final String[] constellationArr = {"水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "魔羯座"};
    private static final int[] constellationEdgeDay = {20, 19, 21, 21, 21, 22, 23, 23, 23, 23, 22, 22};

    public static String getConstellation(Date date) {

        if (date == null) {

            return "";
        }
        Calendar cal = Calendar.getInstance();

        cal.setTime(date);

        int month = cal.get(Calendar.MONTH);

        int day = cal.get(Calendar.DAY_OF_MONTH);

        if (day < constellationEdgeDay[month]) {

            month = month - 1;
        }

        if (month >= 0) {

            return constellationArr[month];
        }
        // default to return 魔羯
        return constellationArr[11];
    }

    /**
     * 设置页面的透明度
     *
     * @param bgAlpha 1表示不透明
     */
    public static void setBackgroundAlpha(Activity activity, float bgAlpha) {
        if (activity == null) {
            return;
        }
        Window window = activity.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        window.setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND, WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        lp.alpha = bgAlpha; //0.0-1.0
        window.setAttributes(lp);
    }


    public static boolean existSDCard() {
        return android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
    }


    public static long parseLong(String number) {
        long result = 0;
        try {
            result = Long.parseLong(number);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String replaceBlank(String str) {
        String dest;
        if (!TextUtils.isEmpty(str)) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        } else {
            dest = null;
        }
        return dest;
    }

    public static String getNotBlankStr(String keyword) {
        return keyword != null ? keyword.replace("\\s*", "") : null;
    }

    public static int randomInt(int start, int end) {
        return (int) (Math.random() * (end - start + 1)) + start;
    }

    public static float randomFloat(float start, float end) {
        return (float) (Math.random() * (end - start + 1)) + start;
    }

    public static boolean isEmpty(Collection collection) {
        return collection == null || collection.size() < 1;
    }

    public static boolean isNotEmpty(Collection collection) {
        return !isEmpty(collection);
    }

    public static String joinListToString(List list) {
        return joinListToString(list, ",");
    }

    public static String joinListToString(List list, String interval) {
        String result;
        StringBuilder sb = new StringBuilder();
        if (isNotEmpty(list)) {
            for (int i = 0; i < list.size(); i++) {
                sb.append(list.get(i));
                if (i != list.size() - 1) {
                    sb.append(interval);
                }
            }
        }
        return sb.toString();
    }

    public static <T> List<T> getNoneNullList(List<T> datas) {
        return datas != null ? datas : new ArrayList<>();
    }

    public static String getNoneNullDefault(String s, String defaultS) {
        return s != null ? s : defaultS;
    }

    public static String getNoneNullStr(String s) {
        return getNoneNullDefault(s, "");
    }

    public static String getNoneEmptyDefault(String s, String defaultS) {
        return TextUtils.isEmpty(s) ? s : defaultS;
    }

    public static String getNoneEmptyZero(String s) {
        return TextUtils.isEmpty(s) ? "0" : s;
    }

    public static CharSequence clearHtmlTag(String text) {
        String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; //定义script的正则表达式
        String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; //定义style的正则表达式
        String regEx_html = "<[^>]+>"; //定义HTML标签的正则表达式

        Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(text);
        text = m_script.replaceAll(""); //过滤script标签

        Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(text);
        text = m_style.replaceAll(""); //过滤style标签

        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(text);
        text = m_html.replaceAll(""); //过滤html标签
        return text.trim(); //返回文本字符串
    }

    public static String getGenderKey(String s) {
        return TextUtils.equals("男", s) ? "M" : (TextUtils.equals("女", s) ? "F" : "");
    }

    public static String getGenderValue(String s) {
        return TextUtils.equals("F", s) ? "女" : (TextUtils.equals("M", s) ? "男" : "");
    }

    public static String showtime() {
        String state = "";
        int hh = getMonth(System.currentTimeMillis());
        if (3 <= hh && hh < 8) {
            state = "早上好";
        } else if (3 <= hh && hh < 12) {
            state = "上午好";
        } else if (12 <= hh && hh < 20) {
            state = "下午好";
        } else {
            state = "晚上好";
        }
        return state;
    }

    public static int getMonth(long time) {
        SimpleDateFormat format = new SimpleDateFormat("HH");
        Date now = new Date(time);
        return Integer.parseInt(format.format(now));
    }

    public static int parseInt(String number) {
        int result = 0;
        try {
            result = Integer.parseInt(number);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String int2chineseNum(int src) {
        final String num[] = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
        final String unit[] = {"", "十", "百", "千", "万", "十", "百", "千", "亿", "十", "百", "千"};
        String dst = "";
        int count = 0;
        while (src > 0) {
            dst = (num[src % 10] + unit[count]) + dst;
            src = src / 10;
            count++;
        }
        return dst.replaceAll("零[千百十]", "零").replaceAll("零+万", "万")
                .replaceAll("零+亿", "亿").replaceAll("亿万", "亿零")
                .replaceAll("零+", "零").replaceAll("零$", "");

    }

    /**
     * MD5加密
     * */
    public static String md5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

}
