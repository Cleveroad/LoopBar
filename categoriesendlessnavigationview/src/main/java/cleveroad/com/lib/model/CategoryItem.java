package cleveroad.com.lib.model;

import cleveroad.com.lib.adapter.CategoriesAdapter;

class CategoryItem implements CategoriesAdapter.ICategoryItem {
    private int categoryItemDrawableId;
    private String categoryName;
    private int position;

    public CategoryItem(int categoryItemDrawableId, String categoryName) {
        this.categoryItemDrawableId = categoryItemDrawableId;
        this.categoryName = categoryName;
    }

    @Override
    public int getCategoryIconDrawable() {
        return categoryItemDrawableId;
    }

    @Override
    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
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
