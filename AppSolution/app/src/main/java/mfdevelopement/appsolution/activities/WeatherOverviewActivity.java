package mfdevelopement.appsolution.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import mfdevelopement.appsolution.R;
import mfdevelopement.appsolution.device.status.InternetStatus;
import mfdevelopement.appsolution.dialogs.DialogNoInternetConnection;
import mfdevelopement.appsolution.dialogs.DialogWeatherCities;
import mfdevelopement.appsolution.dialogs.DialogWeatherForecast;
import mfdevelopement.appsolution.dialogs.DialogWeatherOverviewRemoveCity;
import mfdevelopement.appsolution.listview_adapters.WeatherOverviewListAdapter;
import mfdevelopement.appsolution.models.City;
import mfdevelopement.appsolution.models.WeatherData;

public class WeatherOverviewActivity extends AppCompatActivity {

    public static String FORECAST = "";

    private ListView listView;
    private TextView textView;

    private List<Integer> cityCodes = new ArrayList<>();
    private List<String> cityNames = new ArrayList<>();
    private List<Integer> userCityCodes = new ArrayList<>();
    private List<WeatherData> allCitiesWeatherData = new ArrayList<>();

    private String sharedPrefsUserCityCodes = "userCityCodesString";

    private WeatherOverviewListAdapter weatherOverviewListAdapter;

    private final String LOG_TAG = "WeatherOverviewActivity";

