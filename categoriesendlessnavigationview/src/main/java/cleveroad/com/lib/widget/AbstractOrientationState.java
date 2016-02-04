package cleveroad.com.lib.widget;

import android.view.ViewGroup;

abstract class AbstractOrientationState implements IOrientationState {
    private ISelectionGravityState selectionGravityState;

    AbstractOrientationState(@EndlessNavigationView.GravityAttr int gravityAttribute) {
        selectionGravityState = getGravityState(gravityAttribute);
    }

    @Override
    public int getSelectionGravity() {
        return selectionGravityState.getSelectionGravity();
    }

    protected abstract ISelectionGravityState getGravityState(int gravityAttribute);

    //dispatch to gravity state
    @Override
    public <T extends ViewGroup.MarginLayoutParams> T setSelectionMargin(int marginPx, T layoutParams) {
        return selectionGravityState.setSelectionMargin(marginPx, layoutParams);
    }
}
