package mfdevelopement.appsolution.tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ViewFlipper;

import mfdevelopement.appsolution.R;

public class TabBundesligaTable extends Fragment {

    private ViewFlipper vf = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_bundesliga_table, container, false);

        return rootView;
    }
}
