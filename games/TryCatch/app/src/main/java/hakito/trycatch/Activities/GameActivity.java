package hakito.trycatch.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import hakito.trycatch.Framents.BonusesFragment;
import hakito.trycatch.Game.Game;
import hakito.trycatch.Game.Logic.Helper;
import hakito.trycatch.R;
import hakito.trycatch.Views.TimeUpdater;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class GameActivity extends BaseActivity implements View.OnClickListener {


    private static final int FINISH=1;
    TextView time;
    static TimeUpdater updater;
    static {updater = new TimeUpdater();
    updater.execute();}

    private BonusesFragment bonusesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_game);
        time = (TextView)findViewById(R.id.text_time);
        time.setText(Helper.getTime(0));
        ((TextView)findViewById(R.id.text_level)).setText(String.format(getString(R.string.level), Game.get().getLevel().getIndex()));
        findViewById(R.id.button_reset).setOnClickListener(this);
        Game.get().setOnGameFinishListener(new Game.OnGameFinishListener() {
            @Override
            public void finish() {

                Intent intent = new Intent(GameActivity.this, FinishActivity.class);
                startActivityForResult(intent, FINISH);
            }
        });

        bonusesFragment = new BonusesFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_bonuses, bonusesFragment).commit();

//        AdView adView = (AdView)findViewById(R.id.adView);
//        AdRequest request =new AdRequest.Builder().build();
//        adView.loadAd(request);



        setVolume(0.6f);

        checkHelp();
    }

    private static String PREF_NAME="game_help", PREF_LAUNCHED="launched", PREF_TELEPORTED="teleported";

    void checkHelp()
    {
        SharedPreferences preferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        boolean launched = preferences.getBoolean(PREF_LAUNCHED, false);
        if(!launched)
        {
            Intent intent = new Intent(this, TechActivity.class);
            intent.putExtra(TechActivity.EXTRA_TYPE, TechActivity.INTRO);
            startActivity(intent);
            preferences.edit().putBoolean(PREF_LAUNCHED, true).commit();
        }

        if(Game.get().getLevel().isWithTeleport()) {
            boolean teleported = preferences.getBoolean(PREF_TELEPORTED, false);
            if (!teleported) {
                Intent intent = new Intent(this, TechActivity.class);
                intent.putExtra(TechActivity.EXTRA_TYPE, TechActivity.TELEPORT);
                startActivity(intent);
                preferences.edit().putBoolean(PREF_TELEPORTED, true).commit();
            }
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==FINISH)
        {
            Game.get().pause();
            GameActivity.this.finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_reset:
                Game.get().reset();
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onPause() {
        super.onPause();
        Game.get().pause();

    }

    @Override
    protected void onResume() {
        super.onResume();
        updater.time = time;
        updater.hint = (TextView)findViewById(R.id.text_hint);
        updater.progressCoins = (ProgressBar)findViewById(R.id.progressBar);
        Game.get().resume();
    }


}
