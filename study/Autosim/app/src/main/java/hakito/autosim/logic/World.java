package hakito.autosim.logic;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import hakito.autosim.activities.FinishActivity;
import hakito.autosim.logic.Level.ObjectType;
import hakito.autosim.logic.particle.ParticleSystem;
import hakito.autosim.views.ControllsFragment;

import static hakito.autosim.logic.Level.ObjectType.*;

/**
 * Created by Oleg on 31-Oct-15.
 */
public class World extends WorldObject implements DrawableObject, UpdatableObject, CheckPoint.OnGoneListner {

    static Paint debugPaint;
    static{
        debugPaint  = new Paint();
        debugPaint.setColor(Color.GRAY);
    }



    List<DrawableObject> drawableObjects;
    List<UpdatableObject> updatableObjects;
    List<CheckPoint> checkPoints;
    public Car car;
    float groundStart;

    Paint  timePaint;
    boolean started;
    Parallax ground, skyBack, skyClouds;
    public boolean inited;
    Matrix transform;
    GameResult result;
    CheckPoint finish;


    public World(float groundStart, Car.CarParams carParams, Level level) {
        super(null, new PointF(0, groundStart), null, null, null);
        this.groundStart = groundStart;

        result = new GameResult(Game.level.name, Game.carParams.name);

        drawableObjects = new ArrayList<>();
        updatableObjects = new ArrayList<>();
        checkPoints = new ArrayList<>();


        car = new Car(this, new PointF(0, 0), carParams);
        drawableObjects.add(car);
        updatableObjects.add(car);
        drawableObjects.addAll(car.wheels);

        if (car.particleSystem != null) {
            drawableObjects.add(car.particleSystem);
            updatableObjects.add(car.particleSystem);
        }

        for(Level.LevelObject l:level.objects)
        {
            switch(l.type)
            {
                case TRAFFIC_LIGHT:
                    TrafficLight traffic_light = new TrafficLight(this, new PointF(l.x, 0));
                    traffic_light.setOnGoneListener(this);
                    checkPoints.add(traffic_light); break;
                case FINISH:
                    finish = new CheckPoint(this, new PointF(l.x, 0), Content.content.objects, Content.getRect(l.type), new RectF(0, 0, 2, 2), FINISH);
                    finish.setOnGoneListener(this);
                    checkPoints.add(finish); break;
                case ROW: drawableObjects.add(new WorldObject(this, new PointF(l.x, 0), Content.content.objects, Content.getRect(l.type), new RectF(0, 0, 0.7f, 0.7f))); break;
            }
        }

        for (Level.Periodic p: level.periodics)
        {
            float s = p.x;
            for (int i=0;i<p.n;i++)
            {
                drawableObjects.add(new WorldObject(this, new PointF(s + i*p.d, 0), Content.content.objects, Content.getRect(p.type), new RectF(0, 0, 0.7f, 0.7f)));
            }
        }


        updatableObjects.addAll(checkPoints);
        drawableObjects.addAll(checkPoints);


        timePaint = new Paint();
        timePaint.setTextSize(50);
        timePaint.setColor(Color.BLUE);



    }

    public void postInit()
    {
        int gs= (int)Game.MtoPX(groundStart);

        Rect skyRect= new Rect(0, 0, Game.getGame().w,gs);
        Rect groundRect = new Rect(0, gs, Game.getGame().w, Game.getGame().h);
        ground = new Parallax(Content.content.ground,  1, groundRect, new Rect(0, 0, 512, 512), new Rect(512, 0, 1024, 512));
        skyBack = new Parallax(Content.content.sky, 0.01f, skyRect, new Rect(0, 0, 512, 512), new Rect(512, 0, 1024, 512));
        skyClouds = new Parallax(Content.content.sky,  0.1f, skyRect, new Rect(0, 512, 512, 1024), new Rect(512, 512, 1024, 1024));
        inited=true;
    }

    void drawTime(Canvas c) {

        //c.drawText(String.format("%.2f", result.time), 200, 150, timePaint);

    }

    public boolean needToDraw(RectF rect)
    {

        return true;
    }

    public String getTime()
    {
        return String.format("%.2f", result.time);
    }

    @Override
    public void draw(Canvas c) {

        float cpx = Game.MtoPX(-car.getPosition().x);
        skyBack.draw(c, cpx);
        skyClouds.draw(c, cpx);
        ground.draw(c, cpx);


        drawTime(c);
        c.save();


        float k = 1 - (float) Math.pow(car.getSpeed() / 200, 4);

        if (k < 0.4) k = 0.4f;

            PointF po  = Game.MtoPX(car.getOffset());
            c.translate(-po.x + k * 200, -po.y);

        for (DrawableObject d : drawableObjects) {

            d.draw(c);

        }
        transform = c.getMatrix();
        c.restore();

        TrafficLight next = null;
        for(CheckPoint cp:checkPoints)
        {
            if(!(cp instanceof TrafficLight)){continue;}
            float length = cp.getPosition().x - car.getPosition().x;
            if(length>0 && length<20)
            {
            break;
            }
            else
            if(length<160 && length>0)
            {
                next=(TrafficLight)cp;
                break;
            }
        }
        if(next!=null)
        {
            lightInfo = new LightInfo((int)(next.getPosition().x - car.getPosition().x), next.getColor());
        }
        else
        {
            lightInfo=null;
        }


    }

    LightInfo lightInfo;

    public int getDistToFinish()
    {
        return (int)(finish.getPosition().x - car.getPosition().x);
    }

    public LightInfo getLightInfo()
    {
        return lightInfo;
    }

    public static class LightInfo
    {
        public int dist, color;

        public LightInfo(int dist, int color) {
            this.dist = dist;
            this.color = color;
        }
    }

    @Override
    public void update(double d) {



        if(car.getSpeed()>0)
        {
            started = true;
        }
        if(car.getFuelLevel()<0)
        {
            result.finished=false;
            result.fuelLevel=0;
            Game.getGame().finish(result);
        }

        if (started) {
            result.time+=d;
        }



        for (CheckPoint c:checkPoints)
        {
            c.check(car);
        }

        for (UpdatableObject o : updatableObjects) {

            o.update(d);

        }
    }


    @Override
    public void gone(CheckPoint checkPoint) {
        switch (checkPoint.getType()) {
            case FINISH:
                result.finished=true;
                result.fuelLevel = car.getFuelLevel();
                Game.getGame().finish(result); break;
            case TRAFFIC_LIGHT:
                    if(ControllsFragment.messageListener != null && ((TrafficLight) checkPoint).isRed()) {
                        ControllsFragment.messageListener.show("Red light!", ControllsFragment.MessageType.BAD);
                        result.redLights++;
                    }
                break;

        }
    }
}
