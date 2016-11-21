package com.cleveroad.loopbar.widget;

import android.support.v7.widget.RecyclerView;

class AbstractSpacesItemDecoration extends RecyclerView.ItemDecoration {

    private int mSpace;

    AbstractSpacesItemDecoration(int space) {
        mSpace = space;
    }

    int getSpace() {
        return mSpace;
    }

    void setSpace(int space) {
        mSpace = space;
    }
}