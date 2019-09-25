package mfdevelopement.appsolution.listview_adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import mfdevelopement.appsolution.R;
import mfdevelopement.bundesliga.FootballTeam;

public class BundesligaTableListAdapter extends ArrayAdapter<FootballTeam> {

    private final String LOG_TAG = "BundesligaTableListAdap";

    public BundesligaTableListAdapter(@NonNull Context context, @NonNull List<FootballTeam> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        FootballTeam currentTeam = getItem(position);

        Log.d(LOG_TAG,"adding team " + (position+1) + " to bundesliga table");

        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.adapter_bundesliga_table, parent, false);
        }

        TextView txtvTablePosition = view.findViewById(R.id.txtv_bundesliga_table_adapter_pos);
        TextView txtvTeamName = view.findViewById(R.id.txtv_bundesliga_table_adapter_teamname);
        TextView txtvTeamMatches = view.findViewById(R.id.txtv_bundesliga_table_adapter_num_matches);
        TextView txtvTeamGoals = view.findViewById(R.id.txtv_bundesliga_table_adapter_goals);
        TextView txtvTeamPoints = view.findViewById(R.id.txtv_bundesliga_table_adapter_points);
        ImageView imgTeamIcon = view.findViewById(R.id.img_bundesliga_table_adapter_teamicon);

        txtvTablePosition.setText(String.format(Locale.getDefault(),"%2d",position+1));

        // set team name to TextView, if team name is not null
        // TODO: fix return of null in bundelisga_xxxxxxxx.jar when using method FootballTeam.getTeamName();
        String teamName = currentTeam.getTeamName();
        if (teamName == null) teamName = "";
        txtvTeamName.setText(String.format(Locale.getDefault(),"%-20s",teamName));

        // create String containing the data of the currentTeam
        String stringGoals =
                String.format(Locale.getDefault(),"%3d",currentTeam.getGoals()) + ":"  +
                String.format(Locale.getDefault(),"%-3d",currentTeam.getOpponentGoals());

        txtvTeamMatches.setText(String.format(Locale.getDefault(),"%2d",currentTeam.getMatches()));
        txtvTeamGoals.setText(stringGoals);
        txtvTeamPoints.setText(String.format(Locale.getDefault(),"%2d",currentTeam.getPoints()));

        imgTeamIcon.setVisibility(View.GONE);

        return view;
    }
}
