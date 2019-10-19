package mfdevelopement.appsolution.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import mfdevelopement.appsolution.R;
import mfdevelopement.appsolution.listview_adapters.BundesligaMatchGoalListAdapter;
import mfdevelopement.bundesliga.Bundesliga;
import mfdevelopement.bundesliga.Match;

public class DialogBundesligaMatch implements DialogInterface {

    private Context context;
    private Match bundesligaMatch;
    private final String LOG_TAG = "DialogBundesligaMatch";
    private Dialog dialog;
    private SimpleDateFormat dateTimeFormat = new SimpleDateFormat("E dd.MM.yy HH:mm", Locale.getDefault());
    private ListView lv_goals;
    private TextView txtv_no_goals, txtv_goals_text;
    private final int GOALS_NOT_SET_VALUE = Bundesliga.VALUE_NOT_SET;

    public DialogBundesligaMatch(Context c, Match bundesligaMatch) {
        this.context = c;
        this.bundesligaMatch = bundesligaMatch;
    }

    /**
     * show a dialog containing information about the selected bundesliga match
     */
    public void show() {

        Log.d(LOG_TAG,"DialogBundesligaMatch.show() called. Creating Dialog now ...");

        dialog = new Dialog(this.context);

        // no dialog title
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // content
        dialog.setContentView(R.layout.dialog_bundesliga_match);

        TextView txtv_teams = dialog.findViewById(R.id.txtv_bundesliga_match_teams);
        txtv_no_goals = dialog.findViewById(R.id.txtv_bundesliga_match_no_goals);
        txtv_goals_text = dialog.findViewById(R.id.txtv_bundesliga_match_goals_text);
        TextView txtv_match_start_time = dialog.findViewById(R.id.txtv_bundesliga_match_start_time);
        lv_goals = dialog.findViewById(R.id.lv_bundesliga_match_goals);
        Button btn_ok = dialog.findViewById(R.id.btn_bundesliga_match_dialog_ok);

        String dialogTitle = this.bundesligaMatch.getHomeTeam().getShortName()+ " - " + this.bundesligaMatch.getAwayTeam().getShortName();
        txtv_teams.setText(dialogTitle);

        String startTime = getMatchTime(this.bundesligaMatch.getMatchTime());
        txtv_match_start_time.setText(startTime);

        int goalsHomeTeamHalf = this.bundesligaMatch.getGoalsHomeTeamHalf();
        int goalsAwayTeamHalf = this.bundesligaMatch.getGoalsAwayTeamHalf();
        int goalsHomeTeamFinal = this.bundesligaMatch.getGoalsHomeTeamFinal();
        int goalsAwayTeamFinal = this.bundesligaMatch.getGoalsAwayTeamFinal();

        // if the game has not been startet yet
        if (this.bundesligaMatch.getMatchTime().after(Calendar.getInstance())) {
            txtv_no_goals.setVisibility(View.GONE);
            hideGoalList();
        }
        // if the start time of the match is in the past and the game is not finished yet
        else if (this.bundesligaMatch.getMatchTime().before(Calendar.getInstance()) && !this.bundesligaMatch.isFinished()) {

            // if no goals have been scored yet
            if (goalsHomeTeamHalf == GOALS_NOT_SET_VALUE || goalsAwayTeamHalf == GOALS_NOT_SET_VALUE ||
                    (goalsHomeTeamHalf == 0 && goalsAwayTeamHalf == 0 && goalsHomeTeamFinal == 0 && goalsAwayTeamFinal == 0)) {
                txtv_no_goals.setVisibility(View.VISIBLE);
                hideGoalList();
            } else {
                showGoalList(this.bundesligaMatch);
            }
        }
        // if the game has taken place in the past
        else if (this.bundesligaMatch.isFinished()) {
            showGoalList(this.bundesligaMatch);
        }

        // if button is clicked, the dialog gets dismissed
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        if (dialog != null) {
            dialog.show();
            Log.i(LOG_TAG,"showing DialogBundesligaMatch now");
        }
    }

    @Override
    public void cancel() {
        if (dialog != null)
            dialog.cancel();
        Log.d(LOG_TAG,"DialogBundesligaMatch cancelled");
    }

    @Override
    public void dismiss() {
        if (dialog != null)
            dialog.dismiss();
        Log.d(LOG_TAG,"DialogBundesligaMatch dismissed");
    }

    private BundesligaMatchGoalListAdapter createGoalsListAdapter(Match bundesligaMatch) {
        return new BundesligaMatchGoalListAdapter(this.context,bundesligaMatch.getMatchGoals());
    }

    private void showGoalList(Match bundesligaMatch) {
        BundesligaMatchGoalListAdapter listAdapter = createGoalsListAdapter(bundesligaMatch);
        lv_goals.setAdapter(listAdapter);
        lv_goals.setVisibility(View.VISIBLE);
        txtv_no_goals.setVisibility(View.GONE);
    }

    private void hideGoalList() {
        txtv_goals_text.setVisibility(View.GONE);
        lv_goals.setVisibility(View.GONE);
    }

    private String getMatchTime(Calendar calendar) {

        if (calendar == null)
            return "";

        return dateTimeFormat.format(calendar.getTime());
    }
}
