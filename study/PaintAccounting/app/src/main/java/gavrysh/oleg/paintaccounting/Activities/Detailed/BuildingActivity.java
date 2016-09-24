package gavrysh.oleg.paintaccounting.Activities.Detailed;

import android.annotation.SuppressLint;
import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import gavrysh.oleg.paintaccounting.Activities.BaseActivity;
import gavrysh.oleg.paintaccounting.Constants;
import gavrysh.oleg.paintaccounting.Data.DataBaseHelper;

import gavrysh.oleg.paintaccounting.Fragments.GenericListFragment;
import gavrysh.oleg.paintaccounting.Models.Building;
import gavrysh.oleg.paintaccounting.Models.Room;
import gavrysh.oleg.paintaccounting.R;

public class BuildingActivity extends BaseActivity {

    TextView name, adress;

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_building);
        name = (TextView)findViewById(R.id.textTask);
        adress = (TextView)findViewById(R.id.textAdress);
        tryFill();
        setTitle("Building");

        if(id!=-1) {
           final GenericListFragment rooms = new GenericListFragment(new GenericListFragment.ReaderInterface<Room>() {
                @Override
                public List<Room> getList() {
                    DataBaseHelper h = new DataBaseHelper(name);

                    return h.getRooms(id);
                }
            }, DataBaseHelper.Table.ROOMS, RoomActivity.class);
            Bundle b = new Bundle();
            b.putBoolean(GenericListFragment.EXTRA_READONLY, true);
            rooms.setArguments(b);
            getSupportFragmentManager().beginTransaction().replace(R.id.frame, rooms).commit();
            findViewById(R.id.buttonAddRoom).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rooms.add(id);
                }
            });
        }
        else {findViewById(R.id.layout_rooms).setVisibility(View.INVISIBLE);}
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
        }
    }

    protected void apply() throws SQLException
    {
        Building a=new Building();



        a.id=id;
        a.name = name.getText().toString();
        a.adress = adress.getText().toString();
        DataBaseHelper h = new DataBaseHelper(name);
        if(id == -1)
        {
         h.insertBuilding(a);
        }
        else
        {
            h.updateBuilding(a);
        }

    }

    void fillViews()
    {
        DataBaseHelper h = new DataBaseHelper(name);
        Building b = h.getBuilding(id);
        name.setText(b.name);
        adress.setText(b.adress);

    }
}
