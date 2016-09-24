package hakito.graphplayer;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.CharacterPickerDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class AlarmListActivity extends AppCompatActivity  implements View.OnClickListener{

    AlarmAdapter alarmAdapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        findViewById(R.id.button_add_alarm).setOnClickListener(this);
        listView = (ListView)findViewById(R.id.listView_alarms);



    }

    @Override
    protected void onResume() {
        super.onResume();
        alarmAdapter = new AlarmAdapter(this);
        listView.setAdapter(alarmAdapter);
        alarmAdapter.addAll(new DataBaseHelper(this).getAlarms());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case  R.id.button_add_alarm:
                Intent intent = new Intent(AlarmListActivity.this, AlarmActivity.class);

                startActivity(intent);
                break;
        }
    }
}
