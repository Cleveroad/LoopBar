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

public class SimpleCategoriesAdapter extends RecyclerView.Adapter<SimpleCategoriesAdapter.CategoriesHolder> {
    private static final String TAG = SimpleCategoriesAdapter.class.getSimpleName();

    private List<ICategoryItem> items;
    private OnItemClickListener<ICategoryItem> listener;

    public SimpleCategoriesAdapter(List<ICategoryItem> items, OnItemClickListener<ICategoryItem> listener) {
        this.items = items;
        this.listener = listener;
    }

    public static View createView(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_default, parent, false);
    }

    public static View createEmptyView(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.empty_view, parent, false);
    }

    ICategoryItem getItem(int position) {
        position = position % items.size();
        return items.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        ICategoryItem item = getItem(position);
        return item.isVisible() ? 0 : 1;
    }

    @Override
    public CategoriesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1) {
            return new EmptyHolder(createEmptyView(parent));
        }
        return CategoriesHolder.newBuilder(createView(parent))
                .listener(listener)
                .build();
    }

    @Override
    public void onBindViewHolder(CategoriesHolder holder, int position) {
        holder.bindItem(getItem(position));
    }

    //indeterminate scroll
    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    public interface ICategoryItem {
        int getCategoryIconDrawable();

        boolean isVisible();

        void setVisible(boolean isVisible);

        String getCategoryName();
    }

    public static class EmptyHolder extends CategoriesHolder {

        public EmptyHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setVisibility(View.GONE);
        }

        @Override
        protected void onBindItem(ICategoryItem item) {
        }
    }

    public static class CategoriesHolder extends BaseRecyclerViewHolder<ICategoryItem> {
        private TextView tvCategoryName;
        private ImageView ivCategoryIcon;

        private CategoriesHolder(@NonNull View itemView) {
            super(itemView);
            tvCategoryName = (TextView) itemView.findViewById(R.id.tvCategoryName);
            ivCategoryIcon = (ImageView) itemView.findViewById(R.id.ivCategoryIcon);
        }

        public static Builder newBuilder(View itemView) {
            return new CategoriesHolder(itemView).new Builder();
        }

        @Override
        protected void onBindItem(ICategoryItem item) {
            tvCategoryName.setText(item.getCategoryName());
            ivCategoryIcon.setImageDrawable(ContextCompat.getDrawable(itemView.getContext(), item.getCategoryIconDrawable()));
        }

        @Override
        public boolean isClickAllowed() {
            return getItem().isVisible();
        }

        public class Builder {

            public Builder listener(OnItemClickListener<ICategoryItem> listener) {
                setListener(listener);
                return this;
            }

            public CategoriesHolder build() {
                return CategoriesHolder.this;
            }

        }
    }
}
