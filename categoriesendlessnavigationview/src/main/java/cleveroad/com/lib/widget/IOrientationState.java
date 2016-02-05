package cleveroad.com.lib.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

interface IOrientationState extends ISelectionGravityState {
    LinearLayoutManager getLayoutManager(Context context);

    int getLayoutId();

    boolean isItemsFitOnScreen(int containerWidth, int containerHeight, int itemWidth, int itemHeight, int itemsSize);
}
