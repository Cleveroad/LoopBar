package com.cleveroad.sample.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cleveroad.loopbar.model.MockedItemsFactory;
import com.cleveroad.loopbar.widget.LoopBarView;
import com.cleveroad.loopbar.widget.OnItemClickListener;
import com.cleveroad.loopbar.widget.Orientation;
import com.cleveroad.sample.AbstractPageChangedListener;
import com.cleveroad.sample.IFragmentReplacer;
import com.cleveroad.sample.R;
import com.cleveroad.sample.SimpleFragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractLoopBarFragment extends Fragment implements View.OnClickListener, OnItemClickListener {
    static final String EXTRA_ORIENTATION = "EXTRA_ORIENTATION";

    private LoopBarView loopBarView;
    private ViewPager viewPager;

    //args
    @Orientation
    private int mOrientation;
    @LoopBarView.GravityAttr
    private int endlessGravity = LoopBarView.SELECTION_GRAVITY_START;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        @Orientation
        int orientation = getArguments().getInt(EXTRA_ORIENTATION, Orientation.ORIENTATION_HORIZONTAL);
        this.mOrientation = orientation;

        View rootView = inflater.inflate(
                orientation == Orientation.ORIENTATION_VERTICAL
                        ? R.layout.fragment_loopbar_vertical : R.layout.fragment_loopbar_horizontal,
                container,
                false);

        loopBarView = (LoopBarView) rootView.findViewById(R.id.endlessView);
        viewPager = (ViewPager) rootView.findViewById(R.id.viewPager);
        rootView.findViewById(R.id.btnOrientation).setOnClickListener(this);
        rootView.findViewById(R.id.btnGravity).setOnClickListener(this);
        rootView.findViewById(R.id.btnInfinite).setOnClickListener(this);
        loopBarView.addOnItemClickListener(this);

        SimpleFragmentStatePagerAdapter pagerAdapter = new SimpleFragmentStatePagerAdapter(
                getChildFragmentManager(),
                getMockFragments(),
                MockedItemsFactory.getCategoryItems(getContext()));
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new AbstractPageChangedListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                loopBarView.setCurrentItem(position);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d("tag", "on page scrolled");
            }
        });

        return rootView;
    }

    protected List<Fragment> getMockFragments() {
        List<Fragment> fragments = new ArrayList<>(8);
        fragments.add(ColorFragment.newInstance(android.R.color.holo_red_dark));
        fragments.add(ColorFragment.newInstance(android.R.color.black));
        fragments.add(ColorFragment.newInstance(android.R.color.holo_green_dark));
        fragments.add(ColorFragment.newInstance(android.R.color.holo_orange_dark));
        fragments.add(ColorFragment.newInstance(android.R.color.holo_blue_light));
        fragments.add(ColorFragment.newInstance(android.R.color.holo_green_light));
        fragments.add(ColorFragment.newInstance(android.R.color.holo_purple));
        fragments.add(ColorFragment.newInstance(android.R.color.white));
        return fragments;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnGravity:
                changeGravity();
                break;
            case R.id.btnOrientation:
                changeOrientation();
                break;
            case R.id.btnInfinite:
                changeScrollingMode();
                break;
            default:
                // nothing
                break;
        }
    }

    @Override
    public void onItemClicked(int position) {
        viewPager.setCurrentItem(position);
    }

    protected abstract Fragment getNewInstance(int orientation);

    protected ViewPager getViewPager() {
        return viewPager;
    }

    protected LoopBarView getLoopBarView() {
        return loopBarView;
    }

    private void changeGravity() {
        int nextGravity = endlessGravity == LoopBarView.SELECTION_GRAVITY_START ?
                LoopBarView.SELECTION_GRAVITY_END : LoopBarView.SELECTION_GRAVITY_START;
        endlessGravity = nextGravity;
        loopBarView.setGravity(nextGravity);
    }

    private void changeOrientation() {
        Fragment fragment = getNewInstance(mOrientation == Orientation.ORIENTATION_VERTICAL ?
                Orientation.ORIENTATION_HORIZONTAL : Orientation.ORIENTATION_VERTICAL);
        ((IFragmentReplacer) getActivity()).replaceFragment(fragment);
    }

    private void changeScrollingMode() {
        loopBarView.setIsInfinite(!loopBarView.isInfinite());
    }


}
