package com.example.mydemok.manager

import android.os.Handler
import android.text.TextUtils
import cn.markmjw.platform.util.GsonUtil
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.example.mydemok.constant.EventBusTags
import com.example.mydemok.mvp.model.City
import com.sina.weibo.sdk.utils.LogUtil
import me.jessyan.art.base.BaseApplication
import me.jessyan.art.integration.EventBusManager
import me.jessyan.art.utils.TinyPref
import timber.log.Timber
import java.util.*


class LocateManager private constructor() {
    private var mLocationOption: AMapLocationClientOption? = null
    var city: City? = null
        private set
    private var mLastLocationTime: Long

    // 定位状态
    var status = STATUS_UNKNOWN
        private set
    private val mHandler: Handler
    private val mTimeoutRunnable = TimeoutRunnable()
    private val mLocationListener: AMapLocationListener = object : AMapLocationListener {
        override fun onLocationChanged(loc: AMapLocation) {
            removeTimeoutRunnable()
            stopLocate()
            var location: AMapLocation? = loc
            if (location == null) {
                location = sManager.lastKnownLocation
            }
            if (location != null) {
                val lat = location.latitude
                val lng = location.longitude
                var cityCode = ""
                var desc = ""
                val locBundle = location.extras
                if (locBundle != null) {
                    cityCode = locBundle.getString("citycode")
                    desc = locBundle.getString("desc")
                }
                if (null == city) {
                    city = City()
                }
                city!!.lat = lat
                city!!.lng = lng
                city!!.cityCode = cityCode
                city!!.desc = desc
                city!!.location = location
                LogUtil.i(TAG, "定位成功:" + city)
                val str = """
                    定位成功:[$lng,$lat]
                    精    度    :${location.accuracy}米
                    定位方式:${location.provider}
                    定位时间:${Date(location.time).toLocaleString()}
                    城市编码:$cityCode
                    位置描述:$desc
                    省:${location.province}
                    市:${location.city}
                    区(县):${location.district}
                    区域编码:${location.adCode}
                    """.trimIndent()
                //LogUtil.d(TAG, str);
                Timber.e("-------------------$str")
                if (invalidLatLng(city)) {
                    mLastLocationTime = System.currentTimeMillis()
                    status = STATUS_LOCATED_SUCCESS
                    TinyPref.getInstance().putBoolean(KEY_IS_LOCATED, true)
                    EventBusManager.getInstance().post(
                        EventBusTags.EVENT_LOCATION_SUCCESS,
                        EventBusTags.EVENT_LOCATION_SUCCESS
                    )
                    saveLocatedCity()
                } else {
                    status = STATUS_LOCATED_FAILED
                    TinyPref.getInstance().putBoolean(KEY_IS_LOCATED, false)
                    EventBusManager.getInstance().post(
                        EventBusTags.EVENT_LOCATION_FAILED,
                        EventBusTags.EVENT_LOCATION_FAILED
                    )
                }
            } else {
                status = STATUS_LOCATED_FAILED
                TinyPref.getInstance().putBoolean(KEY_IS_LOCATED, false)
                EventBusManager.getInstance()
                    .post(EventBusTags.EVENT_LOCATION_FAILED, EventBusTags.EVENT_LOCATION_FAILED)
            }
        }
    }

    private fun saveLocatedCity() {
        TinyPref.getInstance().putString(
            LOCATION_CITY_MODEL,
            GsonUtil.toJson(city)
        )
    }

    private fun initLocationOptions() {
        mLocationOption = AMapLocationClientOption()
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption!!.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption!!.isNeedAddress = true
        //设置是否只定位一次,默认为false
        mLocationOption!!.isOnceLocation = false
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption!!.isWifiActiveScan = true
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption!!.isMockEnable = false
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption!!.interval = 2000
        //给定位客户端对象设置定位参数
        sManager.setLocationOption(mLocationOption)
    }

    private fun initCityModel() {
        val cityModel =
            TinyPref.getInstance().getString(LOCATION_CITY_MODEL)
        if (!TextUtils.isEmpty(cityModel)) {
            try {
                city = GsonUtil.fromJson(
                    cityModel,
                    City::class.java
                )
                if (!invalidLatLng(city)) {
                    city = null
                }
            } catch (e: Exception) {
                // ignore
            }
        }
    }

