package com.cleveroad.loopbar.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Base interface for all orientation states. This interface use for implementation UI logic in loopBar.
 * It is implementation of state pattern.
 */
interface IOrientationState extends ISelectionGravityState {

    /**
     * Create layoutManager {@link LinearLayoutManager}
     *
     * @param context instance of Context{@link Context}
     *
     * @return layoutManager {@link LinearLayoutManager}
     */
    LinearLayoutManager getLayoutManager(Context context);

    /**
     * Get layout id
     *
     * @return layout id {@link androidx.annotation.LayoutRes}
     */
    int getLayoutId();

    /**
     * Check if all items of recyclerView fit on screen
     *
     * @param container recyclerView with items
     * @param itemsSize count of items
     */
    boolean isItemsFitOnScreen(RecyclerView container, int itemsSize);

    /**
     * Get header size
     *
     * @param context instance of Context{@link Context}
     *
     * @return header size in pixel
     */
    int getHeaderSize(Context context);

    /**
     * Get header margin
     *
     * @param context instance of Context{@link Context}
     *
     * @return header margin in pixel
     */
    int getHeaderMargins(Context context);

    /**
     * Get selector view size
     *
     * @param selector selector view
     *
     * @return selector view size in pixel
     */
    int getSize(View selector);

    /**
     * Decoration of the selected view
     *
     * @param margin              margin of the selected view
     * @param selectionViewWidth  width of the selected view
     * @param selectionViewHeight height of the selected view
     *
     * @return {@link AbstractSpacesItemDecoration} decorated selected view
     */
    AbstractSpacesItemDecoration getSelectionViewItemDecoration(int margin, int selectionViewWidth, int selectionViewHeight);

    /**
     * Get constant orientation{@link Orientation} depending on the orientation of the loopBar
     *
     * @return orientation constant {@link Orientation}
     */
    @Orientation
    int getOrientation();

    /**
     * Set properties view container in which the selected item is placed
     *
     * @param selectionViewContainer view container{@link ViewGroup}
     */
    void initSelectionContainer(ViewGroup selectionViewContainer);

    /**
     * Set properties to the overlay placeholder view
     *
     * @param overlayPlaceHolder overlay placeholder view
     * @param rvContainer        view for list
     * @param selectorContainer  view container for selected item
     * @param overlaySize        overlay size in pixel
     */
    void initPlaceHolderAndOverlay(@Nullable View overlayPlaceHolder, View rvContainer,
                                   View selectorContainer, int overlaySize);

    /**
     * Set gravity constant{@link LoopBarView.GravityAttr}
     *
     * @param selectionGravity gravity constant{@link LoopBarView.GravityAttr}
     */
    void setSelectionGravity(@LoopBarView.GravityAttr int selectionGravity);
}
