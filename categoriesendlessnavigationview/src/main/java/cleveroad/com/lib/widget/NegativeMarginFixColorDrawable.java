package cleveroad.com.lib.widget;

import android.graphics.Canvas;
import android.graphics.Region;

public class NegativeMarginFixColorDrawable extends android.graphics.drawable.ColorDrawable {

    public NegativeMarginFixColorDrawable(int color){
        super(color);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.clipRect(getBounds(), Region.Op.REPLACE);
        super.draw(canvas);
    }
}
