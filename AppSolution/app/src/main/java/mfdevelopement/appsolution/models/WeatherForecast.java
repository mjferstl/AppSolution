package mfdevelopement.appsolution.models;

import mfdevelopement.appsolution.parser.OpenWeatherMapIconId;

public class WeatherForecast {

    private String city;
    private String time;
    private String temp;
    private String tempMin;
    private String tempMax;
    private int iconId;

    public WeatherForecast() {
        this.city = null;
    }

    public WeatherForecast(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public String getTime() {
        return time;
    }

    public String getTemp() {
        return temp;
    }

    public int getIconId() {
        return iconId;
    }

    public String getTempMin() {
        return tempMin;
    }

    public String getTempMax() {
        return tempMax;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public void setTempMin(String tempMin) {
        this.tempMin = tempMin;
    }

    public void setTempMax(String tempMax) {
        this.tempMax = tempMax;
    }

    public void setIconId(int iconId) {
        this.iconId = OpenWeatherMapIconId.getIconId(iconId);
    }
}
