package hakito.graphplayer;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.List;

public class MasterActivity extends AppCompatActivity implements View.OnClickListener, DaysOfWeekPickerDialog.DaysOfWeekLitener, TimePickerDialog.OnTimeSetListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master);
        findViewById(R.id.button_days).setOnClickListener(this);
        findViewById(R.id.button_graph).setOnClickListener(this);
        findViewById(R.id.button_time).setOnClickListener(this);
        findViewById(R.id.button_list).setOnClickListener(this);

//        Alarm alarm = new Alarm(-1, "yr", 5362346, new boolean[]{false, false, true, false, true, true, false}, "songs", false,
//                new float[]{0.4f, 0.2f, 0.7f, 0.6f, 0.8f, 0.1f, 0.5f, 0.3f, 0.1f, 0.9f});
//        new DataBaseHelper(this).updateOrInsert(alarm);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.button_days:
                new DaysOfWeekPickerDialog(this, null).show();
                break;
            case R.id.button_graph:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.button_time:
                Calendar c= Calendar.getInstance();
                new TimePickerDialog(this, this, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
                break;
            case R.id.button_list:
                startActivity(new Intent(this, AlarmListActivity.class));
                break;
        }
    }

    @Override
    public void onChanged(boolean[] days) {

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

    }
}
