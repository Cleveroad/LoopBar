package com.cleveroad.loopbar.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

class SpacesTopItemDecoration extends AbstractSpacesItemDecoration {

    SpacesTopItemDecoration(int space) {
        super(space);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildAdapterPosition(view) == 0)
            outRect.top = getSpace();
    }

}