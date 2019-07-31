package com.cleveroad.loopbar.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.cleveroad.loopbar.widget.OnItemClickListener;

import java.lang.ref.WeakReference;

/**
 * Base realization of ViewHolder {@link  androidx.recyclerview.widget.RecyclerView.ViewHolder}
 *
 * @param <T> Type of models for displaying in ViewHolder
 */
@SuppressWarnings("WeakerAccess")
public abstract class BaseRecyclerViewHolder<T> extends RecyclerView.ViewHolder implements View.OnClickListener {

    protected static final int KEY_VIEW_TAG = -1;
    private static final String TAG_ITEM_VIEW = "itemView";
    private T item;
    @Nullable
    private WeakReference<OnItemClickListener> mWeakRefListener;

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

    @SuppressWarnings("unused")
    public void setListener(@Nullable OnItemClickListener listener) {
        setClickable(true);
        mWeakRefListener = new WeakReference<>(listener);
    }

    public T getItem() {
        return item;
    }

    public final void bindItem(T item) {
        this.item = item;
        onBindItem(item);
    }

    /**
     * Override this method with {@link #setClickable(boolean)} to receive click events on viewHolder item in child class
     */
    public void onItemClicked(T item) {
        //do nothing
    }

    /**
     * Override this method to disable/enable item click
     *
     * @return boolean value representing if there is enable item click processing
     */
    public boolean isClickAllowed() {
        return true;
    }

    protected abstract void onBindItem(T item);

    @Override
    public void onClick(View v) {
        Object tag = v.getTag(KEY_VIEW_TAG);
        if (tag != null && tag.equals(TAG_ITEM_VIEW) && getAdapterPosition() != -1 && isClickAllowed()) {
            onItemClicked(getItem());
            if (mWeakRefListener != null) {
                OnItemClickListener listener = mWeakRefListener.get();
                if (listener != null) {
                    listener.onItemClicked(getAdapterPosition());
                }
            }
        }
    }
}
