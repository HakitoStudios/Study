package hakito.trycatch.Data.Models;

import android.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Oleg on 28-Dec-15.
 */
public class Player {


    private int coins;
    private List<Good> bonuses;

    public  Player(int coins) {
        this.coins = coins;
        bonuses = new ArrayList<>();
    }

    public List<Good> getBonuses() {
        return bonuses;
    }

    public void addBonus(Good good)
    {
        bonuses.add(good);
    }

    public boolean remove(Good good) {
        return bonuses.remove(good);
    }

    public int getBonusesCount(Good good)
    {
        return Collections.frequency(bonuses, good);
    }

    public int getCoins() {
        return coins;
    }

    public void addCoins(int coins) {
        this.coins += coins;
    }
}
