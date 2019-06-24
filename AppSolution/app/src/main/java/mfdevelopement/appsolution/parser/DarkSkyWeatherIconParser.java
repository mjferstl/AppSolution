package mfdevelopement.appsolution.parser;

import mfdevelopement.appsolution.R;

public class DarkSkyWeatherIconParser {

    /**
     * get the id of the weather image
     * @param iconString: string containing the string returned by dark sky response
     * @return iconId: id of the image located under drawables
     */
    public static int getIconId(String iconString) {

        int iconId = 0;

        if (iconString.equals("clear-day") || iconString.equals("clear-night")) {
            iconId = R.drawable.ic_wi_day_sunny;
        }
        else if (iconString.equals("cloudy") || iconString.equals("partly-cloudy-day") || iconString.equals("partly-cloudy-night")) {
            iconId = R.drawable.ic_wi_cloudy;
        }
        else if (iconString.equals("snow")) {
            iconId = R.drawable.ic_wi_snow;
        }
        else if (iconString.equals("thunderstorm")) {
            iconId = R.drawable.ic_wi_thunderstorm;
        }
        else if (iconString.equals("rain")) {
            iconId = R.drawable.ic_wi_rain;
        }
        else if (iconString.equals("sleet")) {
            iconId = R.drawable.ic_wi_sleet;
        }
        else if (iconString.equals("fog")) {
            iconId = R.drawable.ic_wi_fog;
        }
        else if (iconString.equals("tornado")) {
            iconId = R.drawable.ic_wi_tornado;
        }
        else if (iconString.equals("hail")) {
            iconId = R.drawable.ic_wi_hail;
        }
        else if (iconString.equals("wind")) {
            iconId = R.drawable.ic_wi_strong_wind;
        }
        return iconId;
    }
}
