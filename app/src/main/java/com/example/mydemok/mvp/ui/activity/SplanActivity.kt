package com.example.mydemok.mvp.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.mydemok.R
import com.example.mydemok.application.Constants
import com.example.mydemok.manager.CardManager
import com.example.mydemok.mvp.model.SplashAD
import com.example.mydemok.mvp.presenter.SplanPresenter
import com.example.mydemok.mvp.ui.adapter.SplanFirstAdapter
import com.example.mydemok.mvp.ui.listener.TXPhoneStateListener
import com.example.mydemok.utils.FileUtil
import com.example.mylibrary.mvp.uis.activities.BaseActivity
import com.tencent.rtmp.ITXVodPlayListener
import com.tencent.rtmp.TXLiveConstants
import com.tencent.rtmp.TXVodPlayConfig
import com.tencent.rtmp.TXVodPlayer
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_splan.*
import me.jessyan.art.http.imageloader.glide.GlideArt
import me.jessyan.art.mvp.Message
import me.jessyan.art.ui.util.PixelUtil
import me.jessyan.art.utils.ArtUtils
import me.jessyan.art.utils.NetworkUtil
import me.jessyan.art.utils.TinyPref
import java.util.concurrent.TimeUnit
import kotlin.math.ceil

@Suppress("DEPRECATED_IDENTITY_EQUALS")
class SplanActivity : BaseActivity<SplanPresenter>(), ITXVodPlayListener {
    private val DELAY_KEEP_TIME_MIN = 3
    private lateinit var mPlayer: TXVodPlayer
    private val GET_APP_AGREEMENT = 0x112
    private var mSplashAD: SplashAD? = null
    private var subscribe: Disposable? = null
    private var txPhoneStateListener: TXPhoneStateListener? = null
    private var isMute = true
    private var screenWidth = 0
    override fun getContentViewId(): Int {
        if (!isTaskRoot) {
            if (intent.flags and Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT !== 0) {
                finish()
                return 0
            }
        }
        return R.layout.activity_splan
    }

    override fun init(savedInstanceState: Bundle?) {
        screenWidth = PixelUtil.getScreenWidth(this)
        imgCover.visibility = View.VISIBLE
        if (!TinyPref.getInstance().getBoolean(Constants.PREF_FIRST, false)) {
            imgCover.visibility = View.GONE
            var splanFirstAdapter = SplanFirstAdapter(this)
            view_pager_first.setAdapter(splanFirstAdapter)
        } else {
            getPresenter().getSplashAD(Message.obtain(this, 11))
            subscribe = Observable.timer(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (isFinishing || mSplashAD == null) {
                        return@subscribe
                    }
                    toMainPage()
                }


        }
        getPresenter().getRewardConfig(Message.obtain(this))
        reqAppAgreement()
    }

    private fun reqAppAgreement() {
        val message = Message.obtain(this)
        message.arg1 = GET_APP_AGREEMENT
        getPresenter().getSplashAD(message)
    }

    override fun obtainPresenter(): SplanPresenter? {
        return SplanPresenter(ArtUtils.obtainAppComponentFromContext(this))
    }

    override fun handleMessage(message: Message) {
        if (Constants.KEY_SUCCESS == message.arg1) {
            mSplashAD = message.obj as SplashAD
            if (NetworkUtil.isConnected()) {
                if (mSplashAD != null && (!TextUtils.isEmpty(mSplashAD!!.video_url) || mSplashAD?.image != null && mSplashAD?.image?.size!! > 0)) {
                    updateADView(mSplashAD)
                } else {
                    toMainPage()
                }
            } else {
                toMainPage()
            }
        } else {
            mSplashAD = null
        }

    }

