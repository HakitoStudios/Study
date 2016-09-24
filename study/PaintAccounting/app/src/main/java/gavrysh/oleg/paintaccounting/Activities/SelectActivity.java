package gavrysh.oleg.paintaccounting.Activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;

import gavrysh.oleg.paintaccounting.Activities.Detailed.AuthorActivity;
import gavrysh.oleg.paintaccounting.Activities.Detailed.BuildingActivity;
import gavrysh.oleg.paintaccounting.Activities.Detailed.GenreActivity;
import gavrysh.oleg.paintaccounting.Activities.Detailed.RoomActivity;
import gavrysh.oleg.paintaccounting.Activities.Detailed.ServeActivity;
import gavrysh.oleg.paintaccounting.Activities.Detailed.TechActivity;
import gavrysh.oleg.paintaccounting.Activities.Detailed.WorkerActivity;
import gavrysh.oleg.paintaccounting.Data.DataBaseHelper;

import gavrysh.oleg.paintaccounting.Fragments.GenericListFragment;
import gavrysh.oleg.paintaccounting.Models.Author;
import gavrysh.oleg.paintaccounting.Models.Building;
import gavrysh.oleg.paintaccounting.Models.Genre;
import gavrysh.oleg.paintaccounting.Models.Room;
import gavrysh.oleg.paintaccounting.Models.Serve;
import gavrysh.oleg.paintaccounting.Models.Technique;
import gavrysh.oleg.paintaccounting.Models.Worker;
import gavrysh.oleg.paintaccounting.R;

public class SelectActivity extends AppCompatActivity implements IdListener{

    public static final int AUTHOR=1, GENRE=2, TECH=3, BUILDING=4, ROOM=5, WORKER=6;
    public static final String EXTRA_TYPE="extra_type", EXTRA_SELECT_ONLY ="extra_select", EXTRA_ID="extra_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        final DataBaseHelper h = new DataBaseHelper(findViewById(R.id.frame));
        Fragment f = null;
        String title="";
        switch (getIntent().getExtras().getInt(EXTRA_TYPE))
        {
            case AUTHOR:
                title="Authors";
                f = new GenericListFragment(new GenericListFragment.ReaderInterface<Author>() {
                @Override
                public List<Author> getList() {
                    return h.getAuthors();
                }
            }, DataBaseHelper.Table.AUTHORS, AuthorActivity.class);
                break;
            case GENRE:
                title="Genres";
                f = new GenericListFragment(new GenericListFragment.ReaderInterface<Genre>() {
                    @Override
                    public List<Genre> getList() {
                        return h.getGenres();
                    }
                }, DataBaseHelper.Table.GENRES, GenreActivity.class);
                break;
            case ROOM:
                title="Rooms";
                f = new GenericListFragment(new GenericListFragment.ReaderInterface<Room>() {
                @Override
                public List<Room> getList() {
                    return h.getRooms(-1);
                }
            }, DataBaseHelper.Table.ROOMS,  RoomActivity.class);
                break;
            case TECH:
                title="Techs";
                f = new GenericListFragment(new GenericListFragment.ReaderInterface<Technique>() {
                    @Override
                    public List<Technique> getList() {
                        return h.getTechs();
                    }
                }, DataBaseHelper.Table.TECHS, TechActivity.class);
                break;
            case BUILDING:
                title="Buildings";
                f = new GenericListFragment(new GenericListFragment.ReaderInterface<Building>() {
                    @Override
                    public List<Building> getList() {
                        return h.getBuildings();
                    }
                }, DataBaseHelper.Table.BUILDINGS, BuildingActivity.class);
                break;
            case WORKER:
                title="Workers";
                f = new GenericListFragment(new GenericListFragment.ReaderInterface<Worker>() {
                    @Override
                    public List<Worker> getList() {
                        return h.getWorkers();
                    }
                }, DataBaseHelper.Table.WORKERS, WorkerActivity.class);
                break;
        }
        setTitle(title);
        Bundle arg = new Bundle();
        arg.putBoolean(EXTRA_SELECT_ONLY, true);
        f.setArguments(arg);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame, f).commit();
    }

    @Override
    public void onSelected(int id) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_ID, id);
        setResult(RESULT_OK, intent);
        finish();
    }
}