    private boolean isCityListLoaded = false;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_overview);

        FORECAST = getString(R.string.forecast);

        // init button
        initFloatingActionButton();

        // reference to TextView and ProgressBar
        textView = findViewById(R.id.tv_weather_overview_no_city);
        progressBar = findViewById(R.id.pb_weather_overview);

        // load the cities for the current user
        userCityCodes = loadUserCities();

        initListView();

        InternetStatus internetStatus = new InternetStatus(this);
        if (internetStatus.isConnected()) {
            updateWeatherData();
        } else {
            DialogNoInternetConnection dia = new DialogNoInternetConnection(this);
            dia.show();
        }

        // Activity title will be updated after the locale has changed in Runtime
        setTitle(R.string.title_activity_weather_overview);

        // load all cities from the resource file in alphabetical order
        new initCityList(this).execute();
    }

    public void updateTextViewEmptyList() {

        if (userCityCodes.isEmpty()) {
            Log.d(LOG_TAG, "updateTextViewEmptyList: show the text and undisplay the listView");
            listView.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
            textView.setText(R.string.txt_no_city_selected);
        } else {
            Log.d(LOG_TAG, "updateTextViewEmptyList: show the listView and undisplay the text");
            listView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
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
     * create a dialog containing all available citites
     */
    private void showCitiesList() {

        final Handler handler = new Handler();
        final int delay = 100; //milliseconds

        handler.postDelayed(new Runnable() {
            public void run() {
                if (isCityListLoaded && !cityNames.isEmpty()) {
                    progressBar.setProgress(100);
                    progressBar.setVisibility(View.GONE);
                    DialogWeatherCities dialogWeatherCities = new DialogWeatherCities(WeatherOverviewActivity.this, cityNames);
                    dialogWeatherCities.show();
                } else {
                    handler.postDelayed(this, delay);
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(50);
                }
            }
        }, delay);
    }

    public void addUserCityCode(int code) {
        userCityCodes.add(code);
        updateTextViewEmptyList();
        updateWeatherData(new City(userCityCodes.get(userCityCodes.size() - 1), this));
        saveUserCities();
    }

    public int getCityCode(int index) {
        if (!cityCodes.isEmpty())
            return cityCodes.get(index);
        else
            return 0;
    }

    /**
     * inititalize the list view and set listeners
     */
    private void initListView() {

        listView = findViewById(R.id.lv_weather_overview);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(LOG_TAG, "OnItemClickListener: selected item " + position + " of weather overview list");
                WeatherData selectedWeatherData = allCitiesWeatherData.get(position);
                DialogWeatherForecast dia = new DialogWeatherForecast(WeatherOverviewActivity.this, selectedWeatherData);
                dia.show();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // remove the selected item
                Log.d(LOG_TAG, "OnItemLongClickListener: selected item " + position + " of weather overview list");
                DialogWeatherOverviewRemoveCity dia = new DialogWeatherOverviewRemoveCity(WeatherOverviewActivity.this, position);
                dia.show();
                return true;
            }
        });

        // check if TextView or ListView must to be displayed
        updateTextViewEmptyList();
    }

    public void removeCity(int position) {
        Log.d(LOG_TAG, "removeCity: removing item at position " + position + " from the listView");
        allCitiesWeatherData.remove(position);
        userCityCodes.remove(position);
        weatherOverviewListAdapter.notifyDataSetChanged();
        saveUserCities();
        updateTextViewEmptyList();
    }

    public void updateWeatherData() {
        if (userCityCodes.size() != 0) {
            List<City> cities = new ArrayList<>(userCityCodes.size());
            for (int i = 0; i < userCityCodes.size(); i++) {
                cities.add(new City(userCityCodes.get(i), this));
            }
            updateWeatherData(cities);
        }
    }

    private void updateWeatherData(City city) {
        List<City> cities = new ArrayList<>(1);
        cities.add(city);
        updateWeatherData(cities);
    }

    private void updateWeatherData(List<City> cities) {

        int numCities = cities.size();
        Log.d(LOG_TAG,"updateWeatherDate for " + numCities + " cities");

        // update weather data for every city in the list
        int progress;
        progressBar.setVisibility(View.VISIBLE);
        for (int i = 0; i < numCities; i++) {

            // update progress
            progress = (int)((double)i / numCities * 100);

            // update weather data
            new updateWeatherForCity(this).execute(cities.get(i),progress);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_weather_overview_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_update:
                updateWeatherData();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);
    }

    /**
     * save the user specific citites on the phone
     */
    private void saveUserCities() {

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
            String prefsString = str.substring(0, str.length() - 1);

            // save string using SharedPreferences
            prefs.edit().putString(sharedPrefsUserCityCodes, prefsString).apply();

            Log.d(LOG_TAG, "saveUserCitites: " + prefsString);
        } else {
            Log.d(LOG_TAG, "saveUserCities: no citiy ids to save");
        }
    }

    /**
     * load the saved user specific cities on the phone
     *
     * @return List<Integer> containing the cityIds
     */
    private List<Integer> loadUserCities() {

        Log.d(LOG_TAG,"loadUserCities() from SharedPreferences");

        List<Integer> userCityCodes = new ArrayList<>();

        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        String str = prefs.getString(sharedPrefsUserCityCodes, "");
        Log.i(LOG_TAG, "loadUserCities:" + str);

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

    public void updateListView(WeatherData weatherData) {

        Log.d(LOG_TAG,"updateListView() called");

        // check if the city is already in the listView
        int index = -1;
        for (int i = 0; i < allCitiesWeatherData.size(); i++) {
            if (allCitiesWeatherData.get(i).getCity().getId() == weatherData.getCity().getId()) {
                index = i;
                break;
            }
        }

        // update data fro city, if it's in the listView. Otherwise add it to the listView
        if (index >= 0) {
            allCitiesWeatherData.set(index, weatherData);
        } else {
            allCitiesWeatherData.add(weatherData);
            saveUserCities();
        }

        weatherOverviewListAdapter = new WeatherOverviewListAdapter(WeatherOverviewActivity.this, allCitiesWeatherData);
        listView.setAdapter(weatherOverviewListAdapter);
    }

    public void setCityListLoaded(boolean cityListLoaded) {
        this.isCityListLoaded = cityListLoaded;
    }

    public void setCityCodes(List<Integer> integerList) {
        this.cityCodes = integerList;
    }

    public void setCityNames(List<String> stringList) {
        this.cityNames = stringList;
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveUserCities();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveUserCities();
    }

    /**
     * load the weather data in the background
     */
    private static class updateWeatherForCity extends AsyncTask<Object, Integer, WeatherData> {

        private WeakReference<WeatherOverviewActivity> activityReference;
        private final String LOG_TAG = "updateWeatherForCity";

        updateWeatherForCity(WeatherOverviewActivity context) {
            activityReference = new WeakReference<>(context);
        }

        protected void onPreExecute() {
        }

        @Override
        protected WeatherData doInBackground(Object... objects) {

            City c = (City) objects[0];
            int progress = (int)objects[1];

            Log.d(LOG_TAG, "updateWeatherForCity: load weather data for city " + c.getCityName());

            publishProgress(progress);
            WeatherData wd = new WeatherData(c);
            wd.loadWeatherData();

            return wd;
        }

        protected void onProgressUpdate(Integer... values) {
            // progess in percent
            int progress = values[0];

            WeatherOverviewActivity weatherOverviewActivity = activityReference.get();
            if (weatherOverviewActivity == null || weatherOverviewActivity.isFinishing()) return;

            // show progress bar and update progess
            ProgressBar progressBar = weatherOverviewActivity.findViewById(R.id.pb_weather_overview);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(progress);
        }

        @Override
        protected void onPostExecute(WeatherData weatherData) {

            Log.i(LOG_TAG, "weather data loaded successfully");

            // return, if parent activity is not running anymore
            WeatherOverviewActivity activity = activityReference.get();
            if (activity == null || activity.isFinishing()) return;

            activity.updateListView(weatherData);

            ProgressBar progressBar = activity.findViewById(R.id.pb_weather_overview);
            progressBar.setVisibility(View.GONE);
        }
    }

    /**
     * load all citites from the resource file and add them alphabetically sorted to a list
     */
    private static class initCityList extends AsyncTask<Activity, Void, List<City>> {

        private final String LOG_TAG = "initCityList";

        private WeakReference<WeatherOverviewActivity> activityReference;

        initCityList(WeatherOverviewActivity context) {
            activityReference = new WeakReference<>(context);
        }

        @Override
        protected void onPreExecute() {
            Log.d(LOG_TAG, "initCityList:start to load all cities from the res-file");
        }

        @Override
        protected List<City> doInBackground(Activity... activities) {

            String[] cityItems = activityReference.get().getResources().getStringArray(R.array.weatherMapCities);
            List<String> citiesString = new ArrayList<>(Arrays.asList(cityItems));
            Collections.sort(citiesString, new Comparator<String>() {
                @Override
                public int compare(String text1, String text2) {
                    return text1.compareToIgnoreCase(text2);
                }
            });

            List<City> cities = new ArrayList<>();
            for (String item : citiesString)
                cities.add(City.getCityByItem(item));

            return cities;
        }

        @Override
        protected void onPostExecute(List<City> citiesList) {

            if (!citiesList.isEmpty()) {

                List<Integer> cityCodes = new ArrayList<>();
                List<String> cityNames = new ArrayList<>();
                for (City c : citiesList) {
                    cityNames.add(c.getCityName());
                    cityCodes.add(c.getId());
                }

                // get reference to activity
                WeatherOverviewActivity weatherOverviewActivity = activityReference.get();
                if (weatherOverviewActivity == null || weatherOverviewActivity.isFinishing())
                    return;

                // update lists in the WeatherOverviewActivty
                weatherOverviewActivity.setCityCodes(cityCodes);
                weatherOverviewActivity.setCityNames(cityNames);
                weatherOverviewActivity.setCityListLoaded(true);

                Log.d(LOG_TAG, "initCityList:finished loading citites");
            } else {
                Log.e(LOG_TAG, "initCityList:failed");
            }
        }
    }
}