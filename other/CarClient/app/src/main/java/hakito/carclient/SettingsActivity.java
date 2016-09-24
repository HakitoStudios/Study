package hakito.carclient;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.net.Inet4Address;
import java.net.InetAddress;

import hakito.carclient.api.Sender;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    TextView ip, port, interval;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        findViewById(R.id.bOk).setOnClickListener(this);
        findViewById(R.id.bSetInterval).setOnClickListener(this);

        ip = (TextView)findViewById(R.id.tIp);
        port = (TextView)findViewById(R.id.tPort);
        interval = (TextView)findViewById(R.id.tInterval);

        PrefsHelper prefsHelper = new PrefsHelper(this);
        interval.setText("" + prefsHelper.getInterval());
        if(prefsHelper.exists())
        {
            Address address = prefsHelper.getAddress();
            ip.setText(address.getUrl());
            port.setText(""+address.getPort());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bOk:
                String i = ip.getText().toString();
                int p = Integer.parseInt(port.getText().toString());
                Sender.getInstance().setAddress(new Address(i, p));

                new PrefsHelper(this).saveAddress(new Address(i, p));
                Toast.makeText(this, "Address changed", Toast.LENGTH_SHORT).show();
                break;
            case R.id.bSetInterval:
                try {
                    int interv = Integer.parseInt(interval.getText().toString());
                    Sender.getInstance().setInterval(interv);
                    new PrefsHelper(this).saveInterval(interv);
                    Toast.makeText(this, "Interval changed", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e)
                {

                }

                break;
        }
    }
}
