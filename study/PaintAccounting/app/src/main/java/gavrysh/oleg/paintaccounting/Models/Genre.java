package gavrysh.oleg.paintaccounting.Models;

/**
 * Created by Oleg on 18-Nov-15.
 */
public class Genre extends Idable implements Descriptable{

    public String name, description;

    public Genre() {
    }

    public Genre(int id, String name, String description) {
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
