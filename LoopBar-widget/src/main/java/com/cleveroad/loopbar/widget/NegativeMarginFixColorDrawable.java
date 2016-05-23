package com.cleveroad.loopbar.widget;

import android.graphics.Canvas;
import android.graphics.Region;

public class NegativeMarginFixColorDrawable extends android.graphics.drawable.ColorDrawable {

    public NegativeMarginFixColorDrawable(int color){
        super(color);
    }

    @Override
    public void draw(Canvas canvas) {
//        Rect bounds = new Rect(getBounds());
//        bounds.top = bounds.top - 10;

        canvas.clipRect(getBounds(), Region.Op.REPLACE);
        super.draw(canvas);
    }
}
