package mfdevelopement.appsolution.parser;

import mfdevelopement.appsolution.R;

public class OpenWeatherMapIconId {

    public static int getIconId(String ID) {
        int id = Integer.parseInt(ID);
        return getIconId(id);
    }

    /**
     * get the id of the weather image
     * @param id: string containing the id returned by open weather map
     * @return iconId: id of the image located under drawables
     */
    public static int getIconId(int id) {

        int iconId = 0;

        if (id == 800) {
            iconId = R.drawable.ic_wi_day_sunny;
        }
        else if (id == 771 || id == 801 || id == 802 || id == 803) {
            iconId = R.drawable.ic_wi_cloudy_gusts;
        }
        else if (id == 804) {
            iconId = R.drawable.ic_wi_cloudy;
        }
        else if (id == 731 || id == 761 || id == 762) {
            iconId = R.drawable.ic_wi_dust;
        }
        else if (id == 310 || id == 511 || id == 611 || id == 612 || id == 615 || id == 616 || id == 620) {
            iconId = R.drawable.ic_wi_rain_mix;
        }
        else if (id == 600 || id == 601 || id == 621 || id == 622) {
            iconId = R.drawable.ic_wi_snow;
        }
        else if (id == 200 || id == 201 || id == 202 || id == 230 || id == 231 || id == 232) {
            iconId = R.drawable.ic_wi_thunderstorm;
        }
        else if (id == 210 || id == 211 || id == 212 || id == 221) {
            iconId = R.drawable.ic_wi_lightning;
        }
        else if (id == 300 || id == 301 || id == 321 || id == 500) {
            iconId = R.drawable.ic_wi_sprinkle;
        }
        else if (id == 302 || id == 311 || id == 312 || id == 314 || id == 501 || id == 502 || id == 503 || id == 504) {
            iconId = R.drawable.ic_wi_rain;
        }
        else if (id == 313 || id == 520 || id == 521 || id == 522 || id == 701) {
            iconId = R.drawable.ic_wi_showers;
        }
        else if (id == 531 || id == 901) {
            iconId = R.drawable.ic_wi_storm_showers;
        }
        else if (id == 602) {
            iconId = R.drawable.ic_wi_sleet;
        }
        else if (id == 711) {
            iconId = R.drawable.ic_wi_smoke;
        }
        else if (id == 721) {
            iconId = R.drawable.ic_wi_day_haze;
        }
        else if (id == 741) {
            iconId = R.drawable.ic_wi_fog;
        }
        else if (id == 781 || id == 900) {
            iconId = R.drawable.ic_wi_tornado;
        }
        else if (id == 902) {
            iconId = R.drawable.ic_wi_hurricane;
        }
        else if (id == 903) {
            iconId = R.drawable.ic_wi_snowflake_cold;
        }
        else if (id == 904) {
            iconId = R.drawable.ic_wi_hot;
        }
        else if (id == 905) {
            iconId = R.drawable.ic_wi_windy;
        }
        else if (id == 906) {
            iconId = R.drawable.ic_wi_hail;
        }
        else if (id == 957) {
            iconId = R.drawable.ic_wi_strong_wind;
        }
        return iconId;
    }
}
