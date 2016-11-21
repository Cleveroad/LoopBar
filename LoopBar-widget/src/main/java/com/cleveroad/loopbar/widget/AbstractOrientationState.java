package com.cleveroad.loopbar.widget;

import android.view.ViewGroup;

abstract class AbstractOrientationState implements IOrientationState {

    private ISelectionGravityState mSelectionGravityState;

    AbstractOrientationState() {
        mSelectionGravityState = retrieveGravityState(LoopBarView.SELECTION_GRAVITY_START);
    }

    @Override
    public final int getSelectionGravity() {
        return mSelectionGravityState.getSelectionGravity();
    }

    @Override
    public final void setSelectionGravity(@LoopBarView.GravityAttr int selectionGravity) {
        mSelectionGravityState = retrieveGravityState(selectionGravity);
    }

    protected abstract ISelectionGravityState retrieveGravityState(int gravityAttribute);

    @SuppressWarnings("WeakerAccess")
    protected ISelectionGravityState getGravityState() {
        return mSelectionGravityState;
    }

    //dispatch to gravity state
    @Override
    public <T extends ViewGroup.MarginLayoutParams> T setSelectionMargin(int marginPx, T layoutParams) {
        return mSelectionGravityState.setSelectionMargin(marginPx, layoutParams);
    }
}
