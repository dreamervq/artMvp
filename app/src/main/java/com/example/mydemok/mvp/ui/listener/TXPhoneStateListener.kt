package com.example.mydemok.mvp.ui.listener

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.app.Service
import android.content.Context
import android.os.Bundle
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import com.tencent.rtmp.TXVodPlayer
import me.jessyan.art.base.BaseApplication
import java.lang.ref.WeakReference

class TXPhoneStateListener(context: Context, player: TXVodPlayer) : PhoneStateListener(),
    ActivityLifecycleCallbacks {
    var mPlayer: WeakReference<TXVodPlayer>? = null
    var mContext: Context? = null
    var activityCount = 0
    fun startListen() {
        val tm =
            mContext!!.getSystemService(Service.TELEPHONY_SERVICE) as TelephonyManager
        tm.listen(this, LISTEN_CALL_STATE)
        BaseApplication.getInstance().registerActivityLifecycleCallbacks(this)
    }
    fun stopListen() {
//        val tm =
//            mContext!!.getSystemService(Service.TELEPHONY_SERVICE) as TelephonyManager
//        tm.listen(this, LISTEN_NONE)
        BaseApplication.getInstance().unregisterActivityLifecycleCallbacks(this)
    }

    override fun onCallStateChanged(state: Int, phoneNumber: String?) {
        super.onCallStateChanged(state, phoneNumber)
        val player = mPlayer!!.get()
        when (state) {
            TelephonyManager.CALL_STATE_RINGING -> player?.pause()
            TelephonyManager.CALL_STATE_OFFHOOK -> player?.pause()
            TelephonyManager.CALL_STATE_IDLE -> if (player != null && activityCount >= 0) player.resume()
        }
    }
    override fun onActivityPaused(p0: Activity?) {
    }

    override fun onActivityResumed(p0: Activity?) {
        activityCount++
    }

    override fun onActivityStarted(p0: Activity?) {
    }

    override fun onActivityDestroyed(p0: Activity?) {
    }

    override fun onActivitySaveInstanceState(p0: Activity?, p1: Bundle?) {
    }

    override fun onActivityStopped(p0: Activity?) {
        activityCount--
    }

    override fun onActivityCreated(p0: Activity?, p1: Bundle?) {
    }
    fun isInBackground(): Boolean {
        return activityCount < 0
    }
    init {
        mContext = context
        mPlayer = WeakReference(player)
    }
}