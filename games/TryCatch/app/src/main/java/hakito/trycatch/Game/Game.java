package hakito.trycatch.Game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;

import hakito.trycatch.Data.Models.Good;
import hakito.trycatch.Data.Models.Level;
import hakito.trycatch.R;

/**
 * Created by Oleg on 12-Dec-15.
 */
public class Game {


    public Level getLevel() {
        return level;
    }

    public void reset()
    {
        setLevel(level);
    }

    public void setLevel(Level level) {
        setHint(R.string.certain_hint);
        this.level = level;
        gameResult = new GameResult();
        gameResult.maxCoins = level.getCoinsCount();
        field = new Field();
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(int resId) {
        hint = context.getString(resId);
    }

    public interface OnGameFinishListener
    {
        void finish();
    }

    private static Game game;

    private OnGameFinishListener onGameFinishListener;
    public void setOnGameFinishListener(OnGameFinishListener onGameFinishListener) {
        this.onGameFinishListener = onGameFinishListener;
    }
    public static Game get()
    {
        if(game!=null)return game;
        return game=new Game();
    }

    private Context context;
    private GameResult gameResult;
    private Field field;
    private Level level;
    private String hint;

    //size of one cell
    public  static final int L=100;
    long lastUpdate;
    private boolean paused;

    public static Rect getEmptyCellRect(int x, int y)
    {
        Rect r=getCellRect(x, y);
        r.inset(r.width()/2, r.height()/2);
        return r;
    }

    public static Rect getCellRect(int x, int y)
    {
        int px = Game.L * x, py = Game.L * y;
        return  new Rect(px, py, px + Game.L, py + Game.L);
    }

    private Game()
    {

    }

    public void activateBonus(Good good) {
        field.activateBonus(good);
    }

    public Point getSizePX() {
        return new Point(L*level.getSize(), L*level.getSize());
    }

    public GameResult getGameResult() {
        return gameResult;
    }

    public Field getField() {
        return field;
    }

    public void touch(int x, int y) {
        field.touch(x, y);
    }

    public void pause()
    {
paused = true;
    }

    public void resume(){
        lastUpdate = System.currentTimeMillis();
        paused = false;
    }

    public void finish()
    {
        if(onGameFinishListener!=null)
            onGameFinishListener.finish();
    }


    public void update()
    {
        if(!paused) {
            float d = (System.currentTimeMillis() - lastUpdate) / 1000f;
            gameResult.time += d;
            resume();
            field.update(d);
        }
    }

    public void draw(Canvas canvas)
    {
       field.draw(canvas);
    }
}
