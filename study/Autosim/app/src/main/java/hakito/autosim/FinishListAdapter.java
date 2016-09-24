package hakito.autosim;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import hakito.autosim.activities.FinishActivity;

/**
 * Created by Oleg on 03-Dec-15.
 */
public class FinishListAdapter extends ArrayAdapter<FinishActivity.ResultPart> {
    LayoutInflater inflater;

    public FinishListAdapter(Context context) {
        super(context, 0);
        inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v;
        if(convertView==null)
        {
            v = inflater.inflate(R.layout.list_item, parent, false);
        }
        else
        {
            v = convertView;
        }
        TextView descr = (TextView)v.findViewById(R.id.text_descr), count = (TextView)v.findViewById(R.id.text_num);
        FinishActivity.ResultPart p = getItem(position);
        descr.setText(p.descr);
        count.setText(String.format("x%.2f", p.count));
        if(p.isGood)
        {
            count.setTextColor(Color.GREEN);
        }
        else
        {
            count.setTextColor(Color.RED);
        }
        return v;
    }
}

