package gavrysh.oleg.paintaccounting.Models;

/**
 * Created by Oleg on 18-Nov-15.
 */
public class Room extends Idable implements Descriptable{
    public int builingId;
    public  String name, description;

    public Room() {
        super();
    }

    public Room(int id, int builingId, String name, String description) {
        super(id);
        this.builingId = builingId;
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
