package mfdevelopement.appsolution.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.ProgressBar;

import mfdevelopement.appsolution.R;
import mfdevelopement.appsolution.tabs.TabBundesligaGoalGetters;
import mfdevelopement.appsolution.tabs.TabBundesligaMatches;
import mfdevelopement.appsolution.tabs.TabBundesligaTable;
import mfdevelopement.bundesliga.Bundesliga;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

public class BundesligaActivity extends AppCompatActivity {

    private static final String LOG_TAG = "BundesligaActivity";

    private ListView lv_bundesliga_table, lv_bundesliga_matches, lv_bundesliga_goal_getters;
    private Bundesliga bundesliga;
    private ProgressBar progressBar;
    private SharedPreferences sharedPrefsBundesliga;
    public static final String SHARED_PREF_STRING_BUNDESLIGA = "bundesliga";
    private final String SHARED_PREF_STRING_TABLE = "jsonResponseTable";
    private final String SHARED_PREF_STRING_MATCHES = "jsonResponseMatches";

    public static final long RELOAD_INTERVALL_SEC = 90;
    public static final String SHARED_PREFS_ENTRY_NAME_TIMESTAMP = "timestamp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d(LOG_TAG, "starting " + LOG_TAG);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bundesliga);

        /*
         * set up the ViewPager
         */
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // set up the action bar
        final ActionBar actionBar = getActionBar();

        if (actionBar != null)
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setElevation(0);
        }

        // Set up the ViewPager with the sections adapter.
        // The {@link ViewPager} that will host the section contents.
        ViewPager mViewPager = findViewById(R.id.bundesliga_viewpager_container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.bundesliga_tabs);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        // get reference to listViews and progress bar
        lv_bundesliga_table = findViewById(R.id.lv_bundesliga_table);
        lv_bundesliga_matches = findViewById(R.id.lv_bundesliga_matches);
        lv_bundesliga_goal_getters = findViewById(R.id.lv_bundesliga_goal_getters);
        progressBar = findViewById(R.id.progBar_bundesliga);

        // Log start of current Activity
        Log.i(LOG_TAG, LOG_TAG + " startet successfully");

        // update Bundesliga data
        //updateBundesligaData();
    }

/*    private void updateBundesligaData() {
        // check, if device is connected to the internet
        // if true, then load Bundesliga data
        // else show a diaog
        InternetStatus internetStatus = new InternetStatus(this);
        if (internetStatus.isConnected()) {
            // start async task to load bundesliga data
            Log.d(LOG_TAG, "start to load bundesliga data");
            //new LoadBundesligaData(this).execute();
        } else {
            Log.i(LOG_TAG, "Device has no internet connection! Bundesliga data cannot be loaded.");
            DialogNoInternetConnection dia = new DialogNoInternetConnection(this);
            dia.show();
        }
    }*/

/*
    private void saveBundesligaJsonResponse(String jsonResponseString, String SharedPrefEntryName) {

        // log function call
        Log.d(LOG_TAG, "saveBundesligaJsonResponse()" +
                "\nSharedPrefEntryName: " + SharedPrefEntryName +
                "\njsonResponse: " + jsonResponseString);

        // save jsonReponse using SharedPreferences
        sharedPrefsBundesliga = getSharedPreferences(SHARED_PREF_STRING_BUNDESLIGA, 0);
        SharedPreferences.Editor editor = sharedPrefsBundesliga.edit();
        editor.clear();
        editor.putString(SharedPrefEntryName, jsonResponseString);
        editor.apply();
    }

    private String getBundesligaJsonResponse(String SharedPrefEntryName) {

        // log function call
        Log.d(LOG_TAG, "getBundesligaJsonResponse()" +
                "\nSharedPrefEntryName: " + SharedPrefEntryName);

        sharedPrefsBundesliga = getSharedPreferences(SHARED_PREF_STRING_BUNDESLIGA, 0);
        String jsonResponse = sharedPrefsBundesliga.getString(SharedPrefEntryName, "");

        // log the returned string
        Log.d(LOG_TAG, "loaded jsonResponse from SharedPreference: " + jsonResponse);

        return jsonResponse;
    }
*/

    /*    *//**
     * set bundesliga data in current actvity and update listView
     *
     * @param bundesliga: object of class Bundesliga
     *//*
    public void setBundesliga(Bundesliga bundesliga) {

        // log function call
        Log.d(LOG_TAG, "setBundesliga. Team 1:" + bundesliga.getTablePos(1).getTeamName());

        this.bundesliga = bundesliga;

        // update bundesliga table listView with current data
        //updateBundesligaTableList(bundesliga.getTable());

        // update bundesliga matches list
        updateBundesligaMatchesList(bundesliga.getMatches());

        // save the json Response string
        saveBundesligaJsonResponse(bundesliga.getOpenLigaDbParser().getJsonResponseTable(), SHARED_PREF_STRING_TABLE);
        saveBundesligaJsonResponse(bundesliga.getOpenLigaDbParser().getJsonResponseMatches(), SHARED_PREF_STRING_MATCHES);
    }

    *//**
     * update Bundesliga Table listView
     *
     * @param bundesligaTable: List<FootballTeam>
     *//*
    private void updateBundesligaTableList(List<FootballTeam> bundesligaTable) {

*//*        // log function call
        Log.d(LOG_TAG, "updateBundesligaTableList");

        // show progress bar and set progress to 0%
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(0);

        // create ListAdapter object
        BundesligaTableListAdapter bundesligaTableListAdapter = new BundesligaTableListAdapter(this, bundesligaTable);

        // update reference to bundesliga table listView and update its content
        if (lv_bundesliga_table == null)
            lv_bundesliga_table = findViewById(R.id.lv_bundesliga_table);
        lv_bundesliga_table.setAdapter(bundesligaTableListAdapter);

        // set progress to 100% and hide progress bar
        progressBar.setProgress(100);
        progressBar.setVisibility(View.GONE);*//*
    }*/

    /*    */

    /**
     * update Bundesliga matches listView
     *
     *//*
    private void updateBundesligaMatchesList(List<Match> matches) {

        // log function call
        Log.d(LOG_TAG, "updateBundesligaMatchesList");

        // show progress bar and set progress to 0%
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(0);

        //
        List<Match> matchesFormatted = new ArrayList<>();
        for (int i = 0; i < matches.size(); i++) {
            // add the first item twice --> 1st separator, 2nd visible item
            if (i == 0) {
                matchesFormatted.add(matches.get(i));
                matchesFormatted.add(matches.get(i));
                continue;
            }

            // if current item has another date, then it is added twice
            String currentMatchDate = BundesligaMatchesListAdapter.getMatchDate(matches.get(i).getMatchTime());
            String lastMatchDate = BundesligaMatchesListAdapter.getMatchDate(matches.get(i - 1).getMatchTime());
            if (!currentMatchDate.equals(lastMatchDate)) {
                matchesFormatted.add(matches.get(i));
            }

            matchesFormatted.add(matches.get(i));
        }

        // create ListAdapter object
        BundesligaMatchesListAdapter bundesligaMatchesListAdapter = new BundesligaMatchesListAdapter(this, matchesFormatted);

        // update reference to bundesliga table listView and update its content
        if (lv_bundesliga_matches == null)
            lv_bundesliga_matches = findViewById(R.id.lv_bundesliga_matches);
        lv_bundesliga_matches.setAdapter(bundesligaMatchesListAdapter);

        // set progress to 100% and hide progress bar
        progressBar.setProgress(100);
        progressBar.setVisibility(View.GONE);
    }*/
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);
    }

