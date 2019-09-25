package mfdevelopement.appsolution.activities;

import android.app.Dialog;
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

import com.androidplot.util.Redrawer;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.StepMode;
import com.androidplot.xy.XYGraphWidget;
import com.androidplot.xy.XYPlot;

import java.text.DecimalFormat;
import java.util.Locale;

import mfdevelopement.appsolution.R;

public class SensorsActivity extends AppCompatActivity implements SensorEventListener {

    private TextView txtv_x_result;
    private TextView txtv_y_result;
    private TextView txtv_z_result;

    private static final int HISTORY_SIZE = 50;            // number of points to plot in history

    private float accelerationx = 0;
    private float accelerationy = 0;
    private float accelerationz = 0;
    private float gravityx = 0;
    private float gravityy = 0;
    private float gravityz = 0;
    private float alpha = 1.0f;

    float[] m_lastMagFields = new float[3];

    float[] m_lastAccels = new float[3];

    private float[] m_rotationMatrix = new float[16];
    private float[] m_orientation = new float[4];

    public float Pitch = 0.f;
    public float Heading = 0.f;
    public float Roll = 0.f;
    public float Declination = 0;

    private long time_old = System.currentTimeMillis() / 1000;
    private double timeStep = 0.0;

    private SimpleXYSeries acc_x = null;
    private SimpleXYSeries acc_y = null;
    private SimpleXYSeries acc_z = null;

    private SensorManager sensorManager;
    private Sensor mAcceleration;
    private Sensor mOrientation;

    private XYPlot plot;

    private Redrawer redrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensors);

        Log.e("SensorsActivity", "Activity gestartet");

        txtv_x_result = findViewById(R.id.txtv_sensors_acceleration_x_value);
        txtv_y_result = findViewById(R.id.txtv_sensors_acceleration_y_value);
        txtv_z_result = findViewById(R.id.txtv_sensors_acceleration_z_value);

        // init the image button for showing the coordinate system of the phone
        initButton();

        plot = findViewById(R.id.sensors_acc_plot);

        acc_x = new SimpleXYSeries("X");
        acc_x.useImplicitXVals();
        acc_y = new SimpleXYSeries("Y");
        acc_y.useImplicitXVals();
        acc_z = new SimpleXYSeries("Z");
        acc_z.useImplicitXVals();

        Log.i("SensorsActivity", "XYSeries für Beschleunigungen erstellt");

        //plot.setRangeBoundaries(-5, 5, BoundaryMode.FIXED);
        plot.setDomainBoundaries(0, HISTORY_SIZE, BoundaryMode.AUTO);

        LineAndPointFormatter acc_x_format = new LineAndPointFormatter(this, R.xml.line_point_formatter_with_labels);
        LineAndPointFormatter acc_y_format = new LineAndPointFormatter(this, R.xml.line_point_formatter_with_labels_2);
        LineAndPointFormatter acc_z_format = new LineAndPointFormatter(this, R.xml.line_point_formatter_with_labels_3);

        Log.i("SensorsActivity", "Formate der Graphen im Plot festgelegt");

        plot.addSeries(acc_x, acc_x_format);
        plot.addSeries(acc_y, acc_y_format);
        plot.addSeries(acc_z, acc_z_format);

        Log.i("SensorsActivity", "XYSeries der Beschleunigungen zum Plot hinzugefügt");

        plot.setDomainStepMode(StepMode.INCREMENT_BY_VAL);
        plot.setDomainStepValue(HISTORY_SIZE / 5);
        //plot.setRangeStepValue(2);
        plot.setLinesPerRangeLabel(2);
        plot.setTitle("");

        plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.LEFT).setFormat(new DecimalFormat("0.0"));
        plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).setFormat(new DecimalFormat("#"));

        plot.getLayoutManager().remove(plot.getLegend());

        Log.i("SensorsActivity", "Formatierung des Plots abgeschlossen");

        // get values from the sensors
        Log.i("SensorsActivity", "Sensoren für Beschleunigung und Schwerkraft laden");
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAcceleration = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    public synchronized void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:

                // alpha is calculated as t / (t + dT)
                // with t, the low-pass filter's time-constant
                // and dT, the event delivery rate

                long time_new = System.currentTimeMillis() / 1000;
                long dT = time_new - time_old;

                alpha = 0.05f / (0.05f + dT);

                Log.i("SensorsActivity", "alpha = " + alpha + ", dT = " + dT);

                time_old = time_new;

                Log.i("SensorsActivity", "neue Beschleunigungswerte vom Sensor auslesen");

                gravityx = alpha * gravityx + (1 - alpha) * event.values[0];
                gravityy = alpha * gravityy + (1 - alpha) * event.values[1];
                gravityz = alpha * gravityz + (1 - alpha) * event.values[2];

                Log.i("SensorsActivity", "Beschleunigungen (ohne Schwerkraft) werden neu berechnet");

                accelerationx = event.values[0] - gravityx;
                accelerationy = event.values[1] - gravityy;
                accelerationz = event.values[2] - gravityz;

                Log.e("SensorsActivity", "aktuelle Werte: ax = " + accelerationx + ", ay = " + accelerationy + ", az = " + accelerationz);
                Log.i("SensorsActivity", "aktuelle Beschleunigungen in Tabelle anzeigen");

                txtv_x_result.setText(String.format(Locale.GERMAN, "%.3f", accelerationx));
                txtv_y_result.setText(String.format(Locale.GERMAN, "%.3f", accelerationy));
                txtv_z_result.setText(String.format(Locale.GERMAN, "%.3f", accelerationz));

                updatePlot();

                computeOrientation();

                break;
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void updatePlot() {
        Log.i("SensorsActivity", "aktuelle Beschleunigungen für den Live-Plot abspeichern");

        acc_x.addLast(null, accelerationx);
        acc_y.addLast(null, accelerationy);
        acc_z.addLast(null, accelerationz);

        Log.i("SensorsActivity", "alte Werte löschen, damit max. " + HISTORY_SIZE + " Werte angezeigt werden");

        // get rid the oldest sample in history:
        if (acc_x.size() > HISTORY_SIZE) {
            acc_x.removeFirst();
            acc_y.removeFirst();
            acc_z.removeFirst();
        }
        Log.i("SensorsActivity", "XYseries enthält " + acc_x.size() + " Werte");

        if (acc_x.size() / 5 == 0) {
            plot.setDomainStepValue(1);
        } else {
            plot.setDomainStepValue((acc_x.size() / 5));
        }

        Log.i("SensorsActivity", "Plot aktualisieren");

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
        //sensorManager = null;
        //redrawer.start();
    }

    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this, mAcceleration);
        //sensorManager = null;
        //redrawer.pause();
    }

    public void onBackPressed() {
        sensorManager.unregisterListener(this, mAcceleration);
        //sensorManager = null;
        //redrawer.finish();
        Intent intent = new Intent(this, MainActivity.class);
        finish();
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        sensorManager.unregisterListener(this, mAcceleration);
        //redrawer.finish();
        super.onDestroy();
    }
}