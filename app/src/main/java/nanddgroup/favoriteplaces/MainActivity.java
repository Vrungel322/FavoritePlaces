package nanddgroup.favoriteplaces;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.bLTPlace)
    Button bLTPlace;
    @Bind(R.id.bAMPlaces)
    Button bAMPlaces;
    @Bind(R.id.bDT)
    Button bDT;
    private PlacesDialogFragment placesDialogFragment;
    private PlaceNameFragment placeNameFragment;
    private DBHelper dbHelper;
    private NavigationHelper navHelper;
    private GoogleMap gMap;
    public static Bus bus;

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
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
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
        //Event Bus stuff
        bus = new Bus();
        bus.register(this);
    }


    @OnClick(R.id.bLTPlace)
    public void bLTPlaceClicked() {
        placeNameFragment = new  PlaceNameFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        placeNameFragment.show(ft,"ft");
    }

    @OnClick(R.id.bAMPlaces)
    public void bAMPlacesClicked(){
        placesDialogFragment = new PlacesDialogFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        placesDialogFragment.show(ft,"ft");
    }

    @OnClick(R.id.bDT)
    public void bDTClicked(){
        dbHelper.dropTableAndCreate("places");
        navHelper.removeAllMarcers(gMap);
    }

    @Subscribe
    public void handlePlaceInDialogClicked(Place place){
        placesDialogFragment.dismiss();
        navHelper.navigateToPlace(gMap,
                new LatLng(place.getdLat(), place.getdLng()), place.getsPlaceName());
    }

    @Subscribe
    public void handlePlaceNameInputted(String name){
        placeNameFragment.dismiss();
        navHelper.navigateToCurrentPlace(gMap, name);
        dbHelper.insertToTable(name, navHelper.getCur_lat(), navHelper.getCur_lng());

    }
}
