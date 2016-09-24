package hakito.wifimap;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import hakito.wifimap.POJO.Point;
import hakito.wifimap.REST.RestClient;

public class AddAPActivity extends AppCompatActivity {

    public static Point p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ap);
        findViewById(R.id.bAddAP).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p.setName(((TextView)findViewById(R.id.tSSID)).getText().toString());
                finish();
            }
        });
    }
}
