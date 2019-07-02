package mfdevelopement.appsolution.dialogs;

import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import mfdevelopement.appsolution.R;
import mfdevelopement.appsolution.activities.WeatherOverviewActivity;
import mfdevelopement.appsolution.device.general.DisplayData;

public class DialogWeatherOverviewRemoveCity {

    private WeatherOverviewActivity context;
    private int itemPosition;
    private final String LOG_TAG = "DialogWeatherOverviewRemoveCity";
    private Dialog dialog;

    public DialogWeatherOverviewRemoveCity(WeatherOverviewActivity c, int itemPosition) {
        this.context = c;
        this.itemPosition = itemPosition;
    }

    /**
     * show a dialog containing the weather forecast data in a listview
     */
    public void show() {

        dialog = new Dialog(this.context);
        // no dialog title
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // content
        dialog.setContentView(R.layout.dialog_weather_overview_long_click);

        // init button
        Button btnRemoveCity = dialog.findViewById(R.id.btn_dia_weather_overview_remove_city);
        btnRemoveCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.removeCity(itemPosition);
                cancel();
            }
        });

        // change the dialog width to 80% of the screen width
        DisplayData displayData = new DisplayData(this.context);
        RelativeLayout layout = dialog.findViewById(R.id.rel_lay_dia_weather_overview_remove_city);
        int width = displayData.getWidthPx()*8/10;
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,RelativeLayout.LayoutParams.WRAP_CONTENT,1);
        layout.setLayoutParams(params);

        // show dialog
        dialog.show();
    }

    public void cancel() {
        if (dialog != null) {dialog.cancel();}
    }
}
