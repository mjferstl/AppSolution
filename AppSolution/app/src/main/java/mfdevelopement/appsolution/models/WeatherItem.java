package mfdevelopement.appsolution.models;

import java.util.Locale;

import mfdevelopement.appsolution.parser.DateTimeParser;
import mfdevelopement.appsolution.parser.DarkSkyParser;
import mfdevelopement.appsolution.parser.DarkSkyWeatherIconParser;

public class WeatherItem {

    private String summary;
    private int imageID, precipProbabilityPercent;
    private long timestamp_utc;
    private Double temperature, temperatureHigh, temperatureLow;
    private double windSpeed, humidity;


    public WeatherItem() {
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setIconByName(String iconName) {
        this.imageID = DarkSkyWeatherIconParser.getIconId(iconName);
    }

    public void setIconId(int iconId) {
        this.imageID = iconId;
    }

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    public int getPrecipProbabilityPercent() {
        return precipProbabilityPercent;
    }

    public void setPrecipProbabilityPercent(int precipProbabilityPercent) {
        if (precipProbabilityPercent > 100) {this.precipProbabilityPercent = 100;}
        else if (precipProbabilityPercent < 0) {this.precipProbabilityPercent = 0;}
        else {this.precipProbabilityPercent = precipProbabilityPercent;}
    }

    public long getTimestampUtc() {
        return timestamp_utc;
    }

    public String getLocalTime() {
        return DateTimeParser.getHoursMinutes(this.timestamp_utc);
    }

    public String getLocalDate() {
        return DateTimeParser.getDate(this.timestamp_utc);
    }

    public String getDateDayname() {
        return DateTimeParser.getDateDayname(this.timestamp_utc);
    }

    public void setTimestamp_utc(long timestamp_utc) {
        this.timestamp_utc = timestamp_utc;
    }

    public Double getTemperature() {
        return temperature;
    }

    public String getTemperatureCelsius() {
        return formatTemperatureCelsius(getTemperature());
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public Double getTemperatureHigh() {
        return temperatureHigh;
    }

    public void setTemperatureHigh(double temperatureHigh) {
        this.temperatureHigh = temperatureHigh;
    }

    public Double getTemperatureLow() {
        return temperatureLow;
    }

    public void setTemperatureLow(double temperatureLow) {
        this.temperatureLow = temperatureLow;
    }

    public String getTemperatureHighCelsius() {
        return formatTemperatureCelsius(getTemperatureHigh());
    }

    public String getTemperatureLowCelsius() {
        return formatTemperatureCelsius(getTemperatureLow());
    }

    private String formatTemperatureCelsius(double temp) {
        return String.format(Locale.getDefault(),"%.1f",temp) + DarkSkyParser.UNIT_TEMPERATURE;
    }
}
