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
import mfdevelopement.appsolution.models.WeatherData;

public class WeatherOverviewListAdapterNew extends ArrayAdapter<WeatherData> {

    private final String errorMsgLoading = "Fehler beim Laden der Daten ...";

    public WeatherOverviewListAdapterNew(@NonNull Context context, @NonNull List<WeatherData> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        WeatherData currentWeatherData = getItem(position);

        View view = convertView;
         if(view == null) {
             view = LayoutInflater.from(getContext()).inflate(R.layout.adapter_wheater_overview, parent, false);
         }

         TextView city = view.findViewById(R.id.tv_weather_overview_city);
         TextView description = view.findViewById(R.id.tv_weather_overview_details);
         ImageView image = view.findViewById(R.id.img_weather_overview_icon);

         if (currentWeatherData.getCity() != null) {
             city.setText(currentWeatherData.getCity().getCityName());

             if (currentWeatherData.getDescription() == null) {
                 description.setVisibility(View.INVISIBLE);
             } else {
                 description.setText(currentWeatherData.getDescription());
             }

             if (currentWeatherData.getImageID() == null) {
                 image.setVisibility(View.INVISIBLE);
             } else {
                 image.setImageResource(currentWeatherData.getImageID());
             }
         } else {
             city.setText(" --- ");
             description.setText(errorMsgLoading);
             image.setVisibility(View.INVISIBLE);
         }
        return view;
    }
}
