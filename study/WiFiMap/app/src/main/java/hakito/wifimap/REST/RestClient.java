package hakito.wifimap.REST;

import hakito.wifimap.POJO.Point;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Oleg on 20.05.2016.
 */
public class RestClient {
    public static Retrofit r;
    static
    {
        reinit("http://192.168.43.13:3000");
    }

    public static void reinit(String addr)
    {
        r = new Retrofit.Builder()
                .baseUrl(addr)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static final  WifiMapService s = r.create(WifiMapService.class);


    public static void addComment()
    {

    }

    public static void postPoint(Point p)
    {
        s.postPoint(p).enqueue(new Callback<Point>() {
            @Override
            public void onResponse(Call<Point> call, Response<Point> response) {
                if(response.code()==200)
                {
                    //OK
                }
            }

            @Override
            public void onFailure(Call<Point> call, Throwable t) {

            }
        });
    }
}
