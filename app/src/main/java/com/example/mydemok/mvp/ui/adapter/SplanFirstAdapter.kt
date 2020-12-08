package com.example.mydemok.mvp.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.example.mydemok.R
import com.example.mydemok.application.Constants
import com.example.mydemok.mvp.ui.activity.SplanActivity
import me.jessyan.art.utils.TinyPref

class SplanFirstAdapter(context: SplanActivity) : PagerAdapter() {
    private var context: SplanActivity? = null
    private val mImages = arrayOf<Int>(
        R.color.colorAccent,
        R.color.colorPrimary
//        R.mipmap.splan_3,
//        R.mipmap.splan_4,
//        R.mipmap.splan_5
    )

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view: View
        view = LayoutInflater.from(context).inflate(R.layout.vw_image, container, false)
        val bgImage = view.findViewById<View>(R.id.iv_image) as ImageView
        bgImage.setImageResource(mImages[position])
        if (position == mImages.size - 1) {
            bgImage.setOnClickListener {
                context?.startActivity(Intent(context, MainTestActivity::class.java))
                TinyPref.getInstance().putBoolean(Constants.PREF_FIRST, true)
                context?.finish()
            }
        }
        container.addView(view)
        return view
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }


    override fun getCount(): Int {
        return mImages.size
    }

    init {
        this.context = context
    }
}