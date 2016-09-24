package hakito.autosim.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Oleg on 01-Nov-15.
 */
public class Indicator extends View {
    private float min, max, value;
    private int cx, cy, l;
    private Paint paint;

    public Indicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        min = 2;
        max = (float)Math.PI*3-min;

        paint = new Paint();
        setValue(0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
       int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);
        int a = Math.min(w, h);
        cy = cx = a/2;
        l=cy - 10;
        setMeasuredDimension(a, a);
    }

    public  void setValue(float value)
    {
        this.value = value;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float angle = min + value*(max-min);
        canvas.drawCircle((int)(cx+ Math.cos(min)*l/2), (int)(cy+Math.sin(min)*l/2), 2, paint);
        //canvas.drawLine(cx, cy, (int)(cx+ Math.cos(max)*l*2), (int)(cy+Math.sin(max)*l*2), paint);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(3);
        canvas.drawLine(cx, cy, (int)(cx+ Math.cos(angle)*l), (int)(cy+Math.sin(angle)*l), paint);
        paint.reset();
        canvas.drawCircle(cx, cy, 5, paint);
    }
}
