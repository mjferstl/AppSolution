package mfdevelopement.appsolution.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import mfdevelopement.appsolution.R;
import mfdevelopement.appsolution.device.status.InternetStatus;

// Verwendetes Tutorial:
// https://www.youtube.com/watch?v=GpyWS2Jgag8
// https://www.youtube.com/watch?v=X0n8TSI3QOU (neuer)

public class CurrencyConverter extends AppCompatActivity implements View.OnClickListener {

    private String strCurrencyFrom = "";
    private String strCurrencyTo = "";
    private String strFrom = "";
    private String strTo = "";
    private String SharedPrefName = "currencies";
    private String TIMESTAMP = "timestamp";
    // old service --> shut down
    //private String base_url1 = "http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.xchange%20where%20pair%20in%20(%22";
    //private String base_url2 = "%22,%20%22EURUSD%22)&format=json&env=store://datatables.org/alltableswithkeys";

    private String APIkey = "2c7cff2891f7cc11d05418d18bac05bb";
    private String baseURL = "http://data.fixer.io/api/latest?access_key=" + APIkey;
    // angemeldet mit frk@existiert.net

    private String strResult = "";
    private String enteredValue = "";
    private String kangaroo1 = "";
    private String kangaroo2 = "";
    private String kangaroo3 = "";
    private String kangaroo4 = "";
    private String kangaroo5 = "";
    private String kangaroo6 = "";
    private String EURO = "";
    private String USDOLLAR = "";
    private String appname = "";
    private String LogTag = "";
    private String ERRORCONVERT = "";

    private boolean useOldValues = false;

    private EditText txteValue;
    private Spinner spinner_from;
    private Spinner spinner_to;
    private Button btnConvert;
    private ImageView btnConvertKangaroo;
    private TextView txtvResult;
    private TextView txtvInternetStatus;
    private TextView txtvInternetInfo;

    private List<String> currenciesList = null;

    private Map<String, String> currenciesMap = new HashMap<>();

    private InternetStatus internetStatus = null;

