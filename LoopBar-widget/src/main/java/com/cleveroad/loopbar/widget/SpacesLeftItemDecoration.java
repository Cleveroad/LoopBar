package com.cleveroad.loopbar.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

class SpacesLeftItemDecoration extends AbstractSpacesItemDecoration {

    SpacesLeftItemDecoration(int space) {
        super(space);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildAdapterPosition(view) == 0)
            outRect.left = getSpace();
    }

}