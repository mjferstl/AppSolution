package mfdevelopement.appsolution.listview_adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import mfdevelopement.appsolution.R;
import mfdevelopement.bundesliga.Match;

public class BundesligaMatchesListAdapter extends ArrayAdapter<Match> {

    private final String LOG_TAG = "BundesligaMatchesListAd";
    private final String NO_RESULT = "-:-";

    public BundesligaMatchesListAdapter(@NonNull Context context, @NonNull List<Match> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Match currentMatch = getItem(position);

        Log.d(LOG_TAG, "adding match " + (position + 1) + " to bundesliga matches list");

        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.adapter_bundesliga_match, parent, false);
        }

        // get references to textViews
        TextView tv_match_date = view.findViewById(R.id.txtv_bundesliga_match_adapter_date);
        TextView tv_match_match = view.findViewById(R.id.txtv_bundesliga_match_adapter_match);
        TextView tv_match_result = view.findViewById(R.id.txtv_bundesliga_match_adapter_result);

        // get date from calendar object and format it
        String matchDateString;
        SimpleDateFormat dateFormat = new SimpleDateFormat("E dd.MM HH:mm",Locale.getDefault());
        try {
            Calendar matchDate = currentMatch.getMatchTime();
            matchDateString = dateFormat.format(matchDate.getTime());
            Log.d(LOG_TAG,"date and time of current match: " + matchDateString);
        } catch (NullPointerException e) {
            Log.e(LOG_TAG,"currentMatch.getMatchTime() produced a NullPointerException");
            matchDateString = " --- ";
        }

        // set match date and hour
        tv_match_date.setText(String.format(Locale.getDefault(), "%s", matchDateString));

        // get short team names and set them as match
        String match = currentMatch.getHomeTeam().getShortName() + " - " + currentMatch.getAwayTeam().getShortName();
        tv_match_match.setText(match);

        // set result if game already startet
        String resultFinal, resultHalfTime;
        Calendar matchDate = currentMatch.getMatchTime();
        if (matchDate != null && matchDate.before(Calendar.getInstance())) {
            resultFinal = String.valueOf(currentMatch.getGoalsHomeTeamFinal()) + ':' + String.valueOf(currentMatch.getGoalsAwayTeamFinal());
            resultHalfTime = String.valueOf(currentMatch.getGoalsHomeTeamHalf()) + ':' + String.valueOf(currentMatch.getGoalsAwayTeamHalf());
        } else {
            resultFinal = NO_RESULT;
            resultHalfTime = NO_RESULT;
        }
        String resultString = resultFinal + " (" + resultHalfTime + ")";
        tv_match_result.setText(resultString);

        return view;
    }
}