package hakito.trycatch.Framents;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import hakito.trycatch.Data.Content;
import hakito.trycatch.Data.Helpers.DBHelper;
import hakito.trycatch.Data.Models.Good;
import hakito.trycatch.Data.Models.Player;
import hakito.trycatch.Game.Game;
import hakito.trycatch.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BonusesFragment extends Fragment {

    public static final String EXTRA_ENABLED="enabled";
    boolean enabled=true;

    public BonusesFragment() {
    }



private LinearLayout layout;
    private View root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(getArguments()!=null)
        enabled = getArguments().getBoolean(EXTRA_ENABLED, true);
        View v = inflater.inflate(R.layout.fragment_bonuses, container, false);
        layout = (LinearLayout)v.findViewById(R.id.bonus_container);
        loadBonuses();
        return root=v;
    }



    public void loadBonuses()
    {
        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layout.removeAllViews();
        final Player player = new DBHelper(getContext()).getPlayer();
        for (int i=0;i< Content.goods.size();i++) {

            View v = inflater.inflate(R.layout.bonus_layout, layout, false);
            v.setTag(i);
            layout.addView(v);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(v.getLayoutParams());
            params.setMargins(10, 0, 10, 0);
            v.setLayoutParams(params);
            ((ImageView)v.findViewById(R.id.imageView)).setImageDrawable(getResources().getDrawable(Content.getDrawable(Content.goods.get(i))));
            int count = player.getBonusesCount(Content.goods.get(i));
            TextView textCount = (TextView)v.findViewById(R.id.text_count);
            if(count==0)
            {
                textCount.setVisibility(View.INVISIBLE);
                v.setEnabled(false);
            }
            textCount.setText(String.format("%d", count));
if(enabled)
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //index of good
                    int index = (int)v.getTag();
                    DBHelper helper = new DBHelper(getContext());
                    Player p = helper.getPlayer();
                    //getting a good
                    Good g = Content.goods.get(index);
                    p.remove(g);
                    helper.savePlayer(p);
                    Game.get().activateBonus(g);
                    loadBonuses();
                }
            });
        }

        //layout.addView(v);

    }

}
