package mfdevelopement.appsolution.activities;

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
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import mfdevelopement.appsolution.R;
import mfdevelopement.appsolution.device.general.DisplayData;
import mfdevelopement.appsolution.device.status.InternetStatus;
import mfdevelopement.appsolution.dialogs.DialogNoInternetConnection;
import mfdevelopement.appsolution.dialogs.DialogWeatherForecast;
import mfdevelopement.appsolution.listview_adapters.WeatherOverviewListAdapter;
import mfdevelopement.appsolution.models.Weather;

public class WeatherOverview extends AppCompatActivity {

    public static String FORECAST = "";
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

        InternetStatus internetStatus = new InternetStatus(this);
        if (internetStatus.isConnected()) {
            updateWeatherData();
        }
        else {
            DialogNoInternetConnection dia = new DialogNoInternetConnection(this);
            dia.show();
        }
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
        // no dialog title
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // content
        dialog.setContentView(R.layout.dialog_weather_add_city);

        ListView listViewCities = dialog.findViewById(R.id.lv_weather_cities);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, cityNames);
        listViewCities.setAdapter(adapter);

        listViewCities.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

        // adjust height of the listview
        DisplayData displayData = new DisplayData(WeatherOverview.this);
        ViewGroup.LayoutParams listViewForecastLayoutParams = listViewCities.getLayoutParams();
        listViewForecastLayoutParams.height = (int) (displayData.getHeightPx()*0.65);
        listViewCities.setLayoutParams(listViewForecastLayoutParams);

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

        final Button btnDismiss = dialog.findViewById(R.id.btn_dia_weather_add_city_dismiss);
        btnDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        // change the dialog width to 80% of the screen width
        LinearLayout layout = dialog.findViewById(R.id.lin_lay_dia_weather_add_city);
        int width = displayData.getWidthPx()*8/10;
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,LinearLayout.LayoutParams.WRAP_CONTENT,1);
        layout.setLayoutParams(params);

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
                DialogWeatherForecast dia = new DialogWeatherForecast(WeatherOverview.this, selectedWeather);
                dia.show();
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

        String prefsString = "";
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);

        // create a string
        if (!weatherData.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < weatherData.size(); i++) {
                Weather weather = weatherData.get(i);
                stringBuilder.append(weather.getCityId()).append(',');
            }

            // create a string and remove last comma
            String str = stringBuilder.toString();
            prefsString = str.substring(0,str.length()-1);
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
