package hakito.autosim.logic;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.DrawFilter;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Created by Oleg on 31-Oct-15.
 */
public class WorldObject implements DrawableObject {
     Paint p ;

    protected PointF position;
    private WorldObject parent;
    protected Bitmap bitmap;
    protected Rect src;
    private RectF dst;

    public WorldObject(WorldObject parent, PointF position, Bitmap b, Rect src, RectF dst) {
        this.parent = parent;
        this.position = position;
        this.bitmap = b;
        this.src = src;
        this.dst = dst;
        this.p= new Paint();
        this.p.setAntiAlias(true);
        p.setFilterBitmap(true);
    }

    protected RectF getDst()
    {
        PointF pp = getPosition();
        dst.offsetTo(pp.x, pp.y - dst.height());
        return dst;
    }

    public float getFrontApprox()
    {
    return getPosition().x + dst.width()/1.5f;
    }

    public final PointF getPosition()
    {
        if(parent==null)
        {
            return position;
        }
        PointF pp = parent.getPosition();
        return new PointF(pp.x + position.x, pp.y+position.y);
    }

    @Override
    public void draw(Canvas c) {
        if(bitmap==null)return;
        if(Game.getGame()!= null && !Game.getGame().world.needToDraw(dst)) return;

        c.drawBitmap(bitmap, src, Game.MtoPX(getDst()), p);
    }
}
