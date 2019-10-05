package mfdevelopement.appsolution.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;

import mfdevelopement.appsolution.R;
import mfdevelopement.appsolution.activities.MainActivity;

public class DialogNoInternetConnection implements DialogInterface{

    private Context context;
    private final String LOG_TAG = "DialogNoInternetConnect";
    private AlertDialog dialog;

    public DialogNoInternetConnection(Context c) {
        this.context = c;

        Log.i(LOG_TAG,"DialogNoInternetConnection. Creating Dialog.");
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.context);
        alertDialogBuilder.setTitle(R.string.no_internet_connection);
        alertDialogBuilder
                .setMessage(R.string.txt_connect_to_internet_please)
                .setCancelable(true)
                .setPositiveButton(R.string.btd_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        returnToMainActivity();
                    }
                });

        dialog = alertDialogBuilder.create();
        Log.i(LOG_TAG,"DialogNoInternetConnection ready to show.");
    }

    public void show() {
        dialog.show();
    }

    private void returnToMainActivity() {
        Intent intent = new Intent(this.context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        cancel();
        ((Activity) context).finish();
        context.startActivity(intent);
    }

    public void cancel() {
        dialog.cancel();
    }

    public void dismiss() {
        dialog.dismiss();
    }

}
