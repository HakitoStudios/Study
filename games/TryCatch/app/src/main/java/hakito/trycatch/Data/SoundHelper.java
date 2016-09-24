package hakito.trycatch.Data;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import hakito.trycatch.R;

/**
 * Created by Oleg on 03-Jan-16.
 */
public class SoundHelper {
    public enum Sound
    {
    CLICK, COIN, TELEPORT, WIN, LOSE
    }

   private static SoundHelper helper;

    public static void init(Context context)
    {
        helper = new SoundHelper(context);
    }

    public static SoundHelper getHelper() {
        return helper;
    }

    private SoundPool pool;
    private int clickId, coinId, teleportId, winId, loseId;

    public SoundHelper(Context context) {
        pool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        clickId = pool.load(context, R.raw.click, 1);
        teleportId = pool.load(context, R.raw.teleport, 1);
        coinId = pool.load(context, R.raw.coin, 1);
        winId = pool.load(context, R.raw.win, 1);
        loseId = pool.load(context, R.raw.lose, 1);
    }

    public  void play(Sound sound)
    {
        switch (sound) {
            case CLICK:

                pool.play(clickId, 1, 1, 0, 0, 1);
                break;
            case COIN:
                pool.play(coinId, 1, 1, 0, 0, 1);
                break;
            case TELEPORT:
                pool.play(teleportId, 1, 1, 0, 0, 1);
                break;
            case WIN:
                pool.play(winId, 1, 1, 0, 0, 1);
                break;
            case LOSE:
                pool.play(loseId, 1, 1, 0, 0, 1);
                break;
        }
    }
}
