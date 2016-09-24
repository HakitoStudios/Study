package hakito.autosim.logic;

import java.io.Serializable;

/**
 * Created by Oleg on 29-Nov-15.
 */
public class GameResult implements Serializable {
    public float time, fuelLevel;
    public int redLights;
    public boolean finished;
    public String level, car;

    public GameResult(String level, String car) {
        this.level=  level;
        this.car = car;
        redLights = 0;
        time=0;
    }
}
