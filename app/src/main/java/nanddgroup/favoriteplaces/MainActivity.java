package nanddgroup.favoriteplaces;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.bLTPlace)
    Button bLTPlace;
    @Bind(R.id.bAMPlaces)
    Button bAMPlaces;
    private DBHelper dbHelper;
    private NavigationHelper navHelper;
    private GoogleMap gMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //DB stuff
        dbHelper = new DBHelper(getApplicationContext());
        dbHelper.init();
        dbHelper.createTablePlaces();
        // Map stuff
        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                gMap = googleMap;
            }
        });
        navHelper = new NavigationHelper(getApplicationContext(), gMap);
        navHelper.init();
        navHelper.startListenCoord();
    }



    @OnClick(R.id.bLTPlace)
    public void bLTPlaceClicked(){
        navHelper.navigateToCurrentPlace(gMap);
            dbHelper.insertToTable("test_", navHelper.getCur_lat(), navHelper.getCur_lng());

    }
}
