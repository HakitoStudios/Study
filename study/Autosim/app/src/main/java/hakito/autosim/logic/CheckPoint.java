package hakito.autosim.logic;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Created by Oleg on 04-Nov-15.
 */
public class CheckPoint extends WorldObject implements UpdatableObject {

    private boolean last;
private     Level.ObjectType type;

    public interface OnGoneListner
    {
        void gone(CheckPoint checkPoint);
    }

    private OnGoneListner listner;

    public Level.ObjectType getType() {
        return type;
    }

    public void setOnGoneListener(OnGoneListner l)
   {
       listner=l;
   }

    public CheckPoint(WorldObject parent, PointF position, Bitmap b, Rect src, RectF dst, Level.ObjectType type) {
        super(parent, position, b, src, dst);
        this.type = type;
    }


    @Override
    public void update(double d) {

    }

    public void check(Car car)
    {
        if(car.getFrontApprox() > getPosition().x)
        {
            if(listner!=null && last==false)
            {
                last=true;
                listner.gone(this);
            }
        }
        else
        {
            last = false;
        }
    }
}
