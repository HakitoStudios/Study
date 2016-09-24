package hakito.trycatch.Activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import java.util.concurrent.ExecutionException;

import hakito.trycatch.Data.Content;
import hakito.trycatch.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_splash);
        ResourcesLoader loader = new ResourcesLoader();
        loader.execute();


    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Content.inited) {
            finish();
            startActivity(new Intent(SplashActivity.this, MenuActivity.class));
        }
    }

    class ResourcesLoader extends AsyncTask<Object, Object, Object>
    {

        @Override
        protected Object doInBackground(Object... params) {
            Content.loadContent(SplashActivity.this);
            startActivity(new Intent(SplashActivity.this, MenuActivity.class));
            finish();
            return null;
        }
    }
}
