package gavrysh.oleg.paintaccounting.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import gavrysh.oleg.paintaccounting.Models.Descriptable;
import gavrysh.oleg.paintaccounting.R;

/**
 * Created by Oleg on 16-Nov-15.
 */
public class  GenericAdapter<T extends Descriptable> extends ArrayAdapter<T> {

    private LayoutInflater inflater;

    public GenericAdapter(Context context, int resource) {
        super(context, resource);
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

            View view;


            if (convertView == null) {
                view = inflater.inflate(R.layout.my_simple_list_item, parent, false);
            } else {
                view = convertView;
            }

        Descriptable a = getItem(position);
        ((TextView) view.findViewById(R.id.textDescription)).setText(a.getDescription());
                ((TextView) view.findViewById(R.id.textTask)).setText(a.getName());

            return view;
    }
}
