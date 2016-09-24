package hakito.trycatch.Activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import hakito.trycatch.Data.AmbiencePlayer;
import hakito.trycatch.Framents.PlayerStateFragment;
import hakito.trycatch.Game.Game;
import hakito.trycatch.Mosaic.MosaicBuilder;
import hakito.trycatch.R;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MenuActivity extends BaseActivity implements View.OnClickListener {

    PlayerStateFragment playerStateFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_menu);
        Game.get().setContext(this);
        playerStateFragment = new PlayerStateFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_bonuses, playerStateFragment).commit();
        findViewById(R.id.root).setOnClickListener(this);
        findViewById(R.id.button_shop).setOnClickListener(this);

        setVolume(0.3f);

        ToggleButton toggleButton = ((ToggleButton) findViewById(R.id.toggle_sound));
        toggleButton.setChecked(getSharedPreferences(AmbiencePlayer.PREF_NAME, MODE_PRIVATE).getBoolean(AmbiencePlayer.PREF_SOUND, true));
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                getSharedPreferences(AmbiencePlayer.PREF_NAME, MODE_PRIVATE).edit().putBoolean(AmbiencePlayer.PREF_SOUND, isChecked).commit();
                AmbiencePlayer.pause();
                AmbiencePlayer.resume();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        playerStateFragment.notifyChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.root:
                startActivity(new Intent(this, LevelActivity.class));
                break;
            case R.id.button_shop:
                startActivity(new Intent(this, ShopActivity.class));
                break;
        }
    }
}
