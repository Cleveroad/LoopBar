package com.cleveroad.sample.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.cleveroad.sample.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ColorFragment extends Fragment {
    private static final String EXTRA_COLOR = "color";

    //args
    private int mColor;

    public static ColorFragment newInstance(int color) {

        Bundle args = new Bundle();
        args.putInt(EXTRA_COLOR, color);

        ColorFragment fragment = new ColorFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() == null || !getArguments().containsKey(EXTRA_COLOR))
            throw new IllegalArgumentException("you should run fragment view newInstance");

        mColor = getArguments().getInt(EXTRA_COLOR, -1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_color, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setBackgroundColor(ContextCompat.getColor(getContext(), mColor));
    }
}
