package hakito.carclient.api;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StreamCorruptedException;
import java.net.Socket;

import hakito.carclient.Address;
import hakito.carclient.sensors.SensorProvider;

/**
 * Created by Oleg on 06.06.2016.
 */
public class DataSender extends AsyncTask<Void, Void, Void> {

    SensorProvider sensorProvider;

    TextView debugView;

    int interval=100;

     Address address;

    Socket socket;
    OutputStreamWriter stream;


    public DataSender() {


    }

    public void setSensorProvider(SensorProvider sensorProvider)
    {
        this.sensorProvider = sensorProvider;
    }

    private void debug(final String s)
    {
        if(debugView!=null)
        {
            debugView.post(new Runnable() {
                @Override
                public void run() {
                    debugView.setText(s);
                }
            });
        }
    }

    public synchronized void setAddress(Address address)
    {


        this.address = address;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public void setDebugView(TextView debugView) {
        this.debugView = debugView;

    }

    private String getCommandString() {
        if(sensorProvider==null)
        {
            return "No sensor selected";
        }
        String res =String.format("m=%3ds=%3d", sensorProvider.getThrottle(), sensorProvider.getSteering()).replace(' ', '0');
        Log.d("qaz", res);
        return res;
    }

    @Override
    protected Void doInBackground(Void... params) {

        try {

            while (true) {
                synchronized (this) {
                    Log.d("qaz", getCommandString());
                    if (address != null) {
                        try {
                            if(stream!=null)
                            {
                                stream.close();
                            }
                            socket = new Socket(address.getUrl(), address.getPort());
                            stream = new OutputStreamWriter(socket.getOutputStream());
                            address=null;
                        } catch (IOException e) {
                            e.printStackTrace();
                            debug(e.getMessage());
                        }
                    }
                    if (stream == null)
                        continue;
                    String command = getCommandString();
                    stream.write(command);
                    stream.flush();

                    debug(command);

                }
                try {
                    Thread.sleep(interval);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            debug(e.getMessage());
        }
        return null;
    }
}
