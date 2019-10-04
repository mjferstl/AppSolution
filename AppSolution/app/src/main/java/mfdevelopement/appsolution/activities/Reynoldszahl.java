package mfdevelopement.appsolution.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.math.RoundingMode;

import mfdevelopement.appsolution.R;

import static android.view.View.VISIBLE;

public class Reynoldszahl extends AppCompatActivity {

    /**
     * Initialisierung Variablen
     */
    // Strings
    private String strVelocityM = "";
    private String strDiameter = "";
    private String strDynViscosity = "";
    private String strDensity = "";
    private String strTitle = "";
    private String strKinViscosity = "";
    private String medium = "";

    // Meldungen bei Fehlenden Eingaben
    private String ENTER_DYN_VIS = "";
    private String ENTER_KIN_VIS = "";
    private String ENTER_DIAMETER = "";
    private String ENTER_DENSITY = "";
    private String ENTER_AVG_VEL = "";

    // Stroemungsbild
    private String LAM_STR = "";
    private String TUR_STR = "";

    // Text fuer Hilfe-Dialoge
    private String TITLE_REYN = "";
    private String OK = "";

    private String appname = "";
    private String LogTag = "";

    // Boolean
    private boolean booChecked = true;

    // Integer
    private int intRoundingScale = 10;
    private int intKindOfHelp = 0;
    private int temp = 0;

    // BigDecimal
    private BigDecimal bdReynKrit = new BigDecimal(2300);
    private BigDecimal bdDynViscosity = BigDecimal.ZERO;
    private BigDecimal bdDensity = BigDecimal.ZERO;
    private BigDecimal bdReynolds = BigDecimal.ZERO;
    private BigDecimal bdVelocitym = BigDecimal.ZERO;
    private BigDecimal bdDiameter = BigDecimal.ZERO;
    private BigDecimal bdKinViscosity = BigDecimal.ZERO;

    /**
     * Deklaration und Initialisierung der Widgets
     */
    // Layouts
    private LinearLayout layKinViscosity = null;
    private LinearLayout layDynViscosity = null;
    private LinearLayout layDensity = null;

    // TextView
    private TextView txtvResultValue = null;
    private TextView txtvResultText = null;
    private TextView txtvCurrentType = null;

    // Edit View
    private EditText txteVelocityText = null;
    private EditText txteDiameterText = null;
    private EditText txteKinViscosity = null;
    private EditText txteDynViscosity = null;
    private EditText txteDensity = null;

    // Button
    private Button btCalcTube = null;
    private Button btTubeHelp = null;
    private Button btUsualValues = null;

    // Check Box
    private CheckBox cbViscosity = null;

    // View
    private View dialogLayout = null;

