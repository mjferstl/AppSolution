package mfdevelopement.appsolution.models;

import java.util.Locale;

import mfdevelopement.appsolution.helper.DateTimeParser;
import mfdevelopement.appsolution.parser.DarkSkyParser;
import mfdevelopement.appsolution.parser.DarkSkyWeatherIconParser;

public class WeatherItem {

    private String summary;
    private int imageID, precipProbabilityPercent;
    private long timestamp;
    private double temperature, windSpeed, humidity;

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

    public long getTimestamp() {
        return timestamp;
    }

    public String getTime() {
        return DateTimeParser.getHoursMinutes(this.timestamp);
    }

    public String getDate() {
        return DateTimeParser.getDate(this.timestamp);
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public double getTemperature() {
        return temperature;
    }

    public String getTemperatureCelsius() {
        return String.format(Locale.getDefault(),"%.1f",getTemperature()) + DarkSkyParser.UNIT_TEMPERATURE;
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
}
