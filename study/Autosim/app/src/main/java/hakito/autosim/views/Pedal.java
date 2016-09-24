package hakito.autosim.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Toast;

import hakito.autosim.R;

/**
 * Created by Oleg on 31-Oct-15.
 */
public class Pedal extends View implements View.OnTouchListener {

    public void setSetZero(boolean setZero) {
        this.setZero = setZero;
    }

    public interface OnPedalChangedListener {
        void onChanged(Pedal p);

    }

    private OnPedalChangedListener listener;
    private int width, height, activeHeight, tHeight;
    private int padd;
    private double progress, max;
    private Paint rPaint, bPaint, dPaint;
    private Drawable drawable;
    private Rect backRect;
    private boolean setZero;

    public Pedal(Context context, AttributeSet attrs) {
        super(context, attrs);
        padd = 30;
        tHeight=padd;
        rPaint = new Paint();
        rPaint.setColor(Color.GREEN);

        bPaint = new Paint();
        bPaint.setColor(Color.argb(31, 168, 178, 255));
setZero=true;
        max = 1;
        setProgress(0);

        dPaint = new Paint();
        dPaint.setColor(Color.BLACK);
        dPaint.setTextSize(25);

        setOnTouchListener(this);
        drawable = getResources().getDrawable(R.drawable.pedal_norm);
    }

    public void setOnChangedListener(OnPedalChangedListener l) {
        listener = l;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int w = MeasureSpec.getSize(widthMeasureSpec), h = MeasureSpec.getSize(heightMeasureSpec);
        w = 70;
        setMeasuredDimension(w, h);
        width = w;
        height = h;
        backRect = new Rect(padd, padd, w-padd, h - padd);
        activeHeight = h - padd*2;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        int y = (int) (progress / max * activeHeight);

        canvas.drawRect(backRect, bPaint);
        int ty = y + tHeight / 2;
        drawable.setBounds(0, ty, width, ty + tHeight);
        if (touched) {
            drawable.setState(new int[]{android.R.attr.state_pressed});
        } else {
            drawable.setState(new int[0]);
        }
        drawable.draw(canvas);
        //canvas.drawText("" + progress, 0, 100, dPaint);

    }

    public void setProgress(double progress) {
        if (progress < 0)progress=0;
        if( progress > max)
            progress=max;
        this.progress = progress;
        invalidate();
        if (listener != null) {
            listener.onChanged(this);
        }
    }

    public double getProgress() {
        return progress;
    }

    boolean touched;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
if(event.getAction()== MotionEvent.ACTION_UP)
{
    if(setZero) {
        setProgress(0);
        touched = false;
        return true;
    }
}
        touched = true;
        setProgress((double) (event.getY() - padd) / activeHeight * max);
        return true;
    }
}
