package hakito.autosim.logic;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Oleg on 29-Nov-15.
 */
public class Settings {

    private static boolean autoClutch;
    private static Context context;
    private static final String PREF_NAME="AutosimSettings", AUTO_CLUTCH="autoClutch";

    public static void setAutoClutch(boolean autoClutch) {
        Settings.autoClutch = autoClutch;

        SharedPreferences.Editor  editor = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean(AUTO_CLUTCH, autoClutch);
        editor.apply();
    }

    public static void init(Context context)
    {
        Settings.context = context;
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        if(preferences.contains(AUTO_CLUTCH))
        {
            autoClutch = preferences.getBoolean(AUTO_CLUTCH, false);
        }
    }

    public static boolean getAutoClutch()
    {
        return autoClutch;
    }

}
