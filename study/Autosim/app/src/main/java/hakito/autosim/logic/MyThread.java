package hakito.autosim.logic;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

import hakito.autosim.views.SurfView;

public class MyThread extends Thread {
    private SurfView view;
    private SurfaceHolder holder;
    private boolean running;

    public MyThread(SurfView view, SurfaceHolder hold) {
        super();
        this.view = view;

        this.holder = hold;

    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public void run() {
        //super.run();
        while (running) {
            Canvas c = null;
            try {
                c = holder.lockCanvas();
                synchronized (holder) {
                    view.draw(c);
                }
            } finally {
                if (c != null) {
                    holder.unlockCanvasAndPost(c);
                }
            }


        }
    }
}
