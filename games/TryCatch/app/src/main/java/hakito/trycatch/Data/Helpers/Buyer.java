package hakito.trycatch.Data.Helpers;

import android.content.Context;
import android.widget.Toast;

import hakito.trycatch.Data.Content;
import hakito.trycatch.Data.Models.Good;
import hakito.trycatch.Data.Models.Player;
import hakito.trycatch.R;

/**
 * Created by Oleg on 29-Dec-15.
 */
public class Buyer {

    public static boolean buy(Good good, Context context)
    {
        DBHelper dbHelper = new DBHelper(context);
        Player player =dbHelper.getPlayer();
        if(player.getCoins() >= good.getCost())
        {
            player.addCoins(-good.getCost());
            player.addBonus(good);
            dbHelper.savePlayer(player);
            return true;
        }
        else
        {
            Toast.makeText(context, R.string.you_need_more_money, Toast.LENGTH_LONG).show();
            return false;
        }
    }
}
