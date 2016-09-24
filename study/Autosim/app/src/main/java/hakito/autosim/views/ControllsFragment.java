package hakito.autosim.views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import hakito.autosim.R;
import hakito.autosim.activities.GameMenuActivity;
import hakito.autosim.activities.SettingsActivity;
import hakito.autosim.logic.Car;
import hakito.autosim.logic.Game;
import hakito.autosim.logic.MessageListener;
import hakito.autosim.logic.World;

/**
 * Created by Oleg on 31-Oct-15.
 */
public class ControllsFragment extends Fragment implements Pedal.OnPedalChangedListener, View.OnClickListener, MessageListener {

    private static Pedal clutchP;

public static void addClutch(float c)
{
    if(clutchP!=null)
    {
        clutchP.setProgress(clutchP.getProgress() + c);
    }
}

    @Override
    public void show(String text, MessageType type) {
        int color=0;
        switch (type) {
            case BAD:color = Color.RED;
                break;
            case GOOD:color = Color.GREEN;
                break;
            case NEUTRAL:color = Color.YELLOW;
                break;
        }
        message.setTextColor(color);
        message.setText(text);
        message.startAnimation(messageAnimation);

    }

    public enum MessageType
    {
        BAD, GOOD, NEUTRAL
    }



    public static MessageListener messageListener;

    public static void setMessageListener(MessageListener listener) {
        messageListener = listener;
    }

    public ControllsFragment()
    {

    }

    VerticalprogressBar fuel;
    Pedal clutch, gas, brake;
    Indicator speed,tach;
    TextView gear, message, time, light_dist, dist_to_finish;
    ImageView light_color;
    View light_container;
    Button starter;
    ImageButton  up, down;
    Animation messageAnimation, warnAnim;


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        starter.startAnimation(warnAnim);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setMessageListener(this);
        messageAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.message_anim);
        warnAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.warn_anim);

        messageAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
            message.setText("");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        View v = inflater.inflate(R.layout.controls_layout, container, false);
        dist_to_finish = (TextView)v.findViewById(R.id.text_to_finish);
        light_container = v.findViewById(R.id.light_notifier);
        light_color = (ImageView)v.findViewById(R.id.light_color);
        light_dist= (TextView) v.findViewById(R.id.light_dist);
        time = (TextView) v.findViewById(R.id.textTime);
        gas = (Pedal) v.findViewById(R.id.gas);
        message = (TextView)v.findViewById(R.id.mesage);
        brake = (Pedal) v.findViewById(R.id.brake);
        clutch = (Pedal) v.findViewById(R.id.clutch);
        speed = (Indicator)v.findViewById(R.id.speedometer);
        tach = (Indicator)v.findViewById(R.id.tachometer);
        gear = (TextView)v.findViewById(R.id.gear);
        up = (ImageButton)v.findViewById(R.id.shiftUp);
        down = (ImageButton)v.findViewById(R.id.shiftDown);
        starter = (Button)v.findViewById(R.id.starter);
        fuel = (VerticalprogressBar)v.findViewById(R.id.fuelLevel);

        clutchP = clutch;


        v.findViewById(R.id.buttonPause).setOnClickListener(this);
        starter.setOnClickListener(this);
        down.setOnClickListener(this);
        up.setOnClickListener(this);
        gas.setOnChangedListener(this);
        clutch.setOnChangedListener(this);
        brake.setOnChangedListener(this);

        Game.getGame().setOnChangedListener(new Game.OnChangedListener() {
            @Override
            public void changed() {
                if(Game.getGame().world.getLightInfo()==null)
                {
                    light_container.setVisibility(View.INVISIBLE);
                }
                else
                {
                    light_container.setVisibility(View.VISIBLE);
                    World.LightInfo info = Game.getGame().world.getLightInfo();
                    light_dist.setText(String.format("%dm", info.dist));
                    light_dist.setTextColor(info.color);
                }
                time.post(new Runnable() {
                    @Override
                    public void run() {
                        time.setText(Game.getGame().world.getTime());
                    }
                });

                speed.post(new Runnable() {
                    @Override
                    public void run() {
                        speed.setValue(Game.getGame().world.car.getSpeed() / 200);
                    }
                });
                tach.post(new Runnable() {
                    @Override
                    public void run() {
                        tach.setValue(Game.getGame().world.car.getRPM() / 6000);
                    }
                });
                gear.post(new Runnable() {
                    @Override
                    public void run() {
                        gear.setText(Game.getGame().world.car.getStringGear());
                    }
                });

                fuel.post(new Runnable() {
                    @Override
                    public void run() {
                        fuel.setProgress((int) (Game.getGame().world.car.getFuelLevel() * fuel.getMax()));
                    }
                });
dist_to_finish.post(new Runnable() {
    @Override
    public void run() {
   dist_to_finish.setText(String.format("%dm to finish", Game.getGame().world.getDistToFinish()));
    }
});
            }
        });

        return v;
    }

    @Override
    public void onChanged(Pedal p) {
        switch (p.getId())
        {
            case R.id.gas:
                Game.getGame().gas = p.getProgress();
                break;
            case R.id.brake:
                Game.getGame().brake = p.getProgress();
                break;
            case R.id.clutch:
                Game.getGame().clutch = p.getProgress();
                break;
        }
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonPause:
                Intent intent =new Intent(getActivity(), GameMenuActivity.class);
                startActivity(intent);
                break;
            case R.id.shiftUp:
                Game.getGame().world.car.shift(true);
                break;
            case R.id.shiftDown:
                Game.getGame().world.car.shift(false);
                break;
            case R.id.starter:
                starter.setTextColor(Color.WHITE);
                Game.getGame().world.car.starter();
                break;
        }

    }
}
