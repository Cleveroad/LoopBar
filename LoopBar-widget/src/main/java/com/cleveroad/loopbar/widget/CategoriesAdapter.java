package com.cleveroad.loopbar.widget;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cleveroad.loopbar.R;
import com.cleveroad.loopbar.adapter.IOperationItem;
import com.cleveroad.loopbar.adapter.OperationItem;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

class CategoriesAdapter extends RecyclerView.Adapter<BaseRecyclerViewHolder<IOperationItem>>
        implements OnItemClickListener {

    static final int VIEW_TYPE_OTHER = 0;
    private static final int VIEW_TYPE_RESERVED_HIDDEN = -1;
    @Orientation
    private int mOrientation = Orientation.ORIENTATION_VERTICAL;

    private RecyclerView.Adapter<? extends RecyclerView.ViewHolder> mInputAdapter;

    @SuppressLint("UseSparseArrays")
    private HashMap<Integer, IOperationItem> mWrappedItems = new HashMap<>();
    private OnItemClickListener mListener;
    private List<OnItemClickListener> mOuterItemClickListeners = new LinkedList<>();
    private boolean mIsIndeterminate = true;

    CategoriesAdapter(RecyclerView.Adapter<? extends RecyclerView.ViewHolder> inputAdapter) {
        mInputAdapter = inputAdapter;
        for (int i = 0; i < inputAdapter.getItemCount(); i++) {
            mWrappedItems.put(i, new OperationItem());
        }
    }


    void addOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOuterItemClickListeners.add(onItemClickListener);
    }

    void removeOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOuterItemClickListeners.remove(onItemClickListener);
    }

    private void notifyItemClicked(int position) {
        for (OnItemClickListener listener : mOuterItemClickListeners) {
            listener.onItemClicked(position);
        }
    }

    Collection<IOperationItem> getWrappedItems() {
        return mWrappedItems.values();
    }

    void setListener(OnItemClickListener listener) {
        mListener = listener;
    }

    private View createEmptyView(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.enls_empty_view, parent, false);
    }

    /**
     * Set mode for scrolling (Infinite or finite)
     *
     * @param isIndeterminate true for infinite
     */
    void setIndeterminate(boolean isIndeterminate) {
        mIsIndeterminate = isIndeterminate;
        notifyDataSetChanged();
    }

    IOperationItem getItem(int position) {
        return mWrappedItems.get(normalizePosition(position));
    }

    public void setOrientation(int orientation) {
        mOrientation = orientation;
    }

    int normalizePosition(int position) {
        return position % mWrappedItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        IOperationItem item = getItem(position);
        return item.isVisible() ? mInputAdapter.getItemViewType(position) : VIEW_TYPE_RESERVED_HIDDEN;
    }

    @Override
    public BaseRecyclerViewHolder<IOperationItem> onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_RESERVED_HIDDEN) {
            return new EmptyHolder(createEmptyView(parent));
        }

        RecyclerView.ViewHolder viewHolder = mInputAdapter.createViewHolder(parent, viewType);
        CategoriesHolder categoriesHolder = new CategoriesHolder(viewHolder);

        if (mOrientation == Orientation.ORIENTATION_VERTICAL) {
            //if mOrientation vertical set layout params to MATCH_PARENT to center item in view
            ViewGroup.LayoutParams layoutParams = viewHolder.itemView.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            viewHolder.itemView.requestLayout();
        } else {
            //if mOrientation vertical set layout params to MATCH_PARENT to center item in view
            ViewGroup.LayoutParams layoutParams = viewHolder.itemView.getLayoutParams();
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            viewHolder.itemView.requestLayout();
        }

        if (mListener != null) {
            categoriesHolder.setListener(this);
        }
        return categoriesHolder;
    }

    @Override
    public void onItemClicked(int position) {
        int normalizedPosition = normalizePosition(position);
        mListener.onItemClicked(normalizedPosition);
        notifyItemClicked(normalizedPosition);
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder<IOperationItem> holder, int position) {
        holder.bindItem(getItem(position), position);
    }

    @Override
    public int getItemCount() {
        int innerCount = mWrappedItems.size();
        if (innerCount < 1) {
            return 0;
        }
        //infinite scroll
        if (mIsIndeterminate) {
            return Integer.MAX_VALUE;
        } else {
            return innerCount;
        }
    }

    @SuppressWarnings("WeakerAccess")
    static class EmptyHolder extends BaseRecyclerViewHolder<IOperationItem> {

        EmptyHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setVisibility(View.GONE);
        }

        @Override
        protected void onBindItem(IOperationItem item, int position) {
            //do nothing
        }

    }

    class CategoriesHolder extends BaseRecyclerViewHolder<IOperationItem> {

        private RecyclerView.ViewHolder mViewHolder;

        CategoriesHolder(RecyclerView.ViewHolder viewHolder) {
            super(viewHolder.itemView);
            mViewHolder = viewHolder;
        }

        @SuppressWarnings("unchecked cast")
        <T extends RecyclerView.ViewHolder> void bindItemWildcardHelper(RecyclerView.Adapter<T> adapter, int position) {
            T vh = (T) mViewHolder;
            adapter.onBindViewHolder(vh, normalizePosition(position));
        }


        @Override
        protected final void onBindItem(IOperationItem item, int position) {
            bindItemWildcardHelper(mInputAdapter, position);
            itemView.setVisibility(item.isVisible() ? View.VISIBLE : View.GONE);
        }

        @Override
        public boolean isClickAllowed() {
            return getItem().isVisible();
        }

    }
}
