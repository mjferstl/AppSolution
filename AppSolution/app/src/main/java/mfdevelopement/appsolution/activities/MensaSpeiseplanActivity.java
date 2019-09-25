package mfdevelopement.appsolution.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RadioButton;
import android.widget.Toast;

import mfdevelopement.appsolution.R;

public class MensaSpeiseplanActivity extends AppCompatActivity {

    /**
     * Initialisierung Variablen
     */

    private int iInitialHeight = 0;

    /**
     * Deklaration und Initialisierung der Widgets
     */
    // Radio Button
    private RadioButton buttonOTH = null;
    private RadioButton buttonUni = null;

    // urls of websites
    private final String URL_OTH_MENSA_DAILY = "http://www.stwno.my-mensa.de/index.php?v=4759748&hyp=1#m_hs_reg_tage";
    private final String URL_UNI_MENSA_DAILY = "http://www.stwno.my-mensa.de/index.php?v=4760332&hyp=1#m_uni_reg_tage";

    SharedPreferences myUni;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensa_speiseplan);

        webView = findViewById(R.id.mensa_webView);
        iInitialHeight = webView.getHeight();

        checkInternetStatus();

        myUni = getSharedPreferences("myUni1", 0);
        String myUniReturned = myUni.getString("myUni", "error");
        switch (myUniReturned) {
            case "OTH":
                this.buttonOTH = findViewById(R.id.mensa_button_OTH);
                buttonOTH.setChecked(true);
                showMensaOth();
                break;
            case "Uni":
                this.buttonUni = findViewById(R.id.mensa_button_Uni);
                buttonUni.setChecked(true);
                showMensaUni();
                break;
        }
    }

    public void checkInternetStatus() {
        ConnectivityManager cm = (ConnectivityManager) MensaSpeiseplanActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                webView.setVisibility(View.VISIBLE);
                webView.setMinimumHeight(iInitialHeight);
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to the mobile provider's data plan
                webView.setVisibility(View.VISIBLE);
                webView.setMinimumHeight(iInitialHeight);
            }
        } else {
            // not connected to the internet
            webView.setVisibility(View.INVISIBLE);
            webView.setMinimumHeight(0);
            Toast.makeText(MensaSpeiseplanActivity.this, getText(R.string.txt_no_internet_connection), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        if (webView.copyBackForwardList().getCurrentIndex() > 0) {
            webView.goBack();
        } else {
            // Your exit alert code, or alternatively line below to finish
            super.onBackPressed(); // finishes activity
        }
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        checkInternetStatus();
        switch (view.getId()) {
            case R.id.mensa_button_OTH:
                if (checked)
                    showMensaOth();
                break;
            case R.id.mensa_button_Uni:
                if (checked)
                    showMensaUni();
                break;
        }
    }

    public void showMensaOth() {
        myUni = getSharedPreferences("myUni1", 0);
        SharedPreferences.Editor editor = myUni.edit();
        editor.clear();
        editor.putString("myUni", "OTH");
        editor.apply();
        webView = findViewById(R.id.mensa_webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(URL_OTH_MENSA_DAILY);
        webView.measure(100, 100);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    public void showMensaUni() {

        myUni = getSharedPreferences("myUni1", 0);
        SharedPreferences.Editor editor = myUni.edit();
        editor.clear();
        editor.putString("myUni", "Uni");
        editor.apply();
        webView = findViewById(R.id.mensa_webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(URL_UNI_MENSA_DAILY);
        webView.measure(100, 100);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }
}
