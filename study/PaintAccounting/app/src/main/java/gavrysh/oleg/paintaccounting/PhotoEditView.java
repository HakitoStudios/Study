package gavrysh.oleg.paintaccounting;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.graphics.RectF;

import java.util.ArrayList;
import java.util.List;

import gavrysh.oleg.paintaccounting.Models.Painting;

/**
 * Created by Oleg on 16-Nov-15.
 */
public class PhotoEditView extends View implements View.OnTouchListener {

    private Bitmap bitmap;
    private Bitmap result;
    private Paint paint;
    private Matrix matrix;

    private List<Corner> corners;

    private float getDist(Corner a, Corner b)
    {
        return (float)(Math.sqrt(Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2)));
    }

    private float avg(float a, float b)
    {
        return (a+b)/2;
    }

    private Point getSize()
    {
        float width = 512;
        float k = avg(getDist(corners.get(1), corners.get(2)), getDist(corners.get(0), corners.get(3)))/
                avg(getDist(corners.get(0), corners.get(1)), getDist(corners.get(2), corners.get(3)));
        return new Point((int)width, (int)(width*k));
    }

    public Bitmap getBitmap() {
        if(bitmap==null)return null;
        Point size = getSize();
        Bitmap res = Bitmap.createBitmap(size.x, size.y, Bitmap.Config.RGB_565);
        Canvas c = new Canvas(res);
        c.drawBitmap(bitmap, matrix, paint);
        return res;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;

        Log.d(TAG, "setBitmap");

        invalidate();
    }

    public PhotoEditView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        corners = new ArrayList<>();
        corners.add(new Corner(100, 100));
        corners.add(new Corner(300, 100));
        corners.add(new Corner(300, 300));
        corners.add(new Corner(100, 300));
        setOnTouchListener(this);
        matrix = new Matrix();



    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d(TAG, "measure");
        float w=getMeasuredWidth(), h=getMeasuredHeight();
        if(bitmap!=null) {
            matrix.setRectToRect(new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight()), new RectF(0, 0, w, h), Matrix.ScaleToFit.START);
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(TAG, "draw");
        if(bitmap!=null)
        {

            canvas.drawBitmap(bitmap, matrix, paint);
            for (Corner corner:corners) {
                corner.draw(canvas);
            }
            canvas.drawLine(corners.get(0).x, corners.get(0).y, corners.get(1).x, corners.get(1).y, paint);
            canvas.drawLine(corners.get(2).x, corners.get(2).y, corners.get(1).x, corners.get(1).y, paint);
            canvas.drawLine(corners.get(2).x, corners.get(2).y, corners.get(3).x, corners.get(3).y, paint);
            canvas.drawLine(corners.get(0).x, corners.get(0).y, corners.get(3).x, corners.get(3).y, paint);
        }
    }

    public void crop()
    {
        Point size = getSize();
        int w=size.x, h=size.y;


        float[] dest = new float[]{0, 0, w, 0, w, h, 0, h};
        float[] source = new float[]{corners.get(0).getX(), corners.get(0).getY(), corners.get(1).getX(), corners.get(1).getY(), corners.get(2).getX(), corners.get(2).getY(), corners.get(3).getX(), corners.get(3).getY()};
        Matrix t = new Matrix();
        //t.setRectToRect(new RectF(0, 0, w, h), new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight()),  Matrix.ScaleToFit.END);
        matrix.invert(t);
        t.mapPoints(source);

        matrix.setPolyToPoly(source, 0, dest, 0, 4);

        invalidate();
    }

    private Corner getCorner(float x, float y) {
        for (Corner c : corners) {
            if (c.interects(x, y)) {
                return c;
            }
        }
        return null;
    }

    private Corner getActive() {
        for (Corner corner : corners) {
            if (corner.isActive()) {
                return corner;
            }
        }
        return null;
    }

    private float px, py;

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                Corner c = getCorner(event.getX(), event.getY());
                if(c!=null)
                {
                    Corner a = getActive();
                    if(a!=null) {a.setActive(false);
                    }
                    c.setActive(true);

                }
                else
                {

                }px = event.getX();
                py = event.getY();
                    break;
            case MotionEvent.ACTION_UP:break;
            case MotionEvent.ACTION_MOVE:
                Corner cc = getActive();
                if(cc!=null)
                {
                    cc.offset(event.getX() - px, event.getY() - py);

                }
                px = event.getX();
                py = event.getY();
                break;
        }



        invalidate();
        return true;
    }

    static final String TAG = "mytag";

    class Corner
    {
        private final int l=20;
        private final int activeColor = getResources().getColor(R.color.corner_active), inactiveColor = getResources().getColor(R.color.corner_norm), thickInactive=1, thickActive=3;

        private float x, y;

        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        public boolean isActive() {
            return active;
        }

        private boolean active;
        private Paint paintActive, paintInactive;


        public Corner(int x, int y) {


            paintActive = new Paint();
            paintActive.setColor(activeColor);
            paintActive.setStrokeWidth(thickActive);

            paintInactive = new Paint();
            paintInactive.setColor(inactiveColor);
            paintInactive.setStrokeWidth(thickInactive);

            this.x = x;
            this.y = y;
        }

        public void offset(float x, float y)
        {
        this.x+=x;
            this.y+=y;
        }

        public boolean interects(float x, float y)
        {
            return Math.sqrt(Math.pow(x-this.x, 2) + Math.pow(y-this.y, 2))<l;
        }

        @Override
        public String toString() {
            return "Corner{" +
                    "x=" + x +
                    ", y=" + y +
                    ", active=" + active +
                    '}';
        }

        public void draw(Canvas c)
        {
            Paint p;
            if(!active)
            {
                p  = paintInactive;
            }
            else
            {
                p=paintActive;
            }
            c.drawLine(x-l, y, x+l, y, p);
            c.drawLine(x, y-l, x, y+l, p);
        }
    }
}
