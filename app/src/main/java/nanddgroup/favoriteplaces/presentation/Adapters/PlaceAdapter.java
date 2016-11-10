package nanddgroup.favoriteplaces.presentation.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import nanddgroup.favoriteplaces.R;
import nanddgroup.favoriteplaces.entity.Place;
import nanddgroup.favoriteplaces.presentation.Activity.MainActivity;

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
        tvPlace.setText(places.get(position).getsPlaceName());
        TextView tvLat = (TextView) rootView.findViewById(R.id.tvLat);
        tvLat.setText(String.valueOf(places.get(position).getdLat()));
        TextView tvLng = (TextView) rootView.findViewById(R.id.tvLng);
        tvLng.setText(String.valueOf(places.get(position).getdLng()));

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TaG", String.valueOf(places.get(position).getdLat()));
                MainActivity.bus.post(new Place(places.get(position).getsPlaceName(),
                        places.get(position).getdLat(),
                        places.get(position).getdLng()));

            }
        });

        return rootView;
    }
}
