package mfdevelopement.appsolution.activities;

import android.app.Dialog;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.androidplot.ui.Anchor;
import com.androidplot.ui.HorizontalPositioning;
import com.androidplot.ui.VerticalPositioning;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.StepMode;
import com.androidplot.xy.XYGraphWidget;
import com.androidplot.xy.XYPlot;

import java.text.DecimalFormat;
import java.util.Locale;

import mfdevelopement.appsolution.R;

public class PhoneSensors extends AppCompatActivity implements SensorEventListener {

    private TextView txtv_x_result;
    private TextView txtv_y_result;
    private TextView txtv_z_result;

    private static int HISTORY_SIZE = 50;            // number of points to plot in history

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
    private float Declination = 0;

    private long time_old = System.currentTimeMillis()/1000;
    private double timeStep = 0.0;

    private String appname = "";
    private String LogTag = "";

    private SimpleXYSeries acc_x = null;
    private SimpleXYSeries acc_y = null;
    private SimpleXYSeries acc_z = null;

    private SensorManager sensorManager;
    private Sensor mAcceleration;
    private Sensor mOrientation;

    private XYPlot plot;

    private ImageButton btnImgPhoneAxis = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensors);

        appname = getString(R.string.app_name);
        LogTag = appname + "/PhoneSensors";

        Log.i(LogTag,"Activity startet successfully");

        loadAccSensor();
    }

    public void loadAccSensor() {

        txtv_x_result = findViewById(R.id.txtv_sensors_acceleration_x_value);
        txtv_y_result = findViewById(R.id.txtv_sensors_acceleration_y_value);
        txtv_z_result = findViewById(R.id.txtv_sensors_acceleration_z_value);

        btnImgPhoneAxis = findViewById(R.id.btd_sensors_phone_coordinate_system);
        btnImgPhoneAxis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPhoneAxis();
            }
        });

        plot = findViewById(R.id.sensors_acc_plot);

        acc_x = new SimpleXYSeries("X");
        acc_x.useImplicitXVals();
        acc_y = new SimpleXYSeries("Y");
        acc_y.useImplicitXVals();
        acc_z = new SimpleXYSeries("Z");
        acc_z.useImplicitXVals();

        Log.i(LogTag,"XYSeries for acceleration values created");

        //plot.setRangeBoundaries(0, 5, BoundaryMode.FIXED);
        plot.setDomainBoundaries(0,HISTORY_SIZE, BoundaryMode.AUTO);

        Log.i(LogTag,"PlotBoundaries are set");

        LineAndPointFormatter acc_x_format = new LineAndPointFormatter(this, R.xml.line_point_formatter_with_labels);
        LineAndPointFormatter acc_y_format = new LineAndPointFormatter(this, R.xml.line_point_formatter_with_labels_2);
        LineAndPointFormatter acc_z_format = new LineAndPointFormatter(this, R.xml.line_point_formatter_with_labels_3);

        plot.addSeries(acc_x, acc_x_format);
        plot.addSeries(acc_y, acc_y_format);
        plot.addSeries(acc_z, acc_z_format);

        plot.setDomainStepMode(StepMode.INCREMENT_BY_VAL);
        plot.setDomainStepValue(HISTORY_SIZE/5);
        //plot.setRangeStepValue(2);
        plot.setLinesPerRangeLabel(2);
        plot.setTitle("");

        plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.LEFT).setFormat(new DecimalFormat("0.0"));
        plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).setFormat(new DecimalFormat("#"));

        plot.getLayoutManager().remove(plot.getLegend());

        // get values from the sensors
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAcceleration = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public synchronized void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()){
            case Sensor.TYPE_ACCELEROMETER:

                // alpha is calculated as t / (t + dT)
                // with t, the low-pass filter's time-constant
                // and dT, the event delivery rate

                long time_new = System.currentTimeMillis()/1000;
                long dT = time_new - time_old;

                alpha = 0.05f/(0.05f + dT);

                Log.i(LogTag,"alpha = " + alpha + ", dT = " + dT);

                time_old = time_new;

                Log.i(LogTag,"read updated acceleration values");

                gravityx = alpha * gravityx + (1 - alpha) * event.values[0];
                gravityy = alpha * gravityy + (1 - alpha) * event.values[1];
                gravityz = alpha * gravityz + (1 - alpha) * event.values[2];

                Log.i(LogTag,"calculate accelerations (no gravity)");

                accelerationx = event.values[0] - gravityx;
                accelerationy = event.values[1] - gravityy;
                accelerationz = event.values[2] - gravityz;

                Log.i(LogTag,"updated values: ax = " + accelerationx + ", ay = " + accelerationy + ", az = " + accelerationz);

                txtv_x_result.setText(String.format(Locale.GERMAN,"%.3f",accelerationx));
                txtv_y_result.setText(String.format(Locale.GERMAN,"%.3f",accelerationy));
                txtv_z_result.setText(String.format(Locale.GERMAN,"%.3f",accelerationz));

                updatePlot();

                computeOrientation();

                break;
        }
    }

    private void computeOrientation() {
        if (SensorManager.getRotationMatrix(m_rotationMatrix, null, m_lastAccels, m_lastMagFields)) {
            SensorManager.getOrientation(m_rotationMatrix, m_orientation);

            float yaw = (float) (Math.toDegrees(m_orientation[0]) + Declination);
            float pitch = (float) Math.toDegrees(m_orientation[1]);
            float roll = (float) Math.toDegrees(m_orientation[2]);
        }
    }

    private void updatePlot(){

        acc_x.addLast(null, accelerationx);
        acc_y.addLast(null, accelerationy);
        acc_z.addLast(null, accelerationz);

        // get rid the oldest sample in history:
        if (acc_x.size() > HISTORY_SIZE) {
            acc_x.removeFirst();
            acc_y.removeFirst();
            acc_z.removeFirst();
        }

        if (acc_x.size()/5 == 0) {
            plot.setDomainStepValue(1);
        }
        else {
            plot.setDomainStepValue((acc_x.size()/5));
        }

        Log.i(LogTag,"update plot");

        // set plot properties
        plot.getDomainTitle().position(0, HorizontalPositioning.RELATIVE_TO_RIGHT,0, VerticalPositioning.RELATIVE_TO_BOTTOM, Anchor.RIGHT_BOTTOM);
        plot.getTitle().position(0,HorizontalPositioning.RELATIVE_TO_RIGHT,0,VerticalPositioning.RELATIVE_TO_TOP,Anchor.RIGHT_TOP);
        plot.getRangeTitle().position(0,HorizontalPositioning.RELATIVE_TO_LEFT,0,VerticalPositioning.RELATIVE_TO_CENTER,Anchor.LEFT_MIDDLE);

        plot.redraw();
    }

    private void showPhoneAxis() {

        final Dialog dialog = new Dialog(this);

        dialog.setContentView(R.layout.dialog_sensors_phone_axis);
        //dialog.setTitle("phone axes");
        Button btHelpDismiss = dialog.findViewById(R.id.sensors_btHelpDismiss);
        btHelpDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_phone_sensors, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
