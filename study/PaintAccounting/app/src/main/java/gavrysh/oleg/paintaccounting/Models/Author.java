package gavrysh.oleg.paintaccounting.Models;

/**
 * Created by Oleg on 17-Nov-15.
 */
public class Author extends  Idable implements Descriptable {

public String description, name;

    public Author() {
        super();
    }

    public Author(String name, String biography, int id) {
        super(id);
        this.description = biography;
        this.name = name;
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
