package nanddgroup.favoriteplaces.presentation.Fragments;


import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import nanddgroup.favoriteplaces.R;
import nanddgroup.favoriteplaces.data.DBHelper;
import nanddgroup.favoriteplaces.entity.Place;
import nanddgroup.favoriteplaces.presentation.Adapters.PlaceAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlacesDialogFragment extends DialogFragment {
    @Bind(R.id.lvPlaces) ListView lvPlaces;
    private PlaceAdapter placeAdapter;
    private PlacesDialogFragment placesDialogFragment;
    private List<Place> data;


    public PlacesDialogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setTitle("My Places");
        getDialog().setCanceledOnTouchOutside(true);
        View view = inflater.inflate(R.layout.fragment_places, container, false);
        ButterKnife.bind(this, view);
        new Thread(new Runnable() {
            @Override
            public void run() {
                data = DBHelper.getAllNotes("places");
            }
        }).start();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        placeAdapter = new PlaceAdapter(getActivity(), R.layout.each_item, data);
        lvPlaces.setAdapter(placeAdapter);
    }
}
