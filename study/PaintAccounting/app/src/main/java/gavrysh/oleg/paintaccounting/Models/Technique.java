package gavrysh.oleg.paintaccounting.Models;

/**
 * Created by Oleg on 18-Nov-15.
 */
public class Technique extends Idable implements Descriptable {

    public String name, description;


    public Technique() {
        super();
    }

    public Technique(int id, String name, String description) {
        super(id);
        this.name = name;
        this.description = description;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
