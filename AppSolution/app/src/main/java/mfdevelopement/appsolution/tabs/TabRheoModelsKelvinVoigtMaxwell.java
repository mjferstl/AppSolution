package mfdevelopement.appsolution.tabs;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidplot.ui.Anchor;
import com.androidplot.ui.HorizontalPositioning;
import com.androidplot.ui.VerticalPositioning;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYGraphWidget;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Locale;

import mfdevelopement.appsolution.R;

public class TabRheoModelsKelvinVoigtMaxwell extends android.support.v4.app.Fragment {

    private EditText etC1;
    private EditText etD1;
    private EditText etC2;
    private EditText etD2;
    private EditText etFMIN;
    private EditText etFMAX;

    private Button btn_calc;

    private XYPlot xyPlot;

    private String INPUTERROR = "";
    private String ADJUSTRANGE = "";
    private String appname = "";
    private String LogTag = "";

    private ImageView imgKV = null;
    private TextView tvValues = null;

    private ImageButton btnImgKV = null;

    private ListView lv = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_rheo_models_kelvin_voigt_maxwell, container, false);

        getElements(rootView);

        INPUTERROR = getString(R.string.toast_rheological_models_input_error);
        ADJUSTRANGE = getString(R.string.toast_rheological_models_adjust_range);
        appname = getString(R.string.app_name);
        LogTag = appname + "/Kelvin-Voigt-Maxwell-Element";

        displayKelvinVoigtMaxwellElement();

        return rootView;
    }


    private void getElements(View view) {

        etC1 = view.findViewById(R.id.et_rheolocical_model_KVM_c1);
        etD1 = view.findViewById(R.id.et_rheolocical_model_KVM_d1);
        etC2 = view.findViewById(R.id.et_rheolocical_model_KVM_c2);
        etD2 = view.findViewById(R.id.et_rheolocical_model_KVM_d2);

        etFMIN = view.findViewById(R.id.et_rheolocical_model_KVM_fmin);
        etFMAX = view.findViewById(R.id.et_rheolocical_model_KVM_fmax);

        btn_calc = view.findViewById(R.id.btn_rheological_models_calculate);

        lv = view.findViewById(R.id.lv_rheological_models_KVM_values);

        xyPlot = view.findViewById(R.id.dynamic_stiffness_KVM_plot);
        //xyPlot.setDomainBoundaries(0,100, BoundaryMode.SHRINK);
        xyPlot.setDomainStepValue(5+1);
        //xyPlot.setRangeBoundaries(0,1000,BoundaryMode.FIXED);
        xyPlot.setRangeStepValue(5+1);
        xyPlot.getGraph().setMarginLeft(80);
        Log.i(LogTag,"boundaries");

        btn_calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateKelvinVoigt(v);
            }
        });

        imgKV = view.findViewById(R.id.img_rheolocical_model_KVM_icon);
        tvValues = view.findViewById(R.id.tv_rheological_models_KVM_values);

        btnImgKV = view.findViewById(R.id.btn_rheological_models_KVM_img);
        btnImgKV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayKelvinVoigtMaxwellElement();
            }
        });
    }

    private void calculateKelvinVoigt(View view) {

        hideKeyboard(view);
        String txtStiffnessC1 = etC1.getText().toString();
        String txtDampingD1 = etD1.getText().toString();
        String txtStiffnessC2 = etC2.getText().toString();
        String txtDampingD2 = etD2.getText().toString();
        String fMin = etFMIN.getText().toString();
        String fMax = etFMAX.getText().toString();

        if (txtStiffnessC1.equals("") && txtDampingD1.equals("") && txtStiffnessC2.equals("") && txtDampingD2.equals("")) {
            Toast.makeText(getContext(), INPUTERROR, Toast.LENGTH_SHORT).show();
        } else {

            double stiffnessC1;
            if (txtStiffnessC1.equals("")) {
                stiffnessC1 = 0.0;
                etC1.setText("0");
            } else {
                stiffnessC1 = Double.parseDouble(txtStiffnessC1) * 1000; // convert to N/m
            }

            double stiffnessC2;
            if (txtStiffnessC2.equals("")) {
                stiffnessC2 = 0.0;
                etC2.setText("0");
            } else {
                stiffnessC2 = Double.parseDouble(txtStiffnessC2) * 1000; // convert to N/m
            }

            double dampingD1;
            if (txtDampingD1.equals("")) {
                dampingD1 = 0.0;
                etD1.setText("0");
            } else {
                dampingD1 = Double.parseDouble(txtDampingD1);
            }

            double dampingD2;
            if (txtDampingD2.equals("")) {
                dampingD2 = 0.0;
                etD2.setText("0");
            } else {
                dampingD2 = Double.parseDouble(txtDampingD2);
            }

            double fmin;
            if (fMin.equals("") || fMin.equals("0")) {
                fmin = 0;
                etFMIN.setText("0");
            } else {
                fmin = Double.parseDouble(fMin);
            }

            double fmax;
            if (fMax.equals("")) {
                fmax = 100;
                etFMAX.setText("100");
            } else {
                fmax = Double.parseDouble(fMax);
            }

            // check if max frequency is greater than min frequency
            if (fmax <= fmin) {
                Toast.makeText(getContext(), ADJUSTRANGE, Toast.LENGTH_SHORT).show();
                fmax = fmin + 5;
                etFMAX.setText(String.valueOf((int) fmax));
            }

            int steps = 100;
            int items = steps + 1;

            double[] freq = new double[items];
            double[] cdyn = new double[items];
            for (int i = 0; i < items; i++) {
                freq[i] = fmin + i * (fmax - fmin) / steps;
                cdyn[i] = calculateDyanmicStiffness(stiffnessC1, dampingD1, stiffnessC2, dampingD2, freq[i]);
                Log.i(LogTag,String.format(Locale.getDefault(),"%.3f",cdyn[i]/1000) + " N/mm bei " + String.format("%.2f",freq[i]) + " Hz");
            }

            plotDynamicStiffness(freq, cdyn);
            showValues(freq,cdyn);

        }
    }

    private double calculateDyanmicStiffness(double c1, double d1, double c2, double d2, double f) {

        double cdyn;
        double omega = 2* Math.PI*f;

        if (c2 == 0 && d2 == 0) {
            // only a Kelvin Voigt Element
            cdyn = Math.sqrt( Math.pow(c1,2) + Math.pow((omega*d1),2) );
        } else {
            // Kelvin-Voigt-Element parallel with Maxwell element
            double real = c1 - c2 * (Math.pow(c2, 2) / (Math.pow(c2, 2) + Math.pow(d2, 2) * Math.pow(omega, 2)) - 1);
            double imag = d1 + (Math.pow(c2, 2) * d2 * omega) / (Math.pow(c2, 2) + Math.pow(d2, 2) * Math.pow(omega, 2));
            cdyn = Math.sqrt( Math.pow(real,2) + Math.pow(imag,2) );
        }

        return cdyn;
    }

    private void plotDynamicStiffness(double[] f, double[] cdyn) {

        imgKV.setVisibility(View.GONE);

        xyPlot.clear();

        xyPlot.setDomainBoundaries(f[0],f[f.length-1], BoundaryMode.FIXED);

        Double[] freq = new Double[f.length];
        Double[] dynStiff = new Double[f.length];
        for (int i=0;i<f.length;i++) {
            freq[i] = f[i];
            dynStiff[i] = cdyn[i]/1000; // convert to N/mm
        }

        //XYSeries fSeries = new SimpleXYSeries(Arrays.asList(freq),SimpleXYSeries.ArrayFormat.XY_VALS_INTERLEAVED,"");
        XYSeries cSeries = new SimpleXYSeries(Arrays.asList(freq), Arrays.asList(dynStiff), "");

        LineAndPointFormatter cFormat = new LineAndPointFormatter(getContext(), R.xml.line_formatter_green);

        xyPlot.setDomainStepValue(6);
        xyPlot.setRangeStepValue(5);

        double maxStiffness = dynStiff[0];
        double minStiffness = dynStiff[0];
        for (int i=0;i<cdyn.length;i++) {
            if (cdyn[i]/1000 > maxStiffness) {
                maxStiffness = cdyn[i]/1000;
            }
            if (cdyn[i]/1000 < minStiffness) {
                minStiffness = cdyn[i]/1000;
            }
        }

        double range =  maxStiffness - minStiffness;

        if (range < 4.0) {
            double margin = (4.0 - range) / 2;
            if (minStiffness-margin < 0.0) {
                xyPlot.setRangeBoundaries(0.0, (maxStiffness + margin), BoundaryMode.FIXED);
            } else {
                xyPlot.setRangeBoundaries((minStiffness - margin), (maxStiffness + margin), BoundaryMode.FIXED);
            }
        } else {
            xyPlot.setRangeBoundaries(minStiffness,maxStiffness,BoundaryMode.FIXED);
        }

        xyPlot.addSeries(cSeries,cFormat);
        xyPlot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).setFormat(new DecimalFormat("0"));
        xyPlot.getLegend().setVisible(false);
        xyPlot.getDomainTitle().position(0, HorizontalPositioning.RELATIVE_TO_RIGHT,0, VerticalPositioning.RELATIVE_TO_BOTTOM, Anchor.RIGHT_BOTTOM);
        xyPlot.getTitle().position(0,HorizontalPositioning.RELATIVE_TO_RIGHT,0,VerticalPositioning.RELATIVE_TO_TOP,Anchor.RIGHT_TOP);
        xyPlot.getRangeTitle().position(0,HorizontalPositioning.RELATIVE_TO_LEFT,0,VerticalPositioning.RELATIVE_TO_CENTER,Anchor.LEFT_MIDDLE);
        xyPlot.redraw();

        displayPlot();

    }

    private void displayKelvinVoigtMaxwellElement() {

        xyPlot.setVisibility(View.GONE);
        imgKV.setVisibility(View.VISIBLE);
        btnImgKV.setVisibility(View.GONE);
        tvValues.setVisibility(View.GONE);
        lv.setVisibility(View.GONE);

    }

    private void displayPlot() {

        imgKV.setVisibility(View.GONE);
        xyPlot.setVisibility(View.VISIBLE);
        btnImgKV.setVisibility(View.VISIBLE);
        tvValues.setVisibility(View.VISIBLE);
        lv.setVisibility(View.VISIBLE);

    }

    private void showValues(double[] f, double[] c) {

        String[] listText = new String[f.length];
        for (int i=0;i<f.length;i++) {
            listText[i] = "f = " + String.format(Locale.getDefault(),"%.1f",f[i]) + " Hz, c_dyn = " + String.format(Locale.getDefault(),"%.3f",c[i]/1000) + " N/mm";
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,android.R.id.text1,listText);
        lv.setAdapter(adapter);

    }

    private void hideKeyboard(View view) {
        // Check if no view has focus:
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
