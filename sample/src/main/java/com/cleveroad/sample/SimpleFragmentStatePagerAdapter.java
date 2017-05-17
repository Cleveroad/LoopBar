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
    private final List<ICategoryItem> mCategoryItems;
    private List<Fragment> mFragmentList;

    public SimpleFragmentStatePagerAdapter(FragmentManager fm, List<Fragment> fragmentList, List<ICategoryItem> categoryItems) {
        super(fm);
        mFragmentList = fragmentList;
        mCategoryItems = categoryItems;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @NonNull
    @Override
    public CharSequence getPageTitle(int position) {
        return mCategoryItems.get(position).getCategoryName();
    }

    @NonNull
    @Override
    public Drawable getPageDrawable(int position) {
        return mCategoryItems.get(position).getCategoryIconDrawable();
    }
}
