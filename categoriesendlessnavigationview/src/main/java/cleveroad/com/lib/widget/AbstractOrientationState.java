package cleveroad.com.lib.widget;

import android.view.ViewGroup;

abstract class AbstractOrientationState implements IOrientationState {
    private ISelectionGravityState selectionGravityState;

    AbstractOrientationState() {
        selectionGravityState = getGravityState(EndlessNavigationView.SELECTION_GRAVITY_START);
    }

    @Override
    public final int getSelectionGravity() {
        return selectionGravityState.getSelectionGravity();
    }

    @Override
    public final void setSelectionGravity(@EndlessNavigationView.GravityAttr int selectionGravity) {
        selectionGravityState = getGravityState(selectionGravity);
    }

    protected abstract ISelectionGravityState getGravityState(int gravityAttribute);

    //dispatch to gravity state
    @Override
    public <T extends ViewGroup.MarginLayoutParams> T setSelectionMargin(int marginPx, T layoutParams) {
        return selectionGravityState.setSelectionMargin(marginPx, layoutParams);
    }
}
