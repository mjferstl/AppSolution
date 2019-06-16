package mfdevelopement.appsolution;

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
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.Locale;

public class Main extends AppCompatActivity {

    private String appname = "";
    private String LogTag = "";
    private String NotIncludedYet = "";
    private String languageChoosen = "";
    private String languageDevice = "";

    private String SharedPrefsName_Language = "SharedPrefs_userLanguage";
    private String USERLANGUAGE = "userLanguage";

    private Button btn_language_Ok = null;

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

    private void initButtons() {

        // Cochtails
        ImageButton btnCocktails = findViewById(R.id.btn_main_cocktails);
        btnCocktails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(CocktailOverview.class);
            }
        });

        // Mensa STWNO
        ImageButton btnMensa = findViewById(R.id.btn_main_mensa);
        btnMensa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(MensaSpeiseplan.class);
            }
        });

        // geometry
        ImageButton btnGeometry = findViewById(R.id.btn_main_geometry_calculator);
        btnGeometry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(GeometryCalculator.class);
            }
        });

        // mechanical engineering
        ImageButton btnMechEng = findViewById(R.id.btn_main_mechanical_engineering);
        btnMechEng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(MechanicalEngineering.class);
            }
        });

        // currency converter
        ImageButton btnCurrencyConverter = findViewById(R.id.btn_main_currency_calculator);
        btnCurrencyConverter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(CurrencyConverter.class);
            }
        });

        // sensors
        ImageButton btnSensors = findViewById(R.id.btn_main_sensors);
        btnSensors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(Sensors.class);
            }
        });

        // library OTH and univertiy regensburg
        ImageButton btnLibrary = findViewById(R.id.btn_main_regensburger_katalog);
        btnLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(RegensburgerKatalog.class);
            }
        });

        // news
        ImageButton btnNews = findViewById(R.id.btn_main_news);
        btnNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(News.class);
            }
        });

        // dress size
        ImageButton btnDressSize = findViewById(R.id.btn_main_clothes_size);
        btnDressSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(ClothesSize.class);
            }
        });

        // weather
        ImageButton btnWeather = findViewById(R.id.btn_main_weather);
        btnWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(WeatherOverview.class);
            }
        });
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

    public void openInternetRadio(View view) {
        Intent intent = new Intent(Main.this, InternetRadio.class);
        Log.i(LogTag,"start activity Internetradio");
        startActivity(intent);
        //Toast.makeText(this, NotIncludedYet, Toast.LENGTH_SHORT).show();
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
        dialog.setContentView(R.layout.select_language);

        btn_language_Ok = dialog.findViewById(R.id.btn_language_ok);
        btn_language_Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocale(languageChoosen);
                dialog.dismiss();
            }
        });

        RadioGroup rg = dialog.findViewById(R.id.rdgr_select_language);

        String actLang = getResources().getConfiguration().locale.toString();
        Log.i(LogTag,actLang);
        RadioButton rbLangDE = rg.findViewById(R.id.lang_de);
        RadioButton rbLangEN = rg.findViewById(R.id.lang_en);

        Log.i(LogTag,"Language: actLang = " + actLang);

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

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int childCount = group.getChildCount();
                for (int x = 0; x < childCount; x++) {
                    RadioButton btn = (RadioButton) group.getChildAt(x);
                    if (btn.getId() == checkedId) {
                        Log.i(LogTag,"selected RadioButton ->" + btn.getText().toString());
                        switch (btn.getId()) {
                            case R.id.lang_de:
                                languageChoosen = "de";
                                break;
                            case R.id.lang_en:
                                languageChoosen = "en";
                                break;
                        }
                    }
                }
            }
        });
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

