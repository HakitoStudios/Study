package hakito.graphplayer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{



    public static final String EXTRA_GRAPHIC="graphic";


    GraphFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        findViewById(R.id.bStart).setOnClickListener(this);
        fragment= new GraphFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment).commit();
        setTitle("Create graphic");
        float[] pp = new float[]{0.4f, 0.5f, 0.2f, 0.1f, 0.2f, 0.99f, 0.1f, 0.0f, 0.6f, 0.2f };
        //fragment.setPoints(pp);
        if(getIntent().hasExtra(EXTRA_GRAPHIC))
        {
            float[] f = getIntent().getFloatArrayExtra(EXTRA_GRAPHIC);
            fragment.setPoints(f);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menu_ok:
                Intent data = new Intent();
                data.putExtra(EXTRA_GRAPHIC, fragment.getValues());
                setResult(RESULT_OK, data);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.accept_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bStart:


                try {
                    new Player(this).execute(new Player.PlayerInfo(fragment.getPoints(), Integer.parseInt(((TextView) findViewById(R.id.tDuration)).getText().toString())));
                } catch (NumberFormatException e)
                {
                    Toast.makeText(this, "Enter valid time", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}
