package com.cleveroad.loopbar.model;

import android.graphics.drawable.Drawable;

import com.cleveroad.loopbar.adapter.ICategoryItem;

public class CategoryItem implements ICategoryItem {
    private Drawable categoryDrawable;
    private String categoryName;

    public CategoryItem(Drawable drawable, String categoryName) {
        this.categoryDrawable = drawable;
        this.categoryName = categoryName;
    }

    @Override
    public Drawable getCategoryIconDrawable() {
        return categoryDrawable;
    }

    @Override
    public String getCategoryName() {
        return categoryName;
    }

    @Override
    public String toString() {
        return categoryName;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof CategoryItem && ((CategoryItem) o).categoryName.equals(categoryName);
    }
}
