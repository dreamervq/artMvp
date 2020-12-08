package me.jessyan.art.ui.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.mylibrary.mvp.uis.fragment.BaseNewFragment;

import java.util.ArrayList;
import java.util.List;

import me.jessyan.art.R;
import me.jessyan.art.ui.util.PixelUtil;

public class TabStripView extends LinearLayout implements View.OnClickListener {

    private static final String KEY_CURRENT_TAG = "net.arvin.afbaselibrary.widgets.TabStripViewTag";

    private List<ViewHolder> mViewHolderList;
    private OnTabSelectedListener mTabSelectListener;
    private FragmentActivity mFragmentActivity;
    private String mCurrentTag;
    private String mRestoreTag;
    /*主内容显示区域View的id*/
    private int mMainContentLayoutId;
    /*选中的Tab文字颜色*/
    private ColorStateList mSelectedTextColor;
    /*正常的Tab文字颜色*/
    private ColorStateList mNormalTextColor;
    /*Tab文字的颜色*/
    private float mTabTextSize;
    /*默认选中的tab index*/
    private int mDefaultSelectedTab = 0;

    private int mCurrentSelectedTab;
    private BaseNewFragment currentFragment;
    protected TabClickJudgeListener clickJudgeListener;
    private long lastClickTime;

    public TabStripView(Context context) {
        this(context, null);
    }

    public TabStripView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabStripView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TabStripView, 0, 0);

        ColorStateList tabTextColor = typedArray.getColorStateList(R.styleable.TabStripView_navigateTabTextColor);
        ColorStateList selectedTabTextColor = typedArray.getColorStateList(R.styleable.TabStripView_navigateTabSelectedTextColor);

        mTabTextSize = typedArray.getDimensionPixelSize(R.styleable.TabStripView_navigateTabTextSize, 0);
        mMainContentLayoutId = typedArray.getResourceId(R.styleable.TabStripView_containerId, 0);

        mNormalTextColor = (tabTextColor != null ? tabTextColor : context.getResources().getColorStateList(R.color.black_normal));


        if (selectedTabTextColor != null) {
            mSelectedTextColor = selectedTabTextColor;
        } else {
            PixelUtil.checkAppCompatTheme(context);
            TypedValue typedValue = new TypedValue();
            context.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
            mSelectedTextColor = context.getResources().getColorStateList(typedValue.resourceId);
        }

        mViewHolderList = new ArrayList<>();
    }

    public void addTab(Class frameLayoutClass, TabParam tabParam) {
        int defaultLayout = R.layout.ui_layout_navi_tab_view;
//        if (tabParam.tabViewResId > 0) {
//            defaultLayout = tabParam.tabViewResId;
//        }
        if (TextUtils.isEmpty(tabParam.title)) {
            tabParam.title = getContext().getString(tabParam.titleStringRes);
        }

        View view = LayoutInflater.from(getContext()).inflate(defaultLayout, null);
        view.setFocusable(true);

        ViewHolder holder = new ViewHolder();

        holder.tabIndex = mViewHolderList.size();

        holder.fragmentClass = frameLayoutClass;
        holder.tag = tabParam.title;
        holder.pageParam = tabParam;

        holder.tabIcon = (ImageView) view.findViewById(R.id.tab_icon);
        holder.tabTitle = ((TextView) view.findViewById(R.id.tab_title));
        holder.tabUnread = ((TextView) view.findViewById(R.id.tv_msg_num));

        if (TextUtils.isEmpty(tabParam.title)) {
            holder.tabTitle.setVisibility(View.INVISIBLE);
        } else {
            holder.tabTitle.setText(tabParam.title);
        }

        if (mTabTextSize != 0) {
            holder.tabTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTabTextSize);
        }
        if (mNormalTextColor != null) {
            holder.tabTitle.setTextColor(mNormalTextColor);
        }

        if (tabParam.backgroundColor > 0) {
            view.setBackgroundResource(tabParam.backgroundColor);
        }

        if (tabParam.iconResId > 0) {
            holder.tabIcon.setImageResource(tabParam.iconResId);
        } else {
            holder.tabIcon.setVisibility(View.INVISIBLE);
        }

        if (tabParam.iconResId > 0 && tabParam.iconSelectedResId > 0) {
            view.setTag(holder);
            view.setOnClickListener(this);
            mViewHolderList.add(holder);
        }

        addView(view, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0F));

        //聊天Tab的红点右移4dp
