package mfdevelopement.appsolution.models;

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

import mfdevelopement.appsolution.parser.JsonParser;
import mfdevelopement.appsolution.parser.OpenWeatherMapIconId;

public class Weather {

    private String cityName;
    private String description;
    private int cityId;
    private int imageID;

    private boolean forecastLoaded;
    private List<WeatherForecast> weatherForecast = new ArrayList<>();

    private final String owmUrlForecast1 = "https://api.openweathermap.org/data/2.5/forecast?id=";
    private final String owmUrlWeatherPart1 = "https://api.openweathermap.org/data/2.5/weather?id=";
    private final String owmUrlWeatherPart2 = "&units=metric&APPID=141a1f2e6d8f32c5c68f011d6e28df08";

    private final String unitSpeed = "m/s";
    private final String unitTemperature = "°C";


    public Weather(String cityName) {
        this.cityName = cityName;
        this.description = null;
        this.cityId = 0;
        this.imageID = 0;
        this.forecastLoaded = false;
    }

    public Weather(int cityId) {
        this.cityName = null;
        this.description = null;
        this.cityId = cityId;
        this.imageID = 0;
        this.forecastLoaded = false;
    }

    public Weather(String cityName, String description) {
        this.cityName = cityName;
        this.description = description;
        this.imageID = 0;
        this.forecastLoaded = false;
    }

    public Weather(String cityName, String description, Integer imageID) {
        this.cityName = cityName;
        this.description = description;
        this.imageID = imageID;
        this.forecastLoaded = false;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getImageID() {
        return imageID;
    }

    public void setImageID(Integer imageID) {
        this.imageID = imageID;
    }

    public void getCurrentWeather() {
        try {
            String response = null;
            if (this.cityId != 0) {
                try {
                    JsonParser jsonParser = new JsonParser();
                    response = jsonParser.parseJsonFromUrl(owmUrlWeatherPart1 + this.cityId + owmUrlWeatherPart2);
                } catch (IOException e) {
                    e.printStackTrace();
                }

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

                this.description = String.format(Locale.getDefault(), "%.1f", aktTemp) + unitTemperature +
                        ", " + String.format(Locale.getDefault(), "%.1f", minTemp) + unitTemperature +
                        " - " + String.format(Locale.getDefault(), "%.1f", maxTemp) + unitTemperature +
                        ", " + String.format(Locale.getDefault(), "%.1f", windSpeed) + unitSpeed;

                this.cityName = jsonObject.get("name").toString();
                if (this.cityName.equals("Muenchen")) {
                    this.cityName = "München";
                }

                JSONObject weather = jsonObject.getJSONArray("weather").getJSONObject(0);
                String id = weather.get("id").toString();

                int iconId = OpenWeatherMapIconId.getIconId(id);
                if (iconId != 0) {
                    this.imageID = iconId;
                }

                this.forecastLoaded = true;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void parseWeatherForecast() {
        try {
            String response = null;

            if (this.cityId != 0) {
                try {
                    JsonParser jsonParser = new JsonParser();
                    response = jsonParser.parseJsonFromUrl(owmUrlForecast1 + this.cityId + owmUrlWeatherPart2);
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

                    weatherForecast.add(wf);
                }
                this.forecastLoaded = true;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public List<WeatherForecast> getWeatherForecast() {
        return weatherForecast;
    }

    private String getDateCurrentTimeZone(long timestamp, String dateFormat) {
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

    private String getDateTimeShort(long timestamp) {
        String dateFormat = "dd.MM. HH:mm";
        return getDateCurrentTimeZone(timestamp, dateFormat);
    }

    private String getDateTimeLong(long timestamp) {
        String dateFormat = "yyyy-MM-dd HH:mm";
        return getDateCurrentTimeZone(timestamp, dateFormat);
    }

    private String formatTemperature(String t, int decimalPlaces) {
        double temp = Double.parseDouble(t);
        double factor = Math.pow(10,decimalPlaces);
        double tempFormatted = Math.round(temp*factor)/factor;
        return String.valueOf(tempFormatted);
    }
}
