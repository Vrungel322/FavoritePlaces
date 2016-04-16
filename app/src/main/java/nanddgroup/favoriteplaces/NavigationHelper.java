package nanddgroup.favoriteplaces;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Nikita on 16.04.2016.
 */
public class NavigationHelper {


    private LocationManager locationManager;
    private double cur_lat;
    private double cur_lng;
    private Context context;
    private LocationListener locationListener;

    public NavigationHelper(Context context, GoogleMap gMap) {
        this.context = context;
    }

    public void init() {
        locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                cur_lat = location.getLatitude();
                cur_lng = location.getLongitude();
                Log.e("coord", "1 : " + cur_lat + " 2 : " + cur_lng);
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
    }

    public void startListenCoord() {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                100, 0.1f, locationListener);
    }

    public void navigateToCurrentPlace(GoogleMap gMap, String name){
        LatLng curPos = new LatLng(cur_lat, cur_lng);
        gMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(curPos, 18,
                0, 0)));
        gMap.addMarker(new MarkerOptions().position(curPos).title(name));
    }

    public void navigateToPlace(GoogleMap gMap, LatLng latLng, String name){
        gMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng, 18,
                0, 0)));
        gMap.addMarker(new MarkerOptions().position(latLng).title(name));
    }

    public void removeAllMarcers(GoogleMap gMap){
        gMap.clear();
    }

    public double getCur_lat() {
        return cur_lat;
    }

    public double getCur_lng() {
        return cur_lng;
    }
}
