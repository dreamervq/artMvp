package me.jessyan.art.ui.listeners;

import android.view.View;

@Deprecated
public abstract class OnClickWithObjects implements View.OnClickListener {
    private Object[] objects;

    public OnClickWithObjects(Object... objects) {
        this.objects = objects;
    }

    @Override
    public void onClick(View view) {
        onClick(view, objects);
    }

    public abstract void onClick(View view, Object[] objects);
}
