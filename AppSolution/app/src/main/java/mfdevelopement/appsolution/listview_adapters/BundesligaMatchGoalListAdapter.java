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

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.adapter_bundesliga_match_goal, parent, false);
        }

        TextView txtv_minute = view.findViewById(R.id.txtv_bundesliga_match_goal_minute);
        TextView txtv_p1 = view.findViewById(R.id.txtv_bundesliga_match_goal_player_name_1);
        TextView txtv_p2 = view.findViewById(R.id.txtv_bundesliga_match_goal_player_name_2);
        TextView txtv_standing = view.findViewById(R.id.txtv_bundesliga_match_goal_standing);

        try {
            String matchMinute = currentGoal.getMatchMinute() + "'";
            txtv_minute.setText(matchMinute);
        } catch (NullPointerException npe) {
            npe.printStackTrace();
            Log.e(LOG_TAG,"NullPointerException occurred when getting getMatchMinute()");
        }

        String standing = currentGoal.getScoreHomeTeam() + ":" + currentGoal.getScoreAwayTeam();
        txtv_standing.setText(standing);

        String goalGetterName = currentGoal.getGoalGetterName();
        String standingCompare = "";
        // if it is the first goal of the game
        if (position == 0) {
            if (currentGoal.getScoreHomeTeam() == 1) {
                txtv_p1.setText(goalGetterName);
                txtv_p2.setText("");
            }
            else if (currentGoal.getScoreAwayTeam() == 1) {
                txtv_p1.setText("");
                txtv_p2.setText(goalGetterName);
            }
            else
                Log.e(LOG_TAG,"error when parsing the first goal of a match at standing " + standing);
        }
        // if ots is not the first goal of the game, then compare the standing with the standing before this goal has been scored
        else {
            // get the match goal before the current one
            MatchGoal lastGoal = getItem(position-1);

            // check if home team scored a goal
            if (currentGoal.getScoreHomeTeam() > lastGoal.getScoreHomeTeam())
                txtv_p1.setText(goalGetterName);
            // check if away team scored a goal
            else if (currentGoal.getScoreAwayTeam() > lastGoal.getScoreAwayTeam())
                txtv_p2.setText(goalGetterName);
            // write an error message to the log, of none of the above checks has been positive
            else
                standingCompare = standing + " vs. " + lastGoal.getScoreHomeTeam() + ":" + lastGoal.getScoreAwayTeam();
                Log.e(LOG_TAG,"Error when parsing goal. current standing vs. last standing: " + standingCompare + ", position: " + (position+1));
        }

        return view;
    }
}
