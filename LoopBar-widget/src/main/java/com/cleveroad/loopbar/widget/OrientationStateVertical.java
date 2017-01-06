package com.cleveroad.loopbar.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.cleveroad.loopbar.R;

import java.util.UnknownFormatFlagsException;

class OrientationStateVertical extends AbstractOrientationState implements IOrientationState {

    private Integer itemHeight;

    OrientationStateVertical() {
    }

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
    public int getHeaderSize(Context context) {
        if (context == null) {
            return 0;
        } else {
            return context.getResources().getDimensionPixelSize(R.dimen.enls_selected_view_height)
                    + 2 * getHeaderMargins(context);
        }
    }

    @Override
    public int getHeaderMargins(Context context) {
        if (context == null) {
            return 0;
        } else {
            return context.getResources().getDimensionPixelOffset(R.dimen.enls_margin_selected_view);
        }
    }

    @Override
    public int getSize(View selector) {
        if (selector == null) {
            return 0;
        } else {
            return selector.getMeasuredHeight();
        }
    }

    @Override
    public AbstractSpacesItemDecoration getSelectionViewItemDecoration(int margin, int selectionViewWidth, int selectionViewHeight) {
        AbstractSpacesItemDecoration itemDecoration = getOffsetItemDecoration();
        itemDecoration.setSpace(margin + selectionViewHeight);
        return itemDecoration;
    }

    private int calcItemHeight(RecyclerView rvCategories) {
        if (itemHeight == null || itemHeight == 0) {
            for (int i = 0; i < rvCategories.getChildCount(); i++) {
                itemHeight = rvCategories.getChildAt(i).getHeight();
                if (itemHeight != 0) {
                    break;
                }
            }
        }
        // in case of call before view was created
        if (itemHeight == null) {
            itemHeight = 0;
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
    public void initPlaceHolderAndOverlay(@Nullable View overlayPlaceHolder, View rvContainer,
                                          View selectorContainer, int overlaySize) {
        if (overlayPlaceHolder != null) {
            int containerWidth = rvContainer.getMeasuredWidth();

            //make placeholder same height as a recyclerView
            overlayPlaceHolder.getLayoutParams().width = containerWidth;
            overlayPlaceHolder.requestLayout();

            selectorContainer.getLayoutParams().width = containerWidth + overlaySize;
            selectorContainer.requestLayout();

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

    private static class TopGravityState implements ISelectionGravityState {

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

    private static class BottomGravityState implements ISelectionGravityState {

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
