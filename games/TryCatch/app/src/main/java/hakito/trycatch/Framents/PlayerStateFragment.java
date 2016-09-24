package hakito.trycatch.Framents;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import hakito.trycatch.Data.Helpers.DBHelper;
import hakito.trycatch.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayerStateFragment extends Fragment {

TextView coins;
    public PlayerStateFragment() {

    }

    public void notifyChanged()
    {
        coins.setText(String.format(getContext().getString(R.string.coinss), new DBHelper(getContext()).getPlayer().getCoins()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_player_state, container, false);
        coins = (TextView)v.findViewById(R.id.text_coins);
        notifyChanged();
        return v;
    }

}
