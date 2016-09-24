package hakito.trycatch.Game.Objects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import hakito.trycatch.Data.Content;
import hakito.trycatch.Data.SoundHelper;
import hakito.trycatch.Game.Game;
import hakito.trycatch.Game.Logic.Helper;
import hakito.trycatch.R;

/**
 * Created by Oleg on 26-Dec-15.
 */
public class Cell extends BasicCell {


    private Cells type;
    private RectangleAnimator animator;


    public Cell(int x, int y, Cells type) {
        super(x, y);

        this.x = x;
        this.y = y;
        setType(type);

        animator = new RectangleAnimator(0.3f, Game.getCellRect(x, y));
    }



    void updateColor() {
        int color = R.color.game_empty;
        switch (type) {
            case EMPTY:
                color = R.color.game_empty;
                p.setStyle(Paint.Style.STROKE);
                break;
            case BLOCK:
                color = R.color.game_block;
                p.setStyle(Paint.Style.FILL);
                break;
            case COIN:
                color = R.color.game_coin;
                p.setStyle(Paint.Style.FILL);
                break;
        }
        p.setColor(Game.get().getContext().getResources().getColor(color));
    }

    public boolean isFree() {
        return type == Cells.EMPTY;
    }



    public void setType(Cells type) {
        this.type = type;
        updateColor();
    }

    public Cells getType() {
        return type;
    }

    @Override
    public void draw(Canvas c) {

        if(type== Cells.BLOCK)
        c.drawRect(animator.getRect(), p);
        else if(type == Cells.COIN)
        c.drawRoundRect(Helper.getRectF(animator.getRect()), 30, 30, p);
        else if(type == Cells.EMPTY)
            c.drawRect(Game.getCellRect(x, y), p);

//if(bitmap!=null)
//        c.drawBitmap(bitmap, new Rect(0, 0, 100, 100), (animator==null)?Game.getCellRect(x, y):animator.getRect(), p);
    }

    public boolean pick() {
        switch (type) {
            case EMPTY:
                setType(Cells.BLOCK);
SoundHelper.getHelper().play(SoundHelper.Sound.CLICK);
                animator = new RectangleAnimator(0.3f, Game.getEmptyCellRect(x, y));
                animator.setNewRect(Game.getCellRect(x, y));
                animator.setListener(new RectangleAnimator.OnAnimatorfinishListener() {
                    @Override
                    public void finished() {



                    }
                });

                return true;
            case BLOCK:
                break;
            case COIN:
                SoundHelper.getHelper().play(SoundHelper.Sound.COIN);
                animator = new RectangleAnimator(0.3f, Game.getCellRect(x, y));
                animator.setNewRect(Game.getEmptyCellRect(x, y));
                animator.setListener(new RectangleAnimator.OnAnimatorfinishListener() {
                    @Override
                    public void finished() {
                        setType( Cells.EMPTY);

                        Game.get().getGameResult().coins++;

                    }
                });

                return true;
        }
        return false;
    }


    @Override
    public void update(float d) {
        if(animator!=null)animator.update(d);
    }
}
