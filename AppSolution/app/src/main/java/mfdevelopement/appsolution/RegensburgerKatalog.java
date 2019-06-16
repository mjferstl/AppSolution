package mfdevelopement.appsolution;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class RegensburgerKatalog extends AppCompatActivity {

    WebView webView;

    private int iInitialHeight = 0;

    private boolean booConnected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regensburger_katalog);

        webView = findViewById(R.id.bib_webView);
        iInitialHeight = webView.getHeight();

        checkInternetStatus();

        if (!booConnected) {
            Toast.makeText(RegensburgerKatalog.this, getText(R.string.txt_no_internet_connection), Toast.LENGTH_SHORT).show();
        }
        else {
            String url = "https://www.regensburger-katalog.de/TouchPoint/start.do?View=ubrfhr&Language=de&Branch=2";
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl(url);
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

    public void checkInternetStatus()
    {
        ConnectivityManager cm = (ConnectivityManager) RegensburgerKatalog.this.getSystemService(Context.CONNECTIVITY_SERVICE);
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
            booConnected = true;
        } else {
            // not connected to the internet
            webView.setVisibility(View.INVISIBLE);
            webView.setMinimumHeight(0);
            Toast.makeText(RegensburgerKatalog.this, getText(R.string.txt_no_internet_connection), Toast.LENGTH_SHORT).show();

            booConnected = false;
        }
    }

    @Override
    public void onBackPressed() {
        if (webView.copyBackForwardList().getCurrentIndex() > 0) {
            webView.goBack();
        }
        else {
            // Your exit alert code, or alternatively line below to finish
            super.onBackPressed(); // finishes activity
        }
    }

}
