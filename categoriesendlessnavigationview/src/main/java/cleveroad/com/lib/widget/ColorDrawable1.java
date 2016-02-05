package cleveroad.com.lib.widget;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Region;

public class ColorDrawable1 extends android.graphics.drawable.ColorDrawable {

    public ColorDrawable1(int color){
        super(color);
    }

    @Override
    public void draw(Canvas canvas) {
//        Paint paint = new Paint();
//        paint.setColor(getColor());
        Rect bounds = new Rect(getBounds());
        bounds.top = bounds.top - 10;
        canvas.clipRect(bounds, Region.Op.REPLACE);
        super.draw(canvas);
    }
}
