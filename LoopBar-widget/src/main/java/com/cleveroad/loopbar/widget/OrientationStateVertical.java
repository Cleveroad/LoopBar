package com.cleveroad.loopbar.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import java.util.UnknownFormatFlagsException;

import com.cleveroad.loopbar.R;

class OrientationStateVertical extends AbstractOrientationState implements IOrientationState {

    private Integer itemHeight;

    OrientationStateVertical() {}

    @Override
    public LinearLayoutManager getLayoutManager(Context context) {
        return new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
    }

    @Override
    public int getLayoutId() {
        return R.layout.enls_view_categories_navigation_vertical;
    }

    @Override
    public boolean isItemsFitOnScreen(RecyclerView rvCategories, int itemsSize) {
        calcItemHeight(rvCategories);
        int itemsHeight = itemHeight * (itemsSize);
        int containerHeight = rvCategories.getHeight();
        return containerHeight >= itemsHeight;
    }

    @Override
    public AbstractSpacesItemDecoration getSelectionViewItemDecoration(int margin, int selectionViewWidth, int selectionViewHeight) {
        AbstractSpacesItemDecoration itemDecoration = getOffsetItemDecoration();
        itemDecoration.setSpace(margin + selectionViewHeight);
        return itemDecoration;
    }

    private int calcItemHeight(RecyclerView rvCategories) {
        if (itemHeight == null) {
            for (int i = 0; i < rvCategories.getChildCount(); i++) {
                itemHeight = rvCategories.getChildAt(i).getHeight();
                if (itemHeight != 0) break;
            }
        }
        return itemHeight;
    }

    @Override
    public int getOrientation() {
        return Orientation.ORIENTATION_VERTICAL;
    }

    @Override
    public void initSelectionContainer(ViewGroup selectionViewContainer) {
        selectionViewContainer.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        selectionViewContainer.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        selectionViewContainer.requestLayout();
    }

    @Override
    public void initPlaceHolderAndOverlay(@Nullable View overlayPlaceHolder, RecyclerView rvCategories, int overlaySize) {
        if (overlayPlaceHolder != null) {
            //make placeholder same height as a recyclerView
            overlayPlaceHolder.getLayoutParams().width = rvCategories.getMeasuredWidth();
            overlayPlaceHolder.requestLayout();

            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) rvCategories.getLayoutParams();
            marginLayoutParams.setMargins(0, 0, overlaySize, 0);
            rvCategories.requestLayout();
        }
    }

    @Override
    protected ISelectionGravityState retrieveGravityState(int gravityAttribute) {
        switch (gravityAttribute) {
            case LoopBarView.SELECTION_GRAVITY_START:
                return new TopGravityState();
            case LoopBarView.SELECTION_GRAVITY_END:
                return new BottomGravityState();
            default:
                throw new UnknownFormatFlagsException("unknown gravity Attribute = " + gravityAttribute + ". Should be one of SELECTION_GRAVITY");
        }
    }

    @Override
    public AbstractSpacesItemDecoration getOffsetItemDecoration() {
        return getGravityState().getOffsetItemDecoration();
    }

    static class TopGravityState implements ISelectionGravityState {

        private SpacesTopItemDecoration itemDecoration = new SpacesTopItemDecoration(0);

        @Override
        public int getSelectionGravity() {
            return Gravity.TOP;
        }

        @Override
        public <T extends ViewGroup.MarginLayoutParams> T setSelectionMargin(int marginPx, T layoutParams) {
            layoutParams.topMargin = marginPx;
            return layoutParams;
        }

        @Override
        public AbstractSpacesItemDecoration getOffsetItemDecoration() {
            return itemDecoration;
        }
    }

    static class BottomGravityState implements ISelectionGravityState {

        private SpacesBottomItemDecoration itemDecoration = new SpacesBottomItemDecoration(0);

        @Override
        public int getSelectionGravity() {
            return Gravity.BOTTOM;
        }

        @Override
        public <T extends ViewGroup.MarginLayoutParams> T setSelectionMargin(int marginPx, T layoutParams) {
            layoutParams.bottomMargin = marginPx;
            return layoutParams;
        }

        @Override
        public AbstractSpacesItemDecoration getOffsetItemDecoration() {
            return itemDecoration;
        }
    }
}
