package cleveroad.com.lib.widget;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;
import java.util.List;

import cleveroad.com.lib.R;
import cleveroad.com.lib.adapter.IOperationItem;
import cleveroad.com.lib.adapter.OperationItem;

public abstract class CategoriesAdapter<T> extends RecyclerView.Adapter<CategoriesAdapter.CategoriesHolder<T>>
        implements OnItemClickListener<IOperationItem<T>> {
    private static final String TAG = CategoriesAdapter.class.getSimpleName();
    public static final int VIEW_TYPE_RESERVED_HIDDEN = -1;
    public static final int VIEW_TYPE_OTHER = 0;

    private RecyclerView.Adapter<RecyclerView.ViewHolder> inputAdapter;

    private List<IOperationItem<T>> wrappedItems = new LinkedList<>();
    private OnItemClickListener<IOperationItem<T>> listener;
    private List<OnItemClickListener<T>> outerItemClickListeners = new LinkedList<>();

    public void addOnItemClickListener(OnItemClickListener<T> onItemClickListener){
        outerItemClickListeners.add(onItemClickListener);
    }

    public void removeOnItemClickListener(OnItemClickListener<T> onItemClickListener){
        outerItemClickListeners.remove(onItemClickListener);
    }

    private void notifyItemClicked(T item, int position){
        for (OnItemClickListener<T> listener : outerItemClickListeners){
            listener.onItemClicked(item, position);
        }
    }

    private boolean isIndeterminate = true;

    public CategoriesAdapter(List<T> items) {
        for (T item : items) {
            wrappedItems.add(new OperationItem<>(item));
        }
    }

    public List<IOperationItem<T>> getWrappedItems() {
        return wrappedItems;
    }

    public void setListener(OnItemClickListener<IOperationItem<T>> listener) {
        this.listener = listener;
    }

    public abstract View createView(ViewGroup parent);

    public View createEmptyView(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.empty_view, parent, false);
    }

    public abstract CategoriesHolder<T> createCategoriesHolder (View itemView);


    public void setIndeterminate(boolean isIndeterminate) {
        this.isIndeterminate = isIndeterminate;
    }

    IOperationItem<T> getItem(int position) {
        position = normalizePosition(position);
        return wrappedItems.get(position);
    }

    int normalizePosition(int position){
        return position % wrappedItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        IOperationItem item = getItem(position);
        return item.isVisible() ? VIEW_TYPE_OTHER : VIEW_TYPE_RESERVED_HIDDEN;
    }

    @Override
    public CategoriesHolder<T> onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_RESERVED_HIDDEN) {
            return new EmptyHolder<>(createEmptyView(parent));
        }
        CategoriesHolder<T> holder = createCategoriesHolder(createView(parent));
        if (listener != null) {
            holder.setListener(this);
        }
        return holder;
    }

    @Override
    public void onItemClicked(IOperationItem<T> item, int position) {
        listener.onItemClicked(item, position);
        position = normalizePosition(position);
        notifyItemClicked(item.getWrappedItem(), position);
    }

    @Override
    public void onBindViewHolder(CategoriesHolder<T> holder, int position) {
        holder.bindItem(getItem(position));
    }

    //indeterminate scroll
    @Override
    public int getItemCount() {
        if (isIndeterminate) {
            return Integer.MAX_VALUE;
        } else {
            return wrappedItems.size();
        }
    }

    public static class EmptyHolder<T> extends CategoriesHolder<T> {

        public EmptyHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setVisibility(View.GONE);
        }

        @Override
        protected void onBindItemToView(Object o) {}

    }

    public abstract static class CategoriesHolder<T> extends BaseRecyclerViewHolder<IOperationItem<T>> {
        public CategoriesHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        protected final void onBindItem(IOperationItem<T> item) {
            onBindItemToView(item.getWrappedItem());
        }

        protected abstract void onBindItemToView(T t);

        @Override
        public boolean isClickAllowed() {
            return getItem().isVisible();
        }

    }
}
