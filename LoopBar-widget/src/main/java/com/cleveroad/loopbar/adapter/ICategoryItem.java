package com.cleveroad.loopbar.adapter;

import android.graphics.drawable.Drawable;

/**
 * Interface for item in LoopBar
 */
public interface ICategoryItem {

    /**
     * Returns image for displaying in Item
     *
     * @return Instance of {@link Drawable}
     */
    Drawable getCategoryIconDrawable();

    /**
     * Returns text for displaying in Item
     *
     * @return Instance of {@link String}
     */
    String getCategoryName();
}
