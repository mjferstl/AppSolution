package mfdevelopement.appsolution.listview_adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import mfdevelopement.appsolution.R;
import mfdevelopement.appsolution.dialogs.DialogBundesligaMatch;
import mfdevelopement.bundesliga.Match;

public class BundesligaMatchesListAdapter extends ArrayAdapter<Match> {

    private final String LOG_TAG = "BundesligaMatchesListAd";
    private Context context;

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("E dd.MM",Locale.getDefault());
    private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm",Locale.getDefault());

    private TextView tv_match_time, tv_match_date, tv_match_result, tv_match_match;
    private LinearLayout lin_lay_separator, lin_lay_match;
    private ImageView imgv_match_inProgress;

    private float activity_single_space;

    public BundesligaMatchesListAdapter(@NonNull Context context, @NonNull List<Match> objects) {
        super(context, 0, objects);
        this.context = context;

        activity_single_space = context.getResources().getDimension(R.dimen.activity_single_space);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final Match currentMatch = getItem(position);

        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.adapter_bundesliga_match, parent, false);
        }

        // get references to textViews
        lin_lay_match = view.findViewById(R.id.lin_lay_bundesliga_matches_adapter_content);
        lin_lay_match.setClickable(true);
        lin_lay_separator = view.findViewById(R.id.lin_lay_bundesliga_matches_adapter_separator);
        tv_match_time = view.findViewById(R.id.txtv_bundesliga_match_adapter_time);
        tv_match_date = view.findViewById(R.id.txtv_bundesliga_match_adapter_date);
        tv_match_match = view.findViewById(R.id.txtv_bundesliga_match_adapter_match);
        tv_match_result = view.findViewById(R.id.txtv_bundesliga_match_adapter_result);
        imgv_match_inProgress = view.findViewById(R.id.imgv_bundesliga_match_in_progress);
        imgv_match_inProgress.setVisibility(View.GONE);

        // add action when clicking on the match
        lin_lay_match.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogBundesligaMatch dia = new DialogBundesligaMatch(context, currentMatch);
                dia.show();
                //Toast.makeText(getContext(),"xxxx",Toast.LENGTH_SHORT).show();
            }
        });

        // get date from calendar object and format it
        String matchDateString;
        matchDateString = getMatchDate(currentMatch.getMatchTime());

        // set match date
        if (position > 0) {
            Match lastItem = getItem(position-1);
            String lastMatchDate = getMatchDate(lastItem.getMatchTime());

            // if current item has another date than the last one --> only separator
            if (lastMatchDate.equals(matchDateString)) {
                hideSeparator();
            } else {
                addSeparator();
                view.setClickable(false);
            }
        } else {
            addSeparator();
        }

        // set match date
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

        // format result depending on the state of the match
        String resultString;
        if (currentMatch.isFinished()) {
            resultFinal = String.valueOf(currentMatch.getGoalsHomeTeamFinal()) + ':' + String.valueOf(currentMatch.getGoalsAwayTeamFinal());
            resultHalfTime = String.valueOf(currentMatch.getGoalsHomeTeamHalf()) + ':' + String.valueOf(currentMatch.getGoalsAwayTeamHalf());
            resultString = resultFinal + " (" + resultHalfTime + ")";
        } else {
            if (matchDate != null && matchDate.before(Calendar.getInstance())) {
                resultFinal = String.valueOf(currentMatch.getGoalsHomeTeamFinal()) + ':' + String.valueOf(currentMatch.getGoalsAwayTeamFinal());
                imgv_match_inProgress.setVisibility(View.VISIBLE);
            } else {
                resultFinal = "";
            }
            resultString = resultFinal;
        }

        // set
        tv_match_result.setText(resultString);

        return view;
    }

    private static String getMatchDate(Calendar calendar) {

        if (calendar == null)
            return "";

        return dateFormat.format(calendar.getTime());
    }

    private String getMatchTime(Calendar calendar) {

        if (calendar == null)
            return "";

        return timeFormat.format(calendar.getTime());
    }

    private void hideSeparator() {
        tv_match_date.setVisibility(View.GONE);
        lin_lay_separator.setVisibility(View.GONE);
        tv_match_time.setVisibility(View.VISIBLE);
        tv_match_match.setVisibility(View.VISIBLE);
        tv_match_result.setVisibility(View.VISIBLE);

        lin_lay_match.setPadding(0,(int)activity_single_space,0,(int)activity_single_space);
        lin_lay_separator.setPadding(0,0,0,0);
    }

    private void addSeparator() {
        tv_match_date.setVisibility(View.VISIBLE);
        lin_lay_separator.setVisibility(View.VISIBLE);
        tv_match_time.setVisibility(View.VISIBLE);
        tv_match_match.setVisibility(View.VISIBLE);
        tv_match_result.setVisibility(View.VISIBLE);

        lin_lay_separator.setPadding(0,(int)activity_single_space,0,(int)activity_single_space);
    }
}