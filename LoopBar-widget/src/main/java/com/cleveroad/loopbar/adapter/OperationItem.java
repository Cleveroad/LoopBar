package com.cleveroad.loopbar.adapter;

public class OperationItem implements IOperationItem {

    private boolean isVisible = true;

    @Override
    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }
}
