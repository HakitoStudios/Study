package gavrysh.oleg.paintaccounting.Fragments;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.Serializable;

import gavrysh.oleg.paintaccounting.Activities.Detailed.PaintingActivity;
import gavrysh.oleg.paintaccounting.Activities.FilterActivity;
import gavrysh.oleg.paintaccounting.Activities.SelectActivity;
import gavrysh.oleg.paintaccounting.Adapters.PaintingsAdapter;
import gavrysh.oleg.paintaccounting.Constants;
import gavrysh.oleg.paintaccounting.Data.DataBaseHelper;

import gavrysh.oleg.paintaccounting.Models.Idable;
import gavrysh.oleg.paintaccounting.Models.Painting;
import gavrysh.oleg.paintaccounting.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class PaintingsFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener, TitleProvider {

    public static class InitPicInfo implements Serializable {
        public int id, field;

        public InitPicInfo(int id, int field) {
            this.id = id;
            this.field = field;
        }
    }

                PaintingsAdapter adapter;
        ListView listView;
    Constants.Order order = Constants.Order.none;
    FilterActivity.FilterInfo filterInfo = FilterActivity.FilterInfo.NONE;
String searchS="";
    GenericListFragment.ReaderInterface reader;

    @SuppressLint("ValidFragment")
    public PaintingsFragment(GenericListFragment.ReaderInterface r)
    {
        reader = r;
    }

        public PaintingsFragment() {
        // Required empty public constructor
    }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_paintings, container, false);

        listView  = (ListView)v.findViewById(R.id.listView);

        listView.setOnItemClickListener(this);
        v.findViewById(R.id.buttonNew).setOnClickListener(this);
        registerForContextMenu(listView);

            if(getArguments() != null && getArguments().containsKey(GenericListFragment.EXTRA_READONLY))
            {
                v.findViewById(R.id.buttonNew).setVisibility(View.INVISIBLE);
            }
        return v;
    }

    public void add(InitPicInfo info)
    {
        Intent intent =new Intent(getActivity(), PaintingActivity.class);
        intent.putExtra(PaintingActivity.EXTRA_INIT_INFO, info);
        startActivity(intent);
    }

    void refillList(Constants.Order order, FilterActivity.FilterInfo info, String search)
    {
        adapter = new PaintingsAdapter(getActivity(), 0);

        if(reader!=null) {
            adapter.addAll(reader.getList());
        }
        else
        {
            adapter.addAll(new DataBaseHelper(listView).getPaintings(order, info, search));
        }

        listView.setAdapter(adapter);
    }

    public void search(String search)
    {
        this.searchS = search;
        refillList(this.order, filterInfo, search);
    }

    public void filter(FilterActivity.FilterInfo info)
    {
        this.filterInfo = info;
        refillList(this.order, filterInfo, searchS);
    }

    public void sortBy(Constants.Order order)
    {
        this.order = order;
        refillList(this.order, filterInfo, searchS);
    }

    @Override
    public void onResume() {
        super.onResume();
        refillList(order, filterInfo, searchS);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        AdapterView.AdapterContextMenuInfo aMenuInfo = (AdapterView.AdapterContextMenuInfo) menuInfo;
        final int pos = aMenuInfo.position;
        menu.add("Remove").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                DataBaseHelper h = new DataBaseHelper(listView);
                h.delete(adapter.getItem(pos).id, DataBaseHelper.Table.PICS);
                adapter.remove(adapter.getItem(pos));
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.buttonNew:
                startActivity(new Intent(getActivity(), PaintingActivity.class));
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), PaintingActivity.class);
        intent.putExtra(Constants.EXTRA_ID, adapter.getItem(position).id);
        startActivity(intent);
    }

    @Override
    public String getTitle() {
        return "Paintings";
    }
}
