package hakito.carclient;

/**
 * Created by Oleg on 07.06.2016.
 */
public class Address {
    String url;
    int port;

    public Address(String url, int port) {
        this.url = url;
        this.port = port;
    }

    public String getUrl() {
        return url;
    }

    public int getPort() {
        return port;
    }
}
