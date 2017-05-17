package com.cleveroad.sample.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

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

public abstract class AbstractLoopBarFragment extends Fragment
        implements
        View.OnClickListener,
        OnItemClickListener,
        AdapterView.OnItemSelectedListener {

    static final String EXTRA_ORIENTATION = "EXTRA_ORIENTATION";

    private LoopBarView loopBarView;
    private ViewPager viewPager;

    private ArrayAdapter<ScrollModeHolder> mSpinnerAdapter;

    //args
    @Orientation
    private int mOrientation;

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
        Spinner spScrollMode = (Spinner) rootView.findViewById(R.id.spScrollMode);
        initSpinner(spScrollMode);
        rootView.findViewById(R.id.btnOrientation).setOnClickListener(this);
        rootView.findViewById(R.id.btnGravity).setOnClickListener(this);
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
        fragments.add(ColorFragment.newInstance(android.R.color.holo_red_light));
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
            default:
                // nothing
                break;
        }
    }

    @Override
    public void onItemClicked(int position) {
        viewPager.setCurrentItem(position);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ScrollModeHolder scrollModeHolder = mSpinnerAdapter.getItem(position);
        if (scrollModeHolder != null) {
            loopBarView.setScrollMode(scrollModeHolder.getScrollMode());
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // nothing
    }

    protected abstract Fragment getNewInstance(int orientation);

    protected ViewPager getViewPager() {
        return viewPager;
    }

    protected LoopBarView getLoopBarView() {
        return loopBarView;
    }

    private void changeGravity() {
        int nextGravity = loopBarView.getGravity() == LoopBarView.SELECTION_GRAVITY_START ?
                LoopBarView.SELECTION_GRAVITY_END : LoopBarView.SELECTION_GRAVITY_START;
        loopBarView.setGravity(nextGravity);
    }

    private void changeOrientation() {
        Fragment fragment = getNewInstance(mOrientation == Orientation.ORIENTATION_VERTICAL ?
                Orientation.ORIENTATION_HORIZONTAL : Orientation.ORIENTATION_VERTICAL);
        ((IFragmentReplacer) getActivity()).replaceFragment(fragment);
    }

    private void initSpinner(Spinner spinner) {
        mSpinnerAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1,
                getScrollModes());
        spinner.setAdapter(mSpinnerAdapter);
        spinner.setOnItemSelectedListener(this);
    }

    private List<ScrollModeHolder> getScrollModes() {
        List<ScrollModeHolder> scrollModeHolders = new ArrayList<>();
        scrollModeHolders.add(new ScrollModeHolder(LoopBarView.SCROLL_MODE_AUTO, getString(R.string.auto)));
        scrollModeHolders.add(new ScrollModeHolder(LoopBarView.SCROLL_MODE_INFINITE, getString(R.string.infinite)));
        scrollModeHolders.add(new ScrollModeHolder(LoopBarView.SCROLL_MODE_FINITE, getString(R.string.finite)));
        return scrollModeHolders;
    }

}
