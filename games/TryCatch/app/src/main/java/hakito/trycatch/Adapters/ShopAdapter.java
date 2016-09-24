package hakito.trycatch.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import hakito.trycatch.Data.Content;
import hakito.trycatch.Data.Models.Good;
import hakito.trycatch.R;

/**
 * Created by Oleg on 29-Dec-15.
 */
public class ShopAdapter extends ArrayAdapter<Good> {
    LayoutInflater inflater;

    public ShopAdapter(Context context) {
        super(context, 0);

        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = inflater.inflate(R.layout.good_layout, parent, false);
        Good g = getItem(position);
        ((TextView)v.findViewById(R.id.text_cost)).setText(String.format("%d", g.getCost()));
        ((TextView)v.findViewById(R.id.text_name)).setText(String.format("%s", g.getName()));
        ((TextView)v.findViewById(R.id.text_description)).setText(String.format("%s", g.getDescription()));

        ((ImageView)v.findViewById(R.id.imageView)).setImageDrawable(getContext().getResources().getDrawable(Content.getDrawable(g)));
        return v;
    }
}
