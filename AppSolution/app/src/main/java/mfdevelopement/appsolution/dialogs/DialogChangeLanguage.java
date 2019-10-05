package mfdevelopement.appsolution.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import mfdevelopement.appsolution.R;
import mfdevelopement.appsolution.device.general.DisplayData;
import mfdevelopement.appsolution.helper.ChangeLanguage;

public class DialogChangeLanguage implements DialogInterface {

    private Context context;
    private Dialog dialog;
    private final String LOG_TAG = "DialogChangeLanguage";

    private final String GERMAN = "de";
    private final String ENGLISH = "en";

    public DialogChangeLanguage(Context context) {
        this.context = context;
        this.dialog = new Dialog(this.context);
    }

    public void show() {

        // creeate a new dialog
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_select_language);

        // initialize radio group and radio buttons
        final RadioGroup rg = dialog.findViewById(R.id.rdgr_select_language);
        final RadioButton rbLangDE = rg.findViewById(R.id.lang_de);
        final RadioButton rbLangEN = rg.findViewById(R.id.lang_en);

        // get the current language
        final String currentLanguage = this.context.getResources().getConfiguration().locale.toString();

        // initialize the OK-Button
        Button btn_language_Ok = dialog.findViewById(R.id.btn_language_ok);
        btn_language_Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lang;
                if (rbLangDE.isChecked() && !currentLanguage.equals(GERMAN)) {
                    lang = GERMAN;
                } else if (rbLangEN.isChecked() && !currentLanguage.equals(ENGLISH)) {
                    lang = ENGLISH;
                } else {
                    dialog.dismiss();
                    return;
                }

                // change the app's language
                ChangeLanguage.setLanguage(context,lang);

                // dismiss the dialog
                dialog.dismiss();
            }
        });

        // set radio buttons depending on current language selection
        Log.d(LOG_TAG, "chooseLanguage:current language: " + currentLanguage);
        switch (currentLanguage) {
            case GERMAN:
                rbLangDE.setChecked(true);
                break;
            case ENGLISH:
            case "en_US":
                rbLangEN.setChecked(true);
                break;
        }

        // change the dialog width to 80% of the screen width
        DisplayData displayData = new DisplayData(this.context);
        LinearLayout layout = dialog.findViewById(R.id.select_language_layout);
        int width = displayData.getWidthPx() * 8 / 10;
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        layout.setLayoutParams(params);

        // show the dialog
        dialog.show();
        Log.d(LOG_TAG, "show:show the dialog");
    }

    @Override
    public void cancel() {
        if (dialog != null)
            dialog.cancel();
    }

    @Override
    public void dismiss() {
        if (dialog != null)
            dialog.dismiss();
    }
}