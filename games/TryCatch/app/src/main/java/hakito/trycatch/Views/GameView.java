package hakito.trycatch.Views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import hakito.trycatch.Game.Game;
import hakito.trycatch.R;

/**
 * Created by Oleg on 12-Dec-15.
 */
public class GameView extends View implements View.OnTouchListener{

    Matrix matrix;


    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setOnTouchListener(this);
    }



    @Override
    protected void onDraw(Canvas canvas) {
        if(isInEditMode())
        {
            String text = "Here will be game!";
            Paint p  = new Paint();
            p.setColor(getResources().getColor(R.color.colorAccent));
            p.setTextSize(40);
            Rect bounds = new Rect();
            p.getTextBounds(text, 0, text.length() - 1, bounds);
            canvas.drawText(text, getWidth() / 2 - bounds.width() / 2, getHeight() / 2 - bounds.height() / 2, p);

        }
        else
        {
        Game.get().update();

        float of=(getY() + getHeight())/2;
        //matrix.postRotate(0.5f, of, of);
        canvas.concat(matrix);

        Game.get().draw(canvas);
        invalidate();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int s = Math.min(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
        setMeasuredDimension(s, s);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if(isInEditMode())return;

        final int padding = 10;
        Point originalSize = Game.get().getSizePX();
        int screenL = Math.min(w, h);
        RectF original = new RectF(0, 0, originalSize.x, originalSize.y), dest = new RectF(padding, padding, screenL-padding, screenL - padding);
        matrix = new Matrix();
//dest = new RectF(0, 0, 200, 200);
        matrix.setRectToRect(original, dest, Matrix.ScaleToFit.START);

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                float[] m = new float[]{event.getX(), event.getY()};
                Matrix invert=new Matrix();
                matrix.invert(invert);
                invert.mapPoints(m);


                long t = System.currentTimeMillis();
                Game.get().touch((int) m[0], (int) m[1]);
                Game.get().resume();

                break;
        }
        return true;
    }
}
