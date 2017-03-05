package hakito.carclient;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import hakito.carclient.api.DataSender;
import hakito.carclient.sensors.SensorProvider;

public class MainActivity extends AppCompatActivity implements SensorProvider {

    private static final String PREF_INTERVAL = "interval";
    private static final String PREF_ADDRESS = "address";
    private static final String PREF_LEFT = "left";
    private static final String PREF_RIGHT = "right";


    static class SensorNormalizer implements SensorProvider {

        private SensorProvider provider;
        private Normalizer steeringNormalizer;
        private Normalizer throttleNormalizer;

        public SensorNormalizer(SensorProvider provider, Normalizer steeringNormalizer, Normalizer throttleNormalizer) {
            this.provider = provider;
            this.steeringNormalizer = steeringNormalizer;
            this.throttleNormalizer = throttleNormalizer;
        }

        @Override
        public double getThrottle() {
            return throttleNormalizer.normalize(provider.getThrottle());
        }

        @Override
        public double getSteering() {
            return steeringNormalizer.normalize(provider.getSteering());
        }
    }

    WifiManager wifiManager;
    TextView wifi;

    DataSender dataSender;
    SensorProvider sensorNormalizer;

    TextView debugText;

    BaseSteeringFragment baseSteeringFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new SeekBarsFragment())
                .commit();
        debugText = (TextView) findViewById(R.id.tDebug);
        wifi = (TextView) findViewById(R.id.tWifiName);
        wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                wifi.post(new Runnable() {
                    @Override
                    public void run() {
                        wifi.setText(wifiManager.getConnectionInfo().getSSID());
                    }
                });
            }
        }, 300, 5000);

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        sensorNormalizer = new SensorNormalizer(this,
                new Normalizer(Integer.valueOf(preferences.getString(PREF_LEFT, "80")),
                        Integer.valueOf(preferences.getString(PREF_RIGHT, "100"))),
                new Normalizer(0, 255));
    }

    @Override
    protected void onResume() {
        super.onResume();
        baseSteeringFragment = (BaseSteeringFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        final int interval = Integer.valueOf(preferences.getString(PREF_INTERVAL, "100"));

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    dataSender = new DataSender(preferences.getString(PREF_ADDRESS, "192.168.4.1:81"), sensorNormalizer);
                } catch (final IOException e) {
                    Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                    //throw new RuntimeException(e);
                }
                dataSender.setDebugView(debugText);
                dataSender.setInterval(interval);
                dataSender.execute();
            }
        }).start();
    }

    @Override
    protected void onPause() {
        if (dataSender != null) {
            try {
                dataSender.stop();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mSettings:
                Intent intent = new Intent(this, PrefsActivity.class);
                startActivity(intent);
                break;
            case R.id.mReconnect:

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public double getThrottle() {
        return baseSteeringFragment.getThrottle();
    }

    @Override
    public double getSteering() {
        return baseSteeringFragment.getSteer();
    }
}
