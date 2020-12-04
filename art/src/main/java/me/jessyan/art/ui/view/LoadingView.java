package me.jessyan.art.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import me.jessyan.art.R;


public class LoadingView extends FrameLayout {

    private ProgressBar progressBar;
    private TextView tv;
    private ImageView iv;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.loading_view, this, true);
        progressBar = view.findViewById(R.id.progressBar);
        tv = findViewById(R.id.tv);
        iv = findViewById(R.id.iv);
    }

    /**
     * loading
     */
    public void showLoading() {
        iv.setVisibility(GONE);
        progressBar.setVisibility(VISIBLE);
    }

    /**
     * 成功
     */
    public void showSuccess() {
        iv.setImageResource(R.drawable.load_success);
        iv.setVisibility(View.VISIBLE);
        progressBar.setVisibility(GONE);
    }

    /**
     * 失败
     */
    public void showFail() {
        iv.setImageResource(R.drawable.load_fail);
        iv.setVisibility(View.VISIBLE);
        progressBar.setVisibility(GONE);
    }

    /**
     * 提示文字
     *
     * @param txt string
     */
    public void setText(String txt) {
        tv.setText(txt);
    }

    /**
     * 提示文字
     */
    public void setText(@StringRes int txtId) {
        tv.setText(txtId);
    }

    public void setTextVisible(boolean visible) {
        tv.setVisibility(visible ? VISIBLE : GONE);
    }
}
