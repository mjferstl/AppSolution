package mfdevelopement.appsolution.listview_adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
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

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("E dd.MM",Locale.getDefault());
    private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm",Locale.getDefault());

    private TextView tv_match_time, tv_match_date, tv_match_result, tv_match_match;
    private LinearLayout lin_lay_separator, lin_lay_match;

    private float activity_single_space;

    public BundesligaMatchesListAdapter(@NonNull Context context, @NonNull List<Match> objects) {
        super(context, 0, objects);

        activity_single_space = context.getResources().getDimension(R.dimen.activity_single_space);
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
        lin_lay_match = view.findViewById(R.id.lin_lay_bundesliga_matches_adapter_content);
        lin_lay_separator = view.findViewById(R.id.lin_lay_bundesliga_matches_adapter_separator);
        tv_match_time = view.findViewById(R.id.txtv_bundesliga_match_adapter_time);
        tv_match_date = view.findViewById(R.id.txtv_bundesliga_match_adapter_date);
        tv_match_match = view.findViewById(R.id.txtv_bundesliga_match_adapter_match);
        tv_match_result = view.findViewById(R.id.txtv_bundesliga_match_adapter_result);

        // get date from calendar object and format it
        String matchDateString;
        matchDateString = getMatchDate(currentMatch.getMatchTime());
        Log.d(LOG_TAG,"date and time of current match: " + matchDateString);

        // set match date
        if (position > 0) {
            Match lastItem = getItem(position-1);
            String lastMatchDate = getMatchDate(lastItem.getMatchTime());

            // if current item has another date than the last one --> only separator
            if (lastMatchDate.equals(matchDateString)) {
                setItemAsMatch();
            } else {
                setItemAsSeparator();
                view.setClickable(false);
            }
        } else {
            setItemAsSeparator();

        }

        tv_match_date.setText(String.format(Locale.getDefault(), "%s", matchDateString));

        // set match time
        String matchTime = getMatchTime(currentMatch.getMatchTime());
        tv_match_time.setText(matchTime);

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

    public static String getMatchDate(Calendar calendar) {

        if (calendar == null)
            return "";

        return dateFormat.format(calendar.getTime());
    }

    private String getMatchTime(Calendar calendar) {

        if (calendar == null)
            return "";

        return timeFormat.format(calendar.getTime());
    }

    private void setItemAsSeparator() {
        tv_match_date.setVisibility(View.VISIBLE);
        lin_lay_separator.setVisibility(View.VISIBLE);
        tv_match_time.setVisibility(View.GONE);
        tv_match_match.setVisibility(View.GONE);
        tv_match_result.setVisibility(View.GONE);

        // adjust paddings
        lin_lay_separator.setPadding(0,(int)activity_single_space,0,(int)activity_single_space);
        lin_lay_match.setPadding(0,0,0,0);
    }

    private void setItemAsMatch() {
        tv_match_date.setVisibility(View.GONE);
        lin_lay_separator.setVisibility(View.GONE);
        tv_match_time.setVisibility(View.VISIBLE);
        tv_match_match.setVisibility(View.VISIBLE);
        tv_match_result.setVisibility(View.VISIBLE);

        lin_lay_match.setPadding(0,(int)activity_single_space,0,(int)activity_single_space);
        lin_lay_separator.setPadding(0,0,0,0);
    }
}