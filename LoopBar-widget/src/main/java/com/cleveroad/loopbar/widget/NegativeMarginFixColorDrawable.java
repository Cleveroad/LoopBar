package com.cleveroad.loopbar.widget;

import android.graphics.Canvas;
import android.graphics.Region;
import android.os.Build;

class NegativeMarginFixColorDrawable extends android.graphics.drawable.ColorDrawable {

    NegativeMarginFixColorDrawable(int color) {
        super(color);
    }

    @Override
    public void draw(Canvas canvas) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            canvas.clipRect(getBounds());
        } else {
            canvas.clipRect(getBounds(), Region.Op.REPLACE);
        }
        super.draw(canvas);
    }
}
