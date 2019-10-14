package mfdevelopement.appsolution.tabs;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import mfdevelopement.appsolution.R;
import mfdevelopement.appsolution.activities.BundesligaActivity;
import mfdevelopement.appsolution.listview_adapters.BundesligaTableListAdapter;
import mfdevelopement.bundesliga.Bundesliga;
import mfdevelopement.bundesliga.FootballTeam;

public class TabBundesligaTable extends Fragment {

    private ListView lv_bundesliga_table;
    private WeakReference<LoadBundesligaTable> asyncTaskWeakRef;
    private SharedPreferences sharedPrefsBundesliga;
    private String jsonReponoseBundesligaTable;

    private final String SHARED_PREF_STRING_BUNDESLIGA = BundesligaActivity.SHARED_PREF_STRING_BUNDESLIGA;
    private final String TIMESTAMP = BundesligaActivity.SHARED_PREFS_ENTRY_NAME_TIMESTAMP;
    private final String SHARED_PREF_ENTRY_NUM_TEAMS = "numberOfTeams";
    private final String SHARED_PREF_STRING_TABLE = "jsonResponseTable";
    private final String sep = ",";
    private final long RELOAD_INTERVALL_SEC = BundesligaActivity.RELOAD_INTERVALL_SEC;
    private static final String LOG_TAG = "TabBundesligaTable";
    private int numTeams = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_bundesliga_table, container, false);

        lv_bundesliga_table = rootView.findViewById(R.id.lv_bundesliga_table);
        loadBundesligaTable();

        return rootView;
    }

    /**
     * force reloading the bundesliga table from the internet
     */
    private void reloadBundesligaTable() {
        loadBundesligaTable(true);
    }

    /**
     * load the Bundesliga table ether from SharedPreferences, if the data is less than 90 second old
     * otherwise load the data from the internet
     * If no data has been saved yet, also reload the data from the internet
     */
    private void loadBundesligaTable() {
        loadBundesligaTable(false);
    }

    /**
     * load the Bundesliga table ether from SharedPreferences, if the data is less than 90 second old
     * otherwise load the data from the internet
     * If no data has been saved yet, also reload the data from the internet
     */
    private void loadBundesligaTable(boolean forceUpdate) {

        long currentTimestamp = System.currentTimeMillis() / 1000;
        long lastUpdateTimestamp = getLastUpdateTimestamp();

        // if last update was less than a defined amount of time before, don't update the list
        if (!forceUpdate && ((currentTimestamp - lastUpdateTimestamp) < RELOAD_INTERVALL_SEC)) {
            List<FootballTeam> bundesligaTable = getBundesligaTableFromSharedPref();
            if (bundesligaTable.size() == 0) {
                reloadBundesligaTable();
            } else {
                Log.i(LOG_TAG, "loading bundesliga table from SharedPrefs");
                updateTableListView(bundesligaTable);
            }
        } else {
            Log.i(LOG_TAG, "loading bundesliga table from the internet");
            LoadBundesligaTable loadBundesligaTableAsyncTask = new LoadBundesligaTable(this);
            asyncTaskWeakRef = new WeakReference<>(loadBundesligaTableAsyncTask);
            loadBundesligaTableAsyncTask.execute();
        }
    }

    /**
     * update the data in the bundeliga table list view
     *
     * @param bundesligaTable List containing objects of type FootballTeam in the order: first to last
     */
    private void updateTableListView(List<FootballTeam> bundesligaTable) {

        if (getActivity() != null) {
            // create ListAdapter object and set it as adapter for the Bundesliga table ListView
            BundesligaTableListAdapter bundesligaTableListAdapter = new BundesligaTableListAdapter(getActivity(), bundesligaTable);
            lv_bundesliga_table.setAdapter(bundesligaTableListAdapter);
        }
    }

    /**
     * get the timestamp when the response from the internet has been saved the last time
     *
     * @return long containing a timestamp in seconds
     */
    private long getLastUpdateTimestamp() {
        long timestamp = 0;

        Log.d(LOG_TAG,"getLastUpdateTimestamp() called");
        if (getActivity() != null) {
            sharedPrefsBundesliga = getActivity().getSharedPreferences(SHARED_PREF_STRING_BUNDESLIGA, Context.MODE_PRIVATE);
            timestamp = sharedPrefsBundesliga.getLong(TIMESTAMP, 0);
            Log.d(LOG_TAG,"getLastUpdateTimestamp(): last timestamp loaded from SharedPrefs");
        }
        return timestamp;
    }

    /**
     * save a response as String in a SharedPreference
     *
     * @param bundesligaTable List containing objects of type FootballTeam
     */
    private void saveBundesligaTable(List<FootballTeam> bundesligaTable) {

        // log function call
        Log.d(LOG_TAG, "saveBundesligaJsonResponse()");

        // save jsonReponse using SharedPreferences
        if (getActivity() != null) {
            sharedPrefsBundesliga = getActivity().getSharedPreferences(SHARED_PREF_STRING_BUNDESLIGA, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPrefsBundesliga.edit();
            editor.clear();

            String response;
            for (int i=0; i<bundesligaTable.size(); i++) {
                FootballTeam fbt = bundesligaTable.get(i);
                response = fbt.getTeamName() + sep + fbt.getMatches() + sep + fbt.getGoals() + sep + fbt.getOpponentGoals() + sep + fbt.getPoints();
                editor.putString(SHARED_PREF_STRING_TABLE + "_" + (i+1), response);
            }
            this.numTeams = bundesligaTable.size();
            editor.putInt(SHARED_PREF_ENTRY_NUM_TEAMS,this.numTeams);

            editor.putLong(TIMESTAMP, System.currentTimeMillis() / 1000);
            editor.apply();
        }
    }

    /**
     * get the saved reponse from the SharedPreference
     *
     * @return String containing the response
     */
    private List<FootballTeam> getBundesligaTableFromSharedPref() {

        List<FootballTeam> table = new ArrayList<>();
        Log.d(LOG_TAG, "calling getBundesligaTableFromSharedPref()");

        if (getActivity() != null) {
            sharedPrefsBundesliga = getActivity().getSharedPreferences(SHARED_PREF_STRING_BUNDESLIGA, Context.MODE_PRIVATE);

            // load number of bundesliga teams from SharedPreferences
            this.numTeams = sharedPrefsBundesliga.getInt(SHARED_PREF_ENTRY_NUM_TEAMS, 0);

            // load all bundesliga teams from SharedPreferences
            String savedString;
            for (int i=0; i<this.numTeams; i++) {
                savedString = sharedPrefsBundesliga.getString(SHARED_PREF_STRING_TABLE + "_" + (i+1), "");
                String[] parts = savedString.split(sep);
                String teamName = parts[0];
                int matches = Integer.valueOf(parts[1]);
                int goals = Integer.valueOf(parts[2]);
                int goalsOpponent = Integer.valueOf(parts[3]);
                int points = Integer.valueOf(parts[4]);
                FootballTeam footballTeam = new FootballTeam();
                footballTeam.setTeamName(teamName);
                footballTeam.setMatches(matches);
                footballTeam.setGoals(goals);
                footballTeam.setOpponentGoals(goalsOpponent);
                footballTeam.setPoints(points);
                table.add(footballTeam);
            }

            // log the returned string
            Log.d(LOG_TAG, "loaded saved bundesliga table data from SharedPreference");
        } else {
            // log the returned string
            Log.wtf(LOG_TAG, "getBundesligaTableFromSharedPref(): getActivity() was Null, so an empty list is returned");
        }

        return table;
    }

    /**
     * write a String to the String variable 'jsonResponse' of the class TabBundesligaTable
     *
     * @param jsonReponoseBundesligaTable String containing the response
     */
    public void setJsonReponoseBundesligaTable(String jsonReponoseBundesligaTable) {
        this.jsonReponoseBundesligaTable = jsonReponoseBundesligaTable;
    }

    /**
     * Asnyc task for loading Bundesliga table from the internet
     */
    private static class LoadBundesligaTable extends AsyncTask<Void, Integer, Bundesliga> {

        private WeakReference<TabBundesligaTable> activityReference;

        // only retain a weak reference to the activity
        LoadBundesligaTable(TabBundesligaTable context) {
            this.activityReference = new WeakReference<>(context);
        }

        protected void onPreExecute() {
            Log.d(LOG_TAG, "Async Task LoadBundesligaTable startet");
        }

        @Override
        protected Bundesliga doInBackground(Void... params) {

            Bundesliga bundesliga = new Bundesliga();
            bundesliga.getTable();

            Log.d(LOG_TAG, "loading bundesliga table");
            return bundesliga;
        }

        @Override
        protected void onPostExecute(Bundesliga bundesliga) {
            super.onPostExecute(bundesliga);

            List<FootballTeam> bundesligaTable = bundesliga.getTable();
            if (this.activityReference.get() != null) {
                Log.i(LOG_TAG, "Bundesliga table loaded successfully");

                TabBundesligaTable tabBundesligaTable = activityReference.get();
                tabBundesligaTable.updateTableListView(bundesligaTable);
                String jsonReponse = bundesliga.getOpenLigaDbParser().getJsonResponseTable();
                tabBundesligaTable.saveBundesligaTable(bundesligaTable);
                tabBundesligaTable.setJsonReponoseBundesligaTable(jsonReponse);
            }
        }
    }
}