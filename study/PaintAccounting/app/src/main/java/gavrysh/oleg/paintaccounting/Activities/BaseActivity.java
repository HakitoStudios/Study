package gavrysh.oleg.paintaccounting.Activities;

import android.content.DialogInterface;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.sql.SQLDataException;

import gavrysh.oleg.paintaccounting.R;

/**
 * Created by Oleg on 20-Nov-15.
 */
public abstract class BaseActivity extends AppCompatActivity implements TextWatcher {

    protected boolean changed;
    protected int id;

    protected void onCreate(Bundle savedInstanceState, int lId)
    {

        super.onCreate(savedInstanceState);
        setContentView(lId);
        findViewById(R.id.buttonOK).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId())
                {
                    case R.id.buttonOK:
                        try {
                            apply();
                        }
                        catch (SQLException e)
                        {
                            Snackbar.make(v, "Fill all fields!", Snackbar.LENGTH_LONG).show();
                                    return;
                        }

                        finish();
                        break;
                }
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        bindChangeListeners();
    }

    private void bindChangeListeners(View v)
    {
        if(v instanceof TextView)
        {
            ((TextView)v).addTextChangedListener(this);
        }
        else if(v instanceof ViewGroup)
        {
            ViewGroup vg = (ViewGroup)v;
            for (int i=0;i<vg.getChildCount();i++) {
                bindChangeListeners(vg.getChildAt(i));
            }
        }

    }

    public void bindChangeListeners()
    {
        bindChangeListeners(findViewById(R.id.root));
    }

    @Override
    public void onBackPressed() {
        if(changed)
        {
            AlertDialog d = new AlertDialog.Builder(this).create();
            d.setMessage("Exit and cancel changes?");
            d.setButton(DialogInterface.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            d.setButton(DialogInterface.BUTTON_POSITIVE, "Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    BaseActivity.super.onBackPressed();
                }
            });
            d.show();
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        changed=true;
    }

    protected abstract void apply() throws SQLException;

}
