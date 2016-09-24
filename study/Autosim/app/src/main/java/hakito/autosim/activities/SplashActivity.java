package hakito.autosim.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import hakito.autosim.R;
import hakito.autosim.activities.MainActivity;
import hakito.autosim.logic.Content;
import hakito.autosim.logic.Settings;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        setContentView(R.layout.activity_splash);

new Loader().execute();
        Settings.init(this);

    }

    void startMenu()
    {
        finish();
        startActivity(new Intent(this, MenuActivity.class));
    }

    class Loader extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... params) {
            Content.content.load(getApplicationContext());
            startMenu();
            return null;
        }
    }
}
