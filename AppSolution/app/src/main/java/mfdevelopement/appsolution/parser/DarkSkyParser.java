package mfdevelopement.appsolution.parser;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import mfdevelopement.appsolution.models.City;
import mfdevelopement.appsolution.models.WeatherItem;

public class DarkSkyParser {

    private static final String URL_PART_1 = "https://api.darksky.net/forecast/";
    private static final String DARK_SKY_KEY = "ee9e68f241a728afe8a9747fe657042e";
    private static final String DARK_SKY_RESPONSE_SETTINGS = "?units=si&lang=de";

    private static final String LOG_TAG = "DarkSkyParser";

    private String jsonResponse = "";

    public static final String UNIT_WIND_SPEED = "m/s";
    public static final String UNIT_TEMPERATURE = "°C";

    private City city;

    public DarkSkyParser(City city){
        this.city = city;
        this.jsonResponse = fetchJsonWeather();
    }

    public String fetchJsonWeather() {

        if (this.city == null) {return "";}

        // create url containing the cityId
        String url = URL_PART_1 + DARK_SKY_KEY + "/" + this.city.getLatitude() + "," + this.city.getLongitude() + DARK_SKY_RESPONSE_SETTINGS;
        Log.d(LOG_TAG,"fetchJsonWeather:url="+url);

        // get the response in json format
        JsonParser jsonParser = new JsonParser();
        String response;
        try {
            response = jsonParser.parseJsonFromUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(LOG_TAG,"IOException when parsing json response");
            response = "";
        }

        return response;
    }

    public static WeatherItem parseWeather(City city) {

        WeatherItem weatherItem = new WeatherItem();

        return weatherItem;
    }

