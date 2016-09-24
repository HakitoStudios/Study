package gavrysh.oleg.paintaccounting.Fragments;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.Serializable;
import java.util.List;

import gavrysh.oleg.paintaccounting.Activities.Detailed.ServeActivity;
import gavrysh.oleg.paintaccounting.Activities.IdListener;
import gavrysh.oleg.paintaccounting.Activities.SelectActivity;
import gavrysh.oleg.paintaccounting.Adapters.GenericAdapter;
import gavrysh.oleg.paintaccounting.Constants;
import gavrysh.oleg.paintaccounting.Data.DataBaseHelper;
import gavrysh.oleg.paintaccounting.Models.Descriptable;
import gavrysh.oleg.paintaccounting.Models.Idable;
import gavrysh.oleg.paintaccounting.Models.Serve;
import gavrysh.oleg.paintaccounting.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class GenericListFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener, TitleProvider{

public static final String EXTRA_READONLY="readOnly", EXTRA_OPTIONAL_ID="optionalId";

    @Override
    public String getTitle() {
        String res;
        if(onlySelect)
        {
            res = "Select ";
        }
        else {
            res = "Edit ";
        }
        switch (table)
        {
            case AUTHORS: res+="authors";
                break;
            case PICS:res+="paintings";
                break;
            case TECHS:res+="techniques";
                break;
            case GENRES:res+="genres";
                break;
            case ROOMS:res+="rooms";
                break;
            case BUILDINGS:res+="buildings";
                break;
            case WORKERS:res+="workers";
                break;
            case SERVES:res+="serves"; break;
        }
        return res;
    }

    public interface  ReaderInterface <T extends Descriptable>
    {
        List<T> getList();
    }

    private ReaderInterface reader;
    ListView listView;
    GenericAdapter adapter;
    private boolean onlySelect;

    private DataBaseHelper.Table table;

    private Class<?> cls;

    public GenericListFragment() {
        // Required empty public constructor
    }


    @SuppressLint("ValidFragment")
    public <T extends Descriptable> GenericListFragment(ReaderInterface<T> reader, DataBaseHelper.Table table, Class<?> cls)
    {
        this.table = table;
        this.reader = reader;
        this.cls = cls;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(getArguments()!=null)
        {
            if(getArguments().getBoolean(SelectActivity.EXTRA_SELECT_ONLY))
            {
                onlySelect = true;
            }
        }
        View v =inflater.inflate(R.layout.fragment_simple_list, container, false);
        listView = (ListView)v.findViewById(R.id.listView);

        listView.setOnItemClickListener(this);


        View fab = v.findViewById(R.id.buttonNew);
        fab.setOnClickListener(this);
        if(getArguments() != null && getArguments().containsKey(EXTRA_READONLY))
        {
           fab.setVisibility(View.INVISIBLE);
        }
        listView.setOnCreateContextMenuListener(this);
        registerForContextMenu(listView);
        return v;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        AdapterView.AdapterContextMenuInfo aMenuInfo = (AdapterView.AdapterContextMenuInfo) menuInfo;
        final int pos = aMenuInfo.position;
        menu.add("Remove").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                DataBaseHelper h = new DataBaseHelper(listView);
                h.delete(((Idable) adapter.getItem(pos)).id, table);
                refillList();
                return false;
            }
        });
    }

    void refillList()
    {
        adapter = new GenericAdapter(getActivity(), 0);
        adapter.addAll(reader.getList());
        listView.setAdapter(adapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        refillList();
    }

    public void add(Serializable s)
    {
        Intent intent =new Intent(getActivity(), cls);
        if(s!=null)
        {
            intent.putExtra(ServeActivity.INIT_DATA_EXTRA, s);
        }
        startActivity(intent);
    }

    public void add(int optionalParentId)
    {

        Intent intent =new Intent(getActivity(), cls);
        if(optionalParentId!=-1)
        {
            intent.putExtra(EXTRA_OPTIONAL_ID, optionalParentId);
        }
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.buttonNew:
                add(-1);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(onlySelect)
        {
            ((IdListener)getActivity()).onSelected(((Idable)adapter.getItem(position)).id);
            return;
        }
        Intent intent = new Intent(getActivity(), cls);
        intent.putExtra(Constants.EXTRA_ID, ((Idable)adapter.getItem(position)).id);
        startActivity(intent);
    }
}
