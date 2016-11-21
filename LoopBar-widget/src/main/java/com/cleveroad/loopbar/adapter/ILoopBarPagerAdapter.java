package com.cleveroad.loopbar.adapter;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

public interface ILoopBarPagerAdapter {

    @NonNull
    CharSequence getPageTitle(int position);

    @NonNull
    Drawable getPageDrawable(int position);

    int getCount();
}
