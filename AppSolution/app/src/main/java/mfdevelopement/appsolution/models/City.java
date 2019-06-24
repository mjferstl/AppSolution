package mfdevelopement.appsolution.models;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mfdevelopement.appsolution.R;

public class City {

    private double longitude, latitude;
    private String cityName;
    private int Id;

    private City() {

    }

    public City(int Id, Context context) {

        this.Id = Id;

        // get the other informations out of the resource file
        String[] cityItems = context.getResources().getStringArray(R.array.weatherMapCities);
        List<String> citiesString = new ArrayList<>(Arrays.asList(cityItems));

        for (String item : citiesString) {
            if (item.contains(String.valueOf(Id))) {
                City c = getCityByItem(item);
                this.cityName = c.getCityName();
                this.longitude = c.getLongitude();
                this.latitude = c.getLatitude();
                break;
            }
        }
    }

    public City(String cityName, int Id, double longitude, double latitude) {
        this.cityName = cityName;
        this.Id = Id;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public static City getCityByItem(String string) {

        City city = new City();

        String[] vars = string.split(":");

        for (String var : vars) {
            String[] key_value = var.split("=");
            switch (key_value[0]) {
                case "name":
                    city.cityName = key_value[1];
                    break;
                case "id":
                    city.Id = Integer.valueOf(key_value[1]);
                    break;
                case "lon":
                    city.longitude = Double.valueOf(key_value[1]);
                    break;
                case "lat":
                    city.latitude = Double.valueOf(key_value[1]);
                    break;
            }
        }

        return city;
    }
}
