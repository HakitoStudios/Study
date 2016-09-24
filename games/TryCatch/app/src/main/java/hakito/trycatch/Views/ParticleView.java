package hakito.trycatch.Views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.support.v4.graphics.ColorUtils;
import android.util.AttributeSet;
import android.view.View;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import hakito.trycatch.Game.Logic.Helper;

/**
 * Created by Oleg on 07-Jan-16.
 */
public class ParticleView extends View {
    class Particle
    {
        private Rect rect;
        private         int color;
        private float rotation;

        public Particle(Rect rect, int color, float rotation) {
            this.rect = rect;
            this.color = color;
            this.rotation = rotation;
        }

        public Rect getRect() {
            return rect;
        }

        public int getColor() {
            return color;
        }

        public float getRotation() {
            return rotation;
        }

        public void update()
        {
            rect.offset(0, 3);
            try {
                color = ColorUtils.setAlphaComponent(color, Color.alpha(color)-2);
            }
            catch (IllegalArgumentException e)
            {}

        }
    }

    List<Particle> particles;
    Paint paint;
    Random random;

    public ParticleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        random = new Random();
        particles = new LinkedList<>();
        paint = new Paint();
        paint.setAntiAlias(true);

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        if(random.nextInt(3)==0)
        particles.add(new Particle(Helper.getRandomRect(getWidth() / 2, getWidth(), -50, 10, 30, 10, 30, 10), random.nextInt(), 0));


        List<Particle> del = new LinkedList<>();
        for(Particle p : particles)
        {
            paint.setColor(p.getColor());
            canvas.drawRect(p.getRect(), paint);
            p.update();
            if(p.getRect().top > getHeight() || Color.alpha(p.getColor()) <= 3)
            {
                del.add(p);
            }
        }

        for(Particle p : del)
        {
            particles.remove(p);
        }


        invalidate();
    }

}
