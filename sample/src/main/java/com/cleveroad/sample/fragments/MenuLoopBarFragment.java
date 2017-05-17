package com.cleveroad.sample.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.cleveroad.loopbar.widget.LoopBarView;
import com.cleveroad.sample.R;

public class MenuLoopBarFragment extends AbstractLoopBarFragment {

    public static MenuLoopBarFragment newInstance(int orientation) {
        Bundle args = new Bundle();
        args.putInt(EXTRA_ORIENTATION, orientation);

        MenuLoopBarFragment fragment = new MenuLoopBarFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        LoopBarView loopBarView = getLoopBarView();
        loopBarView.setCategoriesAdapterFromMenu(R.menu.loopbar);
    }

    @Override
    protected Fragment getNewInstance(int orientation) {
        return newInstance(orientation);
    }
}
