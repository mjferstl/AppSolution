package mfdevelopement.appsolution.models;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import mfdevelopement.appsolution.parser.DarkSkyParser;

public class WeatherData {

    private City city;
    private String description;
    private int imageID;
    private WeatherItem weatherCurrent;
    private List<WeatherItem> weatherHourly, weatherDaily;
    private String jsonReponse;

    public boolean forecastLoaded;
    private List<WeatherForecast> weatherForecast = new ArrayList<>();

    public WeatherData(City city) {
        this.city = city;
        this.imageID = 0;
        this.forecastLoaded = false;
    }

    public WeatherData(City city, String description) {
        this.city = city;
        this.description = description;
        this.imageID = 0;
        this.forecastLoaded = false;
    }

    public WeatherData(City city, String description, Integer imageID) {
        this.city = city;
        this.description = description;
        this.imageID = imageID;
        this.forecastLoaded = false;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
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

    public WeatherItem getWeatherCurrent() {
        return weatherCurrent;
    }

    public void setWeatherCurrent(WeatherItem weatherCurrent) {
        this.weatherCurrent = weatherCurrent;
    }

    public List<WeatherItem> getWeatherHourly() {
        return weatherHourly;
    }

    public void setWeatherHourly(List<WeatherItem> weatherHourly) {
        this.weatherHourly = weatherHourly;
    }

    public List<WeatherItem> getWeatherDaily() {
        return weatherDaily;
    }

    public void setWeatherDaily(List<WeatherItem> weatherDaily) {
        this.weatherDaily = weatherDaily;
    }

    public String getJsonReponse() {
        return this.jsonReponse;
    }

    public void setJsonReponse(String jsonReponse) {
        this.jsonReponse = jsonReponse;
        DarkSkyParser darkSkyParser;
        if (this.city != null)
            darkSkyParser = new DarkSkyParser(this.city);
        else
            darkSkyParser = new DarkSkyParser();

        darkSkyParser.setJsonResponse(this.jsonReponse);
        loadWeatherData(darkSkyParser);
    }

    public void loadWeatherData() {
        loadWeatherData(new DarkSkyParser(this.city));
    }

    private void loadWeatherData(DarkSkyParser darkSkyParser) {
        Log.d("WeatherData","starting to get weather data from DarkSkyParser");
        this.weatherCurrent = darkSkyParser.getCurrentWeather();
        this.weatherHourly = darkSkyParser.getHourlyForecast();
        this.weatherDaily = darkSkyParser.getDailyForecast();
        this.jsonReponse = darkSkyParser.getJsonResponse();

        setDescription(createDescription());
        setImageID(weatherCurrent.getImageID());
        this.forecastLoaded = true;
    }

    public List<WeatherForecast> getWeatherForecast() {
        return weatherForecast;
    }

    private String createDescription() {

        Locale loc = Locale.getDefault();
        String d = String.format(loc, "%.1f", weatherCurrent.getTemperature()) + DarkSkyParser.UNIT_TEMPERATURE +
                " | " + weatherCurrent.getPrecipProbabilityPercent() + "%" +
                " | " + String.format(loc, "%.1f", weatherCurrent.getWindSpeed()) + DarkSkyParser.UNIT_WIND_SPEED;
        return d;
    }

    public boolean isForecastLoaded() {
        return this.forecastLoaded;
    }
}