    fun startLocateNow() {
        EventBusManager.getInstance()
            .post(EventBusTags.EVENT_LOCATION_STARTING, EventBusTags.EVENT_LOCATION_STARTING)
        sManager.setLocationListener(mLocationListener)
        sManager.startLocation()
        postTimeoutRunnable(DEFAULT_TIMEOUT)
    }
    /**
     * 开始定位
     *
     * @param timeout
     */
    /**
     * 开始定位
     */
    @JvmOverloads
    fun startLocate(timeout: Long = DEFAULT_TIMEOUT) {
        if (needLocate()) {
            status = STATUS_LOCATING
            EventBusManager.getInstance()
                .post(EventBusTags.EVENT_LOCATION_STARTING, EventBusTags.EVENT_LOCATION_STARTING)
            sManager.setLocationListener(mLocationListener)
            sManager.startLocation()
            postTimeoutRunnable(timeout)
        } else {
            when (status) {
                STATUS_LOCATING -> {
                    EventBusManager.getInstance().post(
                        EventBusTags.EVENT_LOCATION_FAILED,
                        EventBusTags.EVENT_LOCATION_FAILED
                    )
                    postTimeoutRunnable(timeout)
                }
                STATUS_LOCATED_SUCCESS -> EventBusManager.getInstance()
                    .post(EventBusTags.EVENT_LOCATION_SUCCESS, EventBusTags.EVENT_LOCATION_SUCCESS)
            }
        }
    }

    fun stopLocate() {
        sManager.stopLocation()
        sManager.unRegisterLocationListener(mLocationListener)
        mHandler.removeCallbacks(mTimeoutRunnable)
    }

    fun destroy() {
        sManager.onDestroy()
    }

    private fun needLocate(): Boolean {
        return System.currentTimeMillis() - mLastLocationTime >= INTERVAL_TIME || status == STATUS_LOCATED_FAILED || status ==
                STATUS_LOCATED_TIMEOUT
    }

    private fun postTimeoutRunnable(timeout: Long) {
        mHandler.removeCallbacks(mTimeoutRunnable)
        mHandler.postDelayed(mTimeoutRunnable, timeout)
    }

    private fun removeTimeoutRunnable() {
        mHandler.removeCallbacks(mTimeoutRunnable)
    }

    private inner class TimeoutRunnable : Runnable {
        override fun run() {
            status = STATUS_LOCATED_TIMEOUT
            stopLocate()
            EventBusManager.getInstance()
                .post(EventBusTags.EVENT_LOCATION_FAILED, EventBusTags.EVENT_LOCATION_FAILED)
        }
    }

    companion object {
        const val LOCATION_CITY_MODEL = "location_city_model"

        /**
         * 未知
         */
        const val STATUS_UNKNOWN = 0X01

        /**
         * 定位中
         */
        const val STATUS_LOCATING = STATUS_UNKNOWN shl 1

        /**
         * 定位成功
         */
        const val STATUS_LOCATED_SUCCESS = STATUS_LOCATING shl 1

        /**
         * 定位失败
         */
        const val STATUS_LOCATED_FAILED = STATUS_LOCATED_SUCCESS shl 1

        /**
         * 定位超时
         */
        const val STATUS_LOCATED_TIMEOUT = STATUS_LOCATED_FAILED shl 1

        /**
         * 是否定位过
         */
        const val KEY_IS_LOCATED = "is_located"
        private const val TAG = "LocateManager"

        // 时间间隔1小时
        private const val INTERVAL_TIME: Long = 10000

        // 距离1000米
        private const val INTERVAL_DISTANCE: Long = 1000

        /**
         * 默认的超时时间
         */
        private const val DEFAULT_TIMEOUT: Long = 10000
        private var sInstance: LocateManager? = null
        private lateinit var sManager: AMapLocationClient
        fun invalidLatLng(city: City?): Boolean {
            return null != city && (city.lat > 0 || city.lng > 0)
        }

        val instance: LocateManager?
            get() {
                if (sInstance == null) {
                    sInstance = LocateManager()
                }
                return sInstance
            }
    }

    init {
        mHandler = Handler(BaseApplication.getInstance().mainLooper)
        sManager = AMapLocationClient(BaseApplication.getInstance())
        initLocationOptions()
        mLastLocationTime = 0L
        initCityModel()
    }
}