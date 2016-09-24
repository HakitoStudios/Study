package hakito.trycatch.Game.Objects;

import android.graphics.Interpolator;
import android.graphics.Rect;

/**
 * Created by Oleg on 26-Dec-15.
 */
public class RectangleAnimator {


    public boolean isCyclyc() {
        return cyclyc;
    }

    public void setCyclyc(boolean cyclyc) {
        this.cyclyc = cyclyc;
    }

    public void setPrivateListener(OnAnimatorfinishListener privateListener) {
        this.privateListener = privateListener;
    }

    public interface OnAnimatorfinishListener {
        void finished();
    }

    private OnAnimatorfinishListener listener, privateListener;

    public void setListener(OnAnimatorfinishListener listener) {
        this.listener = listener;
    }

    private float overalTime, time;
    private Interpolator interpolator;
    private Rect old, newRect;
    private static final int PRECENDANCE = 1000;
    private boolean cyclyc;


    public RectangleAnimator(float overalTime, Rect baseRect) {
        this.overalTime = time = overalTime;
        this.old = this.newRect = baseRect;
        interpolator = new Interpolator(4, 2);
        updateInterpolator();
    }

    public float getProgress()
    {
        return time/overalTime;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public void setRepeatMirror(float repeatCount, boolean mirror) {
        interpolator.setRepeatMirror(repeatCount, mirror);
    }

    private float[] getRect(Rect r) {
        return new float[]{r.left, r.top, r.right, r.bottom};
    }

    private void updateInterpolator() {
        interpolator.setKeyFrame(0, 0, getRect(old));
        interpolator.setKeyFrame(1, (int) (PRECENDANCE * overalTime), getRect(newRect));
    }

    public void setNewRect(Rect newRect) {
        old = this.newRect;
        this.newRect = newRect;
        updateInterpolator();
        time = 0;
    }

    public boolean finished() {
        return time >= overalTime;
    }

    public Rect getRect() {

        if (finished())
            return newRect;
        float[] rect = new float[4];
        interpolator.timeToValues((int) (time * PRECENDANCE), rect);
        return new Rect((int) rect[0], (int) rect[1], (int) rect[2], (int) rect[3]);
    }

    public void update(float d) {
        if (!finished()) {
            time += d;
            if (finished()) {
                if (cyclyc) time = 0;
                if (listener != null) {
                    listener.finished();
                }
                if(privateListener!=null)
                {
                    privateListener.finished();
                }
            }
        }

    }
}
