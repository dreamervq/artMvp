package me.jessyan.art.ui.listeners;

import android.view.View;

@Deprecated
public abstract class SingleOnclickListener<T> implements View.OnClickListener {

    private T target;

    public SingleOnclickListener(T t) {
        this.target = t;
    }

    @Override
    public void onClick(View view) {
        onSingleClick(view, target);
    }

    public abstract void onSingleClick(View view, T t);

}
