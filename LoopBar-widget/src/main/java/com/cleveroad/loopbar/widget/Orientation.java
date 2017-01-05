package com.cleveroad.loopbar.widget;

import android.support.annotation.IntDef;

/**
 * Interface with pre-defined constants for orientation of LoopBar
 */
@IntDef({Orientation.ORIENTATION_VERTICAL, Orientation.ORIENTATION_HORIZONTAL})
public @interface Orientation {
    
    /**
     * Constant representing vertical orientation state of LoopBar
     */
    int ORIENTATION_VERTICAL = 0;

    /**
     * Constant representing horizontal orientation state of LoopBar
     */
    int ORIENTATION_HORIZONTAL = 1;
}
