package hakito.autosim.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ProgressBar;

/**
 * Created by Oleg on 28-Nov-15.
 */
public class VerticalprogressBar extends ProgressBar {

    private int w, h, progress;
    Paint paint;
    Rect rect;

    public VerticalprogressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setColor(Color.GREEN);

        setIndeterminate(false);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        w = MeasureSpec.getSize(widthMeasureSpec);
        h = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        updRect();
    }

    void updRect()
    {
        float p=progress, m=getMax();
        float v = p/m;
        if(paint!=null)
        paint.setColor(Color.rgb((int)(255*(1-v)), (int)(255*v), 0));
        rect = new Rect(0, (int)((1-v) * h), w, h);
        invalidate();
    }

    @Override
    public synchronized void setProgress(int progress) {
        this.progress  = progress;
        updRect();



    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        canvas.drawColor(Color.LTGRAY);
        canvas.drawRect(rect, paint);
    }
}
