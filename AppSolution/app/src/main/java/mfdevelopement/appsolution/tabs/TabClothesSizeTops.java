package mfdevelopement.appsolution.tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ViewFlipper;

import mfdevelopement.appsolution.R;

public class TabClothesSizeTops extends Fragment {

    private ViewFlipper vf;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_clothes_size_tops, container, false);

        setRadioButtonEvents(rootView);

        return rootView;
    }

    private void setRadioButtonEvents(View view) {

        final RadioGroup rg = view.findViewById(R.id.rg_clothesSize_tops);
        vf = view.findViewById(R.id.clothesSize_tops_flipper);

        final RadioButton rbTopsMen = view.findViewById(R.id.rb_clothesSize_tops_men);
        rbTopsMen.setChecked(true);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_clothesSize_tops_men:
                        vf.setDisplayedChild(0);
                        break;
                    case R.id.rb_clothesSize_tops_women:
                        vf.setDisplayedChild(1);
                        break;
                }
            }
        });

    }
}
