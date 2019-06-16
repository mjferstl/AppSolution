package mfdevelopement.appsolution.dialogs;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import mfdevelopement.appsolution.R;

public class WeatherInfoDialog extends AppCompatActivity {

    private String appname = "";
    private String LogTag = "";

    private ListView listView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_weather_forecast);

        appname = getString(R.string.app_name);
        LogTag = appname + "/WeatherForecast";

       // List<WeatherForecast> weather = getArguments().getInt("num");

        listView = findViewById(R.id.lv_weather_forecast);
        //listView.setAdapter(new WeatherForecastListAdapter(this, weather));
    }
}
