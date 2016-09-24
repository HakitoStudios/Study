package hakito.wifimap.REST;

import com.google.gson.JsonObject;

import hakito.wifimap.POJO.MyResp;
import hakito.wifimap.POJO.Point;
import hakito.wifimap.POJO.PointsResult;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Oleg on 19.05.2016.
 */

public interface WifiMapService {

    String POINTS = "points";

    @GET(POINTS)
    Call<PointsResult> getPoints();

    @POST(POINTS)
    Call<Point> postPoint(@Body Point point);



}
