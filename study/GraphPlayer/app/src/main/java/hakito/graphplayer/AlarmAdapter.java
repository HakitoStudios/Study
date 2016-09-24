package hakito.graphplayer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * Created by Oleg on 25-Dec-15.
 */
public class AlarmAdapter extends ArrayAdapter<Alarm> {



    LayoutInflater inflater;

    public AlarmAdapter(Context context) {
        super(context, R.layout.list_item);
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

            View v = inflater.inflate(R.layout.list_item, parent, false);

        final Alarm a = getItem(position);
        ((ImageView)v.findViewById(R.id.imageView_mode)).setImageDrawable(getContext().getResources().getDrawable(a.isNight()?R.drawable.moon:R.drawable.sun));
        ((TextView) v.findViewById(R.id.textView_time)).setText(TextConverter.getTime(a.getTime()));
        ((TextView)v.findViewById(R.id.textView_lable)).setText(a.getLabel());
        Switch enabled = ((Switch)v.findViewById(R.id.switch_alarm_enabled));
        enabled.setChecked(a.enabled);
        enabled.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                new DataBaseHelper(getContext()).setEnabled(a.id, isChecked);
            }
        });

        TableRow row = (TableRow)v.findViewById(R.id.tableRow_days);
        for (int i=0;i<row.getChildCount();i++)
        {
            DayView dayView =((DayView) row.getChildAt(i));
                    dayView.setChecked(a.days[i]);
            dayView.setEnabled(false);
        }
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AlarmActivity.class);
                intent.putExtra(AlarmActivity.EXTRA_ALARM, getItem(position));
                getContext().startActivity(intent);
            }
        });
        v.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(getContext()).setMessage("Remove alarm?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new DataBaseHelper(getContext()).remove(a.id);
                        remove(a);
                    }
                }).setNegativeButton("No", null).create().show();
                return false;
            }
        });
        return v;
    }


}
