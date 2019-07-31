package com.cleveroad.sample.fragments;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.cleveroad.loopbar.widget.LoopBarView;

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
        LoopBarView loopBarView = getLoopBarView();
        ViewPager viewPager = getViewPager();
        loopBarView.setupWithViewPager(viewPager);
    }

    @Override
    protected Fragment getNewInstance(int orientation) {
        return newInstance(orientation);
    }
}
