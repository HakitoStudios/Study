package hakito.autosim.activities;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import hakito.autosim.R;
import hakito.autosim.logic.Settings;

public class SettingsActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    ToggleButton aClutch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_settings);

        aClutch = (ToggleButton)findViewById(R.id.autoClutch);
        aClutch.setChecked(Settings.getAutoClutch());
        aClutch.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId())
        {
            case R.id.autoClutch:
                Settings.setAutoClutch(isChecked);
                break;

        }
    }
}
