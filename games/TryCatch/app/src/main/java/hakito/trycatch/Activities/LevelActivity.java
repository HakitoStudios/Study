package hakito.trycatch.Activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import hakito.trycatch.Adapters.LevelAdapter;
import hakito.trycatch.Data.Content;
import hakito.trycatch.Data.Helpers.DBHelper;
import hakito.trycatch.Game.Game;
import hakito.trycatch.R;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class LevelActivity extends BaseActivity implements View.OnClickListener {
GridView gridView;
    LevelAdapter adapter;
    TextView stars;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_level);
        findViewById(R.id.root).setOnClickListener(this);
        gridView=(GridView)findViewById(R.id.gridView);
        stars = (TextView)findViewById(R.id.text_stars);

        setVolume(0.4f);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter = new LevelAdapter(this);
        adapter.addAll(Content.levels);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Game.get().setLevel(Content.levels.get(position));
                startActivity(new Intent(LevelActivity.this, GameActivity.class));
            }
        });
        stars.setText(String.format("%d/%d", new DBHelper(this).getStarsCount(), 5 * Content.levels.size()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.root:

                break;
        }
    }
}
