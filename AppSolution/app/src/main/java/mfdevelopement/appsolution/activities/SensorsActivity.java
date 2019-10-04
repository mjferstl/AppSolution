package mfdevelopement.appsolution.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.StepMode;
import com.androidplot.xy.XYGraphWidget;
import com.androidplot.xy.XYPlot;

import java.text.DecimalFormat;
import java.util.Locale;

import mfdevelopement.appsolution.R;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

public class SensorsActivity extends AppCompatActivity implements SensorEventListener {

    private TextView txtv_x_result, txtv_y_result, txtv_z_result;

    private static final int HISTORY_SIZE = 50;            // number of points to plot in history
    private static final String LOG_TAG = "SensorsActivity";

    private float gravityx = 0, gravityy = 0, gravityz = 0;

    private float[] m_lastMagFields = new float[3];
    private float[] m_lastAccels = new float[3];
    private float[] m_rotationMatrix = new float[16];
    private float[] m_orientation = new float[4];

    private float Pitch = 0.f;
    private float Heading = 0.f;
    private float Roll = 0.f;
    private float Declination = 0;

    private long time_old = System.currentTimeMillis() / 1000;

    private SimpleXYSeries acc_x = null;
    private SimpleXYSeries acc_y = null;
    private SimpleXYSeries acc_z = null;

    private SensorManager sensorManager;
    private Sensor mAcceleration;

