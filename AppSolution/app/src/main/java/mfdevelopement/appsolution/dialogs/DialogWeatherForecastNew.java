package mfdevelopement.appsolution.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import mfdevelopement.appsolution.R;
import mfdevelopement.appsolution.activities.WeatherOverview;
import mfdevelopement.appsolution.device.general.DisplayData;
import mfdevelopement.appsolution.listview_adapters.WeatherForecastListAdapter;
import mfdevelopement.appsolution.models.WeatherData;
import mfdevelopement.appsolution.models.WeatherForecast;

public class DialogWeatherForecastNew {

    private Context context;
    private WeatherData weatherData;
    private final String LOG_TAG = "DialogWeatherForecast";
    private Dialog dialog;

    public DialogWeatherForecastNew(Context c, WeatherData wd) {
        this.context = c;
        this.weatherData = wd;
    }

    /**
     * show a dialog containing the weather forecast data in a listview
     */
    public void show() {

        //TODO only show dialog, if weather forecast data is loaded

        // get weather forecast of the selected city
        String selectedCity = this.weatherData.getCity().getCityName();
        List<WeatherForecast> wf = this.weatherData.getWeatherForecast();

        // create a new dialog containing the weather forecast data
        Log.i(LOG_TAG,"show: open weather forecast dialog, city " + selectedCity);
        dialog = new Dialog(this.context);
        // no dialog title
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // content
        dialog.setContentView(R.layout.dialog_weather_forecast);

        // button to close the dialog
        Button btnForecastDismiss = dialog.findViewById(R.id.btn_dia_weather_forecast_dismiss);
        btnForecastDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
                Log.i(LOG_TAG,  "OnClickListener: Close weather forecast dialog");
            }
        });

        // fill in items of the listview
        ListView listViewForecast = dialog.findViewById(R.id.lv_weather_forecast);
        listViewForecast.setAdapter(new WeatherForecastListAdapter(this.context, wf));

        // adjust height of the listview
        DisplayData displayData = new DisplayData(this.context);
        ViewGroup.LayoutParams listViewForecastLayoutParams = listViewForecast.getLayoutParams();
        listViewForecastLayoutParams.height = (int) (displayData.getHeightPx()*0.65);
        listViewForecast.setLayoutParams(listViewForecastLayoutParams);

        // set title of dialog
        TextView tv_title = dialog.findViewById(R.id.tv_dia_weather_forecast_title);
        String title = selectedCity + " - " + WeatherOverview.FORECAST;
        tv_title.setText(title);

        // change the dialog width to 80% of the screen width
        LinearLayout layout = dialog.findViewById(R.id.lin_lay_dia_weather_forecast);
        int width = displayData.getWidthPx()*8/10;
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,LinearLayout.LayoutParams.WRAP_CONTENT,1);
        layout.setLayoutParams(params);

        // show dialog
        dialog.show();
    }

    public void cancel() {
        if (dialog != null) {dialog.cancel();}
    }
}
