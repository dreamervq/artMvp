package me.jessyan.art.ui.adapters;


import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.mylibrary.listeners.ITabContent;
import com.example.mylibrary.listeners.ITabPager;

import java.util.List;

import me.jessyan.art.ui.util.lazyViewPager.LazyFragmentPagerAdapter;

public class TabPagerAdapter<T extends ITabPager> extends LazyFragmentPagerAdapter {
    private List<T> mList;
    private ITabContent tabContent;
    private Fragment mCurrentFragment;

    public TabPagerAdapter(FragmentManager fm, List<T> list, ITabContent tabContent) {
        super(fm);
        this.mList = list;
        this.tabContent = tabContent;
    }

    public void setList(List<T> mList) {
        this.mList = mList;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mList.get(position).getTitle();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

//    @Override
//    public Fragment getItem(int position) {
//        return tabContent.getContent(position);
//    }

    @Override
    protected Fragment getItem(ViewGroup container, int position) {
        return tabContent.getContent(position);
    }

    //----------------------下面才是重点-----------------

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        Fragment fragment = (Fragment) object;
        if (fragment != mCurrentFragment) {
            if (mCurrentFragment != null) {
                mCurrentFragment.setMenuVisibility(false);
                mCurrentFragment.setUserVisibleHint(false);
            }
            fragment.setMenuVisibility(true);
            fragment.setUserVisibleHint(true);
            mCurrentFragment = fragment;
        }
        super.setPrimaryItem(container, position, object);
    }


    public Fragment getCurrentFragment() {
        return mCurrentFragment;
    }
}
