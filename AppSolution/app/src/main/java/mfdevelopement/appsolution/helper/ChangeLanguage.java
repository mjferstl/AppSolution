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

    /**
     * load language from the SharedPreferences and set it
     * @param context: Context of the calling class
     */
    public static void loadLanguage(Context context) {
        sharedPrefs = context.getSharedPreferences(SharedPrefsName, Context.MODE_PRIVATE);
        String userLang = sharedPrefs.getString(KEY_USER_LANGUAGE, Locale.getDefault().getDisplayLanguage());
        changeLanguage(context,new Locale(userLang));
    }

    /**
     * change the app's language
     * @param context: context of the calling class
     * @param locale: the desired locale to be set
     */
    private static void changeLanguage(Context context, Locale locale) {
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
    }

    /**
     * save the user's language selection to a SharedPreferences
     * @param context: context of the calling class
     * @param locale: selected locale
     */
    private static void saveUserLanguage(Context context, Locale locale) {
        sharedPrefs = context.getSharedPreferences(SharedPrefsName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(KEY_USER_LANGUAGE,locale.toString());
        editor.apply();
        Log.d(LOG_TAG,"saveUserLangguage:Language changed to " + locale.toString());
    }

    /**
     * recreate app to make the cahnges take effect on all sub-activities immideately
     * @param context: context of the calling class
     */
    private static void recreateActivity(Context context) {
        Intent intent = new Intent(context,context.getClass());
        ((Activity) context).finish();
        context.startActivity(intent);
        Log.i(LOG_TAG,"recreateActivity:recreating activity due to language change");
    }

    /**
     * method to be called from outside this class
     * @param context: context of the calling class
     * @param language: language as string as a result of Locale.toString()
     */
    public static void setLanguage(Context context,String language){
        Locale myLocale = new Locale(language);
        Locale loc = Locale.getDefault();

        if (myLocale.toString().equals(loc.toString())) {return;}

        changeLanguage(context,myLocale);
        saveUserLanguage(context,myLocale);
        recreateActivity(context);
    }
}
