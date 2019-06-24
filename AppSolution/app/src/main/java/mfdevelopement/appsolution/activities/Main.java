package mfdevelopement.appsolution.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import mfdevelopement.appsolution.R;
import mfdevelopement.appsolution.device.general.DisplayData;

public class Main extends AppCompatActivity {

    private String appname = "";
    private String LogTag = "";
    private String NotIncludedYet = "";
    private String languageChoosen = "";
    private String languageDevice = "";

    private String SharedPrefsName_Language = "SharedPrefs_userLanguage";
    private String USERLANGUAGE = "userLanguage";

    private Button btn_language_Ok = null;

    private ImageButton btnCocktails, btnMensa, btnGeometry, btnMechEng,
            btnCurrencyConverter, btnSensors, btnLibrary, btnNews, btnDressSize, btnWeather;
    private List<ImageButton> imageButtonList = new ArrayList<>();
    private List<Class> targetActivities = new ArrayList<>();

    SharedPreferences userLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appname = getString(R.string.app_name);
        LogTag = appname + "/Main";
        NotIncludedYet = getString(R.string.error_not_included_yet);

        userLanguage = getSharedPreferences(SharedPrefsName_Language, Context.MODE_PRIVATE);
        String userLang = userLanguage.getString(USERLANGUAGE,Locale.getDefault().getDisplayLanguage());
        setLocale(userLang);

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

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(R.string.alert_exit_title);
        alertDialogBuilder
                .setMessage(R.string.alert_exit)
                .setCancelable(true)
                .setPositiveButton(R.string.alert_yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        moveTaskToBack(true);
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);
                    }
                })

                .setNegativeButton(R.string.alert_no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
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
                chooseLanguage();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * opens a dialog to change the language of the app
     */
    private void chooseLanguage() {

        // custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_select_language);

        final RadioGroup rg = dialog.findViewById(R.id.rdgr_select_language);
        final RadioButton rbLangDE = rg.findViewById(R.id.lang_de);
        final RadioButton rbLangEN = rg.findViewById(R.id.lang_en);

        btn_language_Ok = dialog.findViewById(R.id.btn_language_ok);
        btn_language_Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rbLangDE.isChecked()) {
                    setLocale("de");
                }
                else if (rbLangEN.isChecked()) {
                    setLocale("en");
                }

                dialog.dismiss();
            }
        });

        // set radio buttons depending on current language selection
        String actLang = getResources().getConfiguration().locale.toString();
        Log.i(LogTag,"current language: " + actLang);
        switch (actLang) {
            case "de":
                rbLangDE.setChecked(true);
                break;
            case "en":
                rbLangEN.setChecked(true);
                break;
            case "en_US":
                rbLangEN.setChecked(true);
        }

        dialog.show();
    }

    /**
     * method to change to language of the app
     * @param lang: string containing the language code
     */
    private void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            conf.setLocale(myLocale);
        } else{
            conf.locale=myLocale;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            getApplicationContext().createConfigurationContext(conf);
        } else {
            res.updateConfiguration(conf,dm);
        }

        SharedPreferences.Editor editor = userLanguage.edit();
        editor.putString(USERLANGUAGE,lang);
        editor.apply();

        Log.i(LogTag,"Language changed to " + lang);
    }
}