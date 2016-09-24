package hakito.carclient;

import android.content.Context;
import android.content.SharedPreferences;

import java.net.InetAddress;

/**
 * Created by Oleg on 07.06.2016.
 */
public class PrefsHelper {
    static final int SEEK_BARS=0, ACCELEROMETER=1;

    static final String STEER_TYPE="steer_tyoe", IP="ip", PORT="port", INTERVAL  = "interval", ADDRESS_PREFS="address";

    Context context;
    SharedPreferences preferences;

    public PrefsHelper(Context context) {
        this.context = context;

        preferences = context.getSharedPreferences(ADDRESS_PREFS, Context.MODE_PRIVATE);
    }

    public boolean exists()
    {
        return preferences.contains(IP) && preferences.contains(PORT);
    }

    public int getInterval()
    {
        return preferences.getInt(INTERVAL, 100);

    }

    public void saveInterval(int interval)
    {
        SharedPreferences.Editor editor  = preferences.edit();
        editor.putInt(INTERVAL, interval);
        editor.apply();
        editor.commit();
    }

    public Address getAddress()
    {
        return new Address(preferences.getString(IP, null), preferences.getInt(PORT, 0));
    }

    public void saveAddress(Address address)
    {
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(IP, address.getUrl());
        editor.putInt(PORT, address.getPort());

        editor.apply();
        editor.commit();
    }
}
