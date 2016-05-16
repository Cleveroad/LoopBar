package cleveroad.com.lib.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

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

    void initPlaceHolder(@Nullable View overlayPlaceHolder, RecyclerView rvCategories);
}
