package com.cleveroad.sample.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

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

    private LoopBarView mLoopBarView;
    private ViewPager mViewPager;

    private ArrayAdapter<ScrollModeHolder> mSpinnerAdapter;

    //args
    @Orientation
    private int mOrientation;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        @Orientation
        int orientation = getArguments().getInt(EXTRA_ORIENTATION, Orientation.ORIENTATION_HORIZONTAL_BOTTOM);
        this.mOrientation = orientation;
        int layoutId;
        switch (orientation) {
            case Orientation.ORIENTATION_VERTICAL_LEFT:
                layoutId = R.layout.fragment_loopbar_vertical_left;
                break;
            case Orientation.ORIENTATION_VERTICAL_RIGHT:
                layoutId = R.layout.fragment_loopbar_vertical_right;
                break;
            case Orientation.ORIENTATION_HORIZONTAL_TOP:
                layoutId = R.layout.fragment_loopbar_horizontal_top;
                break;
            case Orientation.ORIENTATION_HORIZONTAL_BOTTOM:
                layoutId = R.layout.fragment_loopbar_horizontal_bottom;
                break;
            default:
                layoutId = R.layout.fragment_loopbar_horizontal_bottom;
                break;
        }
        View rootView = inflater.inflate(
                layoutId,
                container,
                false);

        mLoopBarView = (LoopBarView) rootView.findViewById(R.id.endlessView);
        mViewPager = (ViewPager) rootView.findViewById(R.id.viewPager);
        Spinner spScrollMode = (Spinner) rootView.findViewById(R.id.spScrollMode);
        initSpinner(spScrollMode);
        rootView.findViewById(R.id.btnOrientation).setOnClickListener(this);
        rootView.findViewById(R.id.btnGravity).setOnClickListener(this);
        mLoopBarView.addOnItemClickListener(this);
        SimpleFragmentStatePagerAdapter pagerAdapter = new SimpleFragmentStatePagerAdapter(
                getChildFragmentManager(),
                getMockFragments(),
                MockedItemsFactory.getCategoryItems(getContext()));
        mViewPager.setAdapter(pagerAdapter);

        mViewPager.addOnPageChangeListener(new AbstractPageChangedListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mLoopBarView.setCurrentItem(position);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d("tag", "on page scrolled");
            }
        });
        if(orientation == Orientation.ORIENTATION_HORIZONTAL_TOP){
            mLoopBarView.setOrientation(Orientation.ORIENTATION_HORIZONTAL_TOP);
        }

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
        mViewPager.setCurrentItem(position);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ScrollModeHolder scrollModeHolder = mSpinnerAdapter.getItem(position);
        if (scrollModeHolder != null) {
            mLoopBarView.setScrollMode(scrollModeHolder.getScrollMode());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // nothing
    }

    protected abstract Fragment getNewInstance(int orientation);

    protected ViewPager getViewPager() {
        return mViewPager;
    }

    protected LoopBarView getLoopBarView() {
        return mLoopBarView;
    }

    private void changeGravity() {
        int nextGravity = mLoopBarView.getGravity() == LoopBarView.SELECTION_GRAVITY_START ?
                LoopBarView.SELECTION_GRAVITY_END : LoopBarView.SELECTION_GRAVITY_START;
        mLoopBarView.setGravity(nextGravity);
    }

    private void changeOrientation() {
        Fragment fragment;
        switch (mOrientation) {
            case Orientation.ORIENTATION_VERTICAL_LEFT:
                fragment = getNewInstance(Orientation.ORIENTATION_HORIZONTAL_TOP);
                break;
            case Orientation.ORIENTATION_HORIZONTAL_TOP:
                fragment = getNewInstance(Orientation.ORIENTATION_VERTICAL_RIGHT);
                break;
            case Orientation.ORIENTATION_VERTICAL_RIGHT:
                fragment = getNewInstance(Orientation.ORIENTATION_HORIZONTAL_BOTTOM);
                break;
            case Orientation.ORIENTATION_HORIZONTAL_BOTTOM:
                fragment = getNewInstance(Orientation.ORIENTATION_VERTICAL_LEFT);
                break;
            default:
                fragment = getNewInstance(Orientation.ORIENTATION_HORIZONTAL_BOTTOM);
                break;

        }
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
