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
import java.util.Locale;

import mfdevelopement.appsolution.R;
import mfdevelopement.appsolution.models.WeatherForecast;

public class WeatherForecastListAdapter extends ArrayAdapter<WeatherForecast> {

    private final String errorMsgLoading = "Fehler beim Laden der Daten ...";

    public WeatherForecastListAdapter(@NonNull Context context, @NonNull List<WeatherForecast> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // get current weather forecast data
        WeatherForecast currentForecast= getItem(position);

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
         if (currentForecast.getTime() != null) {
             time.setText(currentForecast.getTime());

             // show separator, if it's the first time of this day
             // intervall of the data is 3h
             separator.setText(currentForecast.getDate());
             String t = currentForecast.getTime();
             if (t.equals("00:00") || t.equals("01:00") || t.equals("02:00")) {
                separator.setVisibility(View.VISIBLE);
             }
             else {
                 separator.setVisibility(View.GONE);
             }

             // informations about temperature and rain in mm
             String tempAndRain = "";
             if (currentForecast.getTemp() != null) {
                 tempAndRain = currentForecast.getTempDegree();
             }
             if (currentForecast.getRain() != null) {
                 double rain = Double.valueOf(currentForecast.getRain());
                 String rain_mm = String.format(Locale.getDefault(),"%.1f",rain) + " mm";
                 tempAndRain = tempAndRain.equals("") ? rain_mm : tempAndRain + " | " + rain_mm;
             }
             temperature.setText(tempAndRain);


             if (currentForecast.getIconId() != 0) {
                 icon.setImageResource(currentForecast.getIconId());
             }
         } else {
             separator.setVisibility(View.GONE);
             time.setText(" - - - ");
             temperature.setText(errorMsgLoading);
             icon.setVisibility(View.INVISIBLE);
         }
        return view;
    }
}