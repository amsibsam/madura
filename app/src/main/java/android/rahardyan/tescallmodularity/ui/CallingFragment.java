package android.rahardyan.tescallmodularity.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.rahardyan.tescallmodularity.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CallingFragment extends Fragment {


    public CallingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calling, container, false);
    }

}
