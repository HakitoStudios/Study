package hakito.wifimap;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Tile;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;

import java.io.IOException;
import java.util.HashMap;

import hakito.wifimap.POJO.Point;
import hakito.wifimap.POJO.PointsResult;
import hakito.wifimap.REST.RestClient;
import hakito.wifimap.REST.WifiMapService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    private HashMap<Marker, Point> h;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        h = new HashMap<>();


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.mSettings)
        {
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    void startMaps()
    {




        class Loader extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... params) {
                try {

                    final PointsResult res = RestClient.s.getPoints().execute().body();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            for (Point p:res.getPoints())
                            {
                                addPoint(p);
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }
        }
        new Loader().execute();

    }
void addPoint(Point p)
{
    LatLng sydney = new LatLng(p.getX(), p.getY());


  Marker m=  mMap.addMarker(new MarkerOptions()
            .position(sydney)
            .title(p.getName())
            .icon(new BitmapDescriptor(BitmapDescriptorFactory.fromResource(R.drawable.wifi_marker).zzzH()))
    );

    h.put(m, p);
}
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLongClickListener(this);
        mMap.setOnInfoWindowClickListener(this);

mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
       // return getLayoutInflater().inflate(R.layout.map_info_window, null);
        return null;
    }
});

        startMaps();
    }
static final int ADD_AP = 2;
    @Override
    public void onMapLongClick(LatLng latLng) {
        Point p = new Point(latLng);
        AddAPActivity.p = p;
        Intent i = new Intent(this, AddAPActivity.class);
        startActivityForResult(i,ADD_AP );


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==ADD_AP)
        {
            RestClient.postPoint(AddAPActivity.p);
            addPoint(AddAPActivity.p);
            AddAPActivity.p=null;
        }
    }


    @Override
    public void onInfoWindowClick(Marker marker) {
        Intent intent = new Intent(this, DetailedAPActivity.class);
        DetailedAPActivity.p = h.get(marker);
        startActivity(intent);


    }
}
