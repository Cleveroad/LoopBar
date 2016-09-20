package com.cleveroad.loopbar.adapter;

import android.graphics.drawable.Drawable;

public interface ILoopBarPagerAdapter {
    CharSequence getPageTitle(int position);
    Drawable getPageDrawable(int position);
    int getCount();
}
