package gavrysh.oleg.paintaccounting.Activities.Detailed;

import android.annotation.SuppressLint;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import gavrysh.oleg.paintaccounting.Activities.BaseActivity;
import gavrysh.oleg.paintaccounting.Activities.SelectActivity;
import gavrysh.oleg.paintaccounting.Constants;
import gavrysh.oleg.paintaccounting.Data.DataBaseHelper;

import gavrysh.oleg.paintaccounting.Fragments.GenericListFragment;
import gavrysh.oleg.paintaccounting.Fragments.PaintingsFragment;
import gavrysh.oleg.paintaccounting.Models.Author;
import gavrysh.oleg.paintaccounting.R;

public class AuthorActivity extends BaseActivity {



    TextView name, descr;

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_author);

        name = (TextView)findViewById(R.id.textTask);
        descr = (TextView)findViewById(R.id.textDescription);
        tryFill();
        setTitle("Author");

        if(id!=-1) {
            final PaintingsFragment pics = new PaintingsFragment(new GenericListFragment.ReaderInterface() {
                @Override
                public List getList() {
                    return new DataBaseHelper(AuthorActivity.this.descr).getPaintings(id, -1, -1, -1);
                }
            });
            Bundle b = new Bundle();
            b.putBoolean(GenericListFragment.EXTRA_READONLY, true);
            pics.setArguments(b);
            getSupportFragmentManager().beginTransaction().replace(R.id.frame, pics).commit();
            findViewById(R.id.buttonAddPainting).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pics.add(new PaintingsFragment.InitPicInfo(id, SelectActivity.AUTHOR));
                }
            });
        }
        else {findViewById(R.id.layout_paintings).setVisibility(View.INVISIBLE);}

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

    protected void apply()  throws SQLException
    {
        Author a = new Author();

        a.id=id;
        a.name = name.getText().toString();
        a.description = descr.getText().toString();

        DataBaseHelper h = new DataBaseHelper(descr);
        if(id == -1)
        {

                h.insertAuthor(a);

        }
        else
        {
            h.updateAuthor(a);
        }
    }

    void fillViews()
    {
        DataBaseHelper h = new DataBaseHelper(name);
        Author p = h.getAuthor(id);
        name.setText(p.name);
        descr.setText(p.description);

    }


}
