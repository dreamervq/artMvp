/*
 * Copyright 2017 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.jessyan.art.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import me.jessyan.art.base.delegate.IFragment;
import me.jessyan.art.integration.cache.Cache;
import me.jessyan.art.integration.cache.CacheType;
import me.jessyan.art.mvp.IPresenter;
import me.jessyan.art.utils.ArtUtils;

/**
 * ================================================
 * 因为 Java 只能单继承, 所以如果要用到需要继承特定 @{@link Fragment} 的三方库, 那你就需要自己自定义 @{@link Fragment}
 * 继承于这个特定的 @{@link Fragment}, 然后再按照 {@link BaseFragment} 的格式, 将代码复制过去, 记住一定要实现{@link IFragment}
 *
 * @see <a href="https://github.com/JessYanCoding/MVPArms/wiki">请配合官方 Wiki 文档学习本框架 (Arms 的文档除了 MVP 部分, 其他的文档内容 Art 和 Arms 都可以共用)</a>
 * @see <a href="https://github.com/JessYanCoding/MVPArms/wiki/Issues">常见 Issues, 踩坑必看!</a>
 * Created by JessYan on 22/03/2016
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public abstract class BaseFragment<P extends IPresenter> extends Fragment implements IFragment<P> {
    protected final String TAG = this.getClass().getSimpleName();
    private Cache<String, Object> mCache;
    protected Context mContext;
    protected P mPresenter;

    protected void shortToast(int resId) {
        ArtUtils.makeText(getContext(),getString(resId) );
    }
    protected void shortToast(String msg) {
        ArtUtils.makeText(getContext(),msg );

    }
    /**
     * 是否可见状态 为了避免和{@link Fragment#isVisible()}冲突 换个名字
     */
    private boolean isFragmentVisible;
    /**
     * 标志位，View已经初始化完成。
     */
    private boolean isPrepared;
    /**
     * 是否第一次加载
     */
    private boolean isFirstLoad = true;
    private boolean forceLoad = false;
    private String fragmentTitle;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null && bundle.size() > 0) {
            initVariables(bundle);
        }
    }
    public void initVariables(Bundle bundle) {}
    public boolean isPrepared() {
        return isPrepared;
    }
    public boolean isFirstLoad() {
        return isFirstLoad;
    }
    public void setForceLoad(boolean forceLoad) {
        this.forceLoad = forceLoad;
    }

    public boolean isFragmentVisible() {
        return isFragmentVisible;
    }
    @NonNull
    @Override
    public synchronized Cache<String, Object> provideCache() {
        if (mCache == null) {
            mCache = ArtUtils.obtainAppComponentFromContext(getActivity()).cacheFactory().build(CacheType.FRAGMENT_CACHE);
        }
        return mCache;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    /**
     * 如果是与ViewPager一起使用，调用的是setUserVisibleHint
     *
     * @param isVisibleToUser 是否显示出来了
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            onVisible();
        } else {
            onInvisible();
        }
    }

    /**
     * 如果是通过FragmentTransaction的show和hide的方法来控制显示，调用的是onHiddenChanged.
     * 若是初始就show的Fragment 为了触发该事件 需要先hide再show
     *
     * @param hidden hidden True if the fragment is now hidden, false if it is not
     * visible.
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            onVisible();
        } else {
            onInvisible();
        }
    }

    protected void onVisible() {
        isFragmentVisible = true;
        lazyLoad();
    }
    protected  View rootView;

    protected void onInvisible() {
        isFragmentVisible = false;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = initView(inflater, container, savedInstanceState);
        isFirstLoad = true;
        isPrepared = true;
        lazyLoad();
        return rootView;
    }
    protected void lazyLoad( ) {
        if (isPrepared() && isFragmentVisible()) {
            if (forceLoad || isFirstLoad()) {
                forceLoad = false;
                isFirstLoad = false;
                initData( );
            }
        }
    }
    @Override
    public void setPresenter(@Nullable P presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (mPresenter == null) {
            mPresenter = obtainPresenter();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isPrepared = false;
    }

    /**
     * 是否使用 EventBus
     * Art 核心库现在并不会依赖某个 EventBus, 要想使用 EventBus, 还请在项目中自行依赖对应的 EventBus
     * 现在支持两种 EventBus, greenrobot 的 EventBus 和畅销书 《Android源码设计模式解析与实战》的作者 何红辉 所作的 AndroidEventBus
     * 确保依赖后, 将此方法返回 true, Art 会自动检测您依赖的 EventBus, 并自动注册
     * 这种做法可以让使用者有自行选择三方库的权利, 并且还可以减轻 Art 的体积
     *
     * @return 返回 {@code true} (默认为 {@code true}), Art 会自动注册 EventBus
     */
    @Override
    public boolean useEventBus() {
        return false;
    }


    /**
     * 设置fragment的Title直接调用 {@link BaseFragment#setTitle(String)},若不显示该title 可以不做处理
     *
     * @param title 一般用于显示在TabLayout的标题
     */



}
