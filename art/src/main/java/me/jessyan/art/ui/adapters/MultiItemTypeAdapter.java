package me.jessyan.art.ui.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;


import com.example.mylibrary.mvp.uis.adapters.holders.CommonHolder;

import java.util.List;

public class MultiItemTypeAdapter<T> extends RecyclerView.Adapter<CommonHolder> {
    protected Context mContext;
    protected List<T> mItems;

    protected ItemViewDelegateManager mItemViewDelegateManager;
    protected OnItemClickListener<T> mOnItemClickListener;
    protected OnItemLongClickListener<T> mOnItemLongClickListener;


    public MultiItemTypeAdapter(Context context, List<T> items) {
        mContext = context;
        mItems = items;
        mItemViewDelegateManager = new ItemViewDelegateManager();
    }

    @Override
    public int getItemViewType(int position) {
        if (!useItemViewDelegateManager()) return super.getItemViewType(position);
        return mItemViewDelegateManager.getItemViewType(mItems.get(position), position);
    }


    @Override
    public CommonHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = mItemViewDelegateManager.getItemViewLayoutId(viewType);
        CommonHolder holder = CommonHolder.createViewHolder(mContext, parent, layoutId);
        return holder;
    }

    public void convert(CommonHolder holder, T t) {
        mItemViewDelegateManager.convert(holder, t, holder.getAdapterPosition());
    }

    protected boolean isEnabled(int viewType) {
        return true;
    }

    protected void setListener(final CommonHolder viewHolder, int viewType) {
        if (!isEnabled(viewType)) return;
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    int position = viewHolder.getAdapterPosition();
                    mOnItemClickListener.onItemClick(v, viewHolder, (position >= 0 && position < mItems.size()) ? mItems.get(position) : null, position);
                }
            }
        });

        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemLongClickListener != null) {
                    int position = viewHolder.getAdapterPosition();
                    return mOnItemLongClickListener.onItemLongClick(v, viewHolder, (position >= 0 && position < mItems.size()) ? mItems.get(position) : null, position);
                }
                return false;
            }
        });
    }

    public OnItemClickListener<T> getmOnItemClickListener() {
        return mOnItemClickListener;
    }

    public OnItemLongClickListener<T> getmOnItemLongClickListener() {
        return mOnItemLongClickListener;
    }

    @Override
    public void onBindViewHolder(CommonHolder holder, int position) {
        setListener(holder, getItemViewType(position));
        convert(holder, mItems.get(position));
    }

    @Override
    public int getItemCount() {
        int itemCount = 0;
        if (mItems != null) {
            return mItems.size();
        }
        return itemCount;
    }


    public List<T> getItems() {
        return mItems;
    }

    public MultiItemTypeAdapter addItemViewDelegate(ItemViewDelegate<T> itemViewDelegate) {
        mItemViewDelegateManager.addDelegate(itemViewDelegate);
        return this;
    }

    public MultiItemTypeAdapter addItemViewDelegate(int viewType, ItemViewDelegate<T> itemViewDelegate) {
        mItemViewDelegateManager.addDelegate(viewType, itemViewDelegate);
        return this;
    }

    protected boolean useItemViewDelegateManager() {
        return mItemViewDelegateManager.getItemViewDelegateCount() > 0;
    }

    public interface OnItemClickListener<T> {
        void onItemClick(View view, RecyclerView.ViewHolder holder, T item, int position);
    }

    public interface OnItemLongClickListener<T> {
        boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, T item, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.mOnItemLongClickListener = onItemLongClickListener;
    }
    public void setmItems(List<T> mItems) {
        this.mItems = mItems;
    }
}
