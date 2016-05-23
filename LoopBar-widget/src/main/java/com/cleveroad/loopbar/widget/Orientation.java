package com.cleveroad.loopbar.widget;

import android.support.annotation.IntDef;

@IntDef({Orientation.ORIENTATION_VERTICAL, Orientation.ORIENTATION_HORIZONTAL})
public @interface Orientation {
    int ORIENTATION_VERTICAL = 0;
    int ORIENTATION_HORIZONTAL = 1;
}
