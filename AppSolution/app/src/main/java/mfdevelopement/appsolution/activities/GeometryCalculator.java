package mfdevelopement.appsolution.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import mfdevelopement.appsolution.R;

public class GeometryCalculator extends AppCompatActivity {

    /**
     * Konstanten
     */
    //Strings
    private String REC_AREA = "";
    private String CIRCLE_AREA = "";
    private String OK_BUTTON = "";
    private String APPROX = "";
    private String SET_DIAMETER = "";
    private String SET_LENGTH = "";
    private String SET_WIDTH = "";
    private String E = "e";

    //Integer
    private Integer exponent = 0;

    //BigDecimal
    private static final BigDecimal pi = new BigDecimal(Math.PI);
    private static final BigDecimal thousand = new BigDecimal(1000);
    private static final BigDecimal four = new BigDecimal(4);
    private BigDecimal bdDiameter = null;


    /**
     * Deklaration und Initialisierung der Widgets
     */
    //Buttons
    private Button btdRecCalc = null;
    private Button btdCircCalc = null;
    private Button btdRecHelp = null;
    private Button CircHelp = null;

    //TextViews
    private TextView txtvResult = null;

    //EditText
    private EditText txteDiameter = null;
    private EditText txteLength = null;
    private EditText txteWidth = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("Activity","started geometry calculator");
        setContentView(R.layout.activity_geometry_calculator);

        Log.e("Activity","started Geometrie-Rechner");

        REC_AREA = getString(R.string.maths_rectangle_def);
        CIRCLE_AREA = getString(R.string.maths_circle_def);
        OK_BUTTON = getString(R.string.btd_ok);
        APPROX = getString(R.string.maths_approx);
        SET_DIAMETER = getString(R.string.maths_enter_diameter);
        SET_LENGTH = getString(R.string.maths_enter_length);
        SET_WIDTH = getString(R.string.maths_enter_width);

        btdRecCalc = findViewById(R.id.btd_maths_rectangle_calc);
        btdRecCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Button","calculate rectangular area");
                calcRectangle();
            }
        });

        btdCircCalc = findViewById(R.id.btd_maths_circle_calc);
        btdCircCalc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.i("Button","calculate circular area");
                calcCircle();
            }
        });
    }

    public void calcCircle()
    {
        txtvResult = findViewById(R.id.txtv_maths_circle_result_value);
        txtvResult.setText("");
        txteDiameter = findViewById(R.id.txte_maths_circle_diameter);
        if (txteDiameter.getText().toString().equals("")) {
            Toast.makeText(GeometryCalculator.this, SET_DIAMETER, Toast.LENGTH_SHORT).show();
        }
        else {
            bdDiameter = new BigDecimal(txteDiameter.getText().toString() );

            BigDecimal area = (pi.divide(four, 9, RoundingMode.HALF_UP)).multiply(bdDiameter.multiply(bdDiameter));

            while (area.compareTo(thousand) > 0)
            {
                // To get the right exponent for displaying later
                exponent = exponent + 3;

                // Divide with 1000, to set it *10^(-3)
                area = area.divide(thousand, 9, RoundingMode.HALF_UP);
            }
            area = area.setScale(6, BigDecimal.ROUND_HALF_UP);

            if (exponent == 0)
            {
                txtvResult.setText(APPROX + area.toString());
            }
            else {
                txtvResult.setText(APPROX + area.toString() + E + exponent);
            }
        }
    }

    public void calcRectangle() {
        txtvResult = findViewById(R.id.txtv_maths_rectangle_result_value);
        txtvResult.setText("");
        BigDecimal length;
        BigDecimal width;
        txteLength = findViewById(R.id.txte_maths_rectangle_width);
        txteWidth = findViewById(R.id.txte_maths_rectangle_height);

        // Display Toast if EditText of length is empty
        if (txteLength.getText().toString().equals("")) {
            Toast.makeText(GeometryCalculator.this, SET_LENGTH, Toast.LENGTH_SHORT).show();
        }

        // go ahead if length is not empty
        else {
            length = new BigDecimal(txteLength.getText().toString());

            // Display Toast when EditText of width is empty
            if (txteWidth.getText().toString().equals("")) {
                Toast.makeText(GeometryCalculator.this, SET_WIDTH, Toast.LENGTH_SHORT).show();
            }

            // else calculate area of rectangle
            else {
                width = new BigDecimal(txteWidth.getText().toString());
                BigDecimal area = length.multiply(width);

                while (area.compareTo(thousand) > 0) {
                    // To get the right exponent for displaying later
                    exponent = exponent + 3;

                    // Divide with 1000, to set it *10^(-3)
                    area = area.divide(thousand, 9, RoundingMode.HALF_UP);
                }

                area = area.setScale(6, BigDecimal.ROUND_HALF_UP);

                MathContext mc = new MathContext(9); // 9 precision
                BigDecimal areaNew = new BigDecimal(area.doubleValue(), mc);
                String areaString = areaNew.toString();

                if (area.toString().equals(areaString))
                {
                    if (exponent == 0) {
                        txtvResult.setText(areaString);
                    } else {
                        txtvResult.setText(areaString + E + exponent);
                    }
                }
                else
                {
                    if (exponent == 0) {
                        txtvResult.setText(APPROX + areaString);
                    } else {
                        txtvResult.setText(APPROX + areaString + E + exponent);
                    }
                }
            }
        }
    }

    public void onBackPressed(){
        Intent intent = new Intent(this, Main.class);
        finish();
        startActivity(intent);
    }
}
