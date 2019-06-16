package mfdevelopement.appsolution.listview_adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import mfdevelopement.appsolution.R;
import mfdevelopement.appsolution.models.Nachricht;

public class NachrichtenOverviewListAdapter extends ArrayAdapter<Nachricht> {

    public NachrichtenOverviewListAdapter(@NonNull Context context, @NonNull List<Nachricht> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Nachricht currentNachricht = getItem(position);

        View view = convertView;
         if(view == null) {
             view = LayoutInflater.from(getContext()).inflate(R.layout.adapter_news_short, parent, false);
         }

         TextView title = view.findViewById(R.id.tv_news_short_title);
         TextView mainMessage = view.findViewById(R.id.tv_news_mainMessage);

         title.setText(currentNachricht.getTitle());

         if (currentNachricht.getMainMessage()==null) {
             mainMessage.setVisibility(View.INVISIBLE);
         } else {
             mainMessage.setText(currentNachricht.getMainMessage());
         }

        return view;
    }
}
