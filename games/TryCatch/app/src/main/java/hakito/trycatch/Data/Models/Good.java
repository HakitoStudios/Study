package hakito.trycatch.Data.Models;

/**
 * Created by Oleg on 29-Dec-15.
 */
public class Good {

    public String getDescription() {
        return description;
    }

    public enum GoodType {
        FROZE, EXTRASENSE, TELEPORT
    }

    private GoodType type;
    private int cost;
    private String name, description;

    public Good(GoodType type, int cost, String name, String description) {
        this.type = type;
        this.cost = cost;
        this.name = name;
        this.description = description;
    }



    public GoodType getType() {
        return type;
    }

    public int getCost() {
        return cost;
    }

    public String getName() {
        return name;
    }
}
