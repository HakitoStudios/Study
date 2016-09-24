package hakito.autosim.logic.particle;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.provider.Telephony;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Random;

import hakito.autosim.logic.Content;
import hakito.autosim.logic.Game;
import hakito.autosim.logic.UpdatableObject;
import hakito.autosim.logic.WorldObject;

/**
 * Created by Oleg on 30-Nov-15.
 */
public class ParticleSystem extends WorldObject implements UpdatableObject {




    static class Particle
    {
        public float scale, x, y, tran, sx, sy,  ss, st, size;
        public boolean died;

        public Particle(float scale,  float x, float y, float tran, float sx, float sy,  float ss, float st, float size) {
            this.scale = scale;
            this.size = size;
            this.x = x;
            this.y = y;
            this.tran = tran;
            this.sx = sx;
            this.sy = sy;
            this.ss = ss;
            this.st = st;
        }


        public RectF getRect(float x, float y)
        {
            float tx=this.x + x, ty=this.y+y;
            return new RectF(tx, ty,tx+size*scale, ty+size*scale );
        }

        public void update(double dt)
        {
            x+=sx*dt;
            y+=sy*dt;

            scale += ss*dt;
            tran+=st*dt;
            if(tran<0 || scale<0)
            {
                died=true;
            }
        }
    }

    public static class ParticlesInfo
    {
        public float speed, x, y, gas;

        public ParticlesInfo(float speed, float x, float y, float gas) {
            this.speed = speed;
            this.x = x;
            this.y = y;
            this.gas = gas;
        }
    }

    List<Particle> particles;
    Paint paint;
    Random rand;
    public ParticlesInfo info;
    private float angle;

    public ParticleSystem(WorldObject parent, PointF position, float angle) {
        super(parent, position, null, null, null);
        this.angle = angle;
        particles = new ArrayList<>(100);
        paint = new Paint();
        rand = new Random();

    }

    void addParticle()
    {

        float speed = 1;
        float da = 0.5f;
        float ra = angle + (rand.nextFloat()-0.5f)*da;
        double sx = speed * Math.cos(ra) + info.speed, sy = -speed * Math.sin(ra);

        particles.add(new Particle(1, info.x, info.y, info.gas, (float)sx, (float)sy,  1, -1, 0.1f));
    }

    @Override
    public void update(double d) {
        addParticle();

        List<Particle> dieds = new ArrayList<>(particles.size());
        for (Particle p:particles) {
            p.update(d);
            if(p.died)
            {
                dieds.add(p);
            }
        }
        for (Particle p:dieds             ) {
            particles.removeAll(dieds);
        }

    }

    @Override
    public void draw(Canvas c) {
        if(info==null)return;
        PointF pos = getPosition();
        float x=pos.x, y=pos.y;


        for (Particle p:particles) {
            paint.setAlpha((int) (255 * p.tran));
            c.drawBitmap(Content.content.particle, new Rect(0, 0, 16, 16), Game.MtoPX(p.getRect(x, y)), paint);
        }
    }
}
