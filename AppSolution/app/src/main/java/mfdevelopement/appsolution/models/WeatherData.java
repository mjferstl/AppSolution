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
    private WeatherItem weatherCurrently;
    private List<WeatherItem> weatherHourly, weatherDaily;


    public boolean forecastLoaded;
    private List<WeatherForecast> weatherForecast = new ArrayList<>();

    public WeatherData() {

    }

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

    public WeatherItem getWeatherCurrently() {
        return weatherCurrently;
    }

    public void setWeatherCurrently(WeatherItem weatherCurrently) {
        this.weatherCurrently = weatherCurrently;
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

    public void loadWeatherData() {
        DarkSkyParser darkSkyParser = new DarkSkyParser(this.city);
        darkSkyParser.fetchJsonWeather();
        this.weatherCurrently = darkSkyParser.getCurrentWeather();
        this.weatherHourly = darkSkyParser.getHourlyForecast();
        this.weatherDaily = darkSkyParser.getDailyForecast();

        setDescription(createDescription());
        setImageID(weatherCurrently.getImageID());
    }

    public List<WeatherForecast> getWeatherForecast() {
        return weatherForecast;
    }

    private String createDescription() {

        Log.d("test","createDescription:element " + this.getCity().getCityName());
        Log.d("test","createDescription:temperature = " +weatherCurrently.getTemperature());

        Locale loc = Locale.getDefault();
        String d = String.format(loc, "%.1f", weatherCurrently.getTemperature()) + DarkSkyParser.UNIT_TEMPERATURE +
                ", " + weatherCurrently.getPrecipProbabilityPercent() + "%" +
                ", " + String.format(loc, "%.1f", weatherCurrently.getWindSpeed()) + DarkSkyParser.UNIT_WIND_SPEED;
        return d;
    }
}
