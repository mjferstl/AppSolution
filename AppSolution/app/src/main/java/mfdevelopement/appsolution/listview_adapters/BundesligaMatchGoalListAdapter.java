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
import mfdevelopement.bundesliga.MatchGoal;

public class BundesligaMatchGoalListAdapter extends ArrayAdapter<MatchGoal> {

    private final String LOG_TAG = "BundesligaTableListAdap";

    public BundesligaMatchGoalListAdapter(@NonNull Context context, @NonNull List<MatchGoal> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        MatchGoal currentGoal = getItem(position);

        View view = convertView;

        int listItemLayout;

        if (isGoalOfHomeTeam(currentGoal,position)) {
            listItemLayout = R.layout.adapter_bundesliga_match_goal_home_team;
        } else {
            listItemLayout = R.layout.adapter_bundesliga_match_goal_away_team;
        }

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(listItemLayout, parent, false);
        }

        TextView txtv_score = view.findViewById(R.id.txtv_bundesliga_match_goal_score);
        TextView txtv_minute = view.findViewById(R.id.txtv_bundesliga_match_goal_minute);
        TextView txtv_scorer_name = view.findViewById(R.id.txtv_bundesliga_match_goal_scorer_name);

        if (currentGoal != null) {
            String matchMinute = getMatchMinuteString(currentGoal);

            int stringLength = matchMinute.length();
            if (matchMinute.length() < 3)
                stringLength = 3;

            txtv_minute.setText(String.format("%" + stringLength + "s",matchMinute));
        } else {
            txtv_minute.setText("???");
        }

        String score = currentGoal.getScoreHomeTeam() + ":" + currentGoal.getScoreAwayTeam();
        txtv_score.setText(score);

        String scorer_name = currentGoal.getGoalGetterName();
        if (currentGoal.isOwnGoal())
            scorer_name = scorer_name + " (" + view.getContext().getString(R.string.txt_bundesliga_match_goal_own_goal_short_name) + ")";
        if (currentGoal.isPenalty())
            scorer_name = scorer_name + " (" + view.getContext().getString(R.string.txt_bundesliga_match_goal_penalty) + ")";
        txtv_scorer_name.setText(scorer_name);

        return view;
    }

    private boolean isGoalOfHomeTeam(MatchGoal goal, int position) {

        // return true, if it's not a goal of the away team
        boolean isGoalOfHomeTeam = true;

        // if it is the first goal of the game
        if (position == 0 && goal.getScoreAwayTeam() == 1)
                isGoalOfHomeTeam = false;
        // if ots is not the first goal of the game, then compare the standing with the standing before this goal has been scored
        else if (position > 0){
            // get the match goal before the current one
            MatchGoal lastGoal = getItem(position-1);

            // check if away team scored a goal
            if (goal.getScoreAwayTeam() > lastGoal.getScoreAwayTeam())
                isGoalOfHomeTeam = false;
        }

        return isGoalOfHomeTeam;
    }

    private String getMatchMinuteString(MatchGoal goal) {
        // get the minute when the goal has been scored
        String matchMinute = goal.getMatchMinute() + "'";

        // if the goal has been scored in the overtime, then add an extra string
        if (goal.isOverTime()) {
            int minute = goal.getMatchMinute();
            if (minute > 120)
                matchMinute = "120' + " + (minute-120) + "'";
            else if (minute > 105)
                matchMinute = "105' + " + (minute-105) + "'";
            else if (minute > 90)
                matchMinute = "90' + " + (minute-90) + "'";
            else if (minute > 45)
                matchMinute = "45' + " + (minute-45) + "'";
        }
        return matchMinute;
    }
}
