package hakito.autosim.logic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oleg on 31-Oct-15.
 */
public class Level {

    public static enum ObjectType
    {
        TRAFFIC_LIGHT, ROW, FINISH
    }

    public static class Periodic extends LevelObject
    {
        public float d;
        public int n;
    }

    public static class LevelObject
    {
       public float x;
        public ObjectType type;
    }

    public String name;
    public List<LevelObject> objects;
    public List<Periodic> periodics;

    public Level(String name) {
        this.name = name;
        objects = new ArrayList<>();
        periodics = new ArrayList<>();
    }
}
