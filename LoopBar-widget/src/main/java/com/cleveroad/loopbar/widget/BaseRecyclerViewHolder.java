package com.cleveroad.loopbar.widget;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;

abstract class BaseRecyclerViewHolder<T> extends RecyclerView.ViewHolder implements View.OnClickListener {

    @SuppressWarnings("WeakerAccess")
    protected static final int KEY_VIEW_TAG = -1;
    private static final String TAG_ITEM_VIEW = "itemView";
    private T mItem;
    @Nullable
    private WeakReference<OnItemClickListener> mWeakRefListener;
    private int mCurrentPosition;

    BaseRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    private void setClickable(boolean clickable) {
        if (clickable) {
            itemView.setTag(KEY_VIEW_TAG, TAG_ITEM_VIEW);
            itemView.setOnClickListener(this);
        } else {
            itemView.setOnClickListener(null);
        }
    }

    void setListener(@Nullable OnItemClickListener listener) {
        setClickable(true);
        mWeakRefListener = new WeakReference<>(listener);
    }

    public T getItem() {
        return mItem;
    }

    final void bindItem(T item, int position) {
        mItem = item;
        mCurrentPosition = position;
        onBindItem(item, position);
    }

    private int getCurrentPosition() {
        return mCurrentPosition;
    }

    /**
     * Override this method with {@link #setClickable(boolean)} to receive click events on viewHolder mItem in child class
     */
    @SuppressWarnings("WeakerAccess")
    void onItemClicked(T item) {
        // do nothing
    }

    public boolean isClickAllowed() {
        return true;
    }

    protected abstract void onBindItem(T item, int position);

    @Override
    public void onClick(View v) {
        Object tag = v.getTag(KEY_VIEW_TAG);
        if (tag != null && tag.equals(TAG_ITEM_VIEW) && getAdapterPosition() != -1 && isClickAllowed()) {
            onItemClicked(getItem());
            if (mWeakRefListener != null) {
                OnItemClickListener listener = mWeakRefListener.get();
                if (listener != null) {
                    listener.onItemClicked(getCurrentPosition());
                }
            }
        }
    }
}
