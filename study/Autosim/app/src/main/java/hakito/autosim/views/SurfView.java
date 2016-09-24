package hakito.autosim.views;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import hakito.autosim.logic.Game;
import hakito.autosim.logic.MyThread;

/**
 * Created by Oleg on 04-Nov-15.
 */
public class SurfView extends SurfaceView implements SurfaceHolder.Callback {

    private MyThread thread;
    long last;

    public SurfView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SurfView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    public SurfView(Context context) {
        super(context);
        init();
    }

    private void init() {
        getHolder().addCallback(this);
        last = System.currentTimeMillis();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread = new MyThread(this, getHolder());
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        thread.setRunning(false);
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }

        @Override
        public void draw(Canvas canvas){
            super.draw(canvas);
            final long d = System.currentTimeMillis() - last;
            last = System.currentTimeMillis();
            Game.getGame().world.draw(canvas);
            Game.getGame().world.update(d / 1000.0);
        }

    }

