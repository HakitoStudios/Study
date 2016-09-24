package hakito.trycatch.Data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import hakito.trycatch.Data.Models.Good;
import hakito.trycatch.Data.Models.Level;
import hakito.trycatch.R;

/**
 * Created by Oleg on 12-Dec-15.
 */
public class Content {
    public static List<Level> levels;
    public static List<Good> goods;

    public static boolean inited;

    static void loadGoods(Context c)
    {
        goods = new ArrayList<>();

        goods.add(new Good(Good.GoodType.EXTRASENSE, 3, c.getString(R.string.extrasense_name), c.getString(R.string.extrasense_desc)));
        goods.add(new Good(Good.GoodType.FROZE, 5, c.getString(R.string.froze_name), c.getString(R.string.froze_desc)));
        goods.add(new Good(Good.GoodType.TELEPORT, 15, c.getString(R.string.teleport_name), c.getString(R.string.teleport_desc)));

    }

    public static int getDrawable(Good g)
    {
        switch (goods.indexOf(g))
        {
            case 0:return R.drawable.extrasense;
            case 1:return R.drawable.froze;
            case 2:return R.drawable.teleport;
        }
        return 0;
    }

    static void loadLevels()
    {
        levels = new ArrayList<>();
        //index size coins blocks stars
        levels.add(new Level(0, 5, 1, 5, 0));
        levels.add(new Level(1, 5, 1, 4, 4));
        levels.add(new Level(2, 5, 2, 4, 8));
        levels.add(new Level(3, 6, 1, 3, 12));
        levels.add(new Level(4, 6, 2, 3, 16));
        levels.add(new Level(5, 7, 2, 3, 21));
        levels.add(new Level(6, 7, 1, 3, 25));
        levels.add(new Level(7, 8, 2, 2, 29));
        levels.add(new Level(8, 8, 1, 3, 33));
        levels.add(new Level(9, 9, 2, 2, 37));
        levels.add(new Level(10, 9, 1, 3, 41));
        levels.add(new Level(11, 10, 2, 3, 45));
        levels.add(new Level(12, 10, 2, 2, 50));
        levels.add(new Level(13, 10, 10, 0, 54));
        levels.add(new Level(14, 10, 5, 2, 58));
        levels.add(new Level(15, 10, 4, 3, 62));
        levels.add(new Level(16, 11, 4, 3, 66));
        levels.add(new Level(17, 11, 3, 3, 70));
        levels.add(new Level(18, 11, 2, 2, 75));
        levels.add(new Level(19, 11, 1, 3, 79));
        levels.add(new Level(20, 11, 1, 2, 83));
        levels.add(new Level(21, 11, 2, 0, 87));
        levels.add(new Level(22, 12, 1, 3, 91));
        levels.add(new Level(23, 12, 2, 2, 95));
        levels.add(new Level(24, 12, 2, 1, 100));
        levels.add(new Level(25, 13, 2, 0, 104));
        levels.add(new Level(26, 13, 2, 4, 110));
        levels.add(new Level(27, 13, 3, 4, 114));
        levels.add(new Level(28, 14, 1, 1, 118));
        levels.add(new Level(29, 14, 1, 0, 122));
        levels.add(new Level(30, 14, 3, 5, 127));
    }



    public static void loadContent(Context context) {
        loadLevels();
        loadGoods(context);
        SoundHelper.init(context);
        AmbiencePlayer.init(context);
        inited=true;

    }
}
