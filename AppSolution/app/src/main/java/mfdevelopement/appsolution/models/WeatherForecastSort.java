package mfdevelopement.appsolution.models;

import java.util.Comparator;

/**
 * class to sort WeatherItems by timestamp
 */
public class WeatherForecastSort implements Comparator<WeatherItem> {

    /**
     *
     * @param w1: first weatherItem for comparing timestamp
     * @param w2: second weatherItem for comparing timestamp
     * @return -1 if first is greater, 0 if equal, 1 if second is greater
     */
    @Override
    public int compare(WeatherItem w1, WeatherItem w2) {
        int res = String.valueOf(w1.getTimestampUtc()).compareTo(String.valueOf(w2.getTimestampUtc()));
        if (res == 0) {
            if (w1.getTemperature() == null) {res = -1;}
            else if (w2.getTemperature() == null) {res = 1;}
        }
        return res;
    }
}