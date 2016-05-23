package com.cleveroad.loopbar.adapter;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import com.cleveroad.loopbar.R;

public class SimpleCategoriesAdapter extends RecyclerView.Adapter<SimpleCategoriesAdapter.SimpleCategoriesHolder> {

    private List<ICategoryItem> categoryItems;

    public SimpleCategoriesAdapter(List<ICategoryItem> items) {
        this.categoryItems = items;
    }

    /** create itemView*/
    public View createView(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.enls_item_default, parent, false);
    }

    @Override
    public SimpleCategoriesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SimpleCategoriesHolder(createView(parent));
    }

    @Override
    public void onBindViewHolder(SimpleCategoriesHolder holder, int position) {
        holder.bindItem(getItem(position));
    }

    public ICategoryItem getItem(int position) {
        return categoryItems.get(position);
    }

    @Override
    public int getItemCount() {
        return categoryItems.size();
    }

    public static class SimpleCategoriesHolder extends BaseRecyclerViewHolder<ICategoryItem> {
        private TextView tvCategoryName;
        private ImageView ivCategoryIcon;

        public SimpleCategoriesHolder(@NonNull View itemView) {
            super(itemView);
            tvCategoryName = (TextView) itemView.findViewById(R.id.tvCategoryName);
            ivCategoryIcon = (ImageView) itemView.findViewById(R.id.ivCategoryIcon);
        }

        @Override
        protected void onBindItem(ICategoryItem item) {
            tvCategoryName.setText(item.getCategoryName());
            ivCategoryIcon.setImageDrawable(ContextCompat.getDrawable(itemView.getContext(), item.getCategoryIconDrawable()));
        }

    }
}
