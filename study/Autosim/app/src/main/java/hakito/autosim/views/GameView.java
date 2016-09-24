package hakito.autosim.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import hakito.autosim.activities.FinishActivity;
import hakito.autosim.logic.Game;
import hakito.autosim.logic.GameResult;

/**
 * Created by Oleg on 31-Oct-15.
 */
public class GameView extends View  {
    long last;
    boolean running;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Game.getGame().w = MeasureSpec.getSize(widthMeasureSpec);
        Game.getGame().h = MeasureSpec.getSize(heightMeasureSpec);
                Game.getGame().setOnFinishListener(new Game.OnFinishListener() {
                    @Override
                    public void finish(GameResult result) {
                        Intent intent = new Intent(getContext(), FinishActivity.class);
                        intent.putExtra(FinishActivity.RESULT_TAG, result);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        ((Activity)getContext()).finish();
                        getContext().startActivity(intent);
                    }
                });
    }

    public void resume()
    {
        running=true;
        last = System.currentTimeMillis();
        invalidate();
    }

    public void pause()
    {
        running=false;
    }

    void drawGame(Canvas canvas)
    {
        Game.getGame().draw(canvas);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        invalidate();
        if(Game.getGame().world.inited)
        drawGame(canvas);
        if(!running)return;
        final long d = System.currentTimeMillis() - last;
        last = System.currentTimeMillis();

        Game.getGame().update(d / 1000.0);





    }



}
