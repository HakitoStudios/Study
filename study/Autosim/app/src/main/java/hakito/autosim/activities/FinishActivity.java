package hakito.autosim.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import hakito.autosim.FinishListAdapter;
import hakito.autosim.R;
import hakito.autosim.logic.DatabaseHelper;
import hakito.autosim.logic.Game;
import hakito.autosim.logic.GameResult;

public class FinishActivity extends AppCompatActivity implements OnClickListener {

    public static final String RESULT_TAG="result";
    public static final int OUT_OF_FUEL=0, FINISHED=1;

    TextView text, time, best;
    RatingBar ratingBar;
    ListView listView;

    public static class ResultPart
    {
        public String descr;
        public float count;
        public boolean isGood;

        public ResultPart(String descr, float count, boolean isGood) {
            this.descr = descr;
            this.count = count;
            this.isGood = isGood;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_finish);
        listView = (ListView)findViewById(R.id.listView);
        ratingBar = (RatingBar)findViewById(R.id.ratingBar);
        findViewById(R.id.button_restart).setOnClickListener(this);
        findViewById(R.id.button_next).setOnClickListener(this);

        final FinishListAdapter adapter = new FinishListAdapter(this);


        ratingBar.setIsIndicator(true);

        GameResult result = (GameResult)getIntent().getSerializableExtra(RESULT_TAG);

        best = (TextView)findViewById(R.id.textBest);
        time = (TextView)findViewById(R.id.textTime);
        text = (TextView)findViewById(R.id.text);

        if(result.finished) {
            if(result.redLights!=0) {
                adapter.add(new ResultPart("Red light", result.redLights, false));
            }
            adapter.add(new ResultPart("Fuel leftover", (float)(result.fuelLevel), true));
            text.setText("Finished!");
            time.setText(String.format("%.2f sec", result.time));
            ratingBar.setRating(5.0f - result.redLights + result.fuelLevel);
            DatabaseHelper.Record r = new DatabaseHelper(this).getRecord(result.level, result.car);
            if(r!=null&&result.time > r.time)
            {
                    best.setText(r.toString());
            }
            else
            {
                best.setText("You establish a new best time!");
                new DatabaseHelper(this).updateRecord(new DatabaseHelper.Record(result.level, result.car, result.time));
            }
        }
        else {
            text.setText("Out of fuel.");
            ratingBar.setVisibility(View.INVISIBLE);

        }
        listView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MenuActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.button_next:
                startActivity(new Intent(getApplicationContext(), LevelSelectActivity.class));
                finish();
                break;
            case R.id.button_restart:
                Game.getGame().reset();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
                break;
        }
    }
}
