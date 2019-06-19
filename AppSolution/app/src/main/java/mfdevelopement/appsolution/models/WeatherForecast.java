package mfdevelopement.appsolution.models;

import android.support.annotation.NonNull;

import mfdevelopement.appsolution.parser.OpenWeatherMapIconId;

public class WeatherForecast {

    private String city;
    private String time;
    private String date;
    private String temp;
    private String tempMin;
    private String tempMax;
    private String rain_mm;
    private int iconId;

    public WeatherForecast() {
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
    public String getDate() {
        return date;
    }

    public String getTemp() {
        return temp;
    }

    public String getTempDegree(){
        return getTemp() + "°C";
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

    public String getRain() {
        return this.rain_mm;
    }

    public String getRain_mm() {
        return this.rain_mm + "mm";
    }

    public void setCity(@NonNull String city) {
        this.city = city;
    }

    public void setTime(@NonNull String time) {
        this.time = time;
    }

    public void setDate(@NonNull String date) {
        this.date = date;
    }

    public void setTemp(@NonNull String temp) {
        this.temp = temp;
    }

    public void setTempMin(@NonNull String tempMin) {
        this.tempMin = tempMin;
    }

    public void setTempMax(@NonNull String tempMax) {
        this.tempMax = tempMax;
    }

    public void setRain(String rain) {
        this.rain_mm = rain;
    }

    public void setIconId(int iconId) {
        this.iconId = OpenWeatherMapIconId.getIconId(iconId);
    }
}
