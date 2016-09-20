package com.cleveroad.sample;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.cleveroad.loopbar.adapter.ICategoryItem;
import com.cleveroad.loopbar.adapter.ILoopBarPagerAdapter;

import java.util.List;

public final class SimpleFragmentStatePagerAdapter extends FragmentStatePagerAdapter implements ILoopBarPagerAdapter {
    private final List<ICategoryItem> categoryItems;
    private List<Fragment> fragmentList;

    public SimpleFragmentStatePagerAdapter(FragmentManager fm, List<Fragment> fragmentList, List<ICategoryItem> categoryItems) {
        super(fm);
        this.fragmentList = fragmentList;
        this.categoryItems = categoryItems;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @NonNull
    @Override
    public CharSequence getPageTitle(int position) {
        return categoryItems.get(position).getCategoryName();
    }

    @NonNull
    @Override
    public Drawable getPageDrawable(int position) {
        return categoryItems.get(position).getCategoryIconDrawable();
    }
}
