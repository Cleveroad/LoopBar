package com.cleveroad.loopbar.widget;

import android.graphics.Canvas;
import android.graphics.Region;

class NegativeMarginFixColorDrawable extends android.graphics.drawable.ColorDrawable {

    NegativeMarginFixColorDrawable(int color) {
        super(color);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.clipRect(getBounds(), Region.Op.REPLACE);
        super.draw(canvas);
    }
}
