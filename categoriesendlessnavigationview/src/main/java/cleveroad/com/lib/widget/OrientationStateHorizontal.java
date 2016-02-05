package cleveroad.com.lib.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.ViewGroup;

import java.util.UnknownFormatFlagsException;

import cleveroad.com.lib.R;

class OrientationStateHorizontal extends AbstractOrientationState implements IOrientationState {

    OrientationStateHorizontal(@EndlessNavigationView.GravityAttr int gravityAttribute) {
        super(gravityAttribute);
    }

    @Override
    public LinearLayoutManager getLayoutManager(Context context) {
        return new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
    }

    @Override
    public int getLayoutId() {
        return R.layout.view_categories_navigation_horizontal;
    }

    @Override
    public boolean isItemsFitOnScreen(int containerWidth, int containerHeight, int itemWidth, int itemHeight, int itemsSize) {
        int itemsWidth = itemWidth * (itemsSize - 1);
        return containerWidth >= itemsWidth;
    }

    @Override
    protected ISelectionGravityState getGravityState(int gravityAttribute) {
        switch (gravityAttribute) {
            case EndlessNavigationView.SELECTION_GRAVITY_START:
                return new StartGravityState();
            case EndlessNavigationView.SELECTION_GRAVITY_END:
                return new EndGravityState();
            default:
                throw new UnknownFormatFlagsException("unknown gravity Attribute = " + gravityAttribute + ". Should be one of SELECTION_GRAVITY");
        }
    }

    static class StartGravityState implements ISelectionGravityState {

        @Override
        public int getSelectionGravity() {
            return Gravity.START;
        }

        @Override
        public <T extends ViewGroup.MarginLayoutParams> T setSelectionMargin(int marginPx, T layoutParams) {
            layoutParams.leftMargin = marginPx;
            return layoutParams;
        }
    }

    static class EndGravityState implements ISelectionGravityState {
        @Override
        public int getSelectionGravity() {
            return Gravity.END;
        }

        @Override
        public <T extends ViewGroup.MarginLayoutParams> T setSelectionMargin(int marginPx, T layoutParams) {
            layoutParams.rightMargin = marginPx;
            return layoutParams;
        }
    }
}