    private XYPlot plot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "Activity wird gestartet");

        setContentView(R.layout.activity_sensors);

        Log.d(LOG_TAG, "setContentView() abgeschlossen");

        // get TextViews
        txtv_x_result = findViewById(R.id.txtv_sensors_acceleration_x_value);
        txtv_y_result = findViewById(R.id.txtv_sensors_acceleration_y_value);
        txtv_z_result = findViewById(R.id.txtv_sensors_acceleration_z_value);

        // init the image button for showing the coordinate system of the phone
        initButton();

        plot = findViewById(R.id.sensors_acc_plot);

        // create XYSeries for x, y, and z acceleration
        acc_x = new SimpleXYSeries("X");
        acc_x.useImplicitXVals();
        acc_y = new SimpleXYSeries("Y");
        acc_y.useImplicitXVals();
        acc_z = new SimpleXYSeries("Z");
        acc_z.useImplicitXVals();

        Log.d(LOG_TAG, "XYSeries für Beschleunigungen in x, y, und z erstellt");

        // set Boundary for x axis
        plot.setDomainBoundaries(0, HISTORY_SIZE, BoundaryMode.AUTO);

        // create LineAndPointFormatters for every XYSeries
        LineAndPointFormatter acc_x_format = new LineAndPointFormatter(this, R.xml.line_formatter_green);
        LineAndPointFormatter acc_y_format = new LineAndPointFormatter(this, R.xml.line_formatter_blue);
        LineAndPointFormatter acc_z_format = new LineAndPointFormatter(this, R.xml.line_formatter_red);

        // add XYSeries and corresponding format to the plot
        plot.addSeries(acc_x, acc_x_format);
        plot.addSeries(acc_y, acc_y_format);
        plot.addSeries(acc_z, acc_z_format);

        Log.d(LOG_TAG, "XYSeries der Beschleunigungen zum Plot hinzugefügt");

        plot.setDomainStepMode(StepMode.INCREMENT_BY_VAL);
        plot.setDomainStepValue(HISTORY_SIZE / 5);
        plot.setLinesPerRangeLabel(2);
        plot.setTitle("");

        // format axis values
        plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.LEFT).setFormat(new DecimalFormat("0.0"));
        plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).setFormat(new DecimalFormat("#"));

        plot.getLayoutManager().remove(plot.getLegend());

        Log.i(LOG_TAG, "Formatierung des Plots abgeschlossen");

        // get values from the sensors
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        try {
            mAcceleration = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            Log.i(LOG_TAG, "Beschleunigungssensor gefunden");
        } catch (NullPointerException e) {
            Log.e(LOG_TAG,getString(R.string.txt_sensors_dialog_return_to_app));
            Dialog dialog = createDialogBackToMenu();
            dialog.show();
        }
    }

    /**
     * create a Dialog for informing the user, that the device has no acceleration sensor
     * @return Dialog
     */
    private Dialog createDialogBackToMenu() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.txt_sensors_dialog_no_acc_sensor)
                .setPositiveButton(R.string.txt_sensors_dialog_return_to_app, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        // create a new Intent to start MainActivity
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                        // dismiss dialog and finish activity
                        dialog.dismiss();
                        finish();

                        // start another activity
                        startActivity(intent);
                    }
                });

        // Create the AlertDialog object and return it
        return builder.create();
    }

    @Override
    public synchronized void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

                // alpha is calculated as t / (t + dT)
                // with t, the low-pass filter's time-constant
                // and dT, the event delivery rate

                long time_new = System.currentTimeMillis() / 1000;
                long dT = time_new - time_old;

                float alpha = 0.05f / (0.05f + dT);

                Log.d(LOG_TAG, "alpha = " + alpha + ", dT = " + dT);

                time_old = time_new;

                Log.d(LOG_TAG, "neue Beschleunigungswerte vom Sensor auslesen");

                // get gravity in each direction
                gravityx = alpha * gravityx + (1 - alpha) * event.values[0];
                gravityy = alpha * gravityy + (1 - alpha) * event.values[1];
                gravityz = alpha * gravityz + (1 - alpha) * event.values[2];

                // substract gravity
                float accelerationx = event.values[0] - gravityx;
                float accelerationy = event.values[1] - gravityy;
                float accelerationz = event.values[2] - gravityz;

                Log.d(LOG_TAG, "aktuelle Werte: ax = " + accelerationx + ", ay = " + accelerationy + ", az = " + accelerationz);

                // display current values
                txtv_x_result.setText(String.format(Locale.GERMAN, "%.3f", accelerationx));
                txtv_y_result.setText(String.format(Locale.GERMAN, "%.3f", accelerationy));
                txtv_z_result.setText(String.format(Locale.GERMAN, "%.3f", accelerationz));

                updatePlot(accelerationx,accelerationy,accelerationz);

                computeOrientation();
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void updatePlot(float accX, float accY, float accZ) {
        Log.d(LOG_TAG, "aktuelle Beschleunigungen für den Live-Plot abspeichern");

        // add new values
        acc_x.addLast(null, accX);
        acc_y.addLast(null, accY);
        acc_z.addLast(null, accZ);

        // get rid the oldest sample in history:
        if (acc_x.size() > HISTORY_SIZE) {
            acc_x.removeFirst();
            acc_y.removeFirst();
            acc_z.removeFirst();
        }

        if (acc_x.size() / 5 == 0) {
            plot.setDomainStepValue(1);
        } else {
            plot.setDomainStepValue((int)acc_x.size() / 5);
        }

        adjustPlotRange();

        Log.d(LOG_TAG, "Plot aktualisieren");

        plot.redraw();
    }

    private void computeOrientation() {
        if (SensorManager.getRotationMatrix(m_rotationMatrix, null, m_lastAccels, m_lastMagFields)) {
            SensorManager.getOrientation(m_rotationMatrix, m_orientation);

            float yaw = (float) (Math.toDegrees(m_orientation[0]) + Declination);
            float pitch = (float) Math.toDegrees(m_orientation[1]);
            float roll = (float) Math.toDegrees(m_orientation[2]);
        }
    }

    /**
     * initialize the image button
     */
    private void initButton() {
        ImageButton btnCoordinateSystem = findViewById(R.id.btd_sensors_phone_coordinate_system);
        btnCoordinateSystem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPhoneAxis(v);
            }
        });
    }

    private void adjustPlotRange() {
        // find min and max value of all values to be plotted
        double minValue = 0.0, maxValue = 0.0;
        double curr_x, curr_y, curr_z;
        for (int i=0; i<acc_x.size(); i++) {

            // get values a position i of the series
            curr_x = acc_x.getY(i).doubleValue();
            curr_y = acc_y.getY(i).doubleValue();
            curr_z = acc_z.getY(i).doubleValue();

            // check x value
            if (curr_x > maxValue)
                maxValue = curr_x;
            else if (curr_x < minValue)
                minValue = curr_x;

            // check y value
            if (curr_y > maxValue)
                maxValue = curr_y;
            else if (curr_y < minValue)
                minValue = curr_y;

            // check z value
            if (curr_z > maxValue)
                maxValue = curr_z;
            else if (curr_z < minValue)
                minValue = curr_z;
        }

        // round min and max values
        Number upperBound = Math.ceil(maxValue);
        Number lowerBound = Math.floor(minValue);

        Log.d(LOG_TAG,"Adjust plot range. new upper bound: " + upperBound.toString() + ", new lower bound: " + lowerBound.toString());

        // adjust range
        plot.setRangeBoundaries(lowerBound,upperBound,BoundaryMode.FIXED);
    }

    /**
     * show a dialog containing the phone's coordinate system
     *
     * @param view
     */
    public void showPhoneAxis(View view) {

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_sensors_phone_axis);
        dialog.setTitle(R.string.title_alertDialog_Phone_Axis);
        Button btHelpDismiss = dialog.findViewById(R.id.sensors_btHelpDismiss);
        btHelpDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                Log.e("SensorsActivity", "Close the Phone-Axis Dialog");
            }
        });
        dialog.show();

    }

    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, mAcceleration, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this, mAcceleration);
    }

    public void onBackPressed() {
        sensorManager.unregisterListener(this, mAcceleration);
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        sensorManager.unregisterListener(this, mAcceleration);
        super.onDestroy();
    }
}