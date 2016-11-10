package nanddgroup.favoriteplaces.presentation.Activity;

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

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nanddgroup.favoriteplaces.R;
import nanddgroup.favoriteplaces.app.App;
import nanddgroup.favoriteplaces.data.DBHelper;
import nanddgroup.favoriteplaces.entity.Place;
import nanddgroup.favoriteplaces.presentation.Fragments.PlaceNameFragment;
import nanddgroup.favoriteplaces.presentation.Fragments.PlacesDialogFragment;
import nanddgroup.favoriteplaces.presentation.Utils.Constants;
import nanddgroup.favoriteplaces.presentation.Utils.NavigationHelper;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.bLTPlace)
    Button bLTPlace;
    @Bind(R.id.bAMPlaces)
    Button bAMPlaces;
    @Bind(R.id.bDT)
    Button bDT;
    @Inject
    DBHelper dbHelper;
    private PlacesDialogFragment placesDialogFragment;
    private PlaceNameFragment placeNameFragment;
    private NavigationHelper navHelper;
    private GoogleMap gMap;
    public static Bus bus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //injecting
        App.getApp(this).getComponent().inject(this);
        //DB stuff
        dbHelper.createTablePlaces();
        // Map stuff
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                gMap = googleMap;
                navHelper.addMyCurrentLocationMarker(gMap);
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
        placeNameFragment = new PlaceNameFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        placeNameFragment.show(ft, "ft");
    }

    @OnClick(R.id.bAMPlaces)
    public void bAMPlacesClicked() {
        placesDialogFragment = new PlacesDialogFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        placesDialogFragment.show(ft, "ft");
    }

    @OnClick(R.id.bDT)
    public void bDTClicked() {
        dbHelper.dropTableAndCreate(Constants.TABLE_NAME);
        navHelper.removeAllMarcers(gMap);
    }

    @Subscribe
    public void handlePlaceInDialogClicked(Place place) {
        placesDialogFragment.dismiss();
        navHelper.navigateToPlace(gMap,
                new LatLng(place.getdLat(), place.getdLng()), place.getsPlaceName());
    }

    @Subscribe
    public void handlePlaceNameInputted(String name) {
        placeNameFragment.dismiss();
        navHelper.navigateToCurrentPlace(gMap, name);
        dbHelper.insertToTable(name, navHelper.getCur_lat(), navHelper.getCur_lng());

    }
}
