package hakito.wifimap;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AddCommentActivity extends AppCompatActivity {
   public static String res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comment);
        findViewById(R.id.bAcceptComment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               res =  ((TextView)findViewById(R.id.tComment)).getText().toString();
                finish();
            }
        });
    }
}
