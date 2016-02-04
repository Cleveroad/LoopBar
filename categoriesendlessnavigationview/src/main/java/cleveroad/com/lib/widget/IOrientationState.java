package cleveroad.com.lib.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

interface IOrientationState extends ISelectionGravityState {
    LinearLayoutManager getLayoutManager(Context context);

    int getLayoutId();
}
