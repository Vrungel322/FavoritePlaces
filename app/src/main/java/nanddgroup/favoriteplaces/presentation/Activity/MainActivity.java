package nanddgroup.favoriteplaces.presentation.Activity;

import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import nanddgroup.favoriteplaces.presentation.Utils.DirectionsJSONParser;
import nanddgroup.favoriteplaces.presentation.Utils.NavigationHelper;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.bLTPlace)
    Button bLTPlace;
    @Bind(R.id.bAMPlaces)
    Button bAMPlaces;
    @Bind(R.id.bDT)
    Button bDT;
    @Bind(R.id.etToFindPlace)
    EditText etToFindPlace;
    @Bind(R.id.bFindPlace)
    Button bFindPlace;
    @Inject
    DBHelper dbHelper;

    ArrayList<LatLng> markerPoints;
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
                setMapClickListener();
            }
        });
        navHelper = new NavigationHelper(getApplicationContext(), gMap);
        navHelper.init();
        navHelper.startListenCoord();
        //Event Bus stuff
        bus = new Bus();
        bus.register(this);
    }

    private void setMapClickListener() {
        markerPoints = new ArrayList<LatLng>();
        gMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {

                // Already two locations
                if(markerPoints.size()>1){
                    markerPoints.clear();
                    gMap.clear();
                }

                // Adding new item to the ArrayList
                markerPoints.add(point);

                // Creating MarkerOptions
                MarkerOptions options = new MarkerOptions();

                // Setting the position of the marker
                options.position(point);

                /**
                 * For the start location, the color of marker is GREEN and
                 * for the end location, the color of marker is RED.
                 */
                if(markerPoints.size()==1){
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                }else if(markerPoints.size()==2){
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                }

                // Add new marker to the Google Map Android API V2
                gMap.addMarker(options);

                // Checks, whether start and end locations are captured
                if(markerPoints.size() >= 2){
                    LatLng origin = markerPoints.get(0);
                    LatLng dest = markerPoints.get(1);

                    // Getting URL to the Google Directions API
                    String url = navHelper.getDirectionsUrl(origin, dest);

                    DownloadTask downloadTask = new DownloadTask();

                    // Start downloading json data from Google Directions API
                    downloadTask.execute(url);
                }
            }
        });
    }

    @OnClick(R.id.bFindPlace)
    public void bFindPlaceClicked(){
        String sPlace = etToFindPlace.getText().toString();
        if (!sPlace.equals("")) {
            navHelper.removeAllMarcers(gMap);
            navHelper.navigateToPlace(gMap,
                    navHelper.getLocationFromAddress(getApplicationContext(),
                            sPlace),sPlace);
        } else {
            Toast.makeText(getApplicationContext(), "Empty Find field", Toast.LENGTH_LONG).show();
        }
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

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = navHelper.downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(2);
                lineOptions.color(Color.RED);
            }

            // Drawing polyline in the Google Map for the i-th route
            gMap.addPolyline(lineOptions);
        }
    }
}
