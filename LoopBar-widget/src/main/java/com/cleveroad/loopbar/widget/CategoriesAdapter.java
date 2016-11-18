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

public class CategoriesAdapter extends RecyclerView.Adapter<BaseRecyclerViewHolder<IOperationItem>>
        implements OnItemClickListener {

    public static final int VIEW_TYPE_RESERVED_HIDDEN = -1;
    public static final int VIEW_TYPE_OTHER = 0;

    private static final String TAG = CategoriesAdapter.class.getSimpleName();
    @Orientation
    private int orientation = Orientation.ORIENTATION_VERTICAL;

    private RecyclerView.Adapter<? extends RecyclerView.ViewHolder> inputAdapter;

    @SuppressLint("UseSparseArrays")
    private HashMap<Integer, IOperationItem> wrappedItems = new HashMap<>();
    private OnItemClickListener listener;
    private List<OnItemClickListener> outerItemClickListeners = new LinkedList<>();
    private boolean isIndeterminate = true;

    public CategoriesAdapter(RecyclerView.Adapter<? extends RecyclerView.ViewHolder> inputAdapter) {
        this.inputAdapter = inputAdapter;
        for (int i = 0; i < inputAdapter.getItemCount(); i++) {
            wrappedItems.put(i, new OperationItem());
        }
    }

    public void addOnItemClickListener(OnItemClickListener onItemClickListener) {
        outerItemClickListeners.add(onItemClickListener);
    }

    public void removeOnItemClickListener(OnItemClickListener onItemClickListener) {
        outerItemClickListeners.remove(onItemClickListener);
    }

    private void notifyItemClicked(int position) {
        for (OnItemClickListener listener : outerItemClickListeners) {
            listener.onItemClicked(position);
        }
    }

    public Collection<IOperationItem> getWrappedItems() {
        return wrappedItems.values();
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public View createEmptyView(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.enls_empty_view, parent, false);
    }

    /**
     * Set mode for scrolling (Infinite or finite)
     *
     * @param isIndeterminate true for infinite
     */
    public void setIndeterminate(boolean isIndeterminate) {
        this.isIndeterminate = isIndeterminate;
        notifyDataSetChanged();
    }

    public IOperationItem getItem(int position) {
        position = normalizePosition(position);
        return wrappedItems.get(position);
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    int normalizePosition(int position){
        return position % wrappedItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        IOperationItem item = getItem(position);
        return item.isVisible() ? inputAdapter.getItemViewType(position) : VIEW_TYPE_RESERVED_HIDDEN;
    }

    @Override
    public BaseRecyclerViewHolder<IOperationItem> onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_RESERVED_HIDDEN) {
            return new EmptyHolder(createEmptyView(parent));
        }

        RecyclerView.ViewHolder viewHolder = inputAdapter.createViewHolder(parent, viewType);
        CategoriesHolder categoriesHolder = new CategoriesHolder(viewHolder);

        if (orientation == Orientation.ORIENTATION_VERTICAL) {
            //if orientation vertical set layout params to MATCH_PARENT to center item in view
            ViewGroup.LayoutParams layoutParams = viewHolder.itemView.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            viewHolder.itemView.requestLayout();
        } else {
            //if orientation vertical set layout params to MATCH_PARENT to center item in view
            ViewGroup.LayoutParams layoutParams = viewHolder.itemView.getLayoutParams();
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            viewHolder.itemView.requestLayout();
        }

        if (listener != null) {
            categoriesHolder.setListener(this);
        }
        return categoriesHolder;
    }

    @Override
    public void onItemClicked(int position) {
        listener.onItemClicked(position);
        position = normalizePosition(position);
        notifyItemClicked(position);
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder<IOperationItem> holder, int position) {
        holder.bindItem(getItem(position));
    }

    @Override
    public int getItemCount() {
        //infinite scroll
        if (isIndeterminate) {
            return Integer.MAX_VALUE;
        } else {
            return wrappedItems.size();
        }
    }

    public static class EmptyHolder extends BaseRecyclerViewHolder<IOperationItem> {

        public EmptyHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setVisibility(View.GONE);
        }

        @Override
        protected void onBindItem(IOperationItem item) {
            //do nothing
        }

    }

    public class CategoriesHolder extends BaseRecyclerViewHolder<IOperationItem> {

        private RecyclerView.ViewHolder viewHolder;

        public CategoriesHolder(RecyclerView.ViewHolder viewHolder) {
            super(viewHolder.itemView);
            this.viewHolder = viewHolder;
        }

        @SuppressWarnings("unchecked cast")
        public <T extends RecyclerView.ViewHolder> void bindItemWildcardHelper(RecyclerView.Adapter<T> adapter, int position) {
            T vh = (T) viewHolder;
            position = normalizePosition(position);
            adapter.onBindViewHolder(vh, position);
        }


        @Override
        protected final void onBindItem(IOperationItem item) {
            bindItemWildcardHelper(inputAdapter, getAdapterPosition());
            itemView.setVisibility(item.isVisible() ? View.VISIBLE : View.GONE);
        }

        @Override
        public boolean isClickAllowed() {
            return getItem().isVisible();
        }

    }
}
