package mfdevelopement.appsolution.models;

import java.util.ArrayList;
import java.util.List;

import mfdevelopement.appsolution.parser.OpenWeatherMapParser;

public class Weather {

    private String cityName;
    private String description;
    private int cityId;
    private int imageID;

    public boolean forecastLoaded;
    private List<WeatherForecast> weatherForecast = new ArrayList<>();

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

    public int getCityId() {
        return cityId;
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
        Weather w = OpenWeatherMapParser.parseWeather(this.cityId);

        this.cityName = w.getCityName();
        this.description = w.getDescription();
        this.imageID = w.getImageID();
    }

    public void getWeatherForecast() {
        weatherForecast = OpenWeatherMapParser.parseWeatherForecast(this.cityId);
    }
}
