package mfdevelopement.appsolution.parser;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import mfdevelopement.appsolution.models.City;
import mfdevelopement.appsolution.models.WeatherItem;

public class DarkSkyParser {

    private static final String URL_PART_1 = "https://api.darksky.net/forecast/";
    private static final String DARK_SKY_KEY = "ee9e68f241a728afe8a9747fe657042e";
    private static final String DARK_SKY_RESPONSE_SETTINGS = "?units=si&lang=de";

    private static final String LOG_TAG = "DarkSkyParser";

    private static String jsonResponse = "";

    public static final String UNIT_WIND_SPEED = "m/s";
    public static final String UNIT_TEMPERATURE = "°C";

    public DarkSkyParser(City city){
        jsonResponse = fetchJsonWeather(city);
    }

    private static String fetchJsonWeather(City city) {

        // create url containing the cityId
        String url = URL_PART_1 + DARK_SKY_KEY + "/" + city.getLatitude() + "," + city.getLongitude() + DARK_SKY_RESPONSE_SETTINGS;
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

    public static WeatherItem getCurrentWeather(City city) {

        Log.d(LOG_TAG,"getCurrentWeather:started");

        WeatherItem weatherCurrently = new WeatherItem();

        try {
            if (jsonResponse.equals("")) {
                Log.d(LOG_TAG,"getCurrentWeather:JSON response gets loaded");
                jsonResponse =  fetchJsonWeather(city);
            }
            if (city.getId() != 0) {
                // create JSONObject from the response
                Log.d(LOG_TAG,"getCurrentWeather:jsonResponse = " + jsonResponse);
                JSONObject jsonObject = new JSONObject(jsonResponse);
                JSONObject jsonWeatherCurrently = jsonObject.getJSONObject("currently");
                weatherCurrently = parseJsonWeather(jsonWeatherCurrently);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(LOG_TAG,"parseWeather: a error occurred during execution of the method");
        }

        return weatherCurrently;
    }

    public static List<WeatherItem> getHourlyWeather(City city) {

        List<WeatherItem> weatherItemList = new ArrayList<>();

        return weatherItemList;
    }

    public static List<WeatherItem> getDailyWeather(City city) {

        List<WeatherItem> weatherItemList = new ArrayList<>();

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

        int precipProbability = Integer.valueOf(getJsonValueAsString(weather,"precipProbability"));
        weatherItem.setPrecipProbabilityPercent(precipProbability);

        double temperature = Double.valueOf(getJsonValueAsString(weather,"temperature"));
        weatherItem.setTemperature(temperature);

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

    public static void setJsonResponse(String jsonResponse) {
        DarkSkyParser.jsonResponse = jsonResponse;
    }
}
