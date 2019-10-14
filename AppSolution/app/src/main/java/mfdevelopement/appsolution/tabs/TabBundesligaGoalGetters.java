package mfdevelopement.appsolution.tabs;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import mfdevelopement.appsolution.R;
import mfdevelopement.appsolution.activities.BundesligaActivity;
import mfdevelopement.bundesliga.Bundesliga;
import mfdevelopement.bundesliga.GoalGetter;

public class TabBundesligaGoalGetters extends Fragment {

    private static final String LOG_TAG = "TabBundesligaGoalGetter";
    private ListView lv_bundesliga_goal_getters;
    private WeakReference<LoadBundesligaGoalGetters> asyncTaskWeakRef;
    private SharedPreferences sharedPrefsBundesliga;
    private final String SHARED_PREF_STRING_BUNDESLIGA = BundesligaActivity.SHARED_PREF_STRING_BUNDESLIGA;
    private final String SHARED_PREF_STRING_GOAL_GETTERS = "jsonResponseGoalGetters";
    private final String TIMESTAMP = "timestamp";
    private final long RELOAD_INTERVALL_SEC = BundesligaActivity.RELOAD_INTERVALL_SEC;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_bundesliga_goal_getters, container, false);

        lv_bundesliga_goal_getters = rootView.findViewById(R.id.lv_bundesliga_goal_getters);

        setRetainInstance(true);
        updateBundesligaGoalGetters(false);

        return rootView;
    }

    private void reloadBundesligaGoalGetters(){
        updateBundesligaGoalGetters(true);
    }

    private void updateBundesligaGoalGetters(boolean forceUpdate){

        long currentTimestamp = System.currentTimeMillis()/1000;
        long lastUpdateTimestamp = getLastUpdateTimestamp();

        // if last update was less than a defined amount of time before, don't update the list
        if (!forceUpdate && currentTimestamp - lastUpdateTimestamp < RELOAD_INTERVALL_SEC) {
            String jsonReponse = getJsonResponse();
            if (jsonReponse.equals("")) {
                reloadBundesligaGoalGetters();
            } else {
                Log.i(LOG_TAG,"loading bundesliga goal getters from SharedPrefs");
                Bundesliga bundesliga = new Bundesliga();
                List<GoalGetter> goalGetterList = bundesliga.getOpenLigaDbParser().getGoalGettersFromJsonResponse(jsonReponse);
                updateGoalGetterListView(goalGetterList);
            }
        } else {
            Log.i(LOG_TAG,"loading bundesliga goal getters from www.openliagdb.de");
            LoadBundesligaGoalGetters loadBundesligaGoalGettersAsyncTask = new LoadBundesligaGoalGetters(this);
            asyncTaskWeakRef = new WeakReference<>(loadBundesligaGoalGettersAsyncTask);
            loadBundesligaGoalGettersAsyncTask.execute();
        }
    }

    private void updateGoalGetterListView(List<GoalGetter> goalGetterList) {
        List<String> goalGetterNames = new ArrayList<>();
        for (int i=0; i<goalGetterList.size(); i++) {
            goalGetterNames.add(goalGetterList.get(i).getName());
        }
        if (getActivity() != null) {
            ArrayAdapter<String> goalGetterArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, goalGetterNames);
            lv_bundesliga_goal_getters.setAdapter(goalGetterArrayAdapter);
        }
    }

    private void saveJsonResponse(String jsonResponseString) {

        // log function call
        Log.d(LOG_TAG, "saveBundesligaJsonResponse()" +
                "\njsonResponse: " + jsonResponseString);

        // save jsonReponse using SharedPreferences
        if (getActivity() != null) {
            sharedPrefsBundesliga = getActivity().getSharedPreferences(SHARED_PREF_STRING_BUNDESLIGA, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPrefsBundesliga.edit();
            editor.clear();
            editor.putString(SHARED_PREF_STRING_GOAL_GETTERS, jsonResponseString);
            editor.putLong(TIMESTAMP, System.currentTimeMillis()/1000);
            editor.apply();
        }
    }

    private String getJsonResponse() {

        String jsonResponse;
        Log.d(LOG_TAG,"calling getJsonResponse()");

        if (getActivity() != null) {
            sharedPrefsBundesliga = getActivity().getSharedPreferences(SHARED_PREF_STRING_BUNDESLIGA, Context.MODE_PRIVATE);
            jsonResponse = sharedPrefsBundesliga.getString(SHARED_PREF_STRING_GOAL_GETTERS, "");

            // log the returned string
            Log.d(LOG_TAG, "loaded jsonResponse from SharedPreference: " + jsonResponse);
        } else {
            jsonResponse = "";
            // log the returned string
            Log.wtf(LOG_TAG, "getJsonResponse(): getActivity() was Null, so an empty String is returned");
        }

        return jsonResponse;
    }

    private long getLastUpdateTimestamp() {
        long timestamp = 0;

        FragmentActivity fragmentActivity = getActivity();
        if (fragmentActivity != null) {
            sharedPrefsBundesliga = fragmentActivity.getSharedPreferences(SHARED_PREF_STRING_BUNDESLIGA, Context.MODE_PRIVATE);
            timestamp = sharedPrefsBundesliga.getLong(TIMESTAMP, 0);
        }
        return timestamp;
    }


    /**
     * Asnyc task for loading Bundesliga goal getters from the internet
     */
    private static class LoadBundesligaGoalGetters extends AsyncTask<Void, Integer, Bundesliga> {

        private WeakReference<TabBundesligaGoalGetters> activityReference;

        // only retain a weak reference to the activity
        LoadBundesligaGoalGetters(TabBundesligaGoalGetters context) {
            this.activityReference = new WeakReference<>(context);
        }

        protected void onPreExecute() {
            Log.d(LOG_TAG, "Async Task LoadBundesligaGoalGetters startet");
        }

        @Override
        protected Bundesliga doInBackground(Void... params) {

            Bundesliga bundesliga = new Bundesliga();
            bundesliga.getGoalGetters();

            Log.d(LOG_TAG, "loading bundesliga goal getters");
            return bundesliga;
        }

        @Override
        protected void onPostExecute(Bundesliga bundesliga) {
            super.onPostExecute(bundesliga);

            List<GoalGetter> goalGetterList = bundesliga.getGoalGetters();
            if (this.activityReference.get() != null) {
                Log.i(LOG_TAG, "Bundesliga goal getters loaded successfully");

                TabBundesligaGoalGetters tabBundesligaGoalGetters = activityReference.get();
                tabBundesligaGoalGetters.updateGoalGetterListView(goalGetterList);
                tabBundesligaGoalGetters.saveJsonResponse(bundesliga.getOpenLigaDbParser().getJsonResponseGoalGetters());
            }
        }
    }
}