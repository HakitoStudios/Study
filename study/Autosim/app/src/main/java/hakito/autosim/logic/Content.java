package hakito.autosim.logic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Interpolator;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import hakito.autosim.R;

/**
 * Created by Oleg on 31-Oct-15.
 */
public class Content {
    public static Content content = new Content();

    public Bitmap column, sky, objects, ground, myc, particle;
    public List<Car.CarParams> cars;
    public List<Level> levels;


    public Content() {

    }

    private Bitmap loadBitmap(Context c, String path) throws IOException {
        return BitmapFactory.decodeStream(c.getAssets().open(path));
    }

    private static final String LEVEL="level", OBJECT="object", TYPE="type", PERIOD="period", N="n", D= "d";

    private Level.ObjectType getType(String s)
    {
        switch(s.toLowerCase())
        {
            case "trafficlight":return Level.ObjectType.TRAFFIC_LIGHT;
            case "row":return Level.ObjectType.ROW;
            case "finish":return Level.ObjectType.FINISH;
            default: return null;
        }
    }

    private void loadLevels(Context c)
    {
        levels = new ArrayList<>();
        XmlPullParser p = c.getResources().getXml(R.xml.levels);
        Level current = null;
        try
        {
        while(p.next()!= XmlPullParser.END_DOCUMENT)
        {
            if(p.getEventType()==XmlPullParser.START_TAG)
            {
                if(p.getName().equals(LEVEL))
                {
                    current = new Level(p.getAttributeValue(null, NAME));
                    levels.add(current);
                }
                else if(p.getName().equals(OBJECT))
                {
                    Level.LevelObject o= new Level.LevelObject();
                    o.x = getFloat(p, X);
                    o.type=getType(p.getAttributeValue(null, TYPE));
                    current.objects.add(o);
                }
                else if(p.getName().equals(PERIOD))
                {
                    Level.Periodic periodic = new Level.Periodic();
                    periodic.d = getFloat(p, D);
                    periodic.x = getFloat(p, X);
                    periodic.n = (int)getFloat(p, N);
                    periodic.type = getType(p.getAttributeValue(null, TYPE));
                    current.periodics.add(periodic);
                }
            }
        }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void loadTextures(Context c) {
        try {

            column = loadBitmap(c, "column.png");
            sky = loadBitmap(c, "sky.png");
            ground = loadBitmap(c, "ground.png");
            objects = loadBitmap(c, "objects.png");
            myc = loadBitmap(c, "myc.png");
            particle = loadBitmap(c, "particle.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static final String ROOT = "cars", ITEM = "car", NAME = "name", PATH = "path", MASS = "mass", WK = "wk",
            BK = "bk", WIDTH = "width", WHEEL = "wheel", X = "x", RADIUS = "r", TRANSMISSION = "transmission", POINT = "p", Y = "y",
            VALUE = "value", GEAR = "gear", DIFF = "diff", IDLE = "idle", INERTIA = "inertia", BRK = "brk", ENGINE="engine",
    MAX_RPM="maxRPM", TORQUE="torque", ANGLE="angle", EXHAUST="exhaust";

    private static float getFloat(XmlPullParser p, String attrName) {
        return (float) Double.parseDouble(p.getAttributeValue(null, attrName));
    }

    private void loadCars(Context c) {


        XmlPullParser p = c.getResources().getXml(R.xml.cars);


        cars = new ArrayList<>();
        Car.CarParams car = null;
        Car.CarParams.Wheelparams wheel = null;

        try {


            while (p.next() != XmlPullParser.END_DOCUMENT) {
                if (p.getEventType() == XmlPullParser.START_TAG) {
                    if (p.getName().equals(ITEM)) {
                        car = new Car.CarParams();
                        car.bitmap = loadBitmap(c, p.getAttributeValue(null, PATH));
                        car.bk = getFloat(p, BK);
                        car.wk = getFloat(p, WK);
                        car.width = getFloat(p, WIDTH);
                        car.mass = (int) getFloat(p, MASS);
                        car.name = p.getAttributeValue(null, NAME);
                    } else if (p.getName().equals(WHEEL)) {
                        wheel = new Car.CarParams.Wheelparams();
                        wheel.r = getFloat(p, RADIUS);
                        wheel.x = getFloat(p, X);
                    } else if (p.getName().equals(DIFF)) {
                        car.engineParams.diff = getFloat(p, VALUE);
                    } else if (p.getName().equals(GEAR)) {
                        car.engineParams.transmission.add(getFloat(p, VALUE));
                    }else if (p.getName().equals(ENGINE)) {
                        car.engineParams.idle = getFloat(p, IDLE);
                        car.engineParams.inertia = getFloat(p, INERTIA);
                        car.engineParams.brk = getFloat(p, BRK);
                        car.engineParams.maxRPM = getFloat(p, MAX_RPM);
                    }else if(p.getName().equals(POINT))
                    {
                        car.engineParams.torque.add(new Point((int)getFloat(p, X), (int)getFloat(p, Y)));
                    }
                    else if(p.getName().equals(EXHAUST))
                    {
                        car.exhaust = new Car.CarParams.Exhaust(getFloat(p, X), getFloat(p, Y), getFloat(p, ANGLE));
                    }
                } else if (p.getEventType() == XmlPullParser.END_TAG) {
                    if (p.getName().equals(ITEM)) {
                        cars.add(car);
                    }
                    if (p.getName().equals(WHEEL)) {
                        car.wheels.add(wheel);
                    }
                }
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    public void load(Context c) {
        loadCars(c);
        loadTextures(c);
        loadLevels(c);
    }


    public static Rect getRect(Level.ObjectType type)
    {
        switch (type) {
            case ROW:return getRect(512, 512, 0, 0);

            case FINISH:return getRect(512, 512, 3, 0);

        }
        return null;

    }

    public static Rect getRect(int w, int h, int x, int y) {
        x *= w;
        y *= h;
        return new Rect(x, y, x + w, y + h);
    }

}
