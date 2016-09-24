package hakito.quadratic;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {

    TextView A, B, C, D, X1, X2;
    TextInputLayout lA, lB, lC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.bCalc).setOnClickListener(this);
        A = (TextView) findViewById(R.id.tA);
        B = (TextView) findViewById(R.id.tB);
        C = (TextView) findViewById(R.id.tC);
        D = (TextView) findViewById(R.id.tD);
        X1 = (TextView) findViewById(R.id.tX1);
        X2 = (TextView) findViewById(R.id.tX2);
        lA = (TextInputLayout) findViewById(R.id.lA);
        lB = (TextInputLayout) findViewById(R.id.lB);
        lC = (TextInputLayout) findViewById(R.id.lC);

        A.setOnKeyListener(this);
        B.setOnKeyListener(this);
        C.setOnKeyListener(this);


        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }

    void Validate(TextInputLayout l) {
        if (l.getEditText().getText().length() == 0) {
            // l.setErrorEnabled(true);
            l.setError("Incorrect");
        } else {

            l.setErrorEnabled(false);
        }
    }

    String getString(double v)
    {
        return String.format("%.2f", v);
    }

    void Calc() {
        double a = Double.parseDouble(A.getText().toString()),
                b = Double.parseDouble(B.getText().toString()),
                c = Double.parseDouble(C.getText().toString());
        double d = b * b - 4 * a * c;
        double dSqrt = Math.sqrt(d);
        double x1 = (-b - dSqrt) / 2 / a, x2 = (-b + dSqrt) / 2 / a;

        if(!Double.isNaN(x1) && !Double.isNaN(x2) && !Double.isNaN(d)) {
            findViewById(R.id.resultLayout).setVisibility(View.VISIBLE);
            X1.setText("X₁=" + getString(x1));
            X2.setText("X₂=" + getString(x2));
            D.setText("D=" + getString(d));
        }
        else
        {
            lA.setError("Incorrect");
            lB.setError("Incorrect");
            lC.setError("Incorrect");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bCalc:
                findViewById(R.id.resultLayout).setVisibility(View.GONE);
                Validate(lA);
                Validate(lB);
                Validate(lC);
                try {
                    Calc();
                } catch (Exception e) {

                }
                break;
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        return false;
    }
}
