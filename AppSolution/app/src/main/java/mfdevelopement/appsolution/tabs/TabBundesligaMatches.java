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
import java.util.List;

import mfdevelopement.appsolution.R;
import mfdevelopement.appsolution.activities.BundesligaActivity;
import mfdevelopement.appsolution.listview_adapters.BundesligaMatchesListAdapter;
import mfdevelopement.bundesliga.Bundesliga;
import mfdevelopement.bundesliga.Match;

public class TabBundesligaMatches extends Fragment {

    private ListView lv_bundesliga_matches;
    private SharedPreferences sharedPrefsBundesliga;

    private final String SHARED_PREF_STRING_BUNDESLIGA = BundesligaActivity.SHARED_PREF_STRING_BUNDESLIGA;
    private final String TIMESTAMP = BundesligaActivity.SHARED_PREFS_ENTRY_NAME_TIMESTAMP;
    private final String SHARED_PREF_STRING_MATCHES = "jsonResponseMatches";
    private final String sep = ",";
    private final long RELOAD_INTERVALL_SEC = BundesligaActivity.RELOAD_INTERVALL_SEC;
    private final static String LOG_TAG = "TabBundesligaMatches";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_bundesliga_matches, container, false);

        lv_bundesliga_matches = rootView.findViewById(R.id.lv_bundesliga_matches);

        loadBundesligaMatches();

        return rootView;
    }

    /**
     * force reloading the bundesliga matches from the internet
     */
    private void reloadBundesligaMatches() {
        loadBundesligaMatches(true);
    }

    /**
     * load the Bundesliga matches ether from SharedPreferences, if the data is less than 90 second old
     * otherwise load the data from the internet
     * If no data has been saved yet, also reload the data from the internet
     */
    private void loadBundesligaMatches() {
        loadBundesligaMatches(false);
    }

    /**
     * load the Bundesliga matches ether from SharedPreferences, if the data is less than 90 second old
     * otherwise load the data from the internet
     * If no data has been saved yet, also reload the data from the internet
     */
    private void loadBundesligaMatches(boolean forceUpdate) {

        long currentTimestamp = System.currentTimeMillis() / 1000;
        long lastUpdateTimestamp = getLastUpdateTimestamp();

        // if last update was less than a defined amount of time before, don't update the list
        if (!forceUpdate && ((currentTimestamp - lastUpdateTimestamp) < RELOAD_INTERVALL_SEC)) {
            String jsonReponse = getBundesligaMatchesReponse();
            if (jsonReponse.equals("")) {
                reloadBundesligaMatches();
            } else {
                Log.i(LOG_TAG, "loading bundesliga matches from SharedPrefs");
                Bundesliga bundesliga = new Bundesliga();
                List<Match> bundesligaMatches = bundesliga.getOpenLigaDbParser().getMatchesFromJsonResponse(jsonReponse);
                updateMatchesListView(bundesligaMatches);
            }
        } else {
            Log.i(LOG_TAG, "loading bundesliga matches from the internet");
            LoadBundesligaMatches loadBundesligaMatchesAsyncTask = new LoadBundesligaMatches(this);
            loadBundesligaMatchesAsyncTask.execute();
        }
    }

    /**
     * update the data in the bundesliga matches list view
     *
     * @param bundesligaMatches List containing objects of type Match
     */
    private void updateMatchesListView(List<Match> bundesligaMatches) {

        if (getActivity() != null) {
            // create ListAdapter object and set it as adapter for the Bundesliga matches ListView
            BundesligaMatchesListAdapter bundesligaMatchesListAdapter = new BundesligaMatchesListAdapter(getActivity(), bundesligaMatches);
            lv_bundesliga_matches.setAdapter(bundesligaMatchesListAdapter);
        }
    }

    /**
     * save a response as String in a SharedPreference
     *
     * @param jsonReponse String containing the reponse from the internet
     */
    private void saveBundesligaMatches(String jsonReponse) {

        // log function call
        Log.d(LOG_TAG, "saveBundesligaJsonResponse()");

        // save jsonReponse using SharedPreferences
        if (getActivity() != null) {
            sharedPrefsBundesliga = getActivity().getSharedPreferences(SHARED_PREF_STRING_BUNDESLIGA, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPrefsBundesliga.edit();
            editor.clear();
            editor.putString(SHARED_PREF_STRING_MATCHES, jsonReponse);
            editor.putLong(TIMESTAMP, System.currentTimeMillis() / 1000);
            editor.apply();
        }
    }

    /**
     * get the timestamp when the response from the internet has been saved the last time
     *
     * @return long containing a timestamp in seconds
     */
    private long getLastUpdateTimestamp() {
        long timestamp = 0;

        Log.d(LOG_TAG, "getLastUpdateTimestamp() called");
        if (getActivity() != null) {
            sharedPrefsBundesliga = getActivity().getSharedPreferences(SHARED_PREF_STRING_BUNDESLIGA, Context.MODE_PRIVATE);
            timestamp = sharedPrefsBundesliga.getLong(TIMESTAMP, 0);
            Log.d(LOG_TAG, "getLastUpdateTimestamp(): last timestamp loaded from SharedPrefs");
        }
        return timestamp;
    }

    /**
     * get the saved reponse from the SharedPreference
     *
     * @return String containing the response
     */
    private String getBundesligaMatchesReponse() {

        String reponse;
        Log.d(LOG_TAG, "calling getBundesligaMatchesReponse()");

        if (getActivity() != null) {
            sharedPrefsBundesliga = getActivity().getSharedPreferences(SHARED_PREF_STRING_BUNDESLIGA, Context.MODE_PRIVATE);
            reponse = sharedPrefsBundesliga.getString(SHARED_PREF_STRING_MATCHES, "");

            // log the returned string
            Log.d(LOG_TAG, "loaded saved bundesliga matches response from SharedPreference");
        } else {
            reponse = "";
            // log the returned string
            Log.wtf(LOG_TAG, "getBundesligaMatchesReponse(): getActivity() was Null, so an empty string is returned");
        }

        return reponse;
    }


    /**
     * Asnyc task for loading Bundesliga matches from the internet
     */
    private static class LoadBundesligaMatches extends AsyncTask<Void, Integer, Bundesliga> {

        private WeakReference<TabBundesligaMatches> activityReference;

        // only retain a weak reference to the activity
        LoadBundesligaMatches(TabBundesligaMatches context) {
            this.activityReference = new WeakReference<>(context);
        }

        protected void onPreExecute() {
            Log.d(LOG_TAG, "Async Task LoadBundesligaMatches startet");
        }

        @Override
        protected Bundesliga doInBackground(Void... params) {

            Bundesliga bundesliga = new Bundesliga();
            bundesliga.getMatches();

            Log.d(LOG_TAG, "loading bundesliga matches");
            return bundesliga;
        }

        @Override
        protected void onPostExecute(Bundesliga bundesliga) {
            super.onPostExecute(bundesliga);

            List<Match> bundesligaMatches = bundesliga.getMatches();
            if (this.activityReference.get() != null) {
                Log.i(LOG_TAG, "Bundesliga matches loaded successfully");

                TabBundesligaMatches tabBundesligaMatches = activityReference.get();
                tabBundesligaMatches.updateMatchesListView(bundesligaMatches);
                String jsonReponse = bundesliga.getOpenLigaDbParser().getJsonResponseMatches();
                tabBundesligaMatches.saveBundesligaMatches(jsonReponse);
            }
        }
    }
}
