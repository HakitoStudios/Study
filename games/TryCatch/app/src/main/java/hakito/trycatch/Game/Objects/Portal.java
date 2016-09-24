package hakito.trycatch.Game.Objects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Interpolator;
import android.graphics.Paint;
import android.graphics.Rect;

import hakito.trycatch.Game.Game;
import hakito.trycatch.Game.Logic.Helper;
import hakito.trycatch.R;

/**
 * Created by Oleg on 02-Jan-16.
 */
public class Portal extends BasicCell {
    public PortalType getPortalType() {
        return portalType;
    }

    public Portal getNeighbour() {
        return neighbour;
    }

    public enum PortalType{IN, OUT}

    private PortalType portalType;

    private RectangleAnimator interpolatorA, interpolatorB;
    private Portal neighbour;
    private static final float interval=0.8f;

    public Portal(int x, int y, Portal neighbour) {
        super(x, y);
        portalType = (neighbour==null)?PortalType.OUT:PortalType.IN;

        p.setStrokeWidth(5);
        p.setStyle(Paint.Style.STROKE);
        Rect from, to;
        if(portalType == PortalType.OUT) {
          from=  Game.getEmptyCellRect(x, y);
            to=Game.getCellRect(x, y);
            p.setColor(Game.get().getContext().getResources().getColor(R.color.game_portal_out));
        }
        else
        {
            p.setColor(Game.get().getContext().getResources().getColor(R.color.game_portal_in));
            to=  Game.getEmptyCellRect(x, y);
            from=Game.getCellRect(x, y);
            this.neighbour = neighbour;
        }
        interpolatorA = new RectangleAnimator(interval, from);
        interpolatorA.setNewRect(to);

        interpolatorB = new RectangleAnimator(interval, from);
        interpolatorB.setNewRect(to);
        interpolatorB.setTime(interval/2);


interpolatorA.setCyclyc(true);
        interpolatorB.setCyclyc(true);
    }

    @Override
    public void update(float dt) {
        interpolatorA.update(dt);
        interpolatorB.update(dt);
    }

    @Override
    public boolean isFree() {
        return true;
    }

    @Override
    public void draw(Canvas c) {

        p.setAlpha((int)(255*(1-interpolatorA.getProgress())));
        c.drawOval(Helper.getRectF(interpolatorA.getRect()), p);

        p.setAlpha((int) (255 * (1-interpolatorB.getProgress())));
        c.drawOval(Helper.getRectF(interpolatorB.getRect()), p);
    }
}
