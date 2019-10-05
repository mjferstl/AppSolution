package mfdevelopement.appsolution.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.List;

import mfdevelopement.appsolution.R;
import mfdevelopement.appsolution.activities.WeatherOverviewActivity;
import mfdevelopement.appsolution.device.general.DisplayData;

public class DialogWeatherCities implements DialogInterface {

    private final String LOG_TAG = "DialogWeatherCities";

    private Context context;

    private List<String> cityNames;

    private Dialog dialog;

    public DialogWeatherCities(Context context, List<String> cityNames){
        this.context = context;
        this.cityNames = cityNames;
        this.dialog = new Dialog(this.context);
    }

    public void show() {

        Log.d(LOG_TAG,"DialogWeatherCities: creating dialog");

        // no dialog title
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // load content
        dialog.setContentView(R.layout.dialog_weather_add_city);

        Log.d(LOG_TAG,"DialogWeatherCities: adding city names to listView");
        ListView listViewCities = dialog.findViewById(R.id.lv_weather_cities);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this.context, android.R.layout.simple_list_item_1, cityNames);
        listViewCities.setAdapter(adapter);

        Log.d(LOG_TAG,"DialogWeatherCities: adding OnClickViewListeners");
        listViewCities.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(LOG_TAG,"user clicked item " + (position+1) + " of filtered listView");
                String strCity = adapter.getItem(position);
                Log.i(LOG_TAG, "OnCLickListener:add city " + strCity);
                int index = cityNames.indexOf(strCity);
                Log.d(LOG_TAG,"index of " + strCity + " in citiesList is " + index);

                WeatherOverviewActivity weatherOverviewActivity = (WeatherOverviewActivity)context;
                int cityCode = weatherOverviewActivity.getCityCode(index);
                weatherOverviewActivity.addUserCityCode(cityCode);
                dialog.dismiss();
            }
        });

        // adjust height of the listview
        Log.d(LOG_TAG,"DialogWeatherCities: adjusting height of the dialog");
        DisplayData displayData = new DisplayData(this.context);
        ViewGroup.LayoutParams listViewForecastLayoutParams = listViewCities.getLayoutParams();
        listViewForecastLayoutParams.height = (int) (displayData.getHeightPx() * 0.65);
        listViewCities.setLayoutParams(listViewForecastLayoutParams);

        Log.d(LOG_TAG,"DialogWeatherCities: adding TextChangeListener to ListView");
        final EditText etFilter = dialog.findViewById(R.id.et_dia_weather_add_city);
        etFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                adapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        Log.d(LOG_TAG,"DialogWeatherCities: setOnClickListener to dismiss button");
        final Button btnDismiss = dialog.findViewById(R.id.btn_dia_weather_add_city_dismiss);
        btnDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Log.d(LOG_TAG,"DialogWeatherCities: changing width of dialog");
        // change the dialog width to 80% of the screen width
        LinearLayout layout = dialog.findViewById(R.id.lin_lay_dia_weather_add_city);
        int width = displayData.getWidthPx() * 8 / 10;
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        layout.setLayoutParams(params);

        // show the dialog
        Log.i(LOG_TAG,"DialogWeatherCities: show the dialog");
        dialog.show();
    }

    public void cancel() {
        dialog.cancel();
    }

    public void dismiss() {
        dialog.dismiss();
    }
}