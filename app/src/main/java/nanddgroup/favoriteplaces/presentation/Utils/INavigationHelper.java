package nanddgroup.favoriteplaces.presentation.Utils;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Nikita on 10.11.2016.
 */
public interface INavigationHelper {
    void init();

    void startListenCoord();

    void navigateToCurrentPlace(GoogleMap gMap, String name);

    void navigateToPlace(GoogleMap gMap, LatLng latLng, String name);

    void removeAllMarcers(GoogleMap gMap);

    void addMyCurrentLocationMarker(GoogleMap gMap);

    LatLng getLocationFromAddress(Context context, String strAddress);

    String getDirectionsUrl(LatLng origin, LatLng dest);

    String downloadUrl(String strUrl);
}