    // View Group
    private ViewGroup vgParent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reynoldszahl);

        loadStrings();
        loadElements();

        btCalcTube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calcTube();
            }
        });

        btTubeHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tubeHelp();
            }
        });

        btUsualValues.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showUsualValues();
            }
        });

        txtvResultText.setVisibility(View.GONE);

        Log.i(LogTag, "activity reynolds-number startet");
    }

    public void loadElements() {
        //
        txteVelocityText = findViewById(R.id.reyn_tube_params_velocity);
        txteDiameterText = findViewById(R.id.reyn_tube_params_diameter);
        // EditText fields for fluid values
        txteDynViscosity = findViewById(R.id.reyn_tube_params_dyn_viscosity_edit);
        txteDensity = findViewById(R.id.reyn_tube_params_density_edit);
        txteKinViscosity = findViewById(R.id.reyn_tube_params_kin_viscosity_edit);
        //
        txtvResultValue = findViewById(R.id.reyn_tube_result_value);
        txtvCurrentType = findViewById(R.id.reyn_tube_laminar_or_turbulent_text);
        txtvResultText = findViewById(R.id.reyn_tube_result_text);
        //
        // layouts
        layKinViscosity = findViewById(R.id.layout_reyn_tube_params_kin_viscosity);
        layDynViscosity = findViewById(R.id.layout_reyn_tube_params_dyn_viscosity);
        layDensity = findViewById(R.id.layout_reyn_tube_params_density);
        //
        btCalcTube = findViewById(R.id.reyn_tube_btd_calc);
        btTubeHelp = findViewById(R.id.reyn_tube_help);
        btUsualValues = findViewById(R.id.reyn_tube_btd_usual_params);

    }

    private void loadStrings() {

        appname = getString(R.string.app_name);
        LogTag = appname + "/Reynolds number";

        ENTER_DIAMETER = getString(R.string.maths_enter_diameter);
        TITLE_REYN = getString(R.string.title_activity_reynoldszahl);
        OK = getString(R.string.btd_ok);
        LAM_STR = getString(R.string.reyn_txtv_laminar_flow);
        TUR_STR = getString(R.string.reyn_txtv_turbulant_flow);
        ENTER_DYN_VIS = getString(R.string.reyn_toast_enter_dynVisc);
        ENTER_KIN_VIS = getString(R.string.reyn_toast_enter_kinVisc);
        ENTER_DIAMETER = getString(R.string.maths_enter_diameter);
        ENTER_DENSITY = getString(R.string.reyn_toast_enter_density);
        ENTER_AVG_VEL = getString(R.string.reyn_toast_enter_avgVelo);

    }

    public void onCheckboxClicked(View view) {
        booChecked = ((CheckBox) view).isChecked();

        if (booChecked) {
            showKinViscosity();
        } else {
            showDynViscosity();
        }
    }

    public void showKinViscosity() {

        Log.i(LogTag, "showKinViscosity");
        layKinViscosity.setVisibility(View.VISIBLE);
        layDynViscosity.setVisibility(View.GONE);
        layDensity.setVisibility(View.GONE);

    }

    public void showDynViscosity() {

        Log.i(LogTag, "showDynViscosity");
        layKinViscosity.setVisibility(View.GONE);
        layDynViscosity.setVisibility(View.VISIBLE);
        layDensity.setVisibility(View.VISIBLE);

    }

    public void calcTube() {
        calcReynolds();
    }

    public void calcReynolds() {
        strVelocityM = txteVelocityText.getText().toString();
        strDiameter = txteDiameterText.getText().toString();

        if (strVelocityM.equals("")) {
            Toast.makeText(this, ENTER_AVG_VEL, Toast.LENGTH_SHORT).show();
        } else if (strDiameter.equals("")) {
            Toast.makeText(this, ENTER_DIAMETER, Toast.LENGTH_SHORT).show();
        } else {

            bdVelocitym = new BigDecimal(strVelocityM);
            bdDiameter = new BigDecimal(strDiameter);

            // wenn der schneller Weg gewählt ist
            if (booChecked) {
                strKinViscosity = txteKinViscosity.getText().toString();

                if (strKinViscosity.equals("")) {
                    Toast.makeText(this, ENTER_KIN_VIS, Toast.LENGTH_SHORT).show();
                } else {
                    bdKinViscosity = new BigDecimal(strKinViscosity);
                    bdReynolds = (bdVelocitym.multiply(bdDiameter)).divide(bdKinViscosity, 4, RoundingMode.HALF_UP);
                }
            }
            // wenn der längere Weg gewählt ist
            else {
                strDynViscosity = txteDynViscosity.getText().toString();
                strDensity = txteDensity.getText().toString();

                if (strDynViscosity.equals("")) {
                    Toast.makeText(this, ENTER_DYN_VIS, Toast.LENGTH_SHORT).show();
                } else if (strDensity.equals("")) {
                    Toast.makeText(this, ENTER_DENSITY, Toast.LENGTH_SHORT).show();
                } else {
                    bdDynViscosity = new BigDecimal(strDynViscosity);
                    bdDensity = new BigDecimal(strDensity);
                    bdKinViscosity = bdDynViscosity.divide(bdDensity, intRoundingScale, RoundingMode.HALF_UP);
                    bdReynolds = (bdVelocitym.multiply(bdDiameter)).divide(bdKinViscosity, 4, RoundingMode.HALF_UP);
                }
            }

            if (bdReynolds.compareTo(bdReynKrit) < 1) {
                txtvCurrentType.setText("\u2192 " + LAM_STR);
            } else if (bdReynolds.compareTo(bdReynKrit) >= 1) {
                txtvCurrentType.setText("\u2192" + TUR_STR);
            }

            txtvResultText.setVisibility(VISIBLE);
            txtvResultValue.setText(bdReynolds.toString());

            hideKeyboard();
        }
    }

    public void showHelp() {
        final Dialog dialog = new Dialog(this);

        switch (intKindOfHelp) {
            case 1:     // tubeHelp wurde geklickt
                dialog.setContentView(R.layout.reynoldszahl_tube_help);
                dialog.setTitle(TITLE_REYN);
                Button btHelpDismiss = dialog.findViewById(R.id.reyn_btHelpDismiss);
                btHelpDismiss.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                        Log.i(LogTag, "Close the help dialog");
                    }
                });
                break;
            case 2:     // usualValues wurde geklickt
                dialog.setContentView(R.layout.reynoldszahl_tube_values);

                Button btWater10 = dialog.findViewById(R.id.reyn_water_10deg);
                btWater10.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        medium = "water";
                        temp = 10;
                        Log.i(LogTag, "use water at 10 degree");
                        dialog.cancel();
                        setValues();
                    }
                });

                Button btWater20 = dialog.findViewById(R.id.reyn_water_20deg);
                btWater20.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        medium = "water";
                        temp = 20;
                        Log.i(LogTag, "use water at 20 degree");
                        dialog.cancel();
                        setValues();
                    }
                });

                Button btWater30 = dialog.findViewById(R.id.reyn_water_30deg);
                btWater30.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        medium = "water";
                        temp = 30;
                        Log.i(LogTag, "use water at 30 degree");
                        dialog.cancel();
                        setValues();
                    }
                });

                Button btWater50 = dialog.findViewById(R.id.reyn_water_50deg);
                btWater50.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        medium = "water";
                        temp = 50;
                        Log.i(LogTag, "use water at 50 degree");
                        dialog.cancel();
                        setValues();
                    }
                });

                Button btWater70 = dialog.findViewById(R.id.reyn_water_70deg);
                btWater70.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        medium = "water";
                        temp = 70;
                        Log.i(LogTag, "use water at 70 degree");
                        dialog.cancel();
                        setValues();
                    }
                });

                Button btWater90 = dialog.findViewById(R.id.reyn_water_90deg);
                btWater90.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        medium = "water";
                        temp = 90;
                        Log.i(LogTag, "use water at 90 degree");
                        dialog.cancel();
                        setValues();
                    }
                });

                Button btAir0 = dialog.findViewById(R.id.reyn_air_0deg);
                btAir0.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        medium = "air";
                        temp = 0;
                        Log.i(LogTag, "use air at 0 degree");
                        dialog.cancel();
                        setValues();
                    }
                });
                break;
        }
        dialog.show();
    }

    public void tubeHelp() {
        this.intKindOfHelp = 1;
        showHelp();
    }

    public void showUsualValues() {
        intKindOfHelp = 2;

        Log.i(LogTag, "Display dialog for usual values");

        showHelp();
    }

    public void setValues() {

        switch (medium) {
            case "water":
                switch (temp) {
                    case 10:
                        txteKinViscosity.setText("0.000001304", TextView.BufferType.EDITABLE);
                        txteDynViscosity.setText("0.001307");
                        txteDensity.setText("999.7");
                        Log.i(LogTag, "Usual values for water at 10 degree are loaded");
                        break;
                    case 20:
                        txteKinViscosity.setText("0.000001004", TextView.BufferType.EDITABLE);
                        txteDynViscosity.setText("0.001002");
                        txteDensity.setText("998.21");
                        Log.i(LogTag, "Usual values for water at 20 degree are loaded");
                        break;
                    case 30:
                        txteKinViscosity.setText("0.000000801", TextView.BufferType.EDITABLE);
                        txteDynViscosity.setText("0.0007977");
                        txteDensity.setText("995.65");
                        Log.i(LogTag, "Usual values for water at 30 degree are loaded");
                        break;
                    case 50:
                        txteKinViscosity.setText("0.000000554", TextView.BufferType.EDITABLE);
                        txteDynViscosity.setText("0.0005471");
                        txteDensity.setText("988.04");
                        Log.i(LogTag, "Usual values for water at 50 degree are loaded");
                        break;
                    case 70:
                        txteKinViscosity.setText("0.000000413", TextView.BufferType.EDITABLE);
                        txteDynViscosity.setText("0.0004041");
                        txteDensity.setText("977.77");
                        Log.i(LogTag, "Usual values for water at 70 degree are loaded");
                        break;
                    case 90:
                        txteKinViscosity.setText("0.000000326", TextView.BufferType.EDITABLE);
                        txteDynViscosity.setText("0.0003145");
                        txteDensity.setText("965.31");
                        Log.i(LogTag, "Usual values for water at 90 degree are loaded");
                        break;
                }
                break;
            case "air":
                switch (temp) {
                    case 0:
                        txteKinViscosity.setText("", TextView.BufferType.EDITABLE);
                        txteDynViscosity.setText("0.0000171");
                        txteDensity.setText("1.293");
                        Log.i(LogTag, "Usual values for air at 0 degree are loaded");
                        break;
                }
                break;
        }
    }

    public void onBackPressed() {
        Intent intent = new Intent(this, MechanicalEngineeringActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);
    }

    private void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