    private WeatherItem loadCurrentWeather() {

        Log.d(LOG_TAG,"getCurrentWeather:started");

        WeatherItem weatherCurrently = new WeatherItem();

        try {
            if (this.jsonResponse.equals("")) {
                Log.d(LOG_TAG,"getCurrentWeather:JSON response gets loaded");
                this.jsonResponse =  fetchJsonWeather();
            }
            if (city.getId() != 0) {
                // create JSONObject from the response
                JSONObject jsonObject = new JSONObject(this.jsonResponse);
                JSONObject jsonWeatherCurrently = jsonObject.getJSONObject("currently");
                weatherCurrently = parseJsonWeather(jsonWeatherCurrently);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(LOG_TAG,"getCurrentWeather: a error occurred during execution of the method");
        }

        return weatherCurrently;
    }

    private List<WeatherItem> loadHourlyWeather() {

        List<WeatherItem> weatherItemList = new ArrayList<>();

        try {
            if (this.jsonResponse.equals("")) {
                Log.d(LOG_TAG,"getHourlyWeather:JSON response gets loaded");
                this.jsonResponse =  fetchJsonWeather();
            }
            if (city.getId() != 0) {
                // create JSONObject from the response
                JSONObject jsonObject = new JSONObject(jsonResponse);
                JSONArray jsonWeatherHourly = jsonObject.getJSONObject("hourly").getJSONArray("data");
                for (int i=0; i<jsonWeatherHourly.length(); i++) {
                    WeatherItem weatherItem = parseJsonWeather(jsonWeatherHourly.getJSONObject(i));
                    weatherItemList.add(weatherItem);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(LOG_TAG,"getHourlyWeather: a error occurred during execution of the method");
        }

        return weatherItemList;
    }

    private List<WeatherItem> loadDailyWeather() {

        List<WeatherItem> weatherItemList = new ArrayList<>();

        try {
            if (this.jsonResponse.equals("")) {
                Log.d(LOG_TAG,"getDailyWeather:JSON response gets loaded");
                this.jsonResponse =  fetchJsonWeather();
            }
            if (city.getId() != 0) {
                // create JSONObject from the response
                JSONObject jsonObject = new JSONObject(this.jsonResponse);
                JSONArray jsonWeatherHourly = jsonObject.getJSONObject("daily").getJSONArray("data");
                for (int i=0; i<jsonWeatherHourly.length(); i++) {
                    WeatherItem weatherItem = parseJsonForecastDaily(jsonWeatherHourly.getJSONObject(i));
                    weatherItemList.add(weatherItem);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(LOG_TAG,"getDailyWeather: a error occurred during execution of the method");
        }

        return weatherItemList;
    }

    private static WeatherItem parseJsonWeather(JSONObject weather) {

        WeatherItem weatherItem = new WeatherItem();

        long timestamp = Long.valueOf(getJsonValueAsString(weather,"time"));
        weatherItem.setTimestamp(timestamp);

        String summary = getJsonValueAsString(weather,"summary");
        weatherItem.setSummary(summary);

        String iconName = getJsonValueAsString(weather,"icon");
        weatherItem.setIconByName(iconName);

        double precipProbability = Double.valueOf(getJsonValueAsString(weather,"precipProbability"));
        int precipProbabilityPercent = (int) (precipProbability*100);
        weatherItem.setPrecipProbabilityPercent(precipProbabilityPercent);

        double temperature = Double.valueOf(getJsonValueAsString(weather,"temperature"));
        weatherItem.setTemperature(temperature);

        double windSpeed = Double.valueOf(getJsonValueAsString(weather,"windSpeed"));
        weatherItem.setWindSpeed(windSpeed);

        double humidity = Double.valueOf(getJsonValueAsString(weather,"humidity"));
        weatherItem.setHumidity(humidity);

        return weatherItem;
    }

    private static WeatherItem parseJsonForecastDaily(JSONObject weather) {

        WeatherItem weatherItem = new WeatherItem();

        long timestamp = Long.valueOf(getJsonValueAsString(weather,"time"));

        // create time zone object
        TimeZone timezone = TimeZone.getDefault();
        // checking offset value for date
        int dt = timezone.getOffset(Calendar.ZONE_OFFSET);

        int year = DateTimeParser.getYear(timestamp)-1900;
        int month = DateTimeParser.getMoth(timestamp)-1;
        int day = DateTimeParser.getDay(timestamp);
        if (dt < 0) {day = day-1; dt = 24-dt;}
        if (day < 0) {month = month-1;}
        Timestamp ts = new Timestamp(year, month, day, dt, 0, 0, 0);

        Log.d(LOG_TAG,"parseJsonForecastDaily:timeOffset = " + dt);
        //long ts = timestamp + dt/1000;
        weatherItem.setTimestamp(ts.getTime()/1000);
        Log.d(LOG_TAG,"parseJsonForecastDaily:set timestamp: " + ts);

        String summary = getJsonValueAsString(weather,"summary");
        weatherItem.setSummary(summary);

        String iconName = getJsonValueAsString(weather,"icon");
        weatherItem.setIconByName(iconName);

        double precipProbability = Double.valueOf(getJsonValueAsString(weather,"precipProbability"));
        int precipProbabilityPercent = (int) (precipProbability*100);
        weatherItem.setPrecipProbabilityPercent(precipProbabilityPercent);

        double temperatureHigh = Double.valueOf(getJsonValueAsString(weather,"temperatureHigh"));
        double temperatureLow = Double.valueOf(getJsonValueAsString(weather,"temperatureLow"));
        weatherItem.setTemperatureHigh(temperatureHigh);
        weatherItem.setTemperatureLow(temperatureLow);

        double windSpeed = Double.valueOf(getJsonValueAsString(weather,"windSpeed"));
        weatherItem.setWindSpeed(windSpeed);

        double humidity = Double.valueOf(getJsonValueAsString(weather,"humidity"));
        weatherItem.setHumidity(humidity);

        return weatherItem;
    }

    private static String getJsonValueAsString(JSONObject jsonObject, String string) {
        try {
            return jsonObject.get(string).toString();
        } catch (JSONException e) {
            Log.e(LOG_TAG,jsonObject.toString() + "could not be converted to string");
            return "";
        }
    }

    public WeatherItem getCurrentWeather() {
        return loadCurrentWeather();
    }

    public List<WeatherItem> getHourlyForecast() {
        return loadHourlyWeather();
    }

    public List<WeatherItem> getDailyForecast() {
        return loadDailyWeather();
    }
}
