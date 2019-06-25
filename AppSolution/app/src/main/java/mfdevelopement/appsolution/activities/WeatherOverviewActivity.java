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
import mfdevelopement.appsolution.dialogs.DialogWeatherForecastNew;
import mfdevelopement.appsolution.listview_adapters.WeatherOverviewListAdapterNew;
import mfdevelopement.appsolution.models.City;
import mfdevelopement.appsolution.models.WeatherData;

public class WeatherOverviewActivity extends AppCompatActivity {

    public static String FORECAST = "";
    private ListView listView = null;

    private List<Integer> cityCodes = new ArrayList<>();
    private List<String> cityNames = new ArrayList<>();
    private List<Integer> userCityCodes = new ArrayList<>();
    private List<WeatherData> allCitiesWeatherData = new ArrayList<>();

    private String sharedPrefsUserCityCodes = "userCityCodesString";

    private WeatherOverviewListAdapterNew weatherOverviewListAdapter;

    private final String LOG_TAG = "WeatherOverview";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_overview);

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
     * load all citites from the resource file and add them alphabetically sorted to a list
     */
    private void initCities() {
        String[] cityItems = getResources().getStringArray(R.array.weatherMapCities);
        List<String> citiesString = new ArrayList<>(Arrays.asList(cityItems));
        Collections.sort(citiesString, new Comparator<String>()
        {
            @Override
            public int compare(String text1, String text2)
            {
                return text1.compareToIgnoreCase(text2);
            }
        });

        for (String item : citiesString) {
            City c = City.getCityByItem(item);
            //allCities.add(c);
            cityNames.add(c.getCityName());
            cityCodes.add(c.getId());
        }
    }

    /**
     * create a dialog containing all available citites
     */
    private void showCitiesList() {

        final Dialog dialog = new Dialog(WeatherOverviewActivity.this);
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
                Log.i(LOG_TAG,"addCity: " + city);
                int index = cityNames.indexOf(city);
                userCityCodes.add(cityCodes.get(index));
                dialog.dismiss();
                updateWeatherData();
                saveUserCities();
            }
        });

        // adjust height of the listview
        DisplayData displayData = new DisplayData(WeatherOverviewActivity.this);
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
                Log.d(LOG_TAG,"OnItemClickListener: selected item " + position + " of weather overview list");
                WeatherData selectedWeatherData = allCitiesWeatherData.get(position);
                DialogWeatherForecastNew dia = new DialogWeatherForecastNew(WeatherOverviewActivity.this, selectedWeatherData);
                dia.show();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // remove the selected item
                Log.d(LOG_TAG,"OnItemLongClickListener: selected item " + position + " of weather overview list");
                allCitiesWeatherData.remove(position);
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
        if (!allCitiesWeatherData.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < allCitiesWeatherData.size(); i++) {
                WeatherData wd = allCitiesWeatherData.get(i);
                stringBuilder.append(wd.getCity().getId()).append(',');
            }

            // create a string and remove last comma
            String str = stringBuilder.toString();
            prefsString = str.substring(0,str.length()-1);
            Log.i(LOG_TAG,"saveUserCitites:" + prefsString);
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
        Log.i(LOG_TAG,"loadUserCities:" + str);

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
    private class loadWeatherData extends AsyncTask<Activity, Void, List<WeatherData>> {

        Activity activity;

        private loadWeatherData(Activity activity) {
            this.activity = activity;
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<WeatherData> doInBackground(Activity ... strings) {

            // get all ids of current selected cities
            List<Integer> cityIds = getCityIds(allCitiesWeatherData);
            Log.d(LOG_TAG,allCitiesWeatherData.toString());

            // load data for selected cities
            for (int cityId : userCityCodes) {

                // add element, if it is not in the list
                Log.d(LOG_TAG,"cityId: " + cityId);
                Log.d(LOG_TAG,"booelean: " + cityIds.contains(cityId));
                if (!cityIds.contains(cityId)) {
                    WeatherData wd = new WeatherData();
                    City c = new City(cityId, WeatherOverviewActivity.this);
                    wd.setCity(c);
                    wd.loadWeatherData();
                    allCitiesWeatherData.add(wd);
                }
            }
            return allCitiesWeatherData;
        }

        @Override
        protected void onPostExecute(List<WeatherData> weatherData) {
            //
            saveUserCities();
            weatherOverviewListAdapter =  new WeatherOverviewListAdapterNew(activity, weatherData);
            listView.setAdapter(weatherOverviewListAdapter);
        }
    }

    /**
     * get the ids of all cities in a list
     * @param weatherDataList: List containing objects of class Weather
     * @return cityIds: list containing the city ids
     */
    private List<Integer> getCityIds(List<WeatherData> weatherDataList) {

        List<Integer> cityIds = new ArrayList<>(weatherDataList.size());

        for (WeatherData weatherData : weatherDataList) {
            cityIds.add(weatherData.getCity().getId());
        }

        return cityIds;
    }
}
