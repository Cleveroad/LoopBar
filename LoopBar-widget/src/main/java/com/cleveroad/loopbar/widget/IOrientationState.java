package com.cleveroad.loopbar.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

interface IOrientationState extends ISelectionGravityState {

    LinearLayoutManager getLayoutManager(Context context);

    int getLayoutId();

    /**
     * Check if all items of recyclerView fit on screen
     *
     * @param container recyclerView with items
     * @param itemsSize count of items
     */
    boolean isItemsFitOnScreen(RecyclerView container, int itemsSize);

    int getHeaderSize(Context context);

    int getHeaderMargins(Context context);

    int getSize(View selector);

    AbstractSpacesItemDecoration getSelectionViewItemDecoration(int margin, int selectionViewWidth, int selectionViewHeight);

    @Orientation
    int getOrientation();

    void initSelectionContainer(ViewGroup selectionViewContainer);

    void initPlaceHolderAndOverlay(@Nullable View overlayPlaceHolder, View rvContainer,
                                   View selectorContainer, int overlaySize);

    void setSelectionGravity(@LoopBarView.GravityAttr int selectionGravity);
}
