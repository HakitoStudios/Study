package gavrysh.oleg.paintaccounting.Models;

/**
 * Created by Oleg on 18-Nov-15.
 */
public class Worker extends Idable implements Descriptable {
    public String name, phone;

    public Worker() {
        super();
    }

    public Worker(int id, String name, String phone) {
        super(id);
        this.name = name;
        this.phone = phone;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return phone;
    }
}
