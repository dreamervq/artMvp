package com.example.mydemok.weiget;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;

import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;


import com.example.mydemok.R;
import com.example.mydemok.utils.AppUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.jessyan.art.ui.util.Utils;


public class AutoScrollViewPager extends ViewPager {

	private   static final int DEFAULT_TIME = 3000;
	private int scrollTime = DEFAULT_TIME;

	public void setScrollTime(int scrollTime) {
		this.scrollTime = scrollTime;
	}

	public AutoScrollViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		timer = new Timer();
		setSmooth();
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		if(onScollChangeListener != null){
			onScollChangeListener.scroll(l);
		}
	}
	private void setSmooth(){
		try {
			Class clazz= Class.forName("androidx.viewpager.widget.ViewPager");
			Field f=clazz.getDeclaredField("mScroller");
			FixedSpeedScroller fixedSpeedScroller=new FixedSpeedScroller(getContext(),new LinearOutSlowInInterpolator());
			fixedSpeedScroller.setmDuration(800);
			f.setAccessible(true);
			f.set(this,fixedSpeedScroller);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private boolean isScroll;

	public void setIsScroll(boolean scroll) {
		isScroll = scroll;
		if (isScroll)
			startSliding();
	}

	private Timer timer;
	private TimerTask task;
	private Handler handler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			setCurrentItem(getCurrentItem()+1,true);
			return false;
		}
	});

	private OnScollChangeListener onScollChangeListener;
	public void setOnScollChangeListener(OnScollChangeListener onScollChangeListener) {
		this.onScollChangeListener = onScollChangeListener;
	}

	public void setAdapter(PagerAdapter imageAdapter, int  b) {
		super.setAdapter(imageAdapter);
		setCurrentItem(b,false);
	}

	public interface OnScollChangeListener{
		void scroll(int x);
	}

	public void setAdapter(PagerAdapter adapter){
		super.setAdapter(adapter);
		setCurrentItem(0,false);
	}
//	public void setAdapter(Adapter adapter){
//		super.setAdapter(adapter);
//		setCurrentItem(0,false);
//	}
	public void startSliding(){
		if (!isScroll)
			return;
		if(task == null){
			task = new TimerTask() {

				@Override
				public void run() {
					handler.sendEmptyMessage(0);
				}
			};
			timer.schedule(task, scrollTime, scrollTime);
		}
		Activity activity = AppUtil.findActivity(getContext());
		if (getContext() == null || (activity != null && activity.isFinishing())) {
			stopSliding();
		}
	}

	public void stopSliding(){
		if (!isScroll)
			return;
		handler.removeMessages(0);
		if(task != null){
			task.cancel();
			task = null;
		}
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				stopSliding();
				getParent().requestDisallowInterceptTouchEvent(false);
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				startSliding();
				getParent().requestDisallowInterceptTouchEvent(true);
				break;
		}
		return super.onInterceptTouchEvent(ev);
	}
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				startSliding();
				getParent().requestDisallowInterceptTouchEvent(true);

				break;
		}
		return super.onTouchEvent(ev);
	}

}
