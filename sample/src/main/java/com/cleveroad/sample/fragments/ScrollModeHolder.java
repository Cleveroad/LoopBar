package com.cleveroad.sample.fragments;

import com.cleveroad.loopbar.widget.LoopBarView;

final class ScrollModeHolder {
    @LoopBarView.ScrollAttr
    private final int mScrollMode;
    private final String mDescription;

    ScrollModeHolder(@LoopBarView.ScrollAttr int scrollMode, String description) {
        mScrollMode = scrollMode;
        mDescription = description;
    }
    @LoopBarView.ScrollAttr
    int getScrollMode() {
        return mScrollMode;
    }

    @Override
    public String toString() {
        return mDescription;
    }
}
