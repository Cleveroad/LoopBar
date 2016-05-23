package com.cleveroad.loopbar.widget;

import android.view.ViewGroup;

abstract class AbstractOrientationState implements IOrientationState {
    private ISelectionGravityState selectionGravityState;

    AbstractOrientationState() {
        selectionGravityState = retrieveGravityState(LoopBarView.SELECTION_GRAVITY_START);
    }

    @Override
    public final int getSelectionGravity() {
        return selectionGravityState.getSelectionGravity();
    }

    @Override
    public final void setSelectionGravity(@LoopBarView.GravityAttr int selectionGravity) {
        selectionGravityState = retrieveGravityState(selectionGravity);
    }

    protected abstract ISelectionGravityState retrieveGravityState(int gravityAttribute);

    protected  ISelectionGravityState getGravityState() {
        return selectionGravityState;
    }

    //dispatch to gravity state
    @Override
    public <T extends ViewGroup.MarginLayoutParams> T setSelectionMargin(int marginPx, T layoutParams) {
        return selectionGravityState.setSelectionMargin(marginPx, layoutParams);
    }
}