    private fun updateADView(mSplashAD: SplashAD?) {
        if (mSplashAD != null) {
            initKeepTimeView(mSplashAD.keep)
            if (mSplashAD.adv_type == 3) {
                setVideoView(mSplashAD)
            } else {
                imgCover.visibility = View.GONE
                scroll_view.visibility = View.VISIBLE
                scroll_view.isClickable = false
                GlideArt.with(this)
                    .load(mSplashAD.image[0])
                    .placeholder(R.mipmap.img_cover)
                    .apply(requestOptions())
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any,
                            target: Target<Drawable>,
                            isFirstResource: Boolean
                        ): Boolean {
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable,
                            model: Any,
                            target: Target<Drawable>,
                            dataSource: DataSource,
                            isFirstResource: Boolean
                        ): Boolean {
                            img_splash?.let {
                                val layoutParams = it.layoutParams
                                layoutParams.width = screenWidth
                                var height = resource.intrinsicHeight
                                if (resource.intrinsicWidth != screenWidth) {
                                    height = ceil(
                                        resource.intrinsicHeight * (screenWidth * 1f) / resource.intrinsicWidth
                                            .toDouble()
                                    ).toInt()
                                }
                                layoutParams.height = height
                                it.layoutParams = layoutParams
                                scroll_view.scrollTo(0, height)
                                val finalHeight = height
                                scroll_view.post { scroll_view.scrollTo(0, finalHeight) }
                            }

                            return false
                        }
                    }).into(img_splash)
                img_splash.setOnClickListener(this::onAdClick)
            }

        } else {
            toMainPage()
        }
    }

    private fun requestOptions(): RequestOptions {
        return RequestOptions()
            .dontAnimate()
            .override(screenWidth, Target.SIZE_ORIGINAL)
            .skipMemoryCache(true)
            .fitCenter()
            .placeholder(R.color.white)
            .error(R.color.white)
    }

    private fun setVideoView(mSplashAD: SplashAD) {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        fl_video.visibility = View.VISIBLE
        mPlayer = TXVodPlayer(this)
        mPlayer.setRenderRotation(TXLiveConstants.RENDER_ROTATION_PORTRAIT)
        mPlayer.setRenderMode(TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION)
        mPlayer.setVodListener(this)
        mPlayer.setMute(isMute)
        var config = TXVodPlayConfig()
        config.setCacheFolderPath(FileUtil.DIR_VIDEO)
        config.setMaxCacheItems(0)
        mPlayer.setConfig(config)
        mPlayer.setAutoPlay(false)
        mPlayer.setPlayerView(tx_video)
        txPhoneStateListener = TXPhoneStateListener(this, mPlayer)
        txPhoneStateListener?.startListen()
        mPlayer.startPlay(mSplashAD.video_url)
        imgCover.visibility = View.GONE
        img_voice.setOnClickListener {
            isMute != isMute
            mPlayer.setMute(isMute)
            img_voice.isSelected = !isMute
        }
        fl_video.setOnClickListener(this::onAdClick)
    }

    private fun onAdClick(view: View) {
        mSplashAD?.let {
            getPresenter().addAdvClick(Message.obtain(this), it.id)
            if (it.status == 1) {
                toMainPage()
                CardManager.launchAdvlick(
                    it.modelid,
                    it.cid_type,
                    this@SplanActivity,
                    it.cid,
                    it.url,
                    it.title,
                    it.live_type,
                    it.adv_data
                )
            } else {
                if (!TextUtils.isEmpty(it.url)) {
                    toMainPage()
                    CardManager.launchCardClick(this, 3, -1, it.url, it.title)
                }
            }
        }

    }

    @SuppressLint("SetTextI18n")
    private fun initKeepTimeView(keepTime: Int) {
        var countDownTime = Math.max(keepTime, DELAY_KEEP_TIME_MIN)
        tv_jump_ad.apply {
            visibility = View.VISIBLE
            text = "跳过$countDownTime+s"
        }
        tv_jump_ad.setOnClickListener { toMainPage() }
        closeCountDown()
        subscribe = Observable.interval(0, 1000, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                tv_jump_ad?.text = "跳过$countDownTime+s"
                if (countDownTime <= 0) {
                    closeCountDown()
                    toMainPage()
                }
                countDownTime -= 1
            }
    }

    private fun closeCountDown() {
        subscribe?.let {
            if (!it.isDisposed) {
                it.dispose()
            }
        }
        subscribe = null
    }

    @Synchronized
    private fun toMainPage() {
        startActivity<MainTestActivity>()
        finish()
    }

    override fun onPlayEvent(p0: TXVodPlayer, event: Int, param: Bundle) {
        if (event == TXLiveConstants.PLAY_EVT_CHANGE_RESOLUTION) {
            val width: Int = param.getInt(TXLiveConstants.EVT_PARAM1)
            val height: Int = param.getInt(TXLiveConstants.EVT_PARAM2)
            if (width > height) {
            } else {
                mPlayer?.setRenderRotation(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN)
            }
        } else if (event == TXLiveConstants.PLAY_EVT_VOD_PLAY_PREPARED) {
            mPlayer?.resume()
        } else if (event == TXLiveConstants.PLAY_EVT_PLAY_END) {
            toMainPage()
        }
    }

    override fun onNetStatus(p0: TXVodPlayer?, p1: Bundle?) {
    }


    override fun onResume() {
        super.onResume()
        mPlayer?.let {
            it.resume()
            tx_video.onResume()
        }
    }

    override fun onPause() {
        super.onPause()
        mPlayer?.let {
            it.pause()
            tx_video.onPause()
        }
    }

    override fun onDestroy() {
        closeCountDown()
        txPhoneStateListener?.stopListen()
        super.onDestroy()
    }
}