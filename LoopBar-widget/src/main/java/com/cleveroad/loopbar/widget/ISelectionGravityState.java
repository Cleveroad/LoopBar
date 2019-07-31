package com.cleveroad.loopbar.widget;

import android.view.Gravity;
import android.view.ViewGroup;

/**
 * Base interface for all gravity states. This interface use to implement the orientation logic in the loopBar
 */
interface ISelectionGravityState {

    /**
     * Get standard constant{@link Gravity} for placing an object within a container
     *
     * @return gravity constant
     */
    @LayoutGravity
    int getSelectionGravity();

    /**
     * Set margin for container in which the selected item is placed
     *
     * @param marginPx     margin in pixel
     * @param <T>          type layout params which need extends {@link ViewGroup.MarginLayoutParams}
     * @param layoutParams layoutParams that the container returns {@link ViewGroup#getLayoutParams()}
     *
     * @return layoutParams with new margin
     */
    <T extends ViewGroup.MarginLayoutParams> T setSelectionMargin(int marginPx, T layoutParams);

    /**
     * Get decorated selected view
     *
     * @return {@link AbstractSpacesItemDecoration} decorated selected view
     */
    AbstractSpacesItemDecoration getOffsetItemDecoration();
}
