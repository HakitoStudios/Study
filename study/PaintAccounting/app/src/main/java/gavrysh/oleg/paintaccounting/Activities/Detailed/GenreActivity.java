package gavrysh.oleg.paintaccounting.Activities.Detailed;

import android.annotation.SuppressLint;
import android.database.SQLException;
import android.os.Bundle;
import android.widget.TextView;

import gavrysh.oleg.paintaccounting.Activities.BaseActivity;
import gavrysh.oleg.paintaccounting.Constants;
import gavrysh.oleg.paintaccounting.Data.DataBaseHelper;
import gavrysh.oleg.paintaccounting.Models.Genre;
import gavrysh.oleg.paintaccounting.R;

public class GenreActivity extends BaseActivity {

    TextView name, description;

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_genre);

        name = (TextView)findViewById(R.id.textTask);
        description = (TextView)findViewById(R.id.textDescription);
        tryFill();
        setTitle("Genre");
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
        Genre g = new Genre();



        g.id=id;
        g.name = name.getText().toString();
        g.description = description.getText().toString();
        DataBaseHelper h = new DataBaseHelper(name);
        if(id == -1)
        {
            h.insertGenre(g);
        }
        else
        {
            h.updateGenre(g);
        }
    }

    void fillViews()
    {
        DataBaseHelper h = new DataBaseHelper(name);
        Genre g = h.getGenre(id);
        name.setText(g.name);
        description.setText(g.description);

    }

}
