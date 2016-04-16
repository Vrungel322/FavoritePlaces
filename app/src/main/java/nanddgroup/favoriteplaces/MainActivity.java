package nanddgroup.favoriteplaces;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    @Bind(R.id.bLTPlace)
    Button bLTPlace;
    @Bind(R.id.bAMPlaces)
    Button bAMPlaces;
    private DBHelper dbHelper;
    private GoogleMap gMap;
    private LocationManager locationManager;
    private double cur_lat;
    private double cur_lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        dbHelper = new DBHelper(getApplicationContext());
        dbHelper.init();
        dbHelper.createTablePlaces();

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                cur_lat = location.getLatitude();
                cur_lng = location.getLongitude();

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                100, 0.1f, locationListener);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
    }

    @OnClick(R.id.bLTPlace)
    public void bLTPlaceClicked(){
        LatLng curPos = new LatLng(cur_lat, cur_lng);
        gMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(curPos, 18,
                gMap.getCameraPosition().tilt, gMap.getCameraPosition().bearing)));
        gMap.addMarker(new MarkerOptions().position(curPos).title("Liked this place"));
            dbHelper.insertToTable("test_", cur_lat, cur_lng);

    }
}
