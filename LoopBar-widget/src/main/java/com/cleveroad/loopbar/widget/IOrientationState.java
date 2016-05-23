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
     * check if all items of recyclerView fit on screen
     * @param containerWidth width of recyclerView
     * @param containerHeight height of recyclerView
     * @param itemWidth width of item
     * @param itemHeight height of item
     * @param itemsSize count of items
     * */
    boolean isItemsFitOnScreen(int containerWidth, int containerHeight, int itemWidth, int itemHeight, int itemsSize);

    AbstractSpacesItemDecoration getSelectionViewItemDecoration(int margin, int selectionViewWidth, int selectionViewHeight);

    @Orientation
    int getOrientation();

    void initSelectionContainer(ViewGroup selectionViewContainer);

    void initPlaceHolderAndOverlay(@Nullable View overlayPlaceHolder, RecyclerView rvCategories, int overlaySize);

    void setSelectionGravity(@LoopBarView.GravityAttr int selectionGravity);
}
