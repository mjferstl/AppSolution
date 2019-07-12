package mfdevelopement.appsolution.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import mfdevelopement.appsolution.R;
import mfdevelopement.appsolution.device.general.DisplayData;
import mfdevelopement.appsolution.dialogs.DialogChangeLanguage;
import mfdevelopement.appsolution.dialogs.DialogExitApp;

public class Main extends AppCompatActivity {

    private String appname = "";
    private final String LOG_TAG = "Main";
    private String NotIncludedYet = "";

    private Button btn_language_Ok = null;

    private ImageButton btnCocktails, btnMensa, btnGeometry, btnMechEng,
            btnCurrencyConverter, btnSensors, btnLibrary, btnNews, btnDressSize, btnWeather;

    private List<ImageButton> imageButtonList = new ArrayList<>();
    private List<Class> targetActivities = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appname = getString(R.string.app_name);
        NotIncludedYet = getString(R.string.error_not_included_yet);

        // set language
        //ChangeLanguage.loadLanguage(this);

        // initialize buttons
        initButtons();
    }

    /**
     * initialize all buttons
     */
    private void initButtons() {

        // find image buttons by id
        btnCocktails = findViewById(R.id.btn_main_cocktails);
        btnMensa = findViewById(R.id.btn_main_mensa);
        btnGeometry = findViewById(R.id.btn_main_geometry_calculator);
        btnMechEng = findViewById(R.id.btn_main_mechanical_engineering);
        btnCurrencyConverter = findViewById(R.id.btn_main_currency_converter);
        btnSensors = findViewById(R.id.btn_main_sensors);
        btnDressSize = findViewById(R.id.btn_main_clothes_size);
        btnWeather = findViewById(R.id.btn_main_weather);

        // add image buttons and their target activities to lists
        imageButtonList.add(btnCocktails);
        targetActivities.add(CocktailOverview.class);

        imageButtonList.add(btnMensa);
        targetActivities.add(MensaSpeiseplan.class);

        imageButtonList.add(btnGeometry);
        targetActivities.add(GeometryCalculator.class);

        imageButtonList.add(btnMechEng);
        targetActivities.add(MechanicalEngineering.class);

        imageButtonList.add(btnCurrencyConverter);
        targetActivities.add(CurrencyConverter.class);

        imageButtonList.add(btnSensors);
        targetActivities.add(Sensors.class);

        imageButtonList.add(btnDressSize);
        targetActivities.add(ClothesSize.class);

        imageButtonList.add(btnWeather);
        targetActivities.add(WeatherOverviewActivity.class);

        // set all onClickListeners
        for (int i=0; i<imageButtonList.size(); i++) {

            // if no class is specified, no onClickListener is initialized
            if (targetActivities.get(i) == null) {
                imageButtonList.get(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(Main.this,"Noch keine Funktion hinterlegt ... ",Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else {
                final Class activity = targetActivities.get(i);
                imageButtonList.get(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openActivity(activity);
                    }
                });
            }
        }

        changeButtonsSize();
    }

    private void changeButtonsSize() {

        // create DisplayData Object and get screen sizes in pixels
        DisplayData displayData = new DisplayData(this);
        int width = displayData.getWidthPx();
        int height = displayData.getHeightPx();

        int btnWidth = width/4;

        for (int i=0; i<imageButtonList.size(); i++) {
            setImageButtonSize(imageButtonList.get(i),btnWidth);
        }
    }

    private void setImageButtonSize(ImageButton btn, int width) {

        int height = width;

        // Get the last ImageButton's layout parameters
        ViewGroup.LayoutParams params = btn.getLayoutParams();

        // Set the height of this ImageButton
        params.height = height;

        // Set the width of that ImageButton
        params.width = width;

        // Apply the updated layout parameters to last ImageButton
        btn.setLayoutParams(params);

        // Set the ImageButton image scale type for fourth ImageButton
        btn.setScaleType(ImageButton.ScaleType.FIT_XY);
    }

    /**
     * opens a new activity
     * @param cl: Class to be opened
     */
    private void openActivity(Class cl) {
        Intent intent = new Intent(Main.this, cl);
        startActivity(intent);
    }

    /**
     * opens a dialog asking the user if he wants to exit the app
     */
    public void onBackPressed(){
        DialogExitApp dia = new DialogExitApp(this);
        dia.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_about:
                Intent intent = new Intent(this, About.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                return true;
            case R.id.action_change_language:
                changeLanguage();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void changeLanguage(){
        DialogChangeLanguage dia = new DialogChangeLanguage(this);
        dia.show();
    }
}