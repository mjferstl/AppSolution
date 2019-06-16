package mfdevelopement.appsolution.device.general;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public class DisplayData {

    private final int widthPx, heightPx;

    public DisplayData(Context context) {

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);

        this.widthPx = metrics.widthPixels;
        this.heightPx = metrics.heightPixels;
    }

    /**
     * get the device screen width in pixels
     * @return int
     */
    public int getWidthPx() {
        return widthPx;
    }

    /**
     * get the device screen height in pixels
     * @return int
     */
    public int getHeightPx() {
        return heightPx;
    }

}
