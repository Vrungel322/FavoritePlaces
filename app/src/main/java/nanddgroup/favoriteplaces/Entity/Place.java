package nanddgroup.favoriteplaces.entity;

/**
 * Created by Nikita on 16.04.2016.
 */
public class Place {
    protected String sPlaceName;
    protected double dLat;
    protected double dLng;

    public Place(String sPlaceName, double dLat, double dLng) {
        this.dLat = dLat;
        this.dLng = dLng;
        this.sPlaceName = sPlaceName;
    }

    public double getdLat() {
        return dLat;
    }

    public double getdLng() {
        return dLng;
    }

    public String getsPlaceName() {
        return sPlaceName;
    }
}
