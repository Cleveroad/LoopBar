package com.cleveroad.sample.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

public class ViewPagerLoopBarFragment extends AbstractLoopBarFragment {
    public static ViewPagerLoopBarFragment newInstance(int orientation) {
        Bundle args = new Bundle();
        args.putInt(EXTRA_ORIENTATION, orientation);

        ViewPagerLoopBarFragment fragment = new ViewPagerLoopBarFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        loopBarView.setupWithViewPager(viewPager);
    }

    @Override
    protected Fragment getNewInstance(int orientation) {
        return newInstance(orientation);
    }
}
