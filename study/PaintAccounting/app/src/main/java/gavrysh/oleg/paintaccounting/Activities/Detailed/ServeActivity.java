package gavrysh.oleg.paintaccounting.Activities.Detailed;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import gavrysh.oleg.paintaccounting.Activities.BaseActivity;
import gavrysh.oleg.paintaccounting.Activities.SelectActivity;
import gavrysh.oleg.paintaccounting.Constants;
import gavrysh.oleg.paintaccounting.Data.DataBaseHelper;

import gavrysh.oleg.paintaccounting.Fragments.PaintingsFragment;
import gavrysh.oleg.paintaccounting.Models.Room;
import gavrysh.oleg.paintaccounting.Models.Serve;
import gavrysh.oleg.paintaccounting.R;
import gavrysh.oleg.paintaccounting.Fragments.GenericListFragment;

public class ServeActivity extends BaseActivity implements View.OnClickListener{

    private static final int ROOM_CODE = 1, WORKER_CODE=0;
    TextView task, room, worker;
    private int roomId, workerId;
    public static final String INIT_DATA_EXTRA="initData";


    @SuppressLint("MissingSuperCall")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_serve);

        task = (TextView)findViewById(R.id.textTask);
        room = (TextView)findViewById(R.id.textRoom);
        worker = (TextView)findViewById(R.id.textWorker);


        room.setOnClickListener(this);
        worker.setOnClickListener(this);

        findViewById(R.id.buttonRoom).setOnClickListener(this);
        findViewById(R.id.buttonWorker).setOnClickListener(this);
        tryFill();
        setTitle("Serve");
    }

    void tryFill() {id = roomId = workerId = -1;
        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().containsKey(Constants.EXTRA_ID)) {
                id = getIntent().getExtras().getInt(Constants.EXTRA_ID);
                fillViews();
                return;
            } else {


                if (getIntent().getExtras().containsKey(INIT_DATA_EXTRA)) {
                    Serve init = (Serve) getIntent().getExtras().getSerializable(INIT_DATA_EXTRA);
                    if (init.roomId != -1) {
                        roomId = init.roomId;
                        fillRoom();
                    }
                    if (init.workerId != -1) {
                        workerId = init.workerId;
                        fillRoom();
                    }
                }
            }
        } else {

        }
    }

    protected void apply()  throws SQLException
    {
        Serve s = new Serve();

        s.id=id;
        s.roomId = roomId;
        s.workerId = workerId;
        s.tasks = task.getText().toString();

        DataBaseHelper h = new DataBaseHelper(task);
        if(id == -1)        {
            h.insertServe(s);
        }
        else {
            h.updateServe(s);
        }
    }

    void fillWorker()
    {
        try {
            DataBaseHelper h = new DataBaseHelper(task);
            worker.setText(h.getWorker(workerId).name);
            worker.setEnabled(true);
        }
        catch (Exception e)
        {
            worker.setEnabled(false);
        }
    }

    void fillRoom()
    {
        try {
            DataBaseHelper h = new DataBaseHelper(task);
            room.setText(h.getRoom(roomId).name);
            room.setEnabled(true);
        }
        catch (Exception e)
        {
            room.setEnabled(false);
        }
    }

    void fillViews()
    {

        DataBaseHelper h = new DataBaseHelper(task);
        Serve s = h.getServe(id);

        roomId = s.roomId;
        workerId = s.workerId;

        fillWorker();
        fillRoom();

        task.setText(s.tasks);
    }








    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK)
        {
            if(requestCode == ROOM_CODE)
        {
            roomId = data.getIntExtra(SelectActivity.EXTRA_ID, -1);
            fillRoom();
        }
            else if(requestCode == WORKER_CODE)
            {
                workerId = data.getIntExtra(SelectActivity.EXTRA_ID, -1);
                fillWorker();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonRoom:
                Intent intentr =new Intent(this, SelectActivity.class);
                intentr.putExtra(SelectActivity.EXTRA_TYPE, SelectActivity.ROOM);
                startActivityForResult(intentr, ROOM_CODE);
                break;
            case R.id.textRoom:
                Intent intentsr = new Intent(this, RoomActivity.class);
                intentsr.putExtra(Constants.EXTRA_ID, roomId);
                startActivity(intentsr);
                break;

            case R.id.buttonWorker:
                Intent intentrw =new Intent(this, SelectActivity.class);
                intentrw.putExtra(SelectActivity.EXTRA_TYPE, SelectActivity.WORKER);
                startActivityForResult(intentrw, WORKER_CODE);
                break;
            case R.id.textWorker:
                Intent intentsrw = new Intent(this, WorkerActivity.class);
                intentsrw.putExtra(Constants.EXTRA_ID, workerId);
                startActivity(intentsrw);
                break;
        }
    }
}
