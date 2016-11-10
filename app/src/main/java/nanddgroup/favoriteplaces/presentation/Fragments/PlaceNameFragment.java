package nanddgroup.favoriteplaces.presentation.Fragments;


import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nanddgroup.favoriteplaces.R;
import nanddgroup.favoriteplaces.presentation.Activity.MainActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlaceNameFragment extends DialogFragment {
    @Bind(R.id.etPlaceName)
    EditText etPlaceName;
    @Bind(R.id.bOk)
    Button bOk;
    @Bind(R.id.bClose)
    Button bClose;
    private String sPlaceName;


    public PlaceNameFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setTitle("Your new place");
        View view = inflater.inflate(R.layout.fragment_place_name, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.bOk)
    public void bOkClicked() {
        sPlaceName = etPlaceName.getText().toString();
        MainActivity.bus.post(sPlaceName);
    }

    @OnClick(R.id.bClose)
    public void bCloseClicked() {
        getDialog().dismiss();
    }
}
