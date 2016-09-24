package hakito.autosim.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ActionMenuView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import hakito.autosim.R;
import hakito.autosim.logic.Car;
import hakito.autosim.logic.Content;
import hakito.autosim.logic.Game;
import hakito.autosim.logic.Wheel;

public class CarSelectActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout lay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_car_select);
        lay=(LinearLayout)findViewById(R.id.hor_layout);

        LayoutInflater inflater = getLayoutInflater();
        Paint d=  new Paint();
        int i=0;
        for (Car.CarParams p: Content.content.cars)
        {
            View v = inflater.inflate(R.layout.car_item, lay, false);

            Bitmap b = Bitmap.createBitmap(512, 512, Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(b);
            Game.setK(b.getWidth() / p.width);
            Car car = new Car(null, new PointF(0, Game.PXtoM(400)), p);
            car.draw(c);
            for (Wheel w:car.wheels)
            {
                w.draw(c);
            }
            Game.restoreK();
            ImageView iv = (ImageView)v.findViewById(R.id.image);
            iv.setImageBitmap(b);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(v.getLayoutParams());
            lp.setMargins(30, 30, 30, 30);
            v.setLayoutParams(lp);
            ((TextView)v.findViewById(R.id.name)).setText(p.name);
            v.setOnClickListener(this);
            lay.addView(v);
            v.setTag(i);
            i++;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {

        }
        if(v instanceof RelativeLayout) {
            Game.carParams = Content.content.cars.get((int) v.getTag());
            Game.getGame().reset();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
    }
}
