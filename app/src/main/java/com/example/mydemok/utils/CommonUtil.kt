package com.example.mydemok.utils

import android.app.Activity
import android.content.Context
import android.os.Environment
import android.text.TextUtils
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import cn.markmjw.platform.ShareModel
import com.example.mydemok.manager.IShareContentProvider
import com.example.mydemok.manager.ShareManager
import com.example.mydemok.mvp.ui.dialog.ShareDialog
import com.example.mydemok.mvp.ui.listener.OnPlatformClickListener
import com.example.mylibrary.mvp.uis.activities.BaseActivity
import java.io.File
import java.io.UnsupportedEncodingException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

object CommonUtil {
    const val RC_READ_PHONE_STATE = 3001
    const val RC_SETTING = 3000
    const val KEY_LENGTH = "length"
    const val KEY_VALUE_1 = "value_1"
    const val KEY_VALUE_2 = "value_2"
    const val KEY_VALUE_3 = "value_3"
    const val KEY_VALUE_4 = "value_4"
    const val KEY_VALUE_5 = "value_5"
    const val KEY_VALUE_6 = "value_6"
    const val KEY_VALUE_7 = "value_7"
    const val REQ_CODE_1 = 4001
    const val REQ_CODE_2 = 4002
    const val REQ_CODE_3 = 4003
    const val REQ_CODE_4 = 4004
    const val REQ_CODE_5 = 4005
    const val REQ_CODE_6 = 4006
    const val DEFULT_AREA_ID = 0
    fun getEditText(editText: EditText): String {
        return editText.text.toString().trim { it <= ' ' }
    }

    fun getEditText(editText: TextView): String {
        return editText.text.toString().trim { it <= ' ' }
    }

    fun editTextIsEmpty(editText: TextView): Boolean {
        return TextUtils.isEmpty(editText.text.toString().trim { it <= ' ' })
    }

    fun getOneDigits(num: Double): String {
        val df = DecimalFormat("#0")
        return df.format(num)
    }

    fun getTwoDigits(num: Double): String {
        val df = DecimalFormat("#00")
        return df.format(num)
    }

    fun getFourDigits(num: Double): String {
        val df = DecimalFormat("#0000")
        return df.format(num)
    }

    fun getShareDigits(num: Double): String {
        val df = DecimalFormat("#0000000000000")
        return df.format(num)
    }

    fun getFormatUserId(userId: Long): String {
        val df = DecimalFormat("#000000")
        return df.format(userId)
    }

    fun getOneFloat(num: Double): String {
        val df = DecimalFormat("#0.0")
        return df.format(num)
    }

    fun getMayOneFloat(num: Double): String {
        val df = DecimalFormat("#0.#")
        return df.format(num)
    }

    fun getMayTwoFloat(num: Double): String {
        val df = DecimalFormat("#0.##")
        return df.format(num)
    }

    fun getMayTwoMoney(num: Long): String {
        val df = DecimalFormat("#0.##")
        return df.format(num / 100.0)
    }

    fun getTwoFloat(num: Double): String {
        val df = DecimalFormat("#0.00")
        return df.format(num)
    }

    fun getTwoMoney(num: Long): String {
        val df = DecimalFormat("#0.00")
        return df.format(num / 100.0)
    }

    fun getMoneyNoDecimal(num: Long): String {
        val df = DecimalFormat("#0")
        return df.format(num / 100.0)
    }

    fun getMoneyFormat(num: Double): String {
        val df = DecimalFormat("#,##0.00")
        return df.format(num)
    }

