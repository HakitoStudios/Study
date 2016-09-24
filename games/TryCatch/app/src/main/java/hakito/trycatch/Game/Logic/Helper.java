package hakito.trycatch.Game.Logic;

import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;

import java.util.Calendar;
import java.util.Random;

/**
 * Created by Oleg on 26-Dec-15.
 */
public class Helper {

    public static Rect getRandomRect(int x, int dx, int y, int dy, int w, int dw, int h, int dh)
    {
        x = getRandomNumber(x, dx);
        y = getRandomNumber(y, dy);
        w = getRandomNumber(w, dw);
        h = getRandomNumber(h, dh);
        return new Rect(x, y, x+w, y+h);
    }

    public static int getRandomNumber(int a, int r)
    {
        Random random = new Random();
        return  a + random.nextInt(r) - r/2;
    }

    public static Point getRandomPointByDistFromAside(int size, int distFromAside)
    {
        Random r = new Random();
        int side = r.nextInt(4);
        switch (side) {
            case 0:
                //top
                return new Point(distFromAside + r.nextInt(size - distFromAside * 2), distFromAside);

            case 1:
                //left
                return new Point(distFromAside, distFromAside + r.nextInt(size - distFromAside * 2));

            case 2:
                //right
                return new Point(size - 1 - distFromAside, distFromAside + r.nextInt(size - distFromAside * 2));

            default:
                //bottom
                return new Point(distFromAside + r.nextInt(size - distFromAside * 2), size - 1 - distFromAside);

        }
    }

    public static Point getRandomPoint(int size)
    {
        Random r = new Random();
        return new Point(r.nextInt(size), r.nextInt(size));
    }

    public static String getTime(long time)
    {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis((long)(time));
        return String.format("%2d:%2d", c.get(Calendar.MINUTE), c.get(Calendar.SECOND)).replace(' ', '0');
    }

    public static boolean isCorrectIndices(int a, int b, int size)
    {
        return isCorrectIndex(a, size) && isCorrectIndex(b, size);
    }

    public static boolean isCorrectIndex(int a, int size)
    {
         return a >= 0 && a < size;
    }

    public static boolean isFinish(int d, int size)
    {
        return d==0 || d==size-1;
    }

    public static boolean isFinish(int x, int y, int size)
    {
        return isFinish(x, size) || isFinish(y, size);
    }

    public static RectF getRectF(Rect rect)
    {
        return new RectF(rect.left, rect.top, rect.right, rect.bottom);
    }
}
