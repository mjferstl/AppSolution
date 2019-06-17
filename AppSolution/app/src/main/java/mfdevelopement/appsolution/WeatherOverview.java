package mfdevelopement.appsolution;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import mfdevelopement.appsolution.device.general.DisplayData;
import mfdevelopement.appsolution.listview_adapters.WeatherForecastListAdapter;
import mfdevelopement.appsolution.listview_adapters.WeatherOverviewListAdapter;
import mfdevelopement.appsolution.models.Weather;
import mfdevelopement.appsolution.models.WeatherForecast;

public class WeatherOverview extends AppCompatActivity {

    private String FORECAST;
    private ListView listView = null;
    private List<Weather> weatherData = new ArrayList<>();

    private List<Integer> cityCodes = new ArrayList<>();
    private List<String> cityNames = new ArrayList<>();
    private List<Integer> userCityCodes = new ArrayList<>();

    private String LogTag = "";
    private String appname = "";
    private String sharedPrefsUserCityCodes = "userCityCodesString";

    private WeatherOverviewListAdapter weatherOverviewListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_overview);

        appname = getString(R.string.app_name);
        LogTag = appname + "/WeatherOverview";
        FORECAST = getString(R.string.forecast);

        // init button
        initFloatingActionButton();

        // load all cities from the resource file in alphabetical order
        initCities();

        // load the cities for the current user
        userCityCodes = loadUserCities();

        initListView();

        updateWeatherData();
    }

    /**
     * show a dialog containing the weather forecast data in a listview
     * @param weather: Object of class weather containing weather data
     */
    private void showForecastDialog(Weather weather) {

        //TODO only show dialog, if weather forecast data is loaded

        // get weather forecast of the selected city
        String selectedCity = weather.getCityName();
        List<WeatherForecast> wf = weather.getWeatherForecast();

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

        // change the dialog width to 80% of the screen width
        LinearLayout layout = dialog.findViewById(R.id.lin_lay_dia_weather_forecast);
        DisplayData displayData = new DisplayData(WeatherOverview.this);
        int width = displayData.getWidthPx()*8/10;
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,LinearLayout.LayoutParams.WRAP_CONTENT,1);
        layout.setLayoutParams(params);

        // show dialog
        dialog.show();
    }

    /**
     * initialize the floating action button for adding new cities to the list view
     */
    private void initFloatingActionButton() {
        FloatingActionButton fab = findViewById(R.id.btn_float_weather_overview_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCitiesList();
            }
        });
    }

    /**
     * load citites from the resource file and put them in alphabetical order in the list
     */
    private void initCities() {
        String[] owmCityCodes = getResources().getStringArray(R.array.owmCityCodex);
        List<String> owmCityCodesList = new ArrayList<>(Arrays.asList(owmCityCodes));
        Collections.sort(owmCityCodesList, new Comparator<String>()
        {
            @Override
            public int compare(String text1, String text2)
            {
                return text1.compareToIgnoreCase(text2);
            }
        });

        // separate city name and city code and save it to different lists
        String lastCity = "";
        for(int i=0; i<owmCityCodesList.size(); i++) {
            String[] cityCodePair = owmCityCodesList.get(i).split(":");

            String cityName = cityCodePair[0].trim();
            int cityCode = Integer.valueOf(cityCodePair[1].trim());

            // remove if duplicate
            if (cityName.equals(lastCity)) {
                owmCityCodesList.remove(i);
                i--;
                continue;
            }

            cityNames.add(cityName);
            cityCodes.add(cityCode);

            lastCity = cityName;
        }
    }

    /**
     * create a dialog containing all available citites
     */
    private void showCitiesList() {

        final Dialog dialog = new Dialog(WeatherOverview.this);
        dialog.setContentView(R.layout.dialog_weather_add_city);

        ListView listView = dialog.findViewById(R.id.lv_weather_cities);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, cityNames);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String city = adapter.getItem(position);
                Log.i(LogTag,"addCity: " + city);
                int index = cityNames.indexOf(city);
                userCityCodes.add(cityCodes.get(index));
                dialog.dismiss();
                updateWeatherData();
                saveUserCities();
            }
        });

        final EditText etFilter = dialog.findViewById(R.id.et_dia_weather_add_city);
        etFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                adapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // show the dialog
        dialog.show();
    }

    /**
     * inititalize the list view and set listeners
     */
    private void initListView() {

        listView = findViewById(R.id.lv_wheater_overview);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Weather selectedWeather = weatherData.get(position);
                showForecastDialog(selectedWeather);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // remove the selected item
                Log.i(LogTag,"removeListItem: removing item " + position + " of weather overview list");
                weatherData.remove(position);
                weatherOverviewListAdapter.notifyDataSetChanged();
                saveUserCities();
                return true;
            }
        });
    }

    private void updateWeatherData() {
        new loadWeatherData(this).execute();
    }

    /**
     * save the user specific citites on the phone
     */
    private void saveUserCities() {

        //TODO save the data to SharedPreferences as String and parse it back on start

        String prefsString;
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);

        // if there are no user specific values, then save an empty string
        if (weatherData.isEmpty()) {
            prefsString = "";
        }
        else {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < weatherData.size(); i++) {
                Weather weather = weatherData.get(i);
                stringBuilder.append(weather.getCityId()).append(',');
            }

            // create a string and remove last comma
            prefsString = stringBuilder.toString();
            prefsString = prefsString.substring(0,prefsString.length()-1);
            Log.i(LogTag,"saveUserCitites:" + prefsString);
        }

        // save string using SharedPreferences
        prefs.edit().putString(sharedPrefsUserCityCodes, prefsString).apply();
    }

    /**
     * load the saved user specific cities on the phone
     * @return List<Integer> containing the cityIds
     */
    private List<Integer> loadUserCities() {

        List<Integer> userCityCodes = new ArrayList<>();

        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        String str = prefs.getString(sharedPrefsUserCityCodes,"");
        Log.i(LogTag,"loadUserCities:" + str);

        if (!str.equals("")) {
            String[] values = str.split(",");
            for (String v : values) {
                if (!v.isEmpty()) {
                    userCityCodes.add(Integer.valueOf(v.trim()));
                }
            }
        }
        return userCityCodes;
    }


    /**
     * load the weather data in the background
     */
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

            // get all ids of current selected cities
            List<Integer> cityIds = getCityIds(weatherData);

            // load data for selected cities
            for (int cityId : userCityCodes) {

                // add element, if it is not in the list
                if (!cityIds.contains(cityId)) {
                    Weather weather = new Weather(cityId);
                    weather.getWeatherData();
                    weather.getWeatherForecastData();
                    weatherData.add(weather);
                }
            }
            return weatherData;
        }

        @Override
        protected void onPostExecute(List<Weather> weather) {
            //
            saveUserCities();
            weatherOverviewListAdapter =  new WeatherOverviewListAdapter(activity, weather);
            listView.setAdapter(weatherOverviewListAdapter);
        }
    }

    /**
     * get the ids of all cities in a list
     * @param weatherList: List containing objects of class Weather
     * @return cityIds: list containing the city ids
     */
    private List<Integer> getCityIds(List<Weather> weatherList) {

        List<Integer> cityIds = new ArrayList<>(weatherList.size());
        for (Weather weather : weatherList) {
            cityIds.add(weather.getCityId());
        }
        return cityIds;
    }
}
