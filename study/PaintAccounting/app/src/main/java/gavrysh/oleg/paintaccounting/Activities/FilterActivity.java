package gavrysh.oleg.paintaccounting.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.Serializable;
import java.util.logging.Filter;

import gavrysh.oleg.paintaccounting.R;

public class FilterActivity extends AppCompatActivity {

    private static FilterInfo filterInfo = FilterInfo.NONE;

    public static class FilterInfo implements Serializable
    {
        public static final FilterInfo NONE=new FilterInfo(-1,-1,-1,-1,0);
        public int minPrice, maxPrice, minYear, maxYear, photo;

        public FilterInfo(int minPrice, int maxPrice, int minYear, int maxYear, int photo) {
            this.minPrice = minPrice;
            this.maxPrice = maxPrice;
            this.minYear = minYear;
            this.maxYear = maxYear;
            this.photo = photo;
        }

        public FilterInfo()
        {}
    }

    public static final String EXTRA_FILTER="filter";

    TextView minPrice, maxPrice, minYear, maxYear;
    Spinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        minPrice = (TextView)findViewById(R.id.priceMin);
        maxPrice = (TextView)findViewById(R.id.priceMax);
        maxYear = (TextView)findViewById(R.id.maxYear);
        minYear = (TextView)findViewById(R.id.minYear);
        spinner = (Spinner)findViewById(R.id.spinner);

        spinner.setSelection(filterInfo.photo);
        if(filterInfo.minPrice!=-1)minPrice.setText(""+filterInfo.minPrice);
        if(filterInfo.maxPrice!=-1)maxPrice.setText(""+filterInfo.maxPrice);
        if(filterInfo.minYear!=-1)minYear.setText(""+filterInfo.minYear);
        if(filterInfo.maxYear!=-1)maxYear.setText(""+filterInfo.maxYear);

        findViewById(R.id.buttonFilter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilterInfo res = new FilterInfo();
                res.photo = spinner.getSelectedItemPosition();
                if(minYear.getText().toString().isEmpty())
                {
                    res.minYear=-1;
                }
                else {
                    res.minYear = Integer.parseInt(minYear.getText().toString());
                }

                if(maxYear.getText().toString().isEmpty())
                {
                    res.maxYear=-1;
                }
                else {
                    res.maxYear = Integer.parseInt(maxYear.getText().toString());
                }

                if(minPrice.getText().toString().isEmpty())
                {
                    res.minPrice=-1;
                }
                else {
                    res.minPrice = Integer.parseInt(minPrice.getText().toString());
                }

                if(maxPrice.getText().toString().isEmpty())
                {
                    res.maxPrice=-1;
                }
                else {
                    res.maxPrice = Integer.parseInt(maxPrice.getText().toString());
                }
                filterInfo = res;
                Intent intent = new Intent();
                intent.putExtra(EXTRA_FILTER, res);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
