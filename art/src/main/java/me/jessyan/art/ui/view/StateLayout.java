package me.jessyan.art.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.ybq.android.spinkit.style.Wave;

import me.jessyan.art.R;
import me.jessyan.art.ui.util.PixelUtil;
import me.jessyan.art.ui.view.emptyprovider.ViewAnimProvider;


public class StateLayout extends FrameLayout {

    private View contentView;

    private View emptyView;
    private View emptyContentView;

    private View errorView;
    private View errorContentView;

    private View progressView;
    private View progressContentView;

    private TextView emptyTextView;
    private TextView errorTextView;
    private TextView progressTextView;

    private TextView emptyTipView;

    private ImageView errorImageView;
    private ImageView emptyImageView;
    private ProgressBar progressBar;

    private View currentShowingView;


    public StateLayout(Context context) {
        this(context, null);
    }


    public StateLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public StateLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        parseAttrs(context, attrs);

        emptyView.setVisibility(View.GONE);

        errorView.setVisibility(View.GONE);

        progressView.setVisibility(View.GONE);

        currentShowingView = contentView;
    }

    public void setViewTop() {
        ((LinearLayout) emptyView).setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
        emptyView.setPadding(0, PixelUtil.dp2px(120), 0, 0);
        ((LinearLayout) errorView).setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
        errorView.setPadding(0, PixelUtil.dp2px(120), 0, 0);
        ((LinearLayout) progressView).setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
        progressView.setPadding(0, PixelUtil.dp2px(120), 0, 0);
    }

    private void parseAttrs(Context context, AttributeSet attrs) {
        LayoutInflater inflater = LayoutInflater.from(context);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.StateLayout, 0, 0);
        int progressViewId;
        Drawable errorDrawable;
        Drawable emptyDrawable;
        try {
            errorDrawable = a.getDrawable(R.styleable.StateLayout_errorDrawable);
            emptyDrawable = a.getDrawable(R.styleable.StateLayout_emptyDrawable);
            progressViewId = a.getResourceId(R.styleable.StateLayout_progressView, -1);
        } finally {
            a.recycle();
        }

        /******************************************************************************************/

        if (progressViewId != -1) {
            progressView = inflater.inflate(progressViewId, this, false);
        } else {
            progressView = inflater.inflate(R.layout.view_progress, this, false);
            progressBar = (ProgressBar) progressView.findViewById(R.id.progress_wheel);
            progressTextView = (TextView) progressView.findViewById(R.id.progressTextView);
            progressContentView = progressView.findViewById(R.id.progress_content);
            Wave doubleBounce = new Wave();
            doubleBounce.setColor(getContext().getResources().getColor(R.color.colorAccent));
            progressBar.setIndeterminateDrawable(doubleBounce);
        }

        addView(progressView);
        /******************************************************************************************/
        /******************************************************************************************/

        errorView = inflater.inflate(R.layout.view_error, this, false);
        errorContentView = errorView.findViewById(R.id.error_content);
        errorTextView = (TextView) errorView.findViewById(R.id.errorTextView);
        errorImageView = (ImageView) errorView.findViewById(R.id.errorImageView);
        if (errorDrawable != null) {
            errorImageView.setImageDrawable(errorDrawable);
        } else {
            errorImageView.setImageResource(R.drawable.ic_state_layout_error);
        }
        addView(errorView);
        /******************************************************************************************/
        /******************************************************************************************/

        emptyView = inflater.inflate(R.layout.view_empty, this, false);
        emptyContentView = emptyView.findViewById(R.id.empty_content);
        emptyTextView = (TextView) emptyView.findViewById(R.id.emptyTextView);
        emptyTipView = emptyView.findViewById(R.id.tip);
        emptyImageView = (ImageView) emptyView.findViewById(R.id.emptyImageView);
        if (emptyDrawable != null) {
            emptyImageView.setImageDrawable(emptyDrawable);
        } else {
            /**
             * 暂无数据时的图片
             */
            emptyImageView.setImageResource(R.drawable.img_empty_view);
//            emptyImageView.setImageResource(R.drawable.img_empty_statelayout);
        }
        addView(emptyView);
        /******************************************************************************************/

    }

    private void checkIsContentView(View view) {
        if (contentView == null && view != errorView && view != progressView && view != emptyView) {
            contentView = view;
            currentShowingView = contentView;
        }
    }

    public ImageView getErrorImageView() {
        return errorImageView;
    }

    public ImageView getEmptyImageView() {
        return emptyImageView;
    }

    public TextView getEmptyTextView(){
        return emptyTextView;
    }
    public TextView getEmptyTipView(){
        return emptyTipView;
    }

    public void setViewSwitchAnimProvider(ViewAnimProvider viewSwitchAnimProvider) {
        if (viewSwitchAnimProvider != null) {
            this.showAnimation = viewSwitchAnimProvider.showAnimation();
            this.hideAnimation = viewSwitchAnimProvider.hideAnimation();
        }
    }


    public boolean isShouldPlayAnim() {
        return shouldPlayAnim;
    }

    public void setShouldPlayAnim(boolean shouldPlayAnim) {
        this.shouldPlayAnim = shouldPlayAnim;
    }

    private boolean shouldPlayAnim = true;
    private Animation hideAnimation;
    private Animation showAnimation;

    public Animation getShowAnimation() {
        return showAnimation;
    }

    public void setShowAnimation(Animation showAnimation) {
        this.showAnimation = showAnimation;
    }

    public Animation getHideAnimation() {
        return hideAnimation;
    }

    public void setHideAnimation(Animation hideAnimation) {
        this.hideAnimation = hideAnimation;
    }

    private void switchWithAnimation(final View toBeShown) {
        final View toBeHided = currentShowingView;
        if (toBeHided == toBeShown)
            return;
        if (shouldPlayAnim) {
            if (toBeHided != null) {
                if (hideAnimation != null) {
                    hideAnimation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            toBeHided.setVisibility(GONE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    hideAnimation.setFillAfter(false);
                    toBeHided.startAnimation(hideAnimation);
                } else
                    toBeHided.setVisibility(GONE);
            }
            if (toBeShown != null) {
                if (toBeShown.getVisibility() != VISIBLE)
                    toBeShown.setVisibility(VISIBLE);
                currentShowingView = toBeShown;
                if (showAnimation != null) {
                    showAnimation.setFillAfter(false);
                    toBeShown.startAnimation(showAnimation);
                }
            }
        } else {
            if (toBeHided != null) {
                toBeHided.setVisibility(GONE);
            }
            if (toBeShown != null) {
                currentShowingView = toBeShown;
                toBeShown.setVisibility(VISIBLE);
            }
        }

    }

    public void setEmptyContentViewMargin(int left, int top, int right, int bottom) {
        ((LinearLayout.LayoutParams) emptyImageView.getLayoutParams()).setMargins(left, top, right, bottom);
    }

    public void setErrorContentViewMargin(int left, int top, int right, int bottom) {
        ((LinearLayout.LayoutParams) errorImageView.getLayoutParams()).setMargins(left, top, right, bottom);
    }

    public void setProgressContentViewMargin(int left, int top, int right, int bottom) {
        if (progressBar != null)
            ((LinearLayout.LayoutParams) progressBar.getLayoutParams()).setMargins(left, top, right, bottom);
    }

    public void setInfoContentViewMargin(int left, int top, int right, int bottom) {
        setEmptyContentViewMargin(left, top, right, bottom);
        setErrorContentViewMargin(left, top, right, bottom);
        setProgressContentViewMargin(left, top, right, bottom);
    }


    public void showContentView() {
        contentView.setVisibility(VISIBLE);
        errorView.setVisibility(GONE);
        progressView.setVisibility(GONE);
        emptyView.setVisibility(GONE);
        switchWithAnimation(contentView);
    }

    public void showEmptyView() {
        showEmptyView(null);
    }

    public void showEmptyView(String msg) {
        emptyView.setVisibility(VISIBLE);
        onHideContentView();
        errorView.setVisibility(GONE);
        progressView.setVisibility(GONE);
        if (!TextUtils.isEmpty(msg))
            emptyTextView.setText(msg);
        View v = emptyView.findViewById(R.id.tip);
        if (v != null && v.getVisibility() == GONE) {
            v.setVisibility(VISIBLE);
        }
        switchWithAnimation(emptyView);
    }

    public void showEmptyView(String msg, String tip) {
        emptyView.setVisibility(VISIBLE);
        onHideContentView();
        errorView.setVisibility(GONE);
        progressView.setVisibility(GONE);
        if (!TextUtils.isEmpty(msg))
            emptyTextView.setText(msg);
        View v = emptyView.findViewById(R.id.tip);
        if (v != null && v.getVisibility() == GONE) {
            v.setVisibility(VISIBLE);
        }
        switchWithAnimation(emptyView);
    }

    public void setEmptyViewTip(CharSequence tip) {
        if (emptyTipView != null) {
            emptyTipView.setText(TextUtils.isEmpty(tip) ? "轻触屏幕重试" : tip);
        }
    }

    public void showEmptyView(String msg, boolean isShowTip) {
        emptyView.setVisibility(VISIBLE);
        onHideContentView();
        errorView.setVisibility(GONE);
        progressView.setVisibility(GONE);
        if (!TextUtils.isEmpty(msg))
            emptyTextView.setText(msg);
        View v = emptyView.findViewById(R.id.tip);
        if (!isShowTip) {
            if (v != null) {
                v.setVisibility(GONE);
            }
        } else {
            if (v != null) {
                v.setVisibility(VISIBLE);
            }
        }
        switchWithAnimation(emptyView);
    }

    public boolean hasNet(Context context) {
        boolean result = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isAvailable()) {
                int type2 = networkInfo.getType();
                switch (type2) {
                    case ConnectivityManager.TYPE_MOBILE://移动 网络    2G 3G 4G 都是一样的 实测 mix2s 联通卡
                    case ConnectivityManager.TYPE_WIFI: //wifi网络
                    case ConnectivityManager.TYPE_ETHERNET:  //网线连接
                        result = true;
                        break;
                }
            }
        }
        return result;
    }

    public void showErrorView() {
        showErrorView(null);
    }

    public void showErrorView(String msg) {
        onHideContentView();
        emptyView.setVisibility(GONE);
        progressView.setVisibility(GONE);
        boolean hasNet = hasNet(getContext());
        errorImageView.setImageResource(hasNet ? R.drawable.img_error_view : R.drawable.img_net_view);
        errorView.setVisibility(VISIBLE);
        if (msg != null) {
            errorTextView.setText(msg);
        } else {
            emptyTextView.setText(hasNet ? R.string.error : R.string.no_net);
        }
        switchWithAnimation(errorView);
    }

    public void showProgressView() {
        showProgressView(null);
    }

    public void showProgressView(String msg) {
        onHideContentView();
        emptyView.setVisibility(GONE);
        errorView.setVisibility(GONE);
        progressView.setVisibility(VISIBLE);
        if (msg != null)
            progressTextView.setText(msg);
        switchWithAnimation(progressView);
    }

    public void setErrorAction(final OnClickListener onErrorButtonClickListener) {
        errorView.setOnClickListener(onErrorButtonClickListener);
    }

    public void setEmptyAction(final OnClickListener onEmptyButtonClickListener) {
        emptyView.setOnClickListener(onEmptyButtonClickListener);
    }

    public void setEmptyTipAction(OnClickListener onEmptyTipClickListener) {
        emptyTipView.setOnClickListener(onEmptyTipClickListener);
    }


    public void setErrorAndEmptyAction(final OnClickListener errorAndEmptyAction) {
        errorView.setOnClickListener(errorAndEmptyAction);
        emptyView.setOnClickListener(errorAndEmptyAction);
    }

    protected void onHideContentView() {
        //Override me
        contentView.setVisibility(GONE);
    }


    /**
     * addView
     */

    @Override
    public void addView(View child) {
        checkIsContentView(child);
        super.addView(child);
    }

    @Override
    public void addView(View child, int index) {
        checkIsContentView(child);
        super.addView(child, index);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        checkIsContentView(child);
        super.addView(child, index, params);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        checkIsContentView(child);
        super.addView(child, params);
    }

    @Override
    public void addView(View child, int width, int height) {
        checkIsContentView(child);
        super.addView(child, width, height);
    }

    @Override
    protected boolean addViewInLayout(View child, int index, ViewGroup.LayoutParams params) {
        checkIsContentView(child);
        return super.addViewInLayout(child, index, params);
    }

    @Override
    protected boolean addViewInLayout(View child, int index, ViewGroup.LayoutParams params, boolean preventRequestLayout) {
        checkIsContentView(child);
        return super.addViewInLayout(child, index, params, preventRequestLayout);
    }
}
