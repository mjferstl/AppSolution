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
import mfdevelopement.appsolution.models.Weather;

public class WeatherOverviewListAdapter extends ArrayAdapter<Weather> {

    private final String errorMsgLoading = "Fehler beim Laden der Daten ...";

    public WeatherOverviewListAdapter(@NonNull Context context, @NonNull List<Weather> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Weather currentWeather = getItem(position);

        View view = convertView;
         if(view == null) {
             view = LayoutInflater.from(getContext()).inflate(R.layout.adapter_wheater_overview, parent, false);
         }

         TextView city = view.findViewById(R.id.tv_weather_overview_city);
         TextView description = view.findViewById(R.id.tv_weather_overview_details);
         ImageView image = view.findViewById(R.id.img_weather_overview_icon);

         if (currentWeather.getCityName() != null) {
             city.setText(currentWeather.getCityName());

             if (currentWeather.getDescription() == null) {
                 description.setVisibility(View.INVISIBLE);
             } else {
                 description.setText(currentWeather.getDescription());
             }

             if (currentWeather.getImageID() == null) {
                 image.setVisibility(View.INVISIBLE);
             } else {
                 image.setImageResource(currentWeather.getImageID());
             }
         } else {
             city.setText(" --- ");
             description.setText(errorMsgLoading);
             image.setVisibility(View.INVISIBLE);
         }
        return view;
    }
}
