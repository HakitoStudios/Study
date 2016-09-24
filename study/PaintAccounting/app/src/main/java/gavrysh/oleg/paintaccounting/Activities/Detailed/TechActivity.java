package gavrysh.oleg.paintaccounting.Activities.Detailed;

import android.annotation.SuppressLint;
import android.database.SQLException;
import android.os.Bundle;
import android.widget.TextView;

import gavrysh.oleg.paintaccounting.Activities.BaseActivity;
import gavrysh.oleg.paintaccounting.Constants;
import gavrysh.oleg.paintaccounting.Data.DataBaseHelper;
import gavrysh.oleg.paintaccounting.Models.Technique;
import gavrysh.oleg.paintaccounting.R;

public class TechActivity extends BaseActivity {

    TextView name, description;

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_tech);

        name = (TextView)findViewById(R.id.textTask);
        description = (TextView)findViewById(R.id.textDescription);
        tryFill();
        setTitle("Technique");
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
        Technique t = new Technique();



        t.id=id;
        t.name = name.getText().toString();
        t.description = description.getText().toString();
        DataBaseHelper h = new DataBaseHelper(name);
        if(id == -1)
        {
            h.insertTech(t);
        }
        else
        {
            h.updateTech(t);
        }
    }

    void fillViews()
    {
        DataBaseHelper h = new DataBaseHelper(name);
        Technique t = h.getTech(id);
        name.setText(t.name);
        description.setText(t.description);

    }

}
