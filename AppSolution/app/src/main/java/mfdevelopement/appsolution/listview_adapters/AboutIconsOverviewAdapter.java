package mfdevelopement.appsolution.listview_adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import mfdevelopement.appsolution.R;
import mfdevelopement.appsolution.activities.AboutIconsActivity;
import mfdevelopement.appsolution.models.IconCredit;

public class AboutIconsOverviewAdapter extends ArrayAdapter<IconCredit>{

    private final String FLATICON_BASE_LICENSE = AboutIconsActivity.FLATICON_BASE_LICENSE;

    public AboutIconsOverviewAdapter(@NonNull Context context, @NonNull List<IconCredit> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        IconCredit currentIconCredit = getItem(position);

        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.adapter_about_icons, parent, false);
        }

        TextView txtv = view.findViewById(R.id.tv_adapter_about_icon);
        ImageView imgv = view.findViewById(R.id.img_adapter_about_icon);

        int imageID = currentIconCredit.getImageID();
        if (imageID != 0)
            imgv.setImageResource(currentIconCredit.getImageID());

        String licenseText, author, titleLink, license;
        String licenseName = currentIconCredit.getLicense().getLicenseName();
        if (licenseName != null && licenseName.equals(FLATICON_BASE_LICENSE)) {
            author = "<a href=\"" + currentIconCredit.getAuthor().getLink() + "\">" + currentIconCredit.getAuthor().getName() + "</a>";
            licenseText = "Icon made by " + author + " from " + "<a href=\"www.flaticon.com\">www.flaticon.com</a>";
        } else {
            // Example: "Creative Commons 10th Birthday Celebration San Francisco" by tvol is licensed under CC BY 2.0
            titleLink = "<a href=\"" + currentIconCredit.getTitleCredits().getLink() + "\">" + currentIconCredit.getTitleCredits().getTitle() + "</a>";
            author = "<a href=\"" + currentIconCredit.getAuthor().getLink() + "\">" + currentIconCredit.getAuthor().getName() + "</a>";
            license = "<a href=\"" + currentIconCredit.getLicense().getLicenseLink() + "\">" + currentIconCredit.getLicense().getLicenseName() + "</a>";
            licenseText = titleLink + " by " + author + " is licensed under " + license;
        }

        txtv.setText(Html.fromHtml(licenseText));

        return view;
    }
}
