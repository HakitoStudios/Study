package gavrysh.oleg.paintaccounting.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import gavrysh.oleg.paintaccounting.R;

public class SearchActivity extends AppCompatActivity {

    public final static String EXTRA_SEARCH="search";
    private static String search="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ((EditText)findViewById(R.id.textSearch)).setText(search);
        findViewById(R.id.buttonSearch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                search = ((EditText)findViewById(R.id.textSearch)).getText().toString();
                intent.putExtra(EXTRA_SEARCH, search);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
