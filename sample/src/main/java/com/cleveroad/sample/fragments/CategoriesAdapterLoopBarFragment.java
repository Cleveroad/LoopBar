package com.cleveroad.sample.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.cleveroad.loopbar.adapter.ICategoryItem;
import com.cleveroad.loopbar.adapter.SimpleCategoriesAdapter;
import com.cleveroad.loopbar.widget.LoopBarView;

import java.util.ArrayList;

public class CategoriesAdapterLoopBarFragment extends AbstractLoopBarFragment {
    public static CategoriesAdapterLoopBarFragment newInstance(int orientation) {
        Bundle args = new Bundle();
        args.putInt(EXTRA_ORIENTATION, orientation);

        CategoriesAdapterLoopBarFragment fragment = new CategoriesAdapterLoopBarFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        LoopBarView loopBarView = getLoopBarView();
//        loopBarView.setCategoriesAdapter(new SimpleCategoriesAdapter(MockedItemsFactory.getCategoryItems(getContext())));
        loopBarView.setCategoriesAdapter(new SimpleCategoriesAdapter(new ArrayList<ICategoryItem>()));
    }

    @Override
    protected Fragment getNewInstance(int orientation) {
        return newInstance(orientation);
    }
}
