package mfdevelopement.appsolution.listview_adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import mfdevelopement.appsolution.R;
import mfdevelopement.appsolution.models.WeatherItem;

public class WeatherForecastListAdapter extends ArrayAdapter<WeatherItem> {

    private final String ERROR_DATA_LOADING = "Fehler beim Laden der Daten ...";

    public WeatherForecastListAdapter(@NonNull Context context, @NonNull List<WeatherItem> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // get current weather forecast data
        WeatherItem currentWeatherItem = getItem(position);

        // choose the adapter for the list view items
        View view = convertView;
         if(view == null) {
             view = LayoutInflater.from(getContext()).inflate(R.layout.adapter_wheater_forecast, parent, false);
         }

         // get fields of each list item
         TextView separator = view.findViewById(R.id.tv_weather_forecast_separator);
         TextView time = view.findViewById(R.id.tv_weather_forecast_time);
         TextView temperature = view.findViewById(R.id.tv_weather_forecast_temperature);
         ImageView icon = view.findViewById(R.id.img_weather_forecast_icon);

         // if forecast contains no time, then show error message
         if (currentWeatherItem.getTime() != null) {
             time.setText(currentWeatherItem.getTime());

             // show separator, if it's the first time of this day
             String currentDate = currentWeatherItem.getDateDayname();
             separator.setText(currentDate);
             WeatherItem itemBefore = new WeatherItem();
             if (position > 0) {
                 itemBefore = getItem(position-1);
             }
             if (itemBefore.getDate() != null && !itemBefore.getDateDayname().equals(currentDate)) {
                separator.setVisibility(View.VISIBLE);
             }
             else {
                 separator.setVisibility(View.GONE);
             }

             // informations about temperature and rain in %
             StringBuilder stringBuilder = new StringBuilder();
             if (currentWeatherItem.getTemperature() != null) {
                 stringBuilder.append(currentWeatherItem.getTemperatureCelsius());
                 // show the time
                 time.setVisibility(View.VISIBLE);
             }
             else if (currentWeatherItem.getTemperatureHigh() != null && currentWeatherItem.getTemperatureLow() != null){
                 String tempRange = currentWeatherItem.getTemperatureLowCelsius() + " - " + currentWeatherItem.getTemperatureHighCelsius();
                 stringBuilder.append(tempRange);
                 // do not show the time, because there is only one data for the whole day
                 time.setVisibility(View.GONE);
             }

             String rain_precip = currentWeatherItem.getPrecipProbabilityPercent() + "%";

             // add separator, if necessary
             if (!stringBuilder.toString().equals("") && !rain_precip.equals("%")) {
                 stringBuilder.append(" | ");
                 stringBuilder.append(rain_precip);
             }

             // set TextView text
             temperature.setText(stringBuilder.toString());

             int iconId = currentWeatherItem.getImageID();
             if (iconId != 0) {icon.setImageResource(iconId);}

         } else {
             separator.setVisibility(View.GONE);
             time.setText(" - - - ");
             temperature.setText(ERROR_DATA_LOADING);
             icon.setVisibility(View.INVISIBLE);
         }
        return view;
    }
}