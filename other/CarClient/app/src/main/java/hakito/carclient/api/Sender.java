package hakito.carclient.api;

/**
 * Created by Oleg on 07.06.2016.
 */
public class Sender {
    private static DataSender instance = new DataSender();

    public static DataSender getInstance() {
        return instance;
    }

}
