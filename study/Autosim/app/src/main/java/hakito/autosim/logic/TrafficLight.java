package hakito.autosim.logic;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Created by Oleg on 10-Nov-15.
 */
public class TrafficLight extends CheckPoint {


    private static final int  RED=0, RED_YELLOW=1,GREEN=2,  GREEN_BLINKING=3, YELLOW=4,  YELLOW_BLINKING=5;
    float blinkingD=0.5f, red=20, red_yellow=3, green=15, green_blinking=3, yellow=3;
    boolean tr, tg, ty;
    float h=0.72f, radius=0.1f;
    int state;
    Paint rowPaint, lightPaint, r, g, y;
    float elapsed;


    public TrafficLight(WorldObject parent, PointF position) {
        super(parent, position, null, null, new RectF(0, 0, 0.24f, 3f), Level.ObjectType.TRAFFIC_LIGHT);
        lightPaint = new Paint();
        rowPaint = new Paint();
        rowPaint.setColor(Color.GRAY);
        r = new Paint();
        g = new Paint();
        y = new Paint();
        elapsed = 0;
        state=0;
    }

    @Override
    public void draw(Canvas c) {
        RectF dr = Game.MtoPX(getDst());
        c.drawRect(new RectF(dr.left, dr.top, dr.right, dr.top + Game.MtoPX(h)), rowPaint);

        RectF rRect = new RectF(dr);
        rRect.inset(Game.MtoPX(0.07f), 0);
        c.drawRect(rRect, rowPaint);

        float cx = dr.left + dr.width()/2, yo = dr.top+dr.width()/2;

        float pr = Game.MtoPX(radius);

        c.drawCircle(cx, yo, pr, r);
        yo+=dr.width();
        c.drawCircle(cx, yo, pr, y);
        yo+=dr.width();
        c.drawCircle(cx, yo, pr, g);
    }

    public boolean isRed()
    {
        return state == RED || state == YELLOW;
    }

    void incrementState()
    {
        state = (state+1)%5;
        elapsed=0;
    }

    public int getColor()
    {
        switch (state)
        {
            case RED: return Color.RED;
            case YELLOW: return Color.YELLOW;
            case GREEN_BLINKING: return Color.YELLOW;
            default: return Color.GREEN;
        }

    }

    @Override
    public void update(double d) {
        elapsed+=d;
        switch (state)
        {
            case RED:if(elapsed > red)incrementState();break;
            case RED_YELLOW: if(elapsed > red_yellow)incrementState();break;
            case GREEN:if(elapsed > green)incrementState();break;
            case GREEN_BLINKING:if(elapsed > green_blinking)incrementState();break;
            case YELLOW:if(elapsed > yellow)incrementState();break;
        }

        tr=ty=tg=false;
        switch (state)
        {
            case RED:tr=true;break;
            case RED_YELLOW: tr=ty=true;break;
            case GREEN:tg=true;break;
            case GREEN_BLINKING:tg = ((int)(elapsed/blinkingD))%2==0;break;
            case YELLOW:ty=true;break;
            case YELLOW_BLINKING:ty = ((int)(elapsed/blinkingD))%2==0;break;
        }
        if(tr)r.setColor(Color.RED);else r.reset();
        if(ty)y.setColor(Color.YELLOW);else y.reset();
        if(tg)g.setColor(Color.GREEN);else g.reset();
    }
}
