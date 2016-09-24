package hakito.trycatch.Game.Objects;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

/**
 * Created by Oleg on 02-Jan-16.
 */
public abstract class BasicCell implements UpdatableObject, DrawableObject{
    protected int x, y;
    protected Paint p;

    public BasicCell(int x, int y) {
        p = new Paint();
        p.setAntiAlias(true);
        this.x = x;
        this.y = y;
    }
    public Point position() {
        return new Point(x, y);
    }
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isFree()
    {
        return false;
    }

    public abstract void update(float dt);

    public abstract void draw(Canvas c);
}
