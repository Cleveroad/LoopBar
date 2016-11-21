package com.cleveroad.loopbar.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

class SpacesRightItemDecoration extends AbstractSpacesItemDecoration {

    SpacesRightItemDecoration(int space) {
        super(space);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildAdapterPosition(view) == parent.getAdapter().getItemCount() - 1)
            outRect.right = getSpace();
    }

}