package com.cleveroad.loopbar.widget;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class SpacesLeftItemDecoration extends AbstractSpacesItemDecoration {

    SpacesLeftItemDecoration(int space) {
        super(space);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                               @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        if (parent.getChildAdapterPosition(view) == 0)
            outRect.left = getSpace();
    }
}
