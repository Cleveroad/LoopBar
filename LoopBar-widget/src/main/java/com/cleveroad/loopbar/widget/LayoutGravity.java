package com.cleveroad.loopbar.widget;

import android.support.annotation.IntDef;

/**
 * Interface with pre-defined limit of layout gravity for LoopBar
 */
@IntDef({android.view.Gravity.TOP, android.view.Gravity.BOTTOM, android.view.Gravity.LEFT,
        android.view.Gravity.RIGHT, android.view.Gravity.START, android.view.Gravity.END})
@interface LayoutGravity {
}
