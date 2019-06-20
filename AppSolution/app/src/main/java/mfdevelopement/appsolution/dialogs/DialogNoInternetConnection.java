package mfdevelopement.appsolution.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;

import mfdevelopement.appsolution.R;
import mfdevelopement.appsolution.activities.Main;

public class DialogNoInternetConnection implements DialogInterface{

    private Context context;
    private String LogTag = "DialogNoInternetConnection";
    private AlertDialog dialog;

    public DialogNoInternetConnection(Context c) {
        this.context = c;
    }

    public void show() {
        showDialog();
    }

    private void showDialog() {

        Log.i(LogTag,"showDialog:DialogNoInternetConnection");
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.context);
        alertDialogBuilder.setTitle(R.string.no_internet_connection);
        alertDialogBuilder
                .setMessage(R.string.no_internet_connection_text)
                .setCancelable(true)
                .setPositiveButton(R.string.btd_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        returnToMainActivity();
                    }
                });

        dialog = alertDialogBuilder.create();
        dialog.show();
    }

    private void returnToMainActivity() {
        Intent intent = new Intent(this.context, Main.class);
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
