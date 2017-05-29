package hakito.carclient;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import hakito.carclient.api.DataSender;
import hakito.carclient.sensors.SensorProvider;

public class MainActivity extends AppCompatActivity implements SensorProvider, DataSender.SensorsCallback {

    private static final String PREF_INTERVAL = "interval";
    private static final String PREF_ADDRESS = "address";
    private static final String PREF_LEFT = "left";
    private static final String PREF_RIGHT = "right";
    WifiManager wifiManager;
    TextView wifi;
    TextView voltageText;
    DataSender dataSender;
    SensorProvider sensorNormalizer;
    TextView debugText;
    SeekBar ledSeekBar;
    BaseSteeringFragment baseSteeringFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String stterType = preferences.getString("steering_type", "seekbar");
        BaseSteeringFragment fragment;
        if (stterType.equals("seekbar")) {
            fragment = new SeekBarsFragment();
        } else {
            fragment = new AccelerometerFragment();
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
        voltageText = (TextView)findViewById(R.id.voltage_text);
        ledSeekBar = (SeekBar) findViewById(R.id.led_seek_bar);
        debugText = (TextView) findViewById(R.id.tDebug);
        wifi = (TextView) findViewById(R.id.tWifiName);
        wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
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


        sensorNormalizer = new SensorNormalizer(this,
                new Normalizer(Integer.valueOf(preferences.getString(PREF_LEFT, "80")),
                        Integer.valueOf(preferences.getString(PREF_RIGHT, "100"))),
                new Normalizer(0, 255));

        ledSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (dataSender != null) {
                    dataSender.setLed(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
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
                    dataSender = new DataSender(preferences.getString(PREF_ADDRESS, "192.168.4.1:81"), sensorNormalizer, MainActivity.this);
                } catch (final IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
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

    @Override
    public void onVoltageChanged(final int voltage) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                voltageText.setText(String.format("%.2fV", 0.111 * voltage));
            }
        });
    }

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
}