//        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.tabUnread.getLayoutParams();
//        if (layoutParams != null) {
//            layoutParams.rightMargin = TextUtils.equals("聊天", holder.tag) ? ScreenUtil.dp2px(-3) : ScreenUtil.dp2px(1);
//            holder.tabUnread.setLayoutParams(layoutParams);
//        }
    }

    public void setClickJudgeListener(TabClickJudgeListener clickJudgeListener) {
        this.clickJudgeListener = clickJudgeListener;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
//        if (mMainContentLayoutId == 0) {
//            throw new RuntimeException("mFrameLayoutId Cannot be 0");
//        }
//        if (mViewHolderList.size() == 0) {
//            throw new RuntimeException("mViewHolderList.size Cannot be 0, Please call addTab()");
//        }
//        if (!(getContext() instanceof FragmentActivity)) {
//            throw new RuntimeException("parent activity must is extends FragmentActivity");
//        }
        try {
            mFragmentActivity = (FragmentActivity) getContext();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ViewHolder defaultHolder = null;

        if (!TextUtils.isEmpty(mRestoreTag)) {
            for (ViewHolder holder : mViewHolderList) {
                if (TextUtils.equals(mRestoreTag, holder.tag)) {
                    defaultHolder = holder;
                    mRestoreTag = null;
                    break;
                }
            }
        } else {
            if (mViewHolderList.size() > 0) {
                defaultHolder = mViewHolderList.get(mDefaultSelectedTab);
            }
        }

        showFragment(defaultHolder);
    }

    @Override
    public void onClick(View v) {
        long curTime = System.currentTimeMillis();
        if (curTime - lastClickTime > 100L) {
            lastClickTime = curTime;
            dealClick(v);
        }
    }

    private synchronized void dealClick(View v) {
        Object object = v.getTag();
        if (object instanceof ViewHolder) {
            ViewHolder holder = (ViewHolder) v.getTag();
            if (clickJudgeListener != null && clickJudgeListener.judge(holder)) {
                return;
            }
//            if ("服务".equals(holder.tag)) {
//                HermesEventBus.getDefault().post(new JumpToServiceEntity());
//            } else {
//                showFragment(holder);
//            }
            showFragment(holder);
        }
    }


    public boolean isExist(String tag) {
        return mFragmentActivity.getSupportFragmentManager().findFragmentByTag(tag) != null;
    }


    public void createFragmentByTag(Fragment fragment, String tag) {
        Fragment f = mFragmentActivity.getSupportFragmentManager().findFragmentByTag(tag);
        if (f == null) {
            FragmentTransaction transaction = mFragmentActivity.getSupportFragmentManager().beginTransaction();
            transaction.add(mMainContentLayoutId, fragment, tag);
            transaction.hide(fragment);
            transaction.commitAllowingStateLoss();
        }
    }

    /**
     * 显示 holder 对应的 fragment
     */
    protected synchronized void showFragment(ViewHolder holder) {
        if (holder == null) {
            return;
        }
        FragmentTransaction transaction = mFragmentActivity.getSupportFragmentManager().beginTransaction();

        if (isFragmentShown(transaction, holder.tag)) {
            return;
        }
        hideAllFragment();
        setCurrSelectedTabByTag(holder.tag);
        Fragment fragment = mFragmentActivity.getSupportFragmentManager().findFragmentByTag(holder.tag);
        if (fragment == null) {

            fragment = getFragmentInstance(holder.tag);
            transaction.add(mMainContentLayoutId, fragment, holder.tag);
            fragment.setUserVisibleHint(true);
        } else {
            transaction.show(fragment);
            fragment.setUserVisibleHint(true);
        }
        currentFragment = (BaseNewFragment) fragment;
        transaction.commitAllowingStateLoss();
        mCurrentSelectedTab = holder.tabIndex;
        if (mTabSelectListener != null) {
            mTabSelectListener.onTabSelected(holder);
        }
    }

    public BaseNewFragment getCurrentFragment(String tag) {
        return currentFragment;
    }

    private boolean isFragmentShown(FragmentTransaction transaction, String newTag) {
        if (TextUtils.equals(newTag, mCurrentTag)) {
            return true;
        }

        if (TextUtils.isEmpty(mCurrentTag)) {
            return false;
        }

        Fragment fragment = mFragmentActivity.getSupportFragmentManager().findFragmentByTag(mCurrentTag);
        if (fragment != null && !fragment.isHidden()) {
            transaction.hide(fragment);
        }

        return false;
    }

    /*设置当前选中tab的图片和文字颜色*/
    private void setCurrSelectedTabByTag(String tag) {
        if (TextUtils.equals(mCurrentTag, tag)) {
            return;
        }
        for (ViewHolder holder : mViewHolderList) {
            if (TextUtils.equals(mCurrentTag, holder.tag)) {
                holder.tabIcon.setImageResource(holder.pageParam.iconResId);
                holder.tabTitle.setTextColor(mNormalTextColor);
            } else if (TextUtils.equals(tag, holder.tag)) {
                holder.tabIcon.setImageResource(holder.pageParam.iconSelectedResId);
                holder.tabTitle.setTextColor(mSelectedTextColor);
            }
        }
        mCurrentTag = tag;
    }


    private Fragment getFragmentInstance(String tag) {
        Fragment fragment = null;
        for (ViewHolder holder : mViewHolderList) {
            if (TextUtils.equals(tag, holder.tag)) {
                try {
                    fragment = (Fragment) Class.forName(holder.fragmentClass.getName()).newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        return fragment;
    }

    private void hideAllFragment() {
        if (mViewHolderList == null || mViewHolderList.size() == 0) {
            return;
        }
        FragmentTransaction transaction = mFragmentActivity.getSupportFragmentManager().beginTransaction();

        for (ViewHolder holder : mViewHolderList) {
            Fragment fragment = mFragmentActivity.getSupportFragmentManager().findFragmentByTag(holder.tag);
            if (fragment != null && !fragment.isHidden()) {
                transaction.hide(fragment);
                fragment.setUserVisibleHint(false);
            }
        }
        transaction.commitAllowingStateLoss();
    }

    public void setSelectedTabTextColor(ColorStateList selectedTextColor) {
        mSelectedTextColor = selectedTextColor;
    }

    public void setSelectedTabTextColor(int color) {
        mSelectedTextColor = ColorStateList.valueOf(color);
    }

    public void setTabTextColor(ColorStateList color) {
        mNormalTextColor = color;
    }

    public void setTabTextColor(int color) {
        mNormalTextColor = ColorStateList.valueOf(color);
    }

    public void setFrameLayoutId(int frameLayoutId) {
        mMainContentLayoutId = frameLayoutId;
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mRestoreTag = savedInstanceState.getString(KEY_CURRENT_TAG);
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putString(KEY_CURRENT_TAG, mCurrentTag);
    }

    @SuppressWarnings("WeakerAccess")
    public static class ViewHolder {
        public String tag;
        public TabParam pageParam;
        public ImageView tabIcon;
        public TextView tabTitle;
        public TextView tabUnread;
        public Class fragmentClass;
        public int tabIndex;
    }


    @SuppressWarnings("WeakerAccess")
    public static class TabParam {
        public int backgroundColor = android.R.color.white;
        public int iconResId;
        public int iconSelectedResId;
        public int titleStringRes;
        //        public int tabViewResId;
        public String title;

        public TabParam(int iconResId, int iconSelectedResId, String title) {
            this.iconResId = iconResId;
            this.iconSelectedResId = iconSelectedResId;
            this.title = title;
        }

        public TabParam(int iconResId, int iconSelectedResId, int titleStringRes) {
            this.iconResId = iconResId;
            this.iconSelectedResId = iconSelectedResId;
            this.titleStringRes = titleStringRes;
        }

        public TabParam(int backgroundColor, int iconResId, int iconSelectedResId, int titleStringRes) {
            this.backgroundColor = backgroundColor;
            this.iconResId = iconResId;
            this.iconSelectedResId = iconSelectedResId;
            this.titleStringRes = titleStringRes;
        }

        public TabParam(int backgroundColor, int iconResId, int iconSelectedResId, String title) {
            this.backgroundColor = backgroundColor;
            this.iconResId = iconResId;
            this.iconSelectedResId = iconSelectedResId;
            this.title = title;
        }
    }


    public interface OnTabSelectedListener {
        void onTabSelected(ViewHolder holder);
    }

    public void setTabSelectListener(OnTabSelectedListener tabSelectListener) {
        mTabSelectListener = tabSelectListener;
    }

    public void setDefaultSelectedTab(int index) {
        if (index >= 0 && index < mViewHolderList.size()) {
            mDefaultSelectedTab = index;
        }
    }

    public void setCurrentSelectedTab(int index) {
        if (index >= 0 && index < mViewHolderList.size()) {
            ViewHolder holder = mViewHolderList.get(index);
            showFragment(holder);
        }
    }

    public void setTabUnread(int index, long unreadNumber) {
        if (index >= 0 && index < mViewHolderList.size()) {
            ViewHolder holder = mViewHolderList.get(index);
//            holder.tabUnread.setVisibility(unreadNumber > 0L ? View.VISIBLE : View.GONE);
            if (unreadNumber < 1L) {
                holder.tabUnread.setVisibility(View.GONE);
                holder.tabUnread.setText("");
            } else if (unreadNumber < 100L) {
                holder.tabUnread.setVisibility(View.VISIBLE);
                holder.tabUnread.setText(String.valueOf(unreadNumber));
            } else {
                holder.tabUnread.setVisibility(View.VISIBLE);
                holder.tabUnread.setText("99+");
            }
        }
    }

    public void setPublishFailed(int index, long failedNumber) {
        setTabUnread(index, failedNumber);
    }

    public String getTagByPos(int index) {
        String result = null;
        if (index >= 0 && index < mViewHolderList.size()) {
            ViewHolder holder = mViewHolderList.get(index);
            result = holder.tag;
        }
        return result;
    }

    public int getCurrentSelectedTab() {
        return mCurrentSelectedTab;
    }

    public interface TabClickJudgeListener {
        boolean judge(ViewHolder holder);
    }

}