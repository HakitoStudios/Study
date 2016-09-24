package hakito.autosim.activities;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import hakito.autosim.R;
import hakito.autosim.logic.Content;
import hakito.autosim.views.GameView;

public class MainActivity extends AppCompatActivity {

    GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_main);

        gameView = (GameView)findViewById(R.id.gameView);
//        if(gameView!=null){
        gameView.resume();
  //      }




    }



    @Override
    protected void onPause() {
        super.onPause();
        if(gameView!=null)
        gameView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(gameView!=null) {
            gameView.resume();
        }
    }
}
