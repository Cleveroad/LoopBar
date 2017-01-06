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

class OrientationStateHorizontal extends AbstractOrientationState implements IOrientationState {

    private Integer itemWidth;

    OrientationStateHorizontal() {
    }

    @Override
    public LinearLayoutManager getLayoutManager(Context context) {
        return new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
    }

    @Override
    public int getLayoutId() {
        return R.layout.enls_view_categories_navigation_horizontal;
    }

    @Override
    public boolean isItemsFitOnScreen(RecyclerView recyclerView, int itemsSize) {
        calcItemWidth(recyclerView);
        int itemsWidth = itemWidth * (itemsSize);
        int containerWidth = recyclerView.getMeasuredWidth();
        return containerWidth >= itemsWidth;
    }

    @Override
    public int getHeaderSize(Context context) {
        if (context == null) {
            return 0;
        } else {
            return context.getResources().getDimensionPixelSize(R.dimen.enls_selected_view_width)
                    + 2 * getHeaderMargins(context);
        }
    }

    @Override
    public int getHeaderMargins(Context context) {
        if (context == null) {
            return 0;
        } else {
            return context.getResources().getDimensionPixelSize(R.dimen.enls_margin_selected_view);
        }
    }

    @Override
    public int getSize(View selector) {
        if (selector == null) {
            return 0;
        } else {
            return selector.getMeasuredWidth();
        }
    }

    @Override
    public AbstractSpacesItemDecoration getSelectionViewItemDecoration(int margin, int selectionViewWidth, int selectionViewHeight) {
        AbstractSpacesItemDecoration itemDecoration = getGravityState().getOffsetItemDecoration();
        itemDecoration.setSpace(margin + selectionViewWidth);
        return itemDecoration;
    }

    @Override
    public int getOrientation() {
        return Orientation.ORIENTATION_HORIZONTAL;
    }

    @Override
    public void initSelectionContainer(ViewGroup selectionViewContainer) {
        selectionViewContainer.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
        selectionViewContainer.requestLayout();
    }

    //very big duct tape
    private int calcItemWidth(RecyclerView rvCategories) {
        if (itemWidth == null || itemWidth == 0) {
            for (int i = 0; i < rvCategories.getChildCount(); i++) {
                itemWidth = rvCategories.getChildAt(i).getWidth();
                if (itemWidth != 0) {
                    break;
                }
            }
        }
        // in case of call before view was created
        if (itemWidth == null) {
            itemWidth = 0;
        }
        return itemWidth;
    }

    @Override
    public void initPlaceHolderAndOverlay(@Nullable View overlayPlaceHolder,
                                          View rvContainer, View selectorContainer, int overlaySize) {
        if (overlayPlaceHolder != null) {
            int containerHeight = rvContainer.getMeasuredHeight();

            //make placeholder same height as a recyclerView
            overlayPlaceHolder.getLayoutParams().height = containerHeight;
            overlayPlaceHolder.requestLayout();

            selectorContainer.getLayoutParams().height = containerHeight + overlaySize;
            selectorContainer.requestLayout();
        }
    }

    @Override
    protected ISelectionGravityState retrieveGravityState(int gravityAttribute) {
        switch (gravityAttribute) {
            case LoopBarView.SELECTION_GRAVITY_START:
                return new StartGravityState();
            case LoopBarView.SELECTION_GRAVITY_END:
                return new EndGravityState();
            default:
                throw new UnknownFormatFlagsException("unknown gravity Attribute = " + gravityAttribute + ". Should be one of SELECTION_GRAVITY");
        }
    }

    @Override
    public AbstractSpacesItemDecoration getOffsetItemDecoration() {
        return getGravityState().getOffsetItemDecoration();
    }

    private static class StartGravityState implements ISelectionGravityState {

        private SpacesLeftItemDecoration itemDecoration = new SpacesLeftItemDecoration(0);

        @Override
        public int getSelectionGravity() {
            return Gravity.START;
        }

        @Override
        public <T extends ViewGroup.MarginLayoutParams> T setSelectionMargin(int marginPx, T layoutParams) {
            layoutParams.leftMargin = marginPx;
            return layoutParams;
        }

        @Override
        public AbstractSpacesItemDecoration getOffsetItemDecoration() {
            return itemDecoration;
        }
    }

    private static class EndGravityState implements ISelectionGravityState {

        private SpacesRightItemDecoration itemDecoration = new SpacesRightItemDecoration(0);

        @Override
        public int getSelectionGravity() {
            return Gravity.END;
        }

        @Override
        public <T extends ViewGroup.MarginLayoutParams> T setSelectionMargin(int marginPx, T layoutParams) {
            layoutParams.rightMargin = marginPx;
            return layoutParams;
        }

        @Override
        public AbstractSpacesItemDecoration getOffsetItemDecoration() {
            return itemDecoration;
        }
    }
}
