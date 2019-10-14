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
import java.util.Locale;

import mfdevelopement.appsolution.R;
import mfdevelopement.bundesliga.GoalGetter;

public class BundesligaGoalGetterListAdapter extends ArrayAdapter<GoalGetter> {

    private final String LOG_TAG = "BundesligaTableListAdap";

    public BundesligaGoalGetterListAdapter(@NonNull Context context, @NonNull List<GoalGetter> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        GoalGetter currentGoalGetter = getItem(position);

        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.adapter_bundesliga_goal_getter, parent, false);
        }

        // get references to text views
        TextView txtvGoalGetterRank = view.findViewById(R.id.txtv_bundesliga_goal_getter_rank);
        TextView txtvGoalGetterName = view.findViewById(R.id.txtv_bundesliga_goal_getter_name);
        TextView txtvGoalGetterGoals = view.findViewById(R.id.txtv_bundesliga_goal_getter_num_goals);

        // set
        txtvGoalGetterRank.setText(String.format(Locale.getDefault(),"%2d",position+1));

        String goalGetterName = currentGoalGetter.getName();
        if (goalGetterName == null) goalGetterName = "";
        txtvGoalGetterName.setText(String.format(Locale.getDefault(),"%-20s",goalGetterName));

        txtvGoalGetterGoals.setText(String.format(Locale.getDefault(),"%2d",currentGoalGetter.getGoals()));

        return view;
    }
}
