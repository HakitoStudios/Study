package hakito.graphplayer;

/**
 * Created by Oleg on 25-Dec-15.
 */
public class TextConverter {

    static final String pattern = "\\|", dividerWrite="|", TRUE = "1", FALSE = "0";

    public static boolean[] getDays(String days) {
        String[] ss = days.split(pattern);
        boolean[] res = new boolean[7];
        for (int i = 0; i < 7; i++) {
            res[i] = ss[i].toString().equals(TRUE);
        }
        return res;
    }

    public static String getDays(boolean[] days) {
        String res = "";
        for (int i = 0; i < 7; i++) {
            res += days[i] ? TRUE : FALSE;
            if (i != 6) res += dividerWrite;
        }
        return res;
    }

    public static float[] getGraph(String graph)
    {
        String[] ss = graph.split(pattern);
        float[] res = new float[ss.length];
        for (int i = 0; i < ss.length; i++) {
            res[i] = Float.parseFloat(ss[i]);
        }
        return res;
    }


    public static String getGraph(float[] graph)
    {
        String res="";
        for (int i=0; i < graph.length; i++)
        {
            res+=graph[i];
            if(i!=graph.length - 1)res+=dividerWrite;
        }
        return res;
    }

    static final long msInH = 3600000, msInM = 60000;

    public static long getTime(int h, int m)
    {
        return msInH * h + m * msInM;
    }

    public static MTime getTimeInt(long time)
    {
        long hh = time / msInH, mm = (time - hh * msInH) / msInM;
        return new MTime(hh, mm);
    }

    public static String getTime(long time)
    {

        MTime t = getTimeInt(time);
        return String.format("%2d:%2d", t.h, t.m).replace(' ', '0');
    }

public static class MTime
{
    public long h, m;

    public MTime(long h, long m) {
        this.h = h;
        this.m = m;
    }
}
}
