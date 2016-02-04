package cleveroad.com.lib.adapter;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cleveroad.com.lib.R;

public class SimpleCategoriesAdapter extends AbstractCategoriesAdapter<ICategoryItem> {

    public SimpleCategoriesAdapter(List<ICategoryItem> items) {
        super(items);
    }

    /** factory method to create child of CategoriesHolder*/
    @Override
    public CategoriesHolder createCategoriesHolder(View itemView) {
        return new SimpleCategoriesHolder(itemView);
    }

    /** create itemView*/
    public View createView(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_default, parent, false);
    }

    public static class SimpleCategoriesHolder extends CategoriesHolder<ICategoryItem> {
        private TextView tvCategoryName;
        private ImageView ivCategoryIcon;

        public SimpleCategoriesHolder(@NonNull View itemView) {
            super(itemView);
            tvCategoryName = (TextView) itemView.findViewById(R.id.tvCategoryName);
            ivCategoryIcon = (ImageView) itemView.findViewById(R.id.ivCategoryIcon);
        }

        @Override
        protected void onBindItemToView(ICategoryItem item) {
            tvCategoryName.setText(item.getCategoryName());
            ivCategoryIcon.setImageDrawable(ContextCompat.getDrawable(itemView.getContext(), item.getCategoryIconDrawable()));
        }
    }
}
