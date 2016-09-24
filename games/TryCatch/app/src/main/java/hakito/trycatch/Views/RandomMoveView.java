package hakito.trycatch.Views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.graphics.ColorUtils;
import android.util.AttributeSet;
import android.view.View;

import hakito.trycatch.Game.Logic.Helper;
import hakito.trycatch.Game.Objects.RectangleAnimator;
import hakito.trycatch.R;

/**
 * Created by Oleg on 07-Jan-16.
 */
public class RandomMoveView extends View {
    RectangleAnimator animator;
    Paint paint;
    long last;

    public RandomMoveView(Context context, final AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setColor(ColorUtils.setAlphaComponent(getResources().getColor(R.color.game_toy), 100));
        paint.setAntiAlias(true);

        last = System.currentTimeMillis();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        animator = new RectangleAnimator(1, Helper.getRandomRect(getWidth()/2, 50, getHeight()/2, 50, 40, 1, 40, 1));
        animator.setListener(new RectangleAnimator.OnAnimatorfinishListener() {
            @Override
            public void finished() {
                Rect r = animator.getRect();
                animator.setNewRect(Helper.getRandomRect(r.left, 50, r.top, 50, 40, 1, 40, 1));
            }
        });
        animator.setTime(0);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        animator.update((System.currentTimeMillis() - last) / 1000.0f);
        last = System.currentTimeMillis();
        canvas.drawOval(Helper.getRectF(animator.getRect()), paint );
        invalidate();
    }
}
