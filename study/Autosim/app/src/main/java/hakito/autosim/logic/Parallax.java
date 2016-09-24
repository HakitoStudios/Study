package hakito.autosim.logic;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Size;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oleg on 08-Nov-15.
 */
public class Parallax {
    Paint p;
    Bitmap bitmap;
    Rect dest;
    Rect[] src;
    float k;
    int n;
    PointF pSize;
    List<Integer> indices;

    public Parallax(Bitmap b, float k, Rect dest, Rect... src) {
        bitmap=b;
        p = new Paint();

        this.k=k;
        this.dest = dest;
        this.src = src;

        float dk = dest.height()*1.0f / src[0].height();
        pSize = new PointF((dk*src[0].width()), (dk*src[0].height()));
        n = (int)(Game.getGame().w / pSize.x)+2;
        indices = new ArrayList<>();

        for (int i=0;i<n*5;i++)
        {
         indices.add(Game.random.nextInt(src.length));
        }
    }

    public void draw(Canvas c, float scroll)
    {

        float pxRaw = (scroll*k);
        int offset = (int)(pxRaw / pSize.x);
        float px = pxRaw% pSize.x;
        c.save();
        c.translate(px, 0);

        for (int i = -1; i < n; i++) {
            float x = i * pSize.x, y = dest.top;
           // c.drawText(String.format("%d %d", offset,i), x, y, World.debugPaint);
            c.drawBitmap(bitmap, src[indices.get((-offset%indices.size()+i+indices.size())%indices.size())], new RectF(x, y, x + pSize.x, y + pSize.y), p);
        }
        c.restore();
    }

}
