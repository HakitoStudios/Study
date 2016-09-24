package hakito.wifimap.POJO;

import javax.annotation.Generated;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

@Generated("org.jsonschema2pojo")
public class Point {

    @Expose(serialize = false)
    private String _id;

    @SerializedName("x")
    @Expose
    private float x;
    @SerializedName("y")
    @Expose
    private float y;

    @Expose
    private String name;

    @Expose
    private List<Comment> comments = new ArrayList<Comment>();

    public Point() {
    }

    public Point(LatLng latLng)
    {
        this(latLng.latitude, latLng.longitude);

    }



    public Point(double x, double y) {
        this.x = (float)x;
        this.y = (float)y;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     * The x
     */
    public float getX() {
        return x;
    }

    /**
     *
     * @param x
     * The x
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     *
     * @return
     * The y
     */
    public float getY() {
        return y;
    }

    /**
     *
     * @param y
     * The y
     */
    public void setY(float y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return String.format("%f; %f\n", x, y);
    }
}