    /**
     * 获取版本号
     */
    fun getVersion(context: Context): String {
        return try {
            val manager = context.packageManager
            val info = manager.getPackageInfo(context.packageName, 0)
            val version = info.versionName
            "" + version
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    fun calculatePercent(x: Double, total: Double): Long {
        return Math.round(x / total * 100)
    }

    fun getHideMobile(phone: String?): String {
        val result: String
        result = if (phone != null && phone.length > 6) {
            phone.substring(0, 3) + "****" + phone.substring(phone.length - 4)
        } else {
            phone ?: ""
        }
        return result
    }

    fun getHideIdCard(idCard: String?): String {
        val result: String
        result = if (idCard != null && idCard.length > 9) {
            idCard.substring(0, 6) + "********" + idCard.substring(idCard.length - 4)
        } else {
            idCard ?: ""
        }
        return result
    }

    fun getBankCardNum(phone: String): String {
        val sb = StringBuilder()
        for (i in 0 until phone.length) {
            if (i < 4 && i > phone.length - 4) {
                sb.append(phone[i])
            } else {
                val result = "*" + if ((i + 1) % 4 == 0) " " else ""
                sb.append(result)
            }
        }
        return sb.toString()
    }

    private val constellationArr = arrayOf(
        "水瓶座",
        "双鱼座",
        "白羊座",
        "金牛座",
        "双子座",
        "巨蟹座",
        "狮子座",
        "处女座",
        "天秤座",
        "天蝎座",
        "射手座",
        "魔羯座"
    )
    private val constellationEdgeDay =
        intArrayOf(20, 19, 21, 21, 21, 22, 23, 23, 23, 23, 22, 22)

    fun getConstellation(date: Date?): String {
        if (date == null) {
            return ""
        }
        val cal = Calendar.getInstance()
        cal.time = date
        var month = cal[Calendar.MONTH]
        val day = cal[Calendar.DAY_OF_MONTH]
        if (day < constellationEdgeDay[month]) {
            month = month - 1
        }
        return if (month >= 0) {
            constellationArr[month]
        } else constellationArr[11]
        // default to return 魔羯
    }

    /**
     * 设置页面的透明度
     *
     * @param bgAlpha 1表示不透明
     */
    fun setBackgroundAlpha(activity: Activity?, bgAlpha: Float) {
        if (activity == null) {
            return
        }
        val window = activity.window
        val lp = window.attributes
        window.setFlags(
            WindowManager.LayoutParams.FLAG_DIM_BEHIND,
            WindowManager.LayoutParams.FLAG_DIM_BEHIND
        )
        lp.alpha = bgAlpha //0.0-1.0
        window.attributes = lp
    }

    fun existSDCard(): Boolean {
        return Environment.getExternalStorageState() ==
                Environment.MEDIA_MOUNTED
    }

    fun parseLong(number: String): Long {
        var result: Long = 0
        try {
            result = number.toLong()
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
        return result
    }

    fun replaceBlank(str: String?): String? {
        val dest: String?
        dest = if (!TextUtils.isEmpty(str)) {
            val p = Pattern.compile("\\s*|\t|\r|\n")
            val m = p.matcher(str)
            m.replaceAll("")
        } else {
            null
        }
        return dest
    }

    fun getNotBlankStr(keyword: String?): String? {
        return keyword?.replace("\\s*", "")
    }

    fun randomInt(start: Int, end: Int): Int {
        return (Math.random() * (end - start + 1)).toInt() + start
    }

    fun randomFloat(start: Float, end: Float): Float {
        return (Math.random() * (end - start + 1)).toFloat() + start
    }

    fun isEmpty(collection: Collection<*>?): Boolean {
        return collection == null || collection.size < 1
    }

    fun isNotEmpty(collection: Collection<*>?): Boolean {
        return !isEmpty(collection)
    }

    @JvmOverloads
    fun joinListToString(
        list: List<*>,
        interval: String? = ","
    ): String {
        var result: String
        val sb = StringBuilder()
        if (isNotEmpty(list)) {
            for (i in list.indices) {
                sb.append(list[i])
                if (i != list.size - 1) {
                    sb.append(interval)
                }
            }
        }
        return sb.toString()
    }

    fun <T> getNoneNullList(datas: List<T>?): List<T> {
        return datas ?: ArrayList()
    }

    fun getNoneNullDefault(s: String?, defaultS: String?): String {
        return s ?: defaultS!!
    }

    fun getNoneNullStr(s: String?): String {
        return getNoneNullDefault(s, "")
    }

    fun getNoneEmptyDefault(s: String, defaultS: String?): String {
        return if (TextUtils.isEmpty(s)) s else defaultS!!
    }

    fun getNoneEmptyZero(s: String?): String {
        return if (TextUtils.isEmpty(s)) "0" else s!!
    }

    fun clearHtmlTag(text: String): CharSequence {
        var text = text
        val regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>" //定义script的正则表达式
        val regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>" //定义style的正则表达式
        val regEx_html = "<[^>]+>" //定义HTML标签的正则表达式
        val p_script =
            Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE)
        val m_script = p_script.matcher(text)
        text = m_script.replaceAll("") //过滤script标签
        val p_style =
            Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE)
        val m_style = p_style.matcher(text)
        text = m_style.replaceAll("") //过滤style标签
        val p_html =
            Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE)
        val m_html = p_html.matcher(text)
        text = m_html.replaceAll("") //过滤html标签
        return text.trim { it <= ' ' } //返回文本字符串
    }

