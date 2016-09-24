package gavrysh.oleg.paintaccounting.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import gavrysh.oleg.paintaccounting.R;

public class StringEnterActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String EXTRA_FILENAME="fileName";
    TextView path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_string_enter);

        path = (TextView)findViewById(R.id.textPath);
        findViewById(R.id.buttonOK).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonOK) {
            Intent i = new Intent();
            i.putExtra(EXTRA_FILENAME, path.getText().toString());
            setResult(RESULT_OK, i);
            finish();
        }
    }
}
