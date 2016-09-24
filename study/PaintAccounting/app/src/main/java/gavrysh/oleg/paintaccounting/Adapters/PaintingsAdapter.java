package gavrysh.oleg.paintaccounting.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import gavrysh.oleg.paintaccounting.Models.Painting;
import gavrysh.oleg.paintaccounting.R;

/**
 * Created by Oleg on 16-Nov-15.
 */
public class PaintingsAdapter extends ArrayAdapter<Painting> {

    private LayoutInflater inflater;

    public PaintingsAdapter(Context context, int resource) {
        super(context, resource);
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

            View view;
            TextView text;

            if (convertView == null) {
                view = inflater.inflate(R.layout.painting_list_item, parent, false);
            } else {
                view = convertView;
            }

        Painting p = getItem(position);

        ((TextView) view.findViewById(R.id.textTask)).setText(p.name);

        if(p.bitmap!=null)
        {
            ((ImageView)view.findViewById(R.id.image)).setImageBitmap(p.bitmap);
        }
        else
        {
            ((ImageView)view.findViewById(R.id.image)).setImageDrawable(getContext().getResources().getDrawable(R.drawable.header));
        }

            return view;
    }
}
