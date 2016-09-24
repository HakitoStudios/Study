package hakito.trycatch.Activities;

import android.support.v7.app.AppCompatActivity;

import hakito.trycatch.Data.AmbiencePlayer;

/**
 * Created by Oleg on 04-Jan-16.
 */
public abstract class BaseActivity extends AppCompatActivity{
    @Override
    protected void onResume() {
        super.onResume();
        AmbiencePlayer.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        AmbiencePlayer.pause();
    }

    protected  void setVolume(float v)
    {
        AmbiencePlayer.setVolume(v);
    }
}
