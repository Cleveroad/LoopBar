package com.cleveroad.loopbar.model;

import android.graphics.drawable.Drawable;

import com.cleveroad.loopbar.adapter.ICategoryItem;

public class CategoryItem implements ICategoryItem {

    private Drawable mCategoryDrawable;
    private String mCategoryName;

    public CategoryItem(Drawable drawable, String categoryName) {
        mCategoryDrawable = drawable;
        mCategoryName = categoryName;
    }

    @Override
    public Drawable getCategoryIconDrawable() {
        return mCategoryDrawable;
    }

    @Override
    public String getCategoryName() {
        return mCategoryName;
    }

    @Override
    public String toString() {
        return mCategoryName;
    }

    @Override
    public int hashCode() {
        return mCategoryName.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof CategoryItem && ((CategoryItem) o).mCategoryName.equals(mCategoryName);
    }
}
