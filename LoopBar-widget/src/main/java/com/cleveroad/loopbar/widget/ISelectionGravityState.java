package com.cleveroad.loopbar.widget;

import android.view.ViewGroup;

interface ISelectionGravityState {

    @LayoutGravity
    int getSelectionGravity();

    <T extends ViewGroup.MarginLayoutParams> T setSelectionMargin(int marginPx, T layoutParams);

    public AbstractSpacesItemDecoration getOffsetItemDecoration();
}
