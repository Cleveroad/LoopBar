package com.cleveroad.loopbar.widget;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Interface with pre-defined constants for orientation of LoopBar
 */
@IntDef({Orientation.ORIENTATION_VERTICAL_LEFT, Orientation.ORIENTATION_VERTICAL_RIGHT,
        Orientation.ORIENTATION_HORIZONTAL_TOP, Orientation.ORIENTATION_HORIZONTAL_BOTTOM})
@Retention(RetentionPolicy.SOURCE)
public @interface Orientation {

    /**
     * Constant representing vertical left orientation state of LoopBar
     */
    int ORIENTATION_VERTICAL_LEFT = 0;

    /**
     * Constant representing vertical right orientation state of LoopBar
     */
    int ORIENTATION_VERTICAL_RIGHT = 1;

    /**
     * Constant representing horizontal top orientation state of LoopBar
     */
    int ORIENTATION_HORIZONTAL_TOP = 2;

    /**
     * Constant representing horizontal bottom orientation state of LoopBar
     */
    int ORIENTATION_HORIZONTAL_BOTTOM = 3;
}
