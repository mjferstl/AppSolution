package mfdevelopement.appsolution.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import mfdevelopement.appsolution.R;
import mfdevelopement.appsolution.activities.AboutIconsActivity;
import mfdevelopement.appsolution.device.general.DisplayData;
import mfdevelopement.appsolution.models.IconCredit;

public class DialogAboutIconCreditsLinks implements DialogInterface {

    private AboutIconsActivity context;
    private IconCredit iconCredit;
    private final String LOG_TAG = "DialogAboutIconCreditsL";
    private Dialog dialog;
    private int numLinks = 0;

    public DialogAboutIconCreditsLinks(AboutIconsActivity c, IconCredit iconCredit) {
        this.context = c;
        this.iconCredit = iconCredit;
    }

    /**
     * show a dialog containing the links to author,
     */
    public void show() {

        Log.d(LOG_TAG,"DialogAboutIconCreditsLinks.show() called. Creating Dialog now ...");

        dialog = new Dialog(this.context);

        // no dialog title
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // content
        dialog.setContentView(R.layout.dialog_credit_links);

        // get reference to the buttons
        Button btnLinkAuthor = dialog.findViewById(R.id.btn_about_icon_credits_link_author);
        Button btnLinkSource = dialog.findViewById(R.id.btn_about_icon_credits_link_source);
        Button btnLinkLicense = dialog.findViewById(R.id.btn_about_icon_credits_link_license);
        // reference to TextView
        TextView txtvAuthor = dialog.findViewById(R.id.txtv_about_icon_credit_dialog_author);
        TextView txtvSource = dialog.findViewById(R.id.txtv_about_icon_credit_dialog_source);
        TextView txtvLicense = dialog.findViewById(R.id.txtv_about_icon_credit_dialog_license);
        //
        TableRow tableRowAuthor = dialog.findViewById(R.id.table_row_about_icon_credit_dialog_author);
        TableRow tableRowSource = dialog.findViewById(R.id.table_row_about_icon_credit_dialog_source);
        TableRow tableRowLicense = dialog.findViewById(R.id.table_row_about_icon_credit_dialog_license);

        // change the dialog width to 80% of the screen width
        DisplayData displayData = new DisplayData(this.context);
        RelativeLayout layout = dialog.findViewById(R.id.rel_lay_dia_about_icon_credits_links);
        int width = displayData.getWidthPx()*8/10;
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,RelativeLayout.LayoutParams.WRAP_CONTENT,1);
        layout.setLayoutParams(params);

        numLinks = 0;

        // author
        String authorName = iconCredit.getAuthor().getName();
        String authorLink = iconCredit.getAuthor().getLink();
        if (authorName != null && !authorName.equals("") && authorLink != null && !authorLink.equals("")) {
            btnLinkAuthor.setText(authorName);
            btnLinkAuthor.setOnClickListener(createOnClickListenerUri(authorLink));
            btnLinkAuthor.setVisibility(View.VISIBLE);
            txtvAuthor.setVisibility(View.VISIBLE);
            tableRowAuthor.setVisibility(View.VISIBLE);
            numLinks += 1;
        } else {
            btnLinkAuthor.setVisibility(View.GONE);
            txtvAuthor.setVisibility(View.GONE);
            tableRowAuthor.setVisibility(View.GONE);
            Log.d(LOG_TAG,"reference to author cannot be created");
        }

        // source
        String sourceName = iconCredit.getTitleCredits().getTitle();
        String sourceLink = iconCredit.getTitleCredits().getLink();
        if (sourceName != null && !sourceName.equals("") && sourceLink != null && !sourceLink.equals("")) {
            btnLinkSource.setText(sourceName);
            btnLinkSource.setOnClickListener(createOnClickListenerUri(sourceLink));
            btnLinkSource.setVisibility(View.VISIBLE);
            txtvSource.setVisibility(View.VISIBLE);
            tableRowSource.setVisibility(View.VISIBLE);
            numLinks += 1;
        } else {
            btnLinkSource.setVisibility(View.GONE);
            txtvSource.setVisibility(View.GONE);
            tableRowSource.setVisibility(View.GONE);
            Log.d(LOG_TAG,"reference to source cannot be created");
        }

        // license
        String licenseName = iconCredit.getLicense().getLicenseName();
        String licenseLink = iconCredit.getLicense().getLicenseLink();
        if (licenseName != null && !licenseName.equals("") && licenseLink != null && !licenseLink.equals("")) {
            btnLinkLicense.setText(licenseName);
            btnLinkLicense.setOnClickListener(createOnClickListenerUri(licenseLink));
            btnLinkLicense.setVisibility(View.VISIBLE);
            txtvLicense.setVisibility(View.VISIBLE);
            tableRowLicense.setVisibility(View.VISIBLE);
            numLinks += 1;
        } else {
            btnLinkLicense.setVisibility(View.GONE);
            txtvLicense.setVisibility(View.GONE);
            tableRowLicense.setVisibility(View.GONE);
            Log.d(LOG_TAG,"reference to license cannot be created");
        }

        Log.d(LOG_TAG,numLinks + " buttons visible");

        // show dialog
        if (numLinks < 0)
            Log.d(LOG_TAG,"show Dialog now");
            dialog.show();
    }

    /**
     * create View.OnClickListener with reference to an internet adress
     * @param link: String containing the internet adress
     * @return View.OnClickListener
     */
    private View.OnClickListener createOnClickListenerUri(final String link) {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(link); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    context.startActivity(intent);
                    cancel();
                } catch (Exception e) {
                    Toast.makeText(context,"link corrupt",Toast.LENGTH_SHORT).show();
                }
            }
        };
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
