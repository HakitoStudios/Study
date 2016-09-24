package hakito.autosim.logic;


import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.RectF;

import java.util.Random;

public class Game {



    public interface OnChangedListener
    {
        void changed();

    }

    public void setOnChangedListener(OnChangedListener l)
    {if(l==null)return;
        listener = l;
        listener.changed();

    }

    public OnFinishListener finishListener;

    public interface OnFinishListener
    {
        void finish(GameResult result);

    }

    public void finish(GameResult result)
    {
        if(!finished && finishListener!=null)
        {
            finished=true;
         finishListener.finish(result);
        }
    }


    public void setOnFinishListener(OnFinishListener l)
    {if(l==null)return;
        finishListener = l;

    }

    private OnChangedListener listener;

private boolean finished;
    private static Game game;

    public static Random random = new Random();;

    private static float originalK= 40, k=originalK;

    public static void setK(float newK)
    {
        k = newK;
    }

    public static void restoreK()
    {
        k = originalK;
    }


    public static float PXtoM(float px)
    {
        return px/k;
    }

    public static float MtoPX(float m)
    {
        return m*k;
    }

    public static RectF MtoPX(RectF r)
    {
        return new RectF(r.left*k, r.top*k, r.right*k, r.bottom*k);
    }

    public static RectF PXtoM(RectF r)
    {
        return new RectF(r.left/k, r.top/k, r.right/k, r.bottom/k);
    }

    public static PointF MtoPX(PointF p)
    {
        return new PointF(p.x*k, p.y*k);
    }

    public static PointF PXtoM(PointF p)
    {
        return new PointF(p.x/k, p.y/k);
    }

    public double clutch, brake, gas;
    public World world;
    public int h, w;
    public static Car.CarParams carParams;
    public static Level level;

    public void reset()
    {
        init(carParams, level);
    }

    public RectF getBounds()
    {
        return new RectF(0, 0, w, h);
    }

    public static Game getGame()
    {
        if(game==null)
        {
            try {

                game = new Game(carParams, level);
            }
            catch (Exception e)
            {
                return null;
            }
        }
        return game;
    }

    public void update(double d)
    {
       if(!world.inited)
       {
           world.postInit();
       }

        world.update(d);
        if(listener!=null)
        {
            listener.changed();
        }
    }

    public void draw(Canvas c)
    {
        world.draw(c);
    }

    private void init(Car.CarParams carParams, Level level)
    {
        finished=false;
        Game.this.carParams = carParams;
        world = new World(PXtoM(300), carParams, level);
    }
    public Game(Car.CarParams carParams, Level level)
    {
init(carParams, level);
    }
}
