package hakito.graphplayer;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TableRow;

/**
 * Created by Oleg on 24-Dec-15.
 */
public class DaysOfWeekPickerDialog extends AlertDialog implements View.OnClickListener {
    public interface DaysOfWeekLitener {
        void onChanged(boolean[] days);

    }



    Activity a;
    boolean[] days;

    public DaysOfWeekPickerDialog(Activity a, boolean[] days) {
        super(a);
        this.a = a;
        this.days = days;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.days_of_week_layout);
        findViewById(R.id.button_ok).setOnClickListener(this);
        if(days!=null)
        {
            TableRow row = (TableRow)findViewById(R.id.tableRow_days);
            for (int i=0;i<row.getChildCount();i++)
            {
                 ((DayView)row.getChildAt(i)).setChecked(days[i]);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_ok:
                TableRow row = (TableRow)findViewById(R.id.tableRow_days);
                boolean[] days = new boolean[7];
                for (int i=0;i<row.getChildCount();i++)
                {
                    days[i] = ((DayView)row.getChildAt(i)).isChecked();
                }
                ((DaysOfWeekLitener) a).onChanged(days);
                dismiss();
                break;
        }
    }
}
