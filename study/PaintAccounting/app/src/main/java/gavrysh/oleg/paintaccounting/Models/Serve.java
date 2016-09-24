package gavrysh.oleg.paintaccounting.Models;

import java.io.Serializable;

import gavrysh.oleg.paintaccounting.Data.DataBaseHelper;

/**
 * Created by Oleg on 18-Nov-15.
 */
public class Serve extends Idable implements Descriptable, Serializable {
    public int workerId, roomId;
    public String room, worker;
    public String tasks;

    public Serve(int id, int workerId, int roomId, String tasks) {
        super(id);
        this.workerId = workerId;
        this.roomId = roomId;
        this.tasks = tasks;
    }

    public Serve() {
        super();
    }



    @Override
    public String getName() {

        return String.format("%s in %s room", worker, room);
    }

    @Override
    public String getDescription() {
        return tasks;
    }
}
