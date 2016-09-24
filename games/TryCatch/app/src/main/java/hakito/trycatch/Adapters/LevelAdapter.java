package hakito.trycatch.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import hakito.trycatch.Data.Helpers.DBHelper;
import hakito.trycatch.Data.Models.Level;
import hakito.trycatch.Data.Models.Record;
import hakito.trycatch.R;

/**
 * Created by Oleg on 27-Dec-15.
 */
public class LevelAdapter extends ArrayAdapter<Level> {

    LayoutInflater inflater;
    public LevelAdapter(Context context) {
        super(context, 0);
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = inflater.inflate(R.layout.level_layout, parent, false);
        Level l = getItem(position);
        ((TextView)v.findViewById(R.id.text_levelIndex)).setText(""+l.getIndex());
        ((TextView)v.findViewById(R.id.text_levelSize)).setText(String.format("%dx%d", l.getSize(), l.getSize()));
        final int overall_stars = new DBHelper(getContext()).getStarsCount();
        if(l.getStarsToOpen()>overall_stars)
        {
            v.findViewById(R.id.lay_locked).setVisibility(View.VISIBLE);
            ((TextView)v.findViewById(R.id.text_locked)).setText(String.format(getContext().getString(R.string.you_need_more_stars), l.getStarsToOpen() - overall_stars));
        }
        RatingBar ratingBar = ((RatingBar) v.findViewById(R.id.ratingBar_levelRating));
        Record record = new DBHelper(getContext()).getRecord(l.getIndex());
        if(record!=null)
        {
         ratingBar.setRating(record.stars);
        }

        return v;
    }
}
