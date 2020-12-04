package me.jessyan.art.ui.adapters;


import com.example.mylibrary.mvp.uis.adapters.holders.CommonHolder;

public interface ItemViewDelegate<T>
{

    int getItemViewLayoutId();

    boolean isForViewType(T item, int position);

    void convert(CommonHolder holder, T t, int position);


}
