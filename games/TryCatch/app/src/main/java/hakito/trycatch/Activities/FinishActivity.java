package hakito.trycatch.Activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import hakito.trycatch.Data.Content;
import hakito.trycatch.Data.Helpers.DBHelper;
import hakito.trycatch.Data.Models.Player;
import hakito.trycatch.Data.Models.Record;
import hakito.trycatch.Data.SoundHelper;
import hakito.trycatch.Game.Game;
import hakito.trycatch.Game.GameResult;
import hakito.trycatch.Game.Logic.Helper;
import hakito.trycatch.R;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FinishActivity extends BaseActivity implements View.OnClickListener {


    InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_finish);
        findViewById(R.id.root).setOnClickListener(this);
        findViewById(R.id.bNext).setOnClickListener(this);
        findViewById(R.id.bReset).setOnClickListener(this);
        findViewById(R.id.button_shop).setOnClickListener(this);
        findViewById(R.id.button_help).setOnClickListener(this);

        //win
        if (Game.get().getGameResult().win) {
            SoundHelper.getHelper().play(SoundHelper.Sound.WIN);
            updateResults();
            GameResult result = Game.get().getGameResult();
            DBHelper dbHelper = new DBHelper(this);

            dbHelper.writeRecord(new Record(Game.get().getLevel().getIndex(), result.getStars()));
            Player p = dbHelper.getPlayer();
            p.addCoins(result.coins);
            dbHelper.savePlayer(p);

        }
        //loose
        else {
            SoundHelper.getHelper().play(SoundHelper.Sound.LOSE);
            findViewById(R.id.lay_result).setVisibility(View.GONE);
            findViewById(R.id.lay_loose).setVisibility(View.VISIBLE);
            findViewById(R.id.particle_view).setVisibility(View.GONE);
        }

        //next level not available
        boolean lastLevel = Content.levels.indexOf(Game.get().getLevel()) >= Content.levels.size() - 1, unsufficientStars = lastLevel?false:new DBHelper(this).getStarsCount() < Content.levels.get(Content.levels.indexOf(Game.get().getLevel()) + 1).getStarsToOpen();
        if (lastLevel || unsufficientStars) {
            findViewById(R.id.bNext).setEnabled(false);
            if (unsufficientStars)
                findViewById(R.id.text_finish_hint).setVisibility(View.VISIBLE);
            if (lastLevel)
                ((TextView) findViewById(R.id.tResult)).setText(R.string.game_passed);
        }
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.interstitial_ad_id));
        prepareInterstitialAd();

        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                beginGame();
                prepareInterstitialAd();
            }
        });

        setVolume(0.3f);
    }

    void prepareInterstitialAd()
    {
        AdRequest request = new AdRequest.Builder().build();
        interstitialAd.loadAd(request);
    }

    void updateResults()
    {
        GameResult result = Game.get().getGameResult();

        ((TextView) findViewById(R.id.tSteps)).setText(String.format(getString(R.string.steps), result.steps));

        ((TextView) findViewById(R.id.tTime)).setText(Helper.getTime((long)(result.time*1000)));
        ((TextView) findViewById(R.id.tCoins)).setText(String.format(getString(R.string.coins), result.coins, result.maxCoins));
        RatingBar b = (RatingBar)findViewById(R.id.ratingBar);
        b.setIsIndicator(true);
        b.setRating(result.getStars());
    }

    void beginGame()
    {
        startActivity(new Intent(FinishActivity.this, GameActivity.class));
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.root:
               // startActivity(new Intent(this, LevelActivity.class));
                break;
            case R.id.bNext:
                Game.get().setLevel(Content.levels.get(Content.levels.indexOf(Game.get().getLevel()) + 1));
                if(interstitialAd.isLoaded())
                {
                    interstitialAd.show();
                }
                else {
                    beginGame();
                }
                break;
            case R.id.bReset:
                Game.get().reset();
                if(interstitialAd.isLoaded())
                {
                    interstitialAd.show();
                }
                else {
                    beginGame();
                }
                break;
            case R.id.button_shop:
                startActivity(new Intent(this, ShopActivity.class));
                break;
            case R.id.button_help:
                Intent helpIntent = new Intent(this, TechActivity.class);
                helpIntent.putExtra(TechActivity.EXTRA_TYPE, TechActivity.ALL);
                startActivity(helpIntent);
                break;
        }
    }
}
