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
import android.widget.Toast;

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

        listView = findViewById(R.id.lv_wheater_overview);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                Weather selectedWeather = weatherData.get(position);
                Toast.makeText(WeatherOverview.this, selectedWeather.getCityName(), Toast.LENGTH_LONG).show();
                List<WeatherForecast> wf = selectedWeather.getWeatherForecast();

                // open a dialog
                final Dialog dialog = new Dialog(WeatherOverview.this);
                dialog.setContentView(R.layout.dialog_weather_forecast);
                dialog.setTitle("Wettervorhersage");
                Button btnForecastDismiss = dialog.findViewById(R.id.btn_dia_weather_forecast_dismiss);
                btnForecastDismiss.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                        Log.i(LogTag,"Close the forecast dialog");
                    }
                });
                ListView listViewForecast = dialog.findViewById(R.id.lv_weather_forecast);
                listViewForecast.setAdapter(new WeatherForecastListAdapter(WeatherOverview.this, wf));
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
