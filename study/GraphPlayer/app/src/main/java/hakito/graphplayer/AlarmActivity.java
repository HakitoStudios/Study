package hakito.graphplayer;

import android.animation.TimeAnimator;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class AlarmActivity extends AppCompatActivity implements View.OnClickListener, TimePickerDialog.OnTimeSetListener {

    public static final String EXTRA_ALARM="alarm";
    public static final int GRAPH_CODE=1;

    Alarm a;

    TextView time, label, text_duration;
    TableRow tableRow;
    Switch mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        mode=(Switch)findViewById(R.id.switch_mode);
        time=(TextView)findViewById(R.id.textView_time);
        text_duration = (TextView)findViewById(R.id.text_duration);
        tableRow = (TableRow)findViewById(R.id.tableRow_days);
        findViewById(R.id.lay_volume).setOnClickListener(this);
        findViewById(R.id.layout_time).setOnClickListener(this);
        findViewById(R.id.lay_duration).setOnClickListener(this);
        label = (TextView)findViewById(R.id.text_label);

        if(getIntent().hasExtra(EXTRA_ALARM))
        {
          a = (Alarm)getIntent().getSerializableExtra(EXTRA_ALARM);
        }
        fillViews();
    }

    void updateTime()
    {
        time.setText(TextConverter.getTime(a.time));
    }

    void fillViews() {
        if (a != null) {
            mode.setChecked(!a.isNight);
            updateTime();
            fillDuration();
            label.setText(a.getLabel());
            for (int i=0;i<tableRow.getChildCount();i++)
            {
                DayView dayView =((DayView) tableRow.getChildAt(i));
                dayView.setChecked(a.days[i]);
            }
        }
        else
        {
            a = new Alarm();
        }
    }

    void fillDuration()
    {
        text_duration.setText("" + a.duration / 60000 + " minutes");
    }

    void save()
    {
        a.setIsNight(!mode.isChecked());
        a.setLabel(label.getText().toString());
        for (int i=0;i<tableRow.getChildCount();i++)
        {
            DayView dayView =((DayView) tableRow.getChildAt(i));
            a.days[i] = dayView.isChecked();
        }

        new DataBaseHelper(this).updateOrInsert(a);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menu_ok:
                save();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK)
        {
            if(requestCode == GRAPH_CODE)
            {
                a.setGraph(data.getFloatArrayExtra(MainActivity.EXTRA_GRAPHIC));
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.lay_volume:
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra(MainActivity.EXTRA_GRAPHIC, a.getGraph());
                startActivityForResult(intent, GRAPH_CODE);
                break;
            case R.id.layout_time:
            Calendar c= Calendar.getInstance();
                long st = a.id==-1?System.currentTimeMillis():a.time;
                c.setTimeInMillis(st - TextConverter.getTime(2, 0));
            new TimePickerDialog(this, this, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
            break;
            case R.id.lay_duration:
                NumberPicker numberPicker = new NumberPicker(this);
                numberPicker.setMinValue(1);
                numberPicker.setMaxValue(60);
                numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        a.duration = newVal*60000;
                        fillDuration();
                    }
                });
                new AlertDialog.Builder(this).setPositiveButton("OK", null).setTitle("Enter duration in minutes").setView(numberPicker).create().show();
                break;
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        a.time = TextConverter.getTime(hourOfDay, minute);
        updateTime();
    }
}
