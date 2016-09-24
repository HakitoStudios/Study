package gavrysh.oleg.paintaccounting.Activities;

import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import gavrysh.oleg.paintaccounting.Activities.Detailed.AuthorActivity;
import gavrysh.oleg.paintaccounting.Activities.Detailed.BuildingActivity;
import gavrysh.oleg.paintaccounting.Activities.Detailed.ServeActivity;
import gavrysh.oleg.paintaccounting.Activities.Detailed.WorkerActivity;
import gavrysh.oleg.paintaccounting.Constants;
import gavrysh.oleg.paintaccounting.Data.DataBaseHelper;

import gavrysh.oleg.paintaccounting.Fragments.GenericListFragment;
import gavrysh.oleg.paintaccounting.Fragments.PaintingsFragment;
import gavrysh.oleg.paintaccounting.Fragments.ReportsFragment;
import gavrysh.oleg.paintaccounting.Fragments.SQLFragment;
import gavrysh.oleg.paintaccounting.Fragments.TitleProvider;
import gavrysh.oleg.paintaccounting.Models.Author;
import gavrysh.oleg.paintaccounting.Models.Building;
import gavrysh.oleg.paintaccounting.Models.Serve;
import gavrysh.oleg.paintaccounting.Models.Worker;
import gavrysh.oleg.paintaccounting.R;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int FILTER_CODE=1, SEARCH_CODE=2;
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.nav_paintings);
        setFragment(new PaintingsFragment());
      /*
        final DataBaseHelper h = new DataBaseHelper(findViewById(R.id.frame));
        setFragment(new GenericListFragment(new GenericListFragment.ReaderInterface<Serve>() {
            @Override
            public List<Serve> getList() {
                return h.getServes(-1, -1);
            }
        }, DataBaseHelper.Table.SERVES, ServeActivity.class));
        */
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        this.menu= menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        final Fragment f = getSupportFragmentManager().findFragmentById(R.id.frame);
        if (id == R.id.action_sort) {

            if(f!= null && f instanceof PaintingsFragment)
            {
                String[] items = new String[]{"Name", "Year", "Price"};
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Constants.Order order = null;
                        switch (which) {
                            case 0:
                                order = Constants.Order.name;
                                break;
                            case 1:
                                order = Constants.Order.year;
                                break;
                            case 2:
                                order = Constants.Order.price;
                                break;
                        }
                        if (order != null) {
                            ((PaintingsFragment) f).sortBy(order);
                        }
                    }
                });
                builder.setTitle("Sort by");
                builder.create().show();
            }
            return true;
        }
        else if(id == R.id.action_filter)
        {
                       startActivityForResult(new Intent(this, FilterActivity.class), FILTER_CODE);
        }
        else if(id== R.id.search)
        {
            startActivityForResult(new Intent(this, SearchActivity.class), SEARCH_CODE);
        }
        else if(id== R.id.sqlAuthorInfo) {
            if(f instanceof SQLFragment) {
                String query = "select authors.name as Name, count(*) || ' pcs' as Number_of_Paintings from authors, pictures where authors._id = pictures.author_id group by authors.name";
                ((SQLFragment) f).query(query, "Author and count of paintings");
            }
        }
        else if(id== R.id.sqlBuildingInfo) {
            if(f instanceof SQLFragment) {
                String query = "select buildings.name as Name, count(distinct rooms._id) as Rooms, count(distinct pictures._id) as Pictures from rooms, buildings, pictures where buildings._id = rooms.building_id and pictures.room_id = rooms._id group by buildings._id";
                ((SQLFragment) f).query(query, "Buildings and count of rooms");
            }
        }
        else if(id== R.id.sqlPicInfo) {
            if(f instanceof SQLFragment) {
                String query = "select pictures.name as Name, authors.name as Author, year as Year, price as Price from pictures, authors where authors._id=pictures.author_id";
                ((SQLFragment) f).query(query, "Detailed info about paintings");
            }
        }else if(id== R.id.sqlSave) {
            if(f instanceof SQLFragment) {
                ((SQLFragment) f).save();
            }
        }else if(id== R.id.sqlHelp) {
            startActivity(new Intent(this, Database.class));
        }



        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK)
        {
            if(requestCode == FILTER_CODE)
            {
                FilterActivity.FilterInfo filterInfo = (FilterActivity.FilterInfo)data.getSerializableExtra(FilterActivity.EXTRA_FILTER);
                Fragment f = getSupportFragmentManager().findFragmentById(R.id.frame);
                if(f instanceof PaintingsFragment)
                {
                    ((PaintingsFragment)f).filter(filterInfo);
                }
            }else if(requestCode == SEARCH_CODE)
            {
                String search = data.getStringExtra(SearchActivity.EXTRA_SEARCH);
                Fragment f = getSupportFragmentManager().findFragmentById(R.id.frame);
                if(f instanceof PaintingsFragment)
                {
                    ((PaintingsFragment)f).search(search);
                }
            }
        }
    }

    void setFragment(Fragment fragment) {
    if(fragment instanceof TitleProvider)
    {
        setTitle(((TitleProvider) fragment).getTitle());
    }
        if(menu!=null)
        {
                menu.setGroupVisible(R.id.pic, fragment instanceof PaintingsFragment);
                menu.setGroupVisible(R.id.sql, fragment instanceof SQLFragment);
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment).commit();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

       final DataBaseHelper h = new DataBaseHelper(findViewById(R.id.frame));

        int id = item.getItemId();
        switch (id) {
            case R.id.nav_authors:
                setFragment(new GenericListFragment(new GenericListFragment.ReaderInterface<Author>() {
                    @Override
                    public List<Author> getList() {
                        return h.getAuthors();
                    }
                }, DataBaseHelper.Table.AUTHORS, AuthorActivity.class));
                break;
            case R.id.nav_buildings:
                setFragment(new GenericListFragment(new GenericListFragment.ReaderInterface<Building>() {
                    @Override
                    public List<Building> getList() {
                        return h.getBuildings();
                    }
                }, DataBaseHelper.Table.BUILDINGS, BuildingActivity.class));
                break;
            case R.id.nav_help:
                startActivity(new Intent(this, HelpActivity.class));
                break;
            case R.id.nav_paintings:
                setFragment(new PaintingsFragment());
                break;
            case R.id.nav_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.nav_workers:
                setFragment(new GenericListFragment(new GenericListFragment.ReaderInterface<Worker>() {
                    @Override
                    public List<Worker> getList() {
                        return h.getWorkers();
                    }
                }, DataBaseHelper.Table.WORKERS, WorkerActivity.class));
                break;
            case R.id.nav_serves:
                setFragment(new GenericListFragment(new GenericListFragment.ReaderInterface<Serve>() {
                    @Override
                    public List<Serve> getList() {
                        return h.getServes(-1, -1);
                    }
                }, DataBaseHelper.Table.SERVES, ServeActivity.class));
                break;
            case R.id.nav_sql:
                setFragment(new SQLFragment());
                break;
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
