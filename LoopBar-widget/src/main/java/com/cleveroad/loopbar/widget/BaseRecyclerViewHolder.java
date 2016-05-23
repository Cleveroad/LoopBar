package com.cleveroad.loopbar.widget;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

abstract class BaseRecyclerViewHolder<T> extends RecyclerView.ViewHolder implements View.OnClickListener {
    protected static final int KEY_VIEW_TAG = -1;
    private static final String TAG_ITEM_VIEW = "itemView";
    private T item;
    @Nullable
    private OnItemClickListener listener;

    public BaseRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public void setClickable(boolean clickable) {
        if (clickable) {
            itemView.setTag(KEY_VIEW_TAG, TAG_ITEM_VIEW);
            itemView.setOnClickListener(this);
        } else {
            itemView.setOnClickListener(null);
        }
    }

    public void setListener(@Nullable OnItemClickListener listener) {
        setClickable(true);
        this.listener = listener;
    }

    public T getItem() {
        return item;
    }

    public final void bindItem(T item) {
        this.item = item;
        onBindItem(item);
    }

    /**
     * override this method with {@link #setClickable(boolean)} to receive click events on viewHolder item in child class
     */
    public void onItemClicked(T item) {
    }

    public boolean isClickAllowed() {
        return true;
    }

    protected abstract void onBindItem(T item);

    @Override
    public void onClick(View v) {
        Object tag = v.getTag(KEY_VIEW_TAG);
        if (tag != null && tag.equals(TAG_ITEM_VIEW) && getAdapterPosition() != -1 && isClickAllowed()) {
            onItemClicked(getItem());
            if (listener != null) {
                listener.onItemClicked(getAdapterPosition());
            }
        }
    }
}
