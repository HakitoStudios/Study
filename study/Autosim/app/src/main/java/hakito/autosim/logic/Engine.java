package hakito.autosim.logic;

import android.graphics.*;
import android.graphics.Interpolator;

import java.util.List;

import hakito.autosim.activities.FinishActivity;
import hakito.autosim.views.ControllsFragment;

/**
 * Created by Oleg on 31-Oct-15.
 */
public class Engine {

    String debug = "";

    private float RPM, maxRPM;
    private float inertia, brk;
    private float idle;
    private float gas;
    float fuelCapacity, fuel, fuelConsumption;

    private boolean working;
    private int gear;
    private float diff;
    float additiveGas;
    private float[] gears;
    private Interpolator interpolator;

    public Engine(Car.CarParams.EngineParams engineParams) {

        fuelCapacity = 50;
        fuel=fuelCapacity;
        fuelConsumption = 1;

        RPM = 0;
        inertia=engineParams.inertia;
        brk = engineParams.brk;
        idle = engineParams.idle;
        diff = engineParams.diff;
        gears = new float[engineParams.transmission.size()];
maxRPM = engineParams.maxRPM;
        int i=0;
        for (Float f:engineParams.transmission             ) {
            gears[i]=f;
            i++;
        }


        interpolator = new android.graphics.Interpolator(1, engineParams.torque.size());
        i=0;
        for (Point p:engineParams.torque) {
            interpolator.setKeyFrame(i, p.x, new float[]{p.y});
            i++;
        }
    }

    public float getFullGas()
    {
        return gas;
    }

    public int getGear() {
        return gear - 1;
    }

    public float getRPM() {
        return RPM;
    }

    public boolean shift(boolean up) {
        if (up) {
            if (gear + 1 < gears.length) {
                gear++;
            } else return false;
        } else {
            if (gear >= 1) {
                gear--;
            } else return false;
        }
        return true;
    }

    private float getTorque(float r) {
        float[] res = new float[1];
        interpolator.timeToValues((int)r, res);
        return res[0];
    }


    public void starter()
    {
        if(!working && RPM < idle/2) {
            RPM += idle / 2 + 100;
        }
        else if(working)
        {
            working = false;
        }
    }

    private float getK() {
        return (gears[gear] * diff);
    }

    private float speedToRPM(float speed, float wheelRad)
    {
        return (float) (speed / (2 * Math.PI * wheelRad) * getK() * 60);
    }

    public float getFuelLevel()
    {
        return fuel/fuelCapacity;
    }

    public float update(float dt, float speed, float wheelRad) {



        boolean newW =RPM >= idle/2 && fuel>0;
        if(working == true && newW == false)
        {
            ControllsFragment.messageListener.show("Engine stalled", ControllsFragment.MessageType.NEUTRAL);
        }
        working = newW;
        if(working) {
            fuel -= (gas + additiveGas) * fuelConsumption * dt;
        }

        float clutch = (float) Game.getGame().clutch;
        if(getGear()==0)
            clutch=1;


        autoClutch(dt);

        float torq = getTorque(RPM) * (gas+additiveGas);
        float RPMfromWheels = speedToRPM(speed, wheelRad);
        if(RPM>maxRPM)
        {
            torq=0;
            RPM= maxRPM;
        }
        if(!working)
        {
            torq=0;
        }

        RPM += (float) (torq*clutch / inertia  - Math.pow(RPM, 2)*brk)* dt;
        float dr = RPM-RPMfromWheels;
        float inF =(float)(Math.signum(dr)*Math.pow(dr * Math.PI * 2 / 60,1) * inertia);

        RPM = RPMfromWheels + (RPM - RPMfromWheels) * clutch;



        debug = String.format("d:%d T:%d", (int)(1/dt), (int)getTorque(RPM));
        if(!working)return 0;

        if (RPM < 0) {
            RPM = 0;
        }

        gas = (float) Game.getGame().gas;

        if (RPM < idle && additiveGas<0.3f) {
            additiveGas += 1f*dt;
        }
        else
        {
            additiveGas-=0.3f*dt;
            if(additiveGas<0)additiveGas=0;
        }
        float ef = (float)(((torq + inF)*(1-clutch) - Math.pow(RPM/1000, 2)*brk));
        if(ef<0)ef=0;
        float f = (float) (ef * getK() / wheelRad);

        return f;
    }

    private void autoClutch(float dt) {
        if(!Settings.getAutoClutch())return;
        float optimal = idle*1.01f;
        float di = optimal - RPM;
        if(getGear()==0)
        {
            di = 5000;
        }
        int p;
        if(di>0)
        {
            di*=4;
            p=4;
        }
        else
        {
            p=2;
        }
        float dc = (float)(Math.signum(di)*Math.pow(di / optimal, p)*dt);
        ControllsFragment.addClutch(dc);
    }


}
