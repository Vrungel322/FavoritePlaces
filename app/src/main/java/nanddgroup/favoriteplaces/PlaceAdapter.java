package nanddgroup.favoriteplaces;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Nikita on 16.04.2016.
 */
public class PlaceAdapter extends ArrayAdapter<Place> {
    private Context context;
    private List<Place> places;

    public PlaceAdapter(Context context, int resource, List<Place> places) {
        super(context, resource, places);
        this.context = context;
        this.places = places;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View rootView = null;
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rootView = inflater.inflate(R.layout.each_item, parent, false);
        }
        else {
            rootView = convertView;
        }

        TextView tvPlace = (TextView) rootView.findViewById(R.id.tvPlace);
        tvPlace.setText(places.get(position).sPlaceName);
        TextView tvLat = (TextView) rootView.findViewById(R.id.tvLat);
        tvLat.setText(String.valueOf(places.get(position).dLat));
        TextView tvLng = (TextView) rootView.findViewById(R.id.tvLng);
        tvLng.setText(String.valueOf(places.get(position).dLng));

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TaG", String.valueOf(places.get(position).dLat));
                MainActivity.bus.post(new Place(places.get(position).sPlaceName,
                        places.get(position).dLat,
                        places.get(position).dLng));

            }
        });

        return rootView;
    }
}
