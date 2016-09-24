package hakito.graphplayer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class GraphView extends View implements View.OnTouchListener {

    public interface OnUserItteractListener {
        void Itterac(View v);

    }

    class GPoint {
        final int ar=getResources().getDimensionPixelSize(R.dimen.activePointR), nr=getResources().getDimensionPixelSize(R.dimen.normPointR);
         int r;
        float x, y;
        Paint paint;


        public GPoint( float x, float y) {

            this.x = x;
            this.y = y;
            paint = new Paint();
            deactivate();
        }

        public void deactivate()
        {
            this.r = nr;
            paint.setColor(getResources().getColor(R.color.pointColor));
        }

        public void activate()
        {
            this.r = ar;
            paint.setColor(getResources().getColor(R.color.pointColorActive));
        }

        public float dist(float x) {
            return Math.abs(x - getRX());
        }

        public void offset(float dy) {
            y += dy;
            if (y < 0) y = 0;
            if (y > 1) y = 1;
        }


        public float getRX() {
            return x * w;
        }

        public float getRY() {
            return y * h;
        }


        public PointF getPoint() {
            return new PointF(getRX(), getRY());
        }

        public void draw(Canvas c) {
            c.drawCircle(getRX(), getRY(), r, paint);
        }
    }

    private OnUserItteractListener listener;

    public void setListener(OnUserItteractListener listener) {
        this.listener = listener;
    }

    List<GPoint> points;
    int h, w, n, blc;
    float py;
    float cw, ch;
    Paint linePaint, backLinePaint;
    GPoint active;

    public GraphView(Context context, AttributeSet attrs) {
        super(context, attrs);

        backLinePaint = new Paint();
        backLinePaint.setColor(getResources().getColor(R.color.graphBackLines));
        blc = 10;
        linePaint = new Paint();
        linePaint.setStrokeWidth(getResources().getDimensionPixelSize(R.dimen.lineThick));
        linePaint.setColor(getContext().getResources().getColor(R.color.colorAccent));
        points = new ArrayList<>();
        n = 10;
        for (int i = 0; i < n; i++) {
            points.add(new GPoint((float) (i) / (n - 1), 0.5f));
        }
        setOnTouchListener(this);
        setBackgroundResource(R.drawable.graph_back);
    }

    public void setPoints(float[] pp)
    {
        for (int i=0;i<pp.length; i++)
        {
            points.get(i).y = 1 - pp[i];
        }
    }

    public float[] getValues() {
        float[] res = new float[n];
        for (int i = 0; i < n; i++) {
            res[i] =  1 - points.get(i).y;
        }
        return res;
    }

    public float[] getPoints() {
        float[] res = new float[n];
        for (int i = 0; i < n; i++) {
            res[i] =  1 - points.get(i).y;
        }

        return res;
    }

    public  void linear()
    {
        for(int i=0; i < points.size(); i++)
        {
            points.get(i).y = (float)i /( points.size() - 1);
        }
        invalidate();
    }

    public void mirrorVertical() {
        for (GPoint p : points) {
            p.y = 1 - p.y;
        }
        sort();
        invalidate();
    }

    public void mirrorHorizontal() {
        for (GPoint p : points) {
            p.x = 1 - p.x;
        }
        sort();
        invalidate();
    }

    private void sort()
    {
        Collections.sort(points, new Comparator<GPoint>() {
            @Override
            public int compare(GPoint lhs, GPoint rhs) {
                return (int)(lhs.x*100 - rhs.x*100);
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        w = MeasureSpec.getSize(widthMeasureSpec);
        cw = (float) w / (n - 1);
        h = MeasureSpec.getSize(heightMeasureSpec);
        ch = (float) h / blc;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        for (int i = 0; i < blc; i++) {
            float y = (i * ch);
            canvas.drawLine(0, y, w, y, backLinePaint);
        }
        for (int i = 0; i < points.size(); i++) {
            float x = (i * cw);
            canvas.drawLine(x, 0, x, h, backLinePaint);
        }

        for (int i = 1; i < points.size(); i++) {

                canvas.drawLine(points.get(i).getRX(), points.get(i).getRY(), points.get(i - 1).getRX(), points.get(i - 1).getRY(), linePaint);
        }
        for (int i = 0; i < points.size(); i++) {
            points.get(i).draw(canvas);
        }
        if (listener != null) {
            listener.Itterac(this);
        }
    }


    private GPoint getPoint(float x) {
        GPoint point = points.get(0);
        for (GPoint p : points) {
            if (p.dist(x) < point.dist(x)) {
                point = p;
            }
        }
        return point;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            py = event.getY();

            active = getPoint(event.getX());
            if(active!=null)
            active.activate();

        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            if(active!=null)
                active.deactivate();

        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (active != null)
                active.offset((event.getY() - py) / h);
            py = event.getY();
        }
        invalidate();
        return true;
    }
}
