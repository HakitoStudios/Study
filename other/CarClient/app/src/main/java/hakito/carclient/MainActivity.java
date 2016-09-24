package hakito.carclient;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import hakito.carclient.api.DataSender;
import hakito.carclient.api.Sender;
import hakito.carclient.sensors.SensorProvider;
import hakito.carclient.views.BigSeekBar;

public class MainActivity extends AppCompatActivity  {

    class AccelerometerProvider implements SensorProvider, SensorEventListener
    {
        int steer=90, throttle=128;

        public AccelerometerProvider() {
            SensorManager sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
            Sensor s = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            if(s != null)
            {
                sensorManager.registerListener(this, s, SensorManager.SENSOR_DELAY_GAME);
            }
        }

        @Override
        public int getThrottle() {
            return throttle;
        }

        @Override
        public int getSteering() {
            return steer;
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            Log.d("qaz", Arrays.toString(event.values));
            float v = event.values[1];
            v/=9.81;
            v*=90;
            v+=90;
            if(v<0)v=0;
            else if(v>180)v=180;
            steer=(int)v;
            throttle = throt.getProgress();

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }

    class SeekProvider implements SensorProvider
    {

        @Override
        public int getThrottle() {
            return throt.getProgress();
        }

        @Override
        public int getSteering() {
            return steer.getProgress();
        }
    }

    WifiManager wifiManager;
    TextView wifi;
    ProgressBar throt, steer;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wifi = (TextView)findViewById(R.id.tWifiName);
        wifiManager = (WifiManager)getSystemService(WIFI_SERVICE);
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

        throt = (ProgressBar)findViewById(R.id.seekThrottle);
        steer =  (ProgressBar)findViewById(R.id.seekSteer);
        ((BigSeekBar)steer).setHorizontal(true);
        throt.setOnTouchListener(new SeekResetter(128));
        steer.setOnTouchListener(new SeekResetter(90));
        DataSender s = Sender.getInstance();

        s.setSensorProvider(new SeekProvider());

        s.setDebugView((TextView)findViewById(R.id.tDebug));

        PrefsHelper prefsHelper = new PrefsHelper(this);

        s.setInterval(prefsHelper.getInterval());

        if(prefsHelper.exists())
        {
         s.setAddress(prefsHelper.getAddress());
        }
        s.execute();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.mSettings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.mReconnect:
                Sender.getInstance().setAddress(new PrefsHelper(this).getAddress());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.mReconnect).setEnabled(new PrefsHelper(this).exists());
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


}
