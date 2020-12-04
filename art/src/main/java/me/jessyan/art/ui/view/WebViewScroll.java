package me.jessyan.art.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

import com.scwang.smart.refresh.layout.SmartRefreshLayout;

public class WebViewScroll extends WebView {

    private SmartRefreshLayout swipeRefreshLayout;

    public WebViewScroll(Context context) {
        super(context);
    }

    public WebViewScroll(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WebViewScroll(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setSwipeRefreshLayout(SmartRefreshLayout swipeRefreshLayout) {
        this.swipeRefreshLayout = swipeRefreshLayout;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (swipeRefreshLayout == null) {
            return;
        }
        if (this.getScrollY() == 0) {
            swipeRefreshLayout.setEnabled(true);
        } else {
            swipeRefreshLayout.setEnabled(false);
        }
    }
}
