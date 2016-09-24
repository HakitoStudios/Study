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
import gavrysh.oleg.paintaccounting.Models.Descriptable;
import gavrysh.oleg.paintaccounting.Models.Room;
import gavrysh.oleg.paintaccounting.Models.Serve;
import gavrysh.oleg.paintaccounting.R;
import gavrysh.oleg.paintaccounting.Fragments.GenericListFragment;

public class RoomActivity extends BaseActivity implements View.OnClickListener{

    private static final int BUILING_CODE = 1;
    TextView name, descr, building;
    private int buildingId;



    @SuppressLint("MissingSuperCall")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_room);

        name = (TextView)findViewById(R.id.textTask);
        descr = (TextView)findViewById(R.id.textDescription);

        building = (TextView)findViewById(R.id.textBuilding);
        building.setOnClickListener(this);
        findViewById(R.id.buttonBuilding).setOnClickListener(this);
        tryFill();
        setTitle("Room");

        if(id!=-1) {
            final GenericListFragment serves = new GenericListFragment(new GenericListFragment.ReaderInterface<Serve>() {
                @Override
                public List<Serve> getList() {
                    return new DataBaseHelper(name).getServes(id, -1);
                }
            }, DataBaseHelper.Table.SERVES, ServeActivity.class);
            Bundle b = new Bundle();
            b.putBoolean(GenericListFragment.EXTRA_READONLY, true);
            serves.setArguments(b);
            getSupportFragmentManager().beginTransaction().replace(R.id.frame, serves).commit();
            findViewById(R.id.buttonAddServe).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    serves.add(new Serve(0, 0, id, ""));
                }
            });
        }
        else {findViewById(R.id.layout_serves).setVisibility(View.INVISIBLE);}
    }

    void tryFill()
    {
        if(getIntent().getExtras()!=null&& getIntent().getExtras().containsKey(Constants.EXTRA_ID))
        {
            id= getIntent().getExtras().getInt(Constants.EXTRA_ID);
            fillViews();
        }
        else
        {
            id=-1;
            buildingId = getIntent().getIntExtra(GenericListFragment.EXTRA_OPTIONAL_ID, -1);
            fillBuilding();
        }
    }

    protected void apply()  throws SQLException
    {
        Room r = new Room();



r.id=id;
        r.name = name.getText().toString();
        r.description = descr.getText().toString();

        r.builingId = buildingId;
        DataBaseHelper h = new DataBaseHelper(name);
        if(id == -1)
        {

            h.insertRoom(r);
        }
        else
        {
            h.updateRoom(r);
        }
    }

    void fillBuilding()
    {
        try {
            DataBaseHelper h = new DataBaseHelper(name);
            building.setText(h.getBuilding(buildingId).name);
            building.setEnabled(true);
        }
        catch (Exception e)
        {
            building.setEnabled(false);
        }
    }

    void fillViews()
    {

        DataBaseHelper h = new DataBaseHelper(name);
        Room r = h.getRoom(id);

        buildingId = r.builingId;
        fillBuilding();

        name.setText(r.name);
        descr.setText(r.description);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK)
        {
            if(requestCode == BUILING_CODE)
            {
                buildingId = data.getIntExtra(SelectActivity.EXTRA_ID, -1);
                fillBuilding();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonBuilding:
                Intent intentr =new Intent(this, SelectActivity.class);
                intentr.putExtra(SelectActivity.EXTRA_TYPE, SelectActivity.BUILDING);
                startActivityForResult(intentr, BUILING_CODE);
            break;
            case R.id.textBuilding:
                Intent intentsr = new Intent(this, BuildingActivity.class);
                intentsr.putExtra(Constants.EXTRA_ID, buildingId);
                startActivity(intentsr);
                break;
        }
    }
}