//    /**
//     * Asnyc task for loading Bundesliga data from https://www.openligadb.de
//     */
//    private static class LoadBundesligaData extends AsyncTask<Void, Integer, Bundesliga> {
//
//        private WeakReference<BundesligaActivity> activityReference;
//
//        // only retain a weak reference to the activity
//        LoadBundesligaData(BundesligaActivity context) {
//            activityReference = new WeakReference<>(context);
//        }
//
//        protected void onPreExecute() {
//            Log.d(LOG_TAG, "Async Task LoadBundesligaData startet");
//
//            // get reference to progress bar and set visible
//            BundesligaActivity bundesligaActivity = activityReference.get();
//            if (bundesligaActivity == null || bundesligaActivity.isFinishing()) return;
//
//            ProgressBar progressBar = bundesligaActivity.findViewById(R.id.progBar_bundesliga);
//            progressBar.setVisibility(View.VISIBLE);
//        }
//
//        @Override
//        protected Bundesliga doInBackground(Void... params) {
//
//            Bundesliga bundesliga = new Bundesliga();
//
//            Log.d(LOG_TAG, "loading bundesliga table");
//            //bundesliga.updateTable();
//            publishProgress(50);
//
//            Log.d(LOG_TAG, "loading bundesliga matches");
//            bundesliga.updateMatches();
//            return bundesliga;
//        }
//
//        protected void onProgressUpdate(Integer... values) {
//
//            BundesligaActivity bundesligaActivity = activityReference.get();
//            if (bundesligaActivity == null || bundesligaActivity.isFinishing()) return;
//
//            ProgressBar progressBar = bundesligaActivity.findViewById(R.id.progBar_bundesliga);
//            progressBar.setProgress(values[0]);
//        }
//
//        @Override
//        protected void onPostExecute(Bundesliga bundesliga) {
//
//            Log.i(LOG_TAG, "Bundesliga data loaded successfully");
//
//            // return, if parent activity is not running anymore
//            BundesligaActivity bundesligaActivity = activityReference.get();
//            if (bundesligaActivity == null || bundesligaActivity.isFinishing()) return;
//
//            // change margin of bundesliga table title
//            ConstraintLayout.LayoutParams llp = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
//            llp.setMargins(0, 40, 0, 0); // (left, top, right, bottom)
//
//            bundesligaActivity.setBundesliga(bundesliga);
//
//            // get reference to ProgressBar and hide it
//            ProgressBar progressBar = bundesligaActivity.findViewById(R.id.progBar_bundesliga);
//            progressBar.setVisibility(View.GONE);
//
//            // TODO: save jsonResponse in SharedPreferences to restore it, if no updates are available on https://www.openligadb.de
//        }
//    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    return new TabBundesligaTable();
                case 1:
                    return new TabBundesligaMatches();
                case 2:
                    return new TabBundesligaGoalGetters();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }
}