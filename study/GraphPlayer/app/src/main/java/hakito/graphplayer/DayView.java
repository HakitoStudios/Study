package hakito.graphplayer;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Oleg on 24-Dec-15.
 */
public class DayView extends TextView implements View.OnClickListener{

    private boolean checked;

    public DayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnClickListener(this);
        updateCheckness();
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
        updateCheckness();
    }

    void updateCheckness()
    {
        setTextColor(checked ? getResources().getColor(R.color.colorAccent) : getResources().getColor(R.color.text_inactive));
        setTypeface(checked?Typeface.DEFAULT_BOLD:Typeface.DEFAULT);
    }

    @Override
    public void onClick(View v) {
        checked=!checked;
        updateCheckness();
    }
}
