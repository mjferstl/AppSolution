package mfdevelopement.appsolution.listview_adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import mfdevelopement.appsolution.models.Icon;
import mfdevelopement.appsolution.R;

public class AboutIconsOverviewAdapter extends ArrayAdapter<Icon>{

    public AboutIconsOverviewAdapter(@NonNull Context context, @NonNull List<Icon> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Icon currentIcon = getItem(position);

        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.adapter_about_icons, parent, false);
        }

        TextView tv = view.findViewById(R.id.tv_adapter_about_icon);
        ImageView iv = view.findViewById(R.id.img_adapter_about_icon);

        tv.setText(currentIcon.getAuthorReference());
        iv.setImageResource(currentIcon.getImageID());

        return view;
    }
}
