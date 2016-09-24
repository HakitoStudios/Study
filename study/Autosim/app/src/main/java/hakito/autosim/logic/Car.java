package hakito.autosim.logic;

import android.graphics.*;
import android.graphics.Interpolator;
import android.os.Parcel;
import android.support.v4.widget.DrawerLayout;

import java.util.ArrayList;
import java.util.List;

import hakito.autosim.logic.particle.ParticleSystem;

/**
 * Created by Oleg on 31-Oct-15.
 */
public class Car extends WorldObject implements  UpdatableObject {




    public static class CarParams {
        public static class Wheelparams {
            public float x, r;
        }

        public static class EngineParams {
            public List<Float> transmission;
            public float diff, idle, inertia, brk, maxRPM;
            public List<Point> torque;

            public EngineParams() {
                transmission = new ArrayList<>();
                torque = new ArrayList<>();
            }
        }

        public static class Exhaust
        {
            public float x, y, angle;

            public Exhaust(float x, float y, float angle) {
                this.x = x;
                this.y = y;
                this.angle = angle;
            }
        }


        public String name;
        public int mass;
        public Bitmap bitmap;
        public float wk, bk, width;
        public List<Wheelparams> wheels;
        public EngineParams engineParams;
        public Exhaust exhaust;

        public CarParams() {
            wheels = new ArrayList<>();
            engineParams = new EngineParams();
        }
    }

    private Paint d = new Paint();
    public List<Wheel> wheels;
    private Engine engine;
    private int mass;
    private float speed;
    private float wk, bk;
    private float a;
    public String name;
    public ParticleSystem particleSystem;
    float r;
    float ex, ey;

    float F;

    public Car(WorldObject parent, PointF position, CarParams p) {
        super(parent, position, p.bitmap, new Rect(0, 0, 512, 512), new RectF(0, 0, p.width, p.width));


        this.name = p.name;
        if (p.exhaust != null) {
            ex = p.exhaust.x*p.width;
            ey=p.exhaust.y *p.width;
            particleSystem = new ParticleSystem(parent, new PointF(0, 0), p.exhaust.angle);
        }
        wheels = new ArrayList<>();
        Bitmap wb = Bitmap.createBitmap(bitmap, 0, 512, 256, 256);
        r = (p.wheels.get(0).r * p.width);
        for (CarParams.Wheelparams wp : p.wheels) {
            wheels.add(new Wheel(this, new PointF(wp.x * p.width - r, -2 * r), wb, new Rect(0, 0, 256, 256), r));
        }

        engine = new Engine(p.engineParams);
        speed = 0;//.00001f;
        engine.shift(true);
        a = 0;
        mass = p.mass;
        wk = p.wk;
        bk = p.bk;


    }

public float getFullGas()
{
    return engine.getFullGas();
}

    @Override
    public void update(double d) {
        if(particleSystem!=null) {
            particleSystem.info = new ParticleSystem.ParticlesInfo(getSpeedMpS(), getPosition().x + ex, -ey, getFullGas());
        }

        a = 0;
        F = 0;

        //engine pull
        F += (float) (engine.update((float) d, speed, r));
        ;

        //wind resistance
        F -= speed * Math.abs(speed) / 2 * wk;

        //braking
        F -= Math.signum(speed) * bk * Game.getGame().brake;

        a = F / mass;
        double da = a * d;
//        if(Math.signum(speed) != Math.signum(speed+da))
//        {
//            speed=0;
//        }
//        else
        speed += da;

        position.x += speed * d;

        for (Wheel w : wheels) {
            w.rotate(speed * (float) d);
        }


    }

    public float getFuelLevel() {
        return engine.getFuelLevel();
    }

    public boolean shift(boolean up) {
        return engine.shift(up);
    }

    public int getGear() {
        return engine.getGear();
    }

    public String getStringGear() {
        switch (engine.getGear()) {
            case 0:
                return "N";
            case -1:
                return "R";
            default:
                return "" + engine.getGear();

        }
    }

    public float getRPM() {
        return engine.getRPM();
    }

    public void starter() {
        engine.starter();
    }

    public float getSpeedMpS()
    {
        return speed;
    }

    public float getSpeed() {
        return speed * 3.6f;

    }

    public PointF getOffset() {
        return position;
    }

    void drawDebug(Canvas c) {
        PointF p = getPosition();
        int tx = (int) p.x + 150;
        c.drawText("" + (int) engine.getRPM() + " RPM", tx, 100, d);
        c.drawText("Speed: " + (int) (speed * 3.6) + " km/h", tx, 120, d);
        c.drawText("a: " + a + "", tx, 140, d);
        c.drawText("F: " + F + "", tx, 160, d);
        c.drawText(engine.debug, tx, 180, d);
        c.drawText(Game.PXtoM(getPosition().x) + "m", tx, 200, d);
        c.drawLine(tx, 220, tx + 40, 220, d);
        int cx, cy;
        cx = (int) p.x + 280;
        cy = 220;
        c.drawLine(cx, cy, cx, cy + 10, d);
        c.drawLine(cx, cy, cx + a * 100, cy, d);
    }

    @Override
    public void draw(Canvas c) {
        super.draw(c);

        //drawDebug(c);


    }
}
