package hakito.trycatch.Data;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;

import java.io.IOException;

import hakito.trycatch.R;

/**
 * Created by Oleg on 04-Jan-16.
 */
public class AmbiencePlayer {
    private static MediaPlayer mediaPlayer;
    private static Context context;
    public static final String PREF_NAME="settings", PREF_SOUND="sound";


    public static void init(Context c)
    {
        context = c;
        mediaPlayer = MediaPlayer.create(c, R.raw.ambience);
        mediaPlayer.setLooping(true);
        mediaPlayer.setVolume(0.5f, 0.5f);

        resume();



    }

    public static void setVolume(float v)
    {
        mediaPlayer.setVolume(v, v);
    }

    public static void pause()
    {
        if(mediaPlayer.isPlaying())
        mediaPlayer.pause();
    }

    public static void resume()
    {
        if(context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getBoolean(PREF_SOUND, true) && !mediaPlayer.isPlaying())
        mediaPlayer.start();
    }
}
