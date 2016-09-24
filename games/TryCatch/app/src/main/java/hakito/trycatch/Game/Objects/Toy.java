package hakito.trycatch.Game.Objects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import hakito.trycatch.Data.Content;
import hakito.trycatch.Data.SoundHelper;
import hakito.trycatch.Game.Game;
import hakito.trycatch.Game.Logic.Direction;
import hakito.trycatch.Game.Logic.Helper;
import hakito.trycatch.Game.Logic.PathFinder;
import hakito.trycatch.R;

/**
 * Created by Oleg on 12-Dec-15.
 */
public class Toy extends BasicCell implements DrawableObject, UpdatableObject {

    private RectangleAnimator animator;




    public Toy(int x, int y) {
        super(x, y);
        p.setStyle(Paint.Style.FILL);
        p.setColor(Game.get().getContext().getResources().getColor(R.color.game_toy));

        animator = new RectangleAnimator(0.3f, Game.getCellRect(x, y));
    }

    @Override
    public void draw(Canvas c) {

        // c.drawBitmap(Content.toy, new Rect(0, 0, 100, 100), animator.getRect(), p);
        c.drawOval(Helper.getRectF(animator.getRect()), p);

    }



    public static Point getNewPoint(Point p, Direction direction) {
        Point pos = new Point(p);
        switch (direction) {
            case UP:
                pos.y--;
                break;
            case RIGHT:
                pos.x++;
                break;
            case DOWN:
                pos.y++;
                break;
            case LEFT:
                pos.x--;
                break;
        }
        return pos;
    }

    public void step(boolean skipStep, RectangleAnimator.OnAnimatorfinishListener listener) {
        Direction direction = PathFinder.getPath();
        if (direction != null) {
            switch (direction) {
                case UP:
                    if (!skipStep)
                        y--;
                    break;
                case RIGHT:
                    if (!skipStep)
                        x++;
                    break;
                case DOWN:
                    if (!skipStep)
                        y++;
                    break;
                case LEFT:
                    if (!skipStep)
                        x--;
                    break;

                case BLOCKED:
                    Game.get().getGameResult().win = true;
                    animator.setPrivateListener(new RectangleAnimator.OnAnimatorfinishListener() {
                        @Override
                        public void finished() {
                            Game.get().finish();
                        }
                    });

                    break;
            }
        } else {


        }
        if (Helper.isFinish(x, y, Game.get().getLevel().getSize())) {
            animator.setPrivateListener(new RectangleAnimator.OnAnimatorfinishListener() {
                @Override
                public void finished() {
                    Game.get().finish();
                }
            });
        }
        animator.setNewRect(Game.getCellRect(x, y));
        if(listener!=null)animator.setListener(listener);
    }

    public void teleport(final int x, final int y)
    {
        SoundHelper.getHelper().play(SoundHelper.Sound.TELEPORT);
        animator.setNewRect(Game.getEmptyCellRect(this.x, this.y));
        this.x = x;
        this.y = y;
        animator.setListener(new RectangleAnimator.OnAnimatorfinishListener() {
            @Override
            public void finished() {
                animator.setNewRect(Game.getCellRect(x, y));
                animator.setListener(null);
            }
        });

    }


    @Override
    public void update(float d) {
        animator.update(d);
    }
}
