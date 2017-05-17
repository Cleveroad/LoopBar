package com.cleveroad.loopbar.widget;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Interface with pre-defined limit of layout gravity for LoopBar
 */
@IntDef({android.view.Gravity.TOP, android.view.Gravity.BOTTOM, android.view.Gravity.LEFT,
        android.view.Gravity.RIGHT, android.view.Gravity.START, android.view.Gravity.END})
@Retention(RetentionPolicy.SOURCE)
@interface LayoutGravity {
}
