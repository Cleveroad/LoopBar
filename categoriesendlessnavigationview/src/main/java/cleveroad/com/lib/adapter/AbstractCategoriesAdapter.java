package cleveroad.com.lib.adapter;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cleveroad.com.lib.R;
import cleveroad.com.lib.widget.BaseRecyclerViewHolder;
import cleveroad.com.lib.widget.OnItemClickListener;

public abstract class AbstractCategoriesAdapter extends RecyclerView.Adapter<AbstractCategoriesAdapter.CategoriesHolder> {
    private static final String TAG = AbstractCategoriesAdapter.class.getSimpleName();
    public static final int VIEW_TYPE_RESERVED_HIDDEN = -1;
    public static final int VIEW_TYPE_OTHER = 0;

    private List<IOperationItem> items;
    private OnItemClickListener<IOperationItem> listener;
    private boolean isIndeterminate = true;

    public AbstractCategoriesAdapter(List<IOperationItem> items) {
        this.items = items;
    }

    public List<IOperationItem> getItems() {
        return items;
    }

    public void setListener(OnItemClickListener<IOperationItem> listener) {
        this.listener = listener;
    }

    public abstract View createView(ViewGroup parent);

    public View createEmptyView(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.empty_view, parent, false);
    }

    public abstract CategoriesHolder createCategoriesHolder (View itemView);


    public void setIndeterminate(boolean isIndeterminate) {
        this.isIndeterminate = isIndeterminate;
    }

    IOperationItem getItem(int position) {
        position = position % items.size();
        return items.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        IOperationItem item = getItem(position);
        return item.isVisible() ? VIEW_TYPE_OTHER : VIEW_TYPE_RESERVED_HIDDEN;
    }

    @Override
    public CategoriesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_RESERVED_HIDDEN) {
            return new EmptyHolder(createEmptyView(parent));
        }
        CategoriesHolder holder = createCategoriesHolder(createView(parent));
        holder.setListener(listener);
        return holder;
    }

    @Override
    public void onBindViewHolder(CategoriesHolder holder, int position) {
        holder.bindItem(getItem(position));
    }

    //indeterminate scroll
    @Override
    public int getItemCount() {
        if (isIndeterminate) {
            return Integer.MAX_VALUE;
        } else {
            return items.size();
        }
    }

    public static class EmptyHolder extends CategoriesHolder {

        public EmptyHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setVisibility(View.GONE);
        }

        @Override
        protected void onBindItem(IOperationItem item) {
        }
    }

    public abstract static class CategoriesHolder extends BaseRecyclerViewHolder<IOperationItem> {
        public CategoriesHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        public boolean isClickAllowed() {
            return getItem().isVisible();
        }

    }
}
