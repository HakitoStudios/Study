package gavrysh.oleg.paintaccounting.Models;

/**
 * Created by Oleg on 18-Nov-15.
 */
public class Building extends Idable implements Descriptable {
    public String name, adress;

    public Building() {
        super();
    }

    public Building(int id, String name, String adress) {
        super(id);
        this.name = name;
        this.adress = adress;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return adress;
    }
}
