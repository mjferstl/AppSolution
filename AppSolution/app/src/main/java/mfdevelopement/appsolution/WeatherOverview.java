package mfdevelopement.appsolution;

import android.app.Activity;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mfdevelopement.appsolution.listview_adapters.WeatherForecastListAdapter;
import mfdevelopement.appsolution.listview_adapters.WeatherOverviewListAdapter;
import mfdevelopement.appsolution.models.Weather;
import mfdevelopement.appsolution.models.WeatherForecast;

public class WeatherOverview extends AppCompatActivity {

    private ListView listView = null;
    private List<Weather> weatherData = new ArrayList<>();

    private final int[] cities = {
            2849483, // Regensburg
            //2906625, // Hemau
            //2895992, // Ingolstadt
            //2867714, // Muenchen
            //2950159  // Berlin
    };

    private String LogTag = "";
    private String appname = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_overview);

        appname = getString(R.string.app_name);
        LogTag = appname + "/WeatherOverview";

        final String FORECAST = getString(R.string.forecast);

        listView = findViewById(R.id.lv_wheater_overview);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                // get weather forecast for selected city
                Weather selectedWeather = weatherData.get(position);
                String selectedCity = selectedWeather.getCityName();
                List<WeatherForecast> wf = selectedWeather.getWeatherForecast();

                // create a new dialog containing the weather forecast data
                Log.i(LogTag,"open weather forecast dialog, city " + selectedCity);
                final Dialog dialog = new Dialog(WeatherOverview.this);
                dialog.setContentView(R.layout.dialog_weather_forecast);

                // button to close the dialog
                Button btnForecastDismiss = dialog.findViewById(R.id.btn_dia_weather_forecast_dismiss);
                btnForecastDismiss.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                        Log.i(LogTag,"ButtonClick: Close weather forecast dialog");
                    }
                });

                // fill in items of the listview
                ListView listViewForecast = dialog.findViewById(R.id.lv_weather_forecast);
                listViewForecast.setAdapter(new WeatherForecastListAdapter(WeatherOverview.this, wf));

                // set title of dialog
                TextView tv_title = dialog.findViewById(R.id.tv_dia_weather_forecast_title);
                String title = selectedCity + " - " + FORECAST;
                tv_title.setText(title);

                // show dialog
                dialog.show();

                return true;
            }
        });

        new loadWeatherData(this).execute();
    }

    private void showInfoDialog() {

    }


    private class loadWeatherData extends AsyncTask<Activity, Void, List<Weather>> {

        Activity activity;

        private loadWeatherData(Activity activity) {
            this.activity = activity;
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Weather> doInBackground(Activity ... strings) {

                for (int cityId: cities) {
                    Weather weather = new Weather(cityId);
                    weather.getCurrentWeather();
                    weather.parseWeatherForecast();
                    weatherData.add(weather);
                }
            return weatherData;
        }

        @Override
        protected void onPostExecute(List<Weather> weather) {
            //
            listView.setAdapter(new WeatherOverviewListAdapter(activity, weather));
        }
    }
}
