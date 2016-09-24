package gavrysh.oleg.paintaccounting.Activities.Detailed;

import android.annotation.SuppressLint;
import android.database.SQLException;
import android.os.Bundle;
import android.widget.TextView;

import gavrysh.oleg.paintaccounting.Activities.BaseActivity;
import gavrysh.oleg.paintaccounting.Constants;
import gavrysh.oleg.paintaccounting.Data.DataBaseHelper;

import gavrysh.oleg.paintaccounting.Models.Worker;
import gavrysh.oleg.paintaccounting.R;

public class WorkerActivity extends BaseActivity {

        TextView name, phone;

        @SuppressLint("MissingSuperCall")
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState, R.layout.activity_worker);

            name = (TextView)findViewById(R.id.textTask);
            phone = (TextView)findViewById(R.id.textPhone);
            tryFill();
            setTitle("Worker");
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
            Worker w = new Worker();



w.id=id;
            w.name = name.getText().toString();
            w.phone = phone.getText().toString();
            DataBaseHelper h = new DataBaseHelper(name);
            if(id == -1)
            {

                h.insertWorker(w);
            }else
            {
                h.updateWorker(w);
            }
        }

        void fillViews()
        {
            DataBaseHelper h = new DataBaseHelper(name);
            Worker w = h.getWorker(id);
            name.setText(w.name);
            phone.setText(w.phone);

        }




}
