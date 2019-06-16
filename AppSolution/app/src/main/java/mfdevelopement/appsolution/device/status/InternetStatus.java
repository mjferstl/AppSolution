package mfdevelopement.appsolution.device.status;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class InternetStatus {

    private Context c;
    public final static String MOBILE = "MOBILE";
    public final static String WIFI = "WIFI";
    public final static String NOT_CONNECTED = "NONE";

    public InternetStatus(Context context) {
        this.c = context;
    }

    public boolean getInternetStatus() {

        boolean connected = false;
        switch (getConnectionType()) {
            case (WIFI):
                connected = true;
                break;
            case MOBILE:
                connected = true;
                break;
            case NOT_CONNECTED:
                connected = false;
                break;
        }
        return connected;
    }

    public boolean isConnected() {
        return getInternetStatus();
    }

    public String getConnectionType() {

        String type = "";

        ConnectivityManager cm = (ConnectivityManager) this.c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                type = WIFI;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to the mobile provider's data plan
                type = MOBILE;
            }
        } else {
            // not connected to the internet
            type = NOT_CONNECTED;
        }
        return type;
    }
}
