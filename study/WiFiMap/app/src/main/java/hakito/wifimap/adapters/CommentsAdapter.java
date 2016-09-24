package hakito.wifimap.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import hakito.wifimap.POJO.Comment;
import hakito.wifimap.R;

/**
 * Created by Oleg on 20.05.2016.
 */
public class CommentsAdapter extends ArrayAdapter<Comment> {
    LayoutInflater inflater;

    public CommentsAdapter(Context context) {
        super(context, 0);
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = null;

        if(convertView==null) {
            v = inflater.inflate(R.layout.comment_item, parent, false);
        }
        else
        {
            v = convertView;
        }
        Comment c = getItem(position);
        ((TextView)v.findViewById(R.id.tComment)).setText(c.getText());
        ((TextView)v.findViewById(R.id.tVotes)).setText(""+c.getVotes());

        return v;
    }
}
