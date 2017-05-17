package com.cleveroad.loopbar.adapter;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

/**
 * Interface for ViewPager adapter {@link android.support.v4.view.PagerAdapter}
 * Need to be implemented if you want to show images in LoopBar
 */
public interface ILoopBarPagerAdapter {

    /**
     * Returns the title for page to show in LoopBar item
     *
     * @param position Page position
     * @return instance of {@link CharSequence}
     */
    @NonNull
    CharSequence getPageTitle(int position);

    /**
     * Returns the drawable for page to show in LoopBar item
     *
     * @param position Page position
     * @return instance of {@link Drawable}
     */
    @NonNull
    Drawable getPageDrawable(int position);

    /**
     * Returns count of pages in adapter
     *
     * @return int value for page count
     */
    int getCount();
}
