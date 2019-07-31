package com.cleveroad.loopbar.widget;


import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static android.view.Gravity.BOTTOM;
import static android.view.Gravity.END;
import static android.view.Gravity.LEFT;
import static android.view.Gravity.RIGHT;
import static android.view.Gravity.START;
import static android.view.Gravity.TOP;


/**
 * Interface with pre-defined limit of layout gravity for LoopBar
 */
@IntDef({TOP, BOTTOM, LEFT, RIGHT, START, END})
@Retention(RetentionPolicy.SOURCE)
@interface LayoutGravity {
}
