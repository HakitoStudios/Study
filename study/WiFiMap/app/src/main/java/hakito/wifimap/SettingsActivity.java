package hakito.wifimap;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import hakito.wifimap.REST.RestClient;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        findViewById(R.id.bAcceptSettings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RestClient.reinit(((TextView)findViewById(R.id.tServerIP)).getText().toString());
                finish();
            }
        });
    }
}
