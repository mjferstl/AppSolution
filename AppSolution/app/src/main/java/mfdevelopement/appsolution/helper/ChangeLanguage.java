package mfdevelopement.appsolution.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.Locale;

public class ChangeLanguage {

    private static final String LOG_TAG = "ChangeLanguage";
    private static final String SharedPrefsName = "SharedPrefs_userLanguage";
    private static final String KEY_USER_LANGUAGE = "sharedPrefs";

    private static SharedPreferences sharedPrefs;

    public static void loadLanguage(Context context) {
        sharedPrefs = context.getSharedPreferences(SharedPrefsName, Context.MODE_PRIVATE);
        String userLang = sharedPrefs.getString(KEY_USER_LANGUAGE, Locale.getDefault().getDisplayLanguage());
        setLanguage(context,userLang);
    }

    private static void setLanguage(Context context,Locale locale) {

        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            conf.setLocale(locale);
        } else{
            conf.locale = locale;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            context.getApplicationContext().createConfigurationContext(conf);
        } else {
            res.updateConfiguration(conf,dm);
        }

        // recreate the activity to make the changes take effect
        recreateActivity(context);
    }

    private static void recreateActivity(Context context) {
        Intent intent = new Intent(context,context.getClass());
        ((Activity) context).finish();
        context.startActivity(intent);
    }

    public static void setLanguage(Context context,String language){
        Locale myLocale = new Locale(language);
        setLanguage(context,myLocale);

        sharedPrefs = context.getSharedPreferences(SharedPrefsName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(KEY_USER_LANGUAGE,language);
        editor.apply();

        Log.i(LOG_TAG,"setLocale:Language changed to " + language);
    }
}
