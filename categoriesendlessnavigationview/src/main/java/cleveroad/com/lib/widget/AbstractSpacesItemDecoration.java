package cleveroad.com.lib.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

class AbstractSpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public AbstractSpacesItemDecoration(int space) {
        this.space = space;
    }

    public void setSpace(int space) {
        this.space = space;
    }

    protected int getSpace() {
        return space;
    }
}