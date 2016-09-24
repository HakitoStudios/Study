package hakito.graphplayer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class CancelActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel);
        findViewById(R.id.button_close).setOnClickListener(this);
        findViewById(R.id.button_snooze).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.button_close:
                try {
                    DataBaseHelper.reciever.CancelAlarm(this);
                }
                catch (Exception e){}
                finish();
                break;
            case R.id.button_snooze: finish(); break;

        }
    }
}