    SharedPreferences defaultCurrencies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_converter);

        loadElements();
        loadStrings();

        String[] currenciesArray = getResources().getStringArray(R.array.currencies_array);
        currenciesList = Arrays.asList(currenciesArray);
        Collections.sort(currenciesList); //currenciesArray will be sorted

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, currenciesList);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner_from.setAdapter(adapter);
        spinner_to.setAdapter(adapter);

        defaultCurrencies = getSharedPreferences("defaultCurrencies", Context.MODE_PRIVATE);
        Log.i(LogTag, defaultCurrencies.getString("basicCurrency", "nix"));
        // set to default values
        spinner_from.setSelection(adapter.getPosition(defaultCurrencies.getString("basicCurrency", EURO)));
        spinner_to.setSelection(adapter.getPosition(defaultCurrencies.getString("targetCurrency", USDOLLAR)));

        // Mapping the currency description with the Keys
        String [] currencyNames = getResources().getStringArray(R.array.currencies_array);
        List<String> currList = Arrays.asList(currencyNames);
        String [] currencyKeys = getResources().getStringArray(R.array.currencyKeys_array);
        List<String> currKeys = Arrays.asList(currencyKeys);
        for (int i=0; i<currList.size(); i++) {
            currenciesMap.put(currList.get(i), currKeys.get(i));
            //Log.i(LogTag,"Mapping: " + currList.get(i) + " --> " + currKeys.get(i));
        }

        internetStatus = new InternetStatus(this);
        dispInternetStatus();

        if (internetStatus.isConnected()) {
            new loadCurrencies(SharedPrefName).execute();
        }
        else {
            SharedPreferences oldValues = getSharedPreferences(SharedPrefName,Context.MODE_PRIVATE);
            long lastUpdate = (long) oldValues.getFloat(TIMESTAMP,0)*1000;
            Log.i(LogTag,"last available update of currencies from timestamp:" + lastUpdate);
            if (lastUpdate == 0) {
                // parameter 1: message for alert dialog
                // parameter 2: should there be a negative button
                String msg = getString(R.string.txt_no_internet_connection);
                showInternetAlert(msg, false);
            }
            else {

                Date df = new java.util.Date(lastUpdate);
                String date = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.GERMANY).format(df);

                String msg = getString(R.string.txt_no_internet_connection) + " Old values from " + date +" are used";
                showInternetAlert(msg, true);
            }
        }
    }

    private void loadElements() {

        txteValue = findViewById(R.id.txte_currency_value);
        spinner_from = findViewById(R.id.currency_spinner_from);
        spinner_to = findViewById(R.id.currency_spinner_to);
        btnConvert = findViewById(R.id.btd_currency_convert);
        btnConvertKangaroo = findViewById(R.id.btd_currency_calc_kangaroo);
        txtvResult = findViewById(R.id.txtv_currency_result);
        txtvInternetStatus = findViewById(R.id.txtv_currency_internet_status);
        txtvInternetInfo = findViewById(R.id.txtv_currency_info);

        btnConvert.setOnClickListener(this);
        btnConvertKangaroo.setOnClickListener(this);
    }

    private void loadStrings() {

        kangaroo1 = getString(R.string.txt_kangaroo_1);
        kangaroo2 = getString(R.string.txt_kangaroo_2);
        kangaroo3 = getString(R.string.txt_kangaroo_3);
        kangaroo4 = getString(R.string.txt_kangaroo_4);
        kangaroo5 = getString(R.string.txt_kangaroo_5);
        kangaroo6 = getString(R.string.txt_kangaroo_6);

        EURO = getString(R.string.txt_currency_eur);
        USDOLLAR = getString(R.string.txt_currency_usd);

        appname = getString(R.string.app_name);
        LogTag = appname + "/CurrencyConverter";

        ERRORCONVERT = getString(R.string.txt_currency_convert_exchange_rate_error);
    }

    public void onClick(View view) {

        final int id = view.getId();
        switch (id) {
            case R.id.btd_currency_convert:
                calcCurrency();
                break;
            case R.id.btd_currency_calc_kangaroo:
                calcKangaroo();
                break;
        }
    }

    private void dispInternetStatus() {

        boolean isConnected = internetStatus.isConnected();
        String internetType = internetStatus.getConnectionType();

        if (isConnected) {
            if (internetType.equals(InternetStatus.WIFI)) {
                txtvInternetInfo.setVisibility(View.GONE);
                txtvInternetStatus.setText(getText(R.string.txt_connected_wifi));
                txtvInternetStatus.setTextColor(CurrencyConverter.this.getResources().getColor(R.color.DarkGreen));
            } else if (internetType.equals(InternetStatus.MOBILE)) {
                txtvInternetInfo.setVisibility(View.GONE);
                txtvInternetStatus.setText(getText(R.string.txt_connected_mobile));
                txtvInternetStatus.setTextColor(CurrencyConverter.this.getResources().getColor(R.color.DarkGreen));
            }
        } else {
            txtvInternetInfo.setVisibility(View.VISIBLE);
            txtvInternetStatus.setText(getText(R.string.txt_no_internet_connection));
            txtvInternetStatus.setTextColor(CurrencyConverter.this.getResources().getColor(R.color.DarkRed));
        }
    }

    public void calcCurrency() {

        // check internet status
        boolean connectedToInternet = internetStatus.isConnected();
        dispInternetStatus();

        // get the value the user entered
        enteredValue = txteValue.getText().toString();

        if (!connectedToInternet && !useOldValues) {
            strResult = "";
            txtvResult.setText(strResult);
            Toast.makeText(CurrencyConverter.this, getText(R.string.txt_no_internet_connection), Toast.LENGTH_SHORT).show();
        } else if (enteredValue.equals("") || enteredValue.equals(" ")) {
            strResult = "";
            txtvResult.setText(strResult);
            Toast.makeText(CurrencyConverter.this, getText(R.string.txt_enter_value), Toast.LENGTH_SHORT).show();
        } else {
            strCurrencyFrom = spinner_from.getSelectedItem().toString();
            strCurrencyTo = spinner_to.getSelectedItem().toString();

            double exchangeRate = convert();
            if (exchangeRate == 0) {
                showAlert(ERRORCONVERT);
            } else {
                double amount = Double.parseDouble(enteredValue);
                double result = amount * exchangeRate;

                String strResult = String.format(getResources().getConfiguration().locale,"%.6f", result);
                txtvResult.setText(strResult);
            }
            hideKeyboard();
        }
    }

    public void calcKangaroo() {

        // check the internet status and update the text therefore
        boolean connectedToInternet = internetStatus.isConnected();
        dispInternetStatus();

        // get the value the user entered
        enteredValue = txteValue.getText().toString();

        if (!connectedToInternet && !useOldValues) {
            strResult = "";
            txtvResult.setText(strResult);
            Toast.makeText(CurrencyConverter.this, getText(R.string.txt_no_internet_connection), Toast.LENGTH_SHORT).show();
        } else if (enteredValue.equals("") || enteredValue.equals(" ")) {
            strResult = "";
            txtvResult.setText(strResult);
            Toast.makeText(CurrencyConverter.this, getText(R.string.txt_enter_value), Toast.LENGTH_SHORT).show();
        } else {
            // get the currency
            strCurrencyFrom = spinner_from.getSelectedItem().toString();
            // always get the exchange rate for transfer to euro
            strCurrencyTo = EURO;

            // get the exchange rate from the internet and calculate the amount in the target currency
            double exchangeRate = convert();
            if (exchangeRate == 0) {
                showAlert(ERRORCONVERT);
            } else {
                double amount = Double.parseDouble(enteredValue);
                double result = amount * exchangeRate;
                double resultDMark = result * 2;
                double resultOstmark = result * 4;
                double resultOstmarkSchwarz = result * 20;

                String strResult = String.format(getResources().getConfiguration().locale,"%.2f", result);
                String strResultDMark = String.format(getResources().getConfiguration().locale,"%.2f", resultDMark);
                String strResultOstmark = String.format(getResources().getConfiguration().locale,"%.2f", resultOstmark);
                String strResultOstmarkSchwarz = String.format(getResources().getConfiguration().locale,"%.2f", resultOstmarkSchwarz);
                txtvResult.setText(strResult);

                String strText = kangaroo1 + "\n" + enteredValue + " " + strCurrencyFrom + " " + kangaroo2 + " " + strResultDMark + " " + kangaroo3 + "\n" + kangaroo4 + " " + strResultOstmark + " " + kangaroo5 + "\n" + kangaroo4 + " " + strResultOstmarkSchwarz + " " + kangaroo6;
                txtvResult.setText(strText);
            }

            // hide keyboard
            hideKeyboard();
        }
    }

    public void swapCurrencies(View view) {

        int idFromOld = spinner_from.getSelectedItemPosition();
        int idToOld = spinner_to.getSelectedItemPosition();

        spinner_from.setSelection(idToOld);
        spinner_to.setSelection(idFromOld);

        Log.i(LogTag, "Currencies swapped");
    }

    public double convert() {

        SharedPreferences sharedPrefs = getSharedPreferences(SharedPrefName, Context.MODE_PRIVATE);

        strFrom = getCountry(strCurrencyFrom);
        strTo = getCountry(strCurrencyTo);

        // write log
        Log.i(LogTag, "trying to convert from " + strFrom + " to " + strTo);

        Log.i(LogTag,sharedPrefs.toString());
        double resTo = sharedPrefs.getFloat(strTo, 0);
        double resFrom = sharedPrefs.getFloat(strFrom, 0);

        double rate = 0;

        if (!(resTo == 0) && !(resFrom == 0)) {
            rate = (resTo / resFrom);
        } else {
            Log.e(LogTag,"Error during calculation of exchange rate");
        }

        // write log
        Log.i(LogTag, "exchange rate calculated: " + rate);

        return rate;
    }

    private void showAlert(String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setPositiveButton(getString(R.string.btd_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private void showInternetAlert(String message, boolean negativeButton) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        if (negativeButton) {
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(CurrencyConverter.this, Main.class);
                    finish();
                    startActivity(intent);
                }
            })
                    .setPositiveButton(getString(R.string.btd_ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            useOldValues = true;
                            // close dialog
                        }
                    });
        }
        else {
            builder.setPositiveButton(getString(R.string.btd_ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // close dialog
                }
            });
        }
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    // Get the shortcuts for checking the currency
    private String getCountry(String string) {

        if (currenciesMap.get(string) == null) {
            return "error";
        }
        else {
            return currenciesMap.get(string);
        }
    }

    public void onBackPressed() {

        Intent intent = new Intent(this, Main.class);
        finish();
        startActivity(intent);
    }

    @Override
    public void onPause() {
        super.onPause();
        saveDefaultCurrencies();
    }

    // save the choosen currencies as default for next session
    public void saveDefaultCurrencies() {

        SharedPreferences.Editor ed = defaultCurrencies.edit();
        ed.putString("basicCurrency", spinner_from.getSelectedItem().toString());
        ed.apply();
        ed.putString("targetCurrency", spinner_to.getSelectedItem().toString());
        ed.apply();
    }

    private void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    private class loadCurrencies extends AsyncTask<String, Void, Map<String,Double>> {

        SharedPreferences sharedPref;
        private Map<String,Double> map =  new HashMap<>();

        protected void onPreExecute() {
            super.onPreExecute();
        }

        private loadCurrencies(String string) {
            this.sharedPref = getSharedPreferences(string, Context.MODE_PRIVATE);
        }

        @Override
        protected Map<String, Double> doInBackground(String... strings) {
            String URL;
            try {
                URL = getJson(baseURL);

                boolean success = new JSONObject(URL).getBoolean("success");
                if (!success) {
                    Log.i(LogTag, "Response for currencies from fixer.io not successful");
                    Toast.makeText(CurrencyConverter.this, "Response not successful. Contact developer!", Toast.LENGTH_SHORT).show();
                } else {
                    Log.i(LogTag, "Response for currencies from fixer.io successful!");
                    // get values out of JSON response
                    JSONObject euroRates = new JSONObject(URL).getJSONObject("rates");
                    for (String country : currenciesList) {
                        String countyCode = getCountry(country);
                        Double val = Double.parseDouble(euroRates.get(countyCode).toString());
                        Log.i(LogTag,"Loaded currencies: country: " + country + "; exchange rate = " + val);
                        map.put(country,val);
                    }
                    long timestamp = new JSONObject(URL).getInt("timestamp");
                    Date df = new java.util.Date(timestamp*1000);
                    String date = new SimpleDateFormat("dd-MM-yyyy hh:mma").format(df);
                    Log.i(LogTag,"last update of currecies: " + date + "; " + timestamp);
                    map.put(TIMESTAMP,(double)timestamp);
                }
            } catch (JSONException e) {
                    e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return map;
        }

        @Override
        protected void onPostExecute(Map<String,Double> maps) {
            //
            SharedPreferences.Editor editor = this.sharedPref.edit();
            for (String country : currenciesList) {
                String countryKey = getCountry(country);
                Double val = map.get(country);
                editor.putFloat(countryKey, Float.parseFloat(val.toString()));
                editor.apply();
            }
            editor.putFloat(TIMESTAMP, Float.parseFloat(map.get(TIMESTAMP).toString()));
            editor.apply();
        }


        private String getJson(String url) throws ClientProtocolException, IOException {
            StringBuilder build = new StringBuilder();
            HttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            HttpResponse response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            InputStream content = entity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(content));
            String con;
            while ((con = reader.readLine()) != null) {
                build.append(con);
            }
            return build.toString();
        }
    }


}