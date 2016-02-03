package cleveroad.com.lib.adapter;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.LinkedList;

import cleveroad.com.lib.R;
import cleveroad.com.lib.widget.BaseRecyclerViewHolder;
import cleveroad.com.lib.widget.OnItemClickListener;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoriesHolder> {
    private static final String TAG = CategoriesAdapter.class.getSimpleName();

    private LinkedList<ICategoryItem> items = new LinkedList<>();
    private OnItemClickListener<ICategoryItem> listener;

    public CategoriesAdapter(LinkedList<ICategoryItem> items, OnItemClickListener<ICategoryItem> listener) {
        this.items = items;
        this.listener = listener;
    }

    public static View createView(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_default, parent, false);
    }

    ICategoryItem getItem(int position) {
        Log.w(TAG, "items = " + items);
        Log.w(TAG, "getItem position = " + position);

        position = position % items.size();

        Log.w(TAG, "getItem position normalized =" + position);

        ICategoryItem item = items.get(position);
        Log.w(TAG, "item = " + item);

        return item;
    }

    @Override
    public CategoriesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = createView(parent);
        return CategoriesHolder.newBuilder(itemView)
                .listener(listener)
                .build();
    }

    @Override
    public void onBindViewHolder(CategoriesHolder holder, int position) {
        holder.bindItem(getItem(position));
    }


    /**
     * Inserts the specified object at the specified index in the array.
     *
     * @param object The object to insert into the array.
     */
    public void insert(final ICategoryItem object, int deletedPosition) {
        Log.d(TAG, "items before inserting =" + items);

        if (items.contains(object)) {
            Log.e(TAG, "this object already added");
            return;
        }

        int offset = deletedPosition % items.size();
        int startPosition = deletedPosition - offset;
//        notifyItemInserted(startPosition);
//        Log.i(TAG, "inserted in position = " + deletedPosition);

        items.add(object);

//        notifyItemInserted(items.indexOf(object));
//        notifyDataSetChanged();
//        notifyItemInserted(startPosition + object.getPosition());
//        notifyItemRangeChanged(deletedPosition, items.size());
        Log.d(TAG, "insert =" + object);
        Log.d(TAG, "items after inserting =" + items);
    }

    /**
     * Removes the specified object from the array.
     *
     * @param adapterPosition position of object to remove
     */
    public ICategoryItem remove(ICategoryItem categoryItem, int adapterPosition) {
        Log.d(TAG, "items before removing =" + items);

        Log.d(TAG, "remove object = " + categoryItem + ", position =" + adapterPosition);


        notifyItemRemoved(adapterPosition);
        notifyItemInserted(0);

        int idx = items.indexOf(categoryItem);

        items.remove(categoryItem);

        Log.d(TAG, "items after removing =" + items);
//        notifyDataSetChanged();

//        int startFrom = adapterPosition;
//        int endTo = adapterPosition;
//
//        for (int i = 0; i < 5; i++) {
//            startFrom -= items.size();
//            endTo += items.size();
//            notifyItemRemoved(startFrom);
//            notifyItemRemoved(endTo);
//        }

        return categoryItem;
    }

    public ICategoryItem replace(ICategoryItem categoryItem , ICategoryItem oldCategoryItem, int adapterPosition) {
        int idx = items.indexOf(categoryItem);
        items.remove(idx);
        items.add(idx, oldCategoryItem);

        notifyItemChanged(adapterPosition);

        return categoryItem;
    }

    //indeterminate scroll
    @Override
    public int getItemCount() {
//        return items.size()*3;
        return Integer.MAX_VALUE;
    }

    public interface ICategoryItem {
        int getCategoryIconDrawable();

        int getPosition();

        void setPosition(int position);

        String getCategoryName();
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
