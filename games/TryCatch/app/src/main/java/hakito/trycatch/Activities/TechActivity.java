    package hakito.trycatch.Activities;

import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ViewFlipper;

import hakito.trycatch.R;

public class TechActivity extends BaseActivity implements View.OnClickListener {
    ViewFlipper viewFlipper;
    public static final int INTRO=1, TELEPORT=2, ALL=3;
    public static final String EXTRA_TYPE="type";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tech);

        viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);
        int[] layouts = null;
        switch (getIntent().getExtras().getInt(EXTRA_TYPE)) {
            case INTRO:
                layouts = new int[]{R.layout.help_intro, R.layout.help_game, R.layout.help_bonuses};
                break;
            case TELEPORT:
                layouts = new int[]{R.layout.help_teleport};
                break;
            case ALL:
                layouts = new int[]{R.layout.help_intro, R.layout.help_game, R.layout.help_bonuses, R.layout.help_teleport};
                break;
        }


        for (int l : layouts) {
            View.inflate(this, l, viewFlipper);
        }

        if (layouts.length > 1) {
            findViewById(R.id.left).setOnClickListener(this);
            findViewById(R.id.next).setOnClickListener(this);
        } else {
            findViewById(R.id.left).setVisibility(View.GONE);
            findViewById(R.id.next).setVisibility(View.GONE);
        }
        findViewById(R.id.button_ok).setOnClickListener(this);
        setVolume(0.2f);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.left:
                viewFlipper.showPrevious();
                break;
            case R.id.next:
                viewFlipper.showNext();
                break;
            case R.id.button_ok:finish();break;
        }
    }
}
