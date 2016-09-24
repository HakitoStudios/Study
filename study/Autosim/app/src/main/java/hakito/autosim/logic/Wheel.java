package hakito.autosim.logic;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Created by Oleg on 31-Oct-15.
 */
public class Wheel extends WorldObject {
    float rotation;
    Matrix matrix;
float radius;

    public Wheel(WorldObject parent, PointF position, Bitmap b, Rect src, float radius) {
        super(parent, position, b, src, new RectF(0, 0, radius, radius));
        matrix = new Matrix();
this.radius = radius;






    }

    public void rotate(float d)
    {
        rotation+=360*d /(radius*2*Math.PI);
    }

    @Override
    public void draw(Canvas c) {
        if(bitmap==null)return;
        float pr = Game.MtoPX(radius);
        matrix.reset();
        matrix.postScale((float) 2 * pr / src.width(), (float) 2 * pr / src.height());

        PointF pos = Game.MtoPX(getPosition());
        matrix.postTranslate(pos.x, pos.y);

        matrix.postRotate(rotation, pos.x + pr, pos.y + pr);

        c.drawBitmap(bitmap, matrix, p);
    }
}
