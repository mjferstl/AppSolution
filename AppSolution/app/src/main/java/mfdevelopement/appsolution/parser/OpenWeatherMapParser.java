package mfdevelopement.appsolution.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import mfdevelopement.appsolution.models.Weather;
import mfdevelopement.appsolution.models.WeatherForecast;

public class OpenWeatherMapParser {

    private static final String owmUrlForecast1 = "https://api.openweathermap.org/data/2.5/forecast?id=";
    private static final String owmUrlWeatherPart1 = "https://api.openweathermap.org/data/2.5/weather?id=";
    private static final String owmUrlWeatherPart2 = "&units=metric&APPID=141a1f2e6d8f32c5c68f011d6e28df08";

    private static final String unitSpeed = "m/s";
    private static final String unitTemperature = "°C";


    public static Weather parseWeather(int Id) {

        Weather weather = new Weather(Id);

        try {
            String response = null;
            if (Id != 0) {
                try {
                    // create url containing the cityId
                    String url = owmUrlWeatherPart1 + Id + owmUrlWeatherPart2;

                    // get the response in json format
                    JsonParser jsonParser = new JsonParser();
                    response = jsonParser.parseJsonFromUrl(url);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                // create JSONObject from the response
                JSONObject jsonObject = new JSONObject(response);

                // get the "main" informations
                JSONObject mainJO = jsonObject.getJSONObject("main");
                // get the temperatures
                String strAktTemp = mainJO.get("temp").toString();
                String strMinTemp = mainJO.get("temp_min").toString();
                String strMaxTemp = mainJO.get("temp_max").toString();
                // pressure
                String strPressure = mainJO.get("pressure").toString();
                //String strPressSea = mainJO.get("sea_level").toString();
                //String strPressGnd = mainJO.get("grnd_level").toString();
                // humidity
                String strHumidity = mainJO.get("humidity").toString();

                // get the wind speed and angle
                JSONObject windJO = jsonObject.getJSONObject("wind");
                String strWindSpeed = windJO.get("speed").toString();
//                String strWindAngle = windJO.get("deg").toString();

                // cloudness
                String strClouds = jsonObject.getJSONObject("clouds").get("all").toString();

                // parse strings to doubles
                double aktTemp = Double.parseDouble(strAktTemp);
                double minTemp = Double.parseDouble(strMinTemp);
                double maxTemp = Double.parseDouble(strMaxTemp);
                double windSpeed = Double.parseDouble(strWindSpeed);

                weather.setDescription(String.format(Locale.getDefault(), "%.1f", aktTemp) + unitTemperature +
                        ", " + String.format(Locale.getDefault(), "%.1f", minTemp) + unitTemperature +
                        " - " + String.format(Locale.getDefault(), "%.1f", maxTemp) + unitTemperature +
                        ", " + String.format(Locale.getDefault(), "%.1f", windSpeed) + unitSpeed);

                weather.setCityName(jsonObject.get("name").toString());

                JSONObject w = jsonObject.getJSONArray("weather").getJSONObject(0);
                String id = w.get("id").toString();

                int iconId = OpenWeatherMapIconId.getIconId(id);
                if (iconId != 0) {
                    weather.setImageID(iconId);
                }

                weather.forecastLoaded = true;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return weather;
    }


    public static List<WeatherForecast> parseWeatherForecast(int Id) {

        List<WeatherForecast> forecastList = new ArrayList<>();

        try {
            String response = null;

            if (Id != 0) {
                try {
                    JsonParser jsonParser = new JsonParser();
                    String url = owmUrlForecast1 + Id + owmUrlWeatherPart2;
                    response = jsonParser.parseJsonFromUrl(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                JSONObject jsonObject = new JSONObject(response);

                JSONArray listJA = jsonObject.getJSONArray("list");

                for (int i = 0; i < listJA.length(); i++) {
                    WeatherForecast wf = new WeatherForecast();

                    JSONObject currentList = listJA.optJSONObject(i);

                    long timestamp = Integer.valueOf(currentList.get("dt").toString());
                    String localTime = getDateTimeShort(timestamp);

                    wf.setTime(localTime);

                    JSONObject mainJO = currentList.getJSONObject("main");

                    // get temperature and round to one decimal place
                    wf.setTemp(formatTemperature(mainJO.get("temp").toString(),1));

                    // get min and max temperature
                    wf.setTempMin(mainJO.get("temp_min").toString());
                    wf.setTempMax(mainJO.get("temp_max").toString());

                    JSONArray weatherJA = currentList.getJSONArray("weather");

                    String weatherId = weatherJA.optJSONObject(0).get("id").toString();
                    wf.setIconId(Integer.valueOf(weatherId));

                    forecastList.add(wf);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return forecastList;
    }


    private static String getDateCurrentTimeZone(long timestamp, String dateFormat) {
        try {
            Calendar calendar = Calendar.getInstance();
            TimeZone tz = TimeZone.getDefault();
            calendar.setTimeInMillis(timestamp * 1000);
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.getDefault());
            Date currenTimeZone = calendar.getTime();
            return sdf.format(currenTimeZone);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String getDateTimeShort(long timestamp) {
        String dateFormat = "dd.MM. HH:mm";
        return getDateCurrentTimeZone(timestamp, dateFormat);
    }

    private static String getDateTimeLong(long timestamp) {
        String dateFormat = "yyyy-MM-dd HH:mm";
        return getDateCurrentTimeZone(timestamp, dateFormat);
    }

    private static String formatTemperature(String t, int decimalPlaces) {
        double temp = Double.parseDouble(t);
        double factor = Math.pow(10,decimalPlaces);
        double tempFormatted = Math.round(temp*factor)/factor;
        return String.valueOf(tempFormatted);
    }

}
