package mfdevelopement.appsolution.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import mfdevelopement.appsolution.R;

public class DialogExitApp implements DialogInterface{

    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;
    private final Context context;
    private final String LOG_TAG = "DialogExitApp";

    public DialogExitApp(Context context) {
        this.context = context;
        this.alertDialogBuilder = new AlertDialog.Builder(this.context);
    }

    /**
     * create and show the dialog
     */
    public void show() {

        // title of alert
        alertDialogBuilder.setTitle(R.string.alert_exit_title);

        // content of alert
        alertDialogBuilder
                .setMessage(R.string.alert_exit)
                .setCancelable(true)
                .setPositiveButton(R.string.alert_yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //moveTaskToBack(true);
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);
                    }
                })
                .setNegativeButton(R.string.alert_no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        // new AlertDialog object
        alertDialog = alertDialogBuilder.create();

        // show the dialog
        Log.d(LOG_TAG,"show:show the dialog");
        alertDialog.show();
    }

    @Override
    public void cancel() {
        if (alertDialog != null)
            alertDialog.cancel();
    }

    @Override
    public void dismiss() {
        if (alertDialog != null)
            alertDialog.dismiss();
    }
}