    fun getGenderKey(s: String?): String {
        return if (TextUtils.equals("男", s)) "M" else if (TextUtils.equals("女", s)) "F" else ""
    }

    fun getGenderValue(s: String?): String {
        return if (TextUtils.equals("F", s)) "女" else if (TextUtils.equals("M", s)) "男" else ""
    }

    fun showtime(): String {
        var state = ""
        val hh = getMonth(System.currentTimeMillis())
        state = if (3 <= hh && hh < 8) {
            "早上好"
        } else if (3 <= hh && hh < 12) {
            "上午好"
        } else if (12 <= hh && hh < 20) {
            "下午好"
        } else {
            "晚上好"
        }
        return state
    }

    fun getMonth(time: Long): Int {
        val format = SimpleDateFormat("HH")
        val now = Date(time)
        return format.format(now).toInt()
    }

    fun parseInt(number: String): Int {
        var result = 0
        try {
            result = number.toInt()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }

    fun int2chineseNum(src: Int): String {
        var src = src
        val num =
            arrayOf("零", "一", "二", "三", "四", "五", "六", "七", "八", "九")
        val unit =
            arrayOf("", "十", "百", "千", "万", "十", "百", "千", "亿", "十", "百", "千")
        var dst = ""
        var count = 0
        while (src > 0) {
            dst = num[src % 10] + unit[count] + dst
            src = src / 10
            count++
        }
        return dst.replace("零[千百十]".toRegex(), "零").replace("零+万".toRegex(), "万")
            .replace("零+亿".toRegex(), "亿").replace("亿万".toRegex(), "亿零")
            .replace("零+".toRegex(), "零").replace("零$".toRegex(), "")
    }

    /**
     * MD5加密
     */
    @JvmStatic
    fun md5(string: String): String {
        val hash: ByteArray
        hash = try {
            MessageDigest.getInstance("MD5")
                .digest(string.toByteArray(charset("UTF-8")))
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException("Huh, MD5 should be supported?", e)
        } catch (e: UnsupportedEncodingException) {
            throw RuntimeException("Huh, UTF-8 should be supported?", e)
        }
        val hex = StringBuilder(hash.size * 2)
        for (b in hash) {
            if (b.toInt() and 0xFF < 0x10) hex.append("0")
            hex.append(Integer.toHexString(b.toInt() and 0xFF))
        }
        return hex.toString()
    }

    fun compareVersion(
        version1: String,
        version2: String
    ): Int {  //解析版本号a.b.c为a b c三个数字对比大小
        if (TextUtils.isEmpty(version1) || TextUtils.isEmpty(version2)) { //某个版本号异常，认为是同一个版本，不弹更新对话框
            return 0
        }
        val versionStr1 =
            version1.split("\\.".toRegex()).toTypedArray()
        val versionStr2 =
            version2.split("\\.".toRegex()).toTypedArray()
        if (versionStr1.size == 0 || versionStr2.size == 0) { //某个版本号异常，认为是同一个版本，不弹更新对话框
            return 0
        }
        for (i in versionStr1.indices) {
            if (i >= versionStr2.size) { //类似于1.2.2 和1.2的情况
                return 1
            }
            val versionItem1 = versionStr1[i]
            val versionItem2 = versionStr2[i]
            try {
                val versionInt1 = Integer.valueOf(versionItem1)
                val versionInt2 = Integer.valueOf(versionItem2)
                if (versionInt1 > versionInt2) {
                    return 1
                } else if (versionInt1 < versionInt2) {
                    return -1
                }
            } catch (e: Exception) { //某个版本号异常，认为是同一个版本，不弹更新对话框
                return 0
            }
        }
        return if (versionStr1.size == versionStr2.size) {
            0
        } else -1
        //类似于1.2和1.2.2的情况
    }

    fun isInstallByread(packageName: String): Boolean {
        return File("/data/data/$packageName").exists()
    }

    @Synchronized
    fun showShareDialog(
        title: String?,
        image: String?,
        desc: String?,
        shareUrl: String?,
        shareDialog: ShareDialog? = null,
        mcontext: BaseActivity<*>
    ) {
        var desc = desc
        if (" ".equals(desc, ignoreCase = true) || desc.isNullOrEmpty() && !title.isNullOrEmpty()) {
            desc = title
        }
        var mShareDialog: ShareDialog
        if (shareDialog == null)
            mShareDialog = ShareDialog(mcontext)
        else
            mShareDialog = shareDialog

        val shareDesc = desc
        mShareDialog?.setOnPlatformClickListener(object : OnPlatformClickListener {
            override fun onPlatformClick(platform: String?) {
                mShareDialog?.dismiss()
                val shareManager = ShareManager(mcontext)
                shareManager.setShareContentProvider(object : IShareContentProvider {
                    override fun getWeiboShareModel(): ShareModel {
                        return ShareModel().apply {
                            text = shareDesc + shareUrl
                            this.title = title
                            imageUri = image
                            if (!TextUtils.isEmpty(shareDesc)) {
                                description = shareDesc?.substring(
                                    0, Math.min(shareDesc.length, 140)
                                )
                            }
                        }
                    }

                    override fun getQQShareModel(): ShareModel {
                        return ShareModel().apply {
                            this.title = title
                            imageUri = image
                            this.shareUrl = shareUrl
                            if (!TextUtils.isEmpty(shareDesc)) {
                                description = shareDesc?.substring(
                                    0, Math.min(shareDesc.length, 140)
                                )
                            }
                        }
                    }

                    override fun getQzoneShareModel(): ShareModel {
                        return ShareModel().apply {
                            this.title = title
                            imageUri = image
                            this.shareUrl = shareUrl
                            if (!TextUtils.isEmpty(shareDesc)) {
                                description = shareDesc?.substring(
                                    0, Math.min(shareDesc.length, 140)
                                )
                            }
                        }
                    }

                    override fun getWeChatShareModel(): ShareModel {
                        return ShareModel().apply {
                            this.title = title
                            imageUri = image
                            this.shareUrl = shareUrl
                            if (!TextUtils.isEmpty(shareDesc)) {
                                description = shareDesc?.substring(
                                    0, Math.min(shareDesc.length, 140)
                                )
                            }
                        }
                    }

                    override fun generatePoster(): ShareModel? {
                        return ShareModel().apply {
                            text = shareDesc + shareUrl
                            this.title = title
                            this.shareUrl = shareUrl
                            imageUri = image
                        }
                    }

                    override fun copy(): String {
                        return shareUrl ?: ""
                    }
                })
                shareManager.shareTo(platform)
            }
        })
        mShareDialog?.show()
    }
}