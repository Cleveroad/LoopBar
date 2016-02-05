package cleveroad.com.lib.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.ViewGroup;

import java.util.UnknownFormatFlagsException;

import cleveroad.com.lib.R;

class OrientationStateVertical extends AbstractOrientationState implements IOrientationState {

    OrientationStateVertical(int gravityAttribute) {
        super(gravityAttribute);
    }

    @Override
    public LinearLayoutManager getLayoutManager(Context context) {
        return new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
    }

    @Override
    public int getLayoutId() {
        return R.layout.view_categories_navigation_vertical;
    }

    @Override
    public boolean isItemsFitOnScreen(int containerWidth, int containerHeight, int itemWidth, int itemHeight, int itemsSize) {
        int itemsHeight = itemHeight * (itemsSize - 1);
        return containerHeight >= itemHeight;
    }

    @Override
    protected ISelectionGravityState getGravityState(int gravityAttribute) {
        switch (gravityAttribute) {
            case EndlessNavigationView.SELECTION_GRAVITY_START:
                return new TopGravityState();
            case EndlessNavigationView.SELECTION_GRAVITY_END:
                return new BottomGravityState();
            default:
                throw new UnknownFormatFlagsException("unknown gravity Attribute = " + gravityAttribute + ". Should be one of SELECTION_GRAVITY");
        }
    }

    static class TopGravityState implements ISelectionGravityState {

        @Override
        public int getSelectionGravity() {
            return Gravity.TOP;
        }

        @Override
        public <T extends ViewGroup.MarginLayoutParams> T setSelectionMargin(int marginPx, T layoutParams) {
            layoutParams.topMargin = marginPx;
            return layoutParams;
        }
    }

    static class BottomGravityState implements ISelectionGravityState {

        @Override
        public int getSelectionGravity() {
            return Gravity.BOTTOM;
        }

        @Override
        public <T extends ViewGroup.MarginLayoutParams> T setSelectionMargin(int marginPx, T layoutParams) {
            layoutParams.bottomMargin = marginPx;
            return layoutParams;
        }
    }
}
