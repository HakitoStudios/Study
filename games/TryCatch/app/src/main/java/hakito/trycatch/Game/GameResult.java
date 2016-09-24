package hakito.trycatch.Game;

import java.io.Serializable;

/**
 * Created by Oleg on 12-Dec-15.
 */
public class GameResult implements Serializable {
    public float time;
    public int coins, maxCoins, steps;
    public boolean win;

    public int getStars()
    {
        return (int)((float)coins / maxCoins * 5);
    }

    public int getCoinsProgressIn100()
    {
        return (int)((float)coins/maxCoins * 100);
    }
}
