package mfdevelopement.appsolution.activities;

import android.app.ActionBar;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.lang.ref.WeakReference;
import java.util.List;

import mfdevelopement.appsolution.R;
import mfdevelopement.appsolution.device.status.InternetStatus;
import mfdevelopement.appsolution.dialogs.DialogNoInternetConnection;
import mfdevelopement.appsolution.listview_adapters.BundesligaTableListAdapter;
import mfdevelopement.appsolution.tabs.TabBundesligaMatches;
import mfdevelopement.appsolution.tabs.TabBundesligaTable;
import mfdevelopement.bundesliga.Bundesliga;
import mfdevelopement.bundesliga.FootballTeam;

public class BundesligaActivity extends AppCompatActivity {

    private static final String LOG_TAG = "BundesligaActivity";
    private Bundesliga bundesliga;
    private ListView lv_bundesliga;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d(LOG_TAG,"starting " + LOG_TAG);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bundesliga);

        /*
         * set up the ViewPager
         */
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // set up the action bar
        final ActionBar actionBar = getActionBar();
        try {
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            e.printStackTrace();
            Log.i(LOG_TAG,"error when setting up options for action bar");
        }

        // Set up the ViewPager with the sections adapter.
        // The {@link ViewPager} that will host the section contents.
        ViewPager mViewPager = findViewById(R.id.bundesliga_viewpager_container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.bundesliga_tabs);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));


        lv_bundesliga = findViewById(R.id.lv_bundesliga_table);

        Log.d(LOG_TAG,LOG_TAG + " startet successfully");

        updateBundesligaData();
    }

    private void updateBundesligaData() {
        // check, if device is connected to the internet
        // if true, then load Bundesliga data
        // else show a diaog
        InternetStatus internetStatus = new InternetStatus(this);
        if (internetStatus.isConnected()) {
            // start async task to load bundesliga data
            new LoadBundesligaData(this).execute();
        }
        else {
            DialogNoInternetConnection dia = new DialogNoInternetConnection(this);
            dia.show();
        }
    }

    /**
     * set bundesliga data in current actvity and update listView
     * @param bundesliga: object of class Bundesliga
     */
    public void setBundesliga(Bundesliga bundesliga) {

        // log function call
        Log.d(LOG_TAG,"setBundesliga. Team 1:" + bundesliga.getTablePos(1).getTeamName());

        this.bundesliga = bundesliga;

        // update bundesliga table listView with current data
        updateBundesligaTableList(bundesliga.getTable());
    }

    /**
     * update Bundesliga Table listView
     * @param bundesligaTable: List<FootballTeam>
     */
    private void updateBundesligaTableList(List<FootballTeam> bundesligaTable) {

        // log function call
        Log.d(LOG_TAG,"updateBundesligaTableList");

        // show progress bar and set progress to 0%
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(0);

        // create ListAdapter object
        BundesligaTableListAdapter bundesligaTableListAdapter = new BundesligaTableListAdapter(this, bundesligaTable);

        // update reference to bundesliga table listView and update its content
        if (lv_bundesliga == null) lv_bundesliga = findViewById(R.id.lv_bundesliga_table);
        lv_bundesliga.setAdapter(bundesligaTableListAdapter);

        // set progress to 100% and hide progress bar
        progressBar.setProgress(100);
        progressBar.setVisibility(View.GONE);
    }

    /**
     * Asnyc task for loading Bundesliga data from https://www.openligadb.de
     */
    private static class LoadBundesligaData extends AsyncTask<Void, Integer, Bundesliga> {

        private WeakReference<BundesligaActivity> activityReference;

        // only retain a weak reference to the activity
        LoadBundesligaData(BundesligaActivity context) {
            activityReference = new WeakReference<>(context);
        }

        protected void onPreExecute() {
            Log.d(LOG_TAG,"Async Task LoadBundesligaData startet");

            // get reference to progress bar and set visible
            BundesligaActivity bundesligaActivity = activityReference.get();
            if (bundesligaActivity == null || bundesligaActivity.isFinishing()) return;

            ProgressBar progressBar = bundesligaActivity.findViewById(R.id.progBar_bundesliga);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Bundesliga doInBackground(Void ... params) {

            Bundesliga bundesliga = new Bundesliga();

            Log.d(LOG_TAG,"loading bundesliga table");
            bundesliga.updateTable();
            publishProgress(50);

            Log.d(LOG_TAG,"loading bundesliga matches");
            bundesliga.updateMatches();
            return bundesliga;
        }

        protected void onProgressUpdate(Integer... values) {

            BundesligaActivity bundesligaActivity = activityReference.get();
            if (bundesligaActivity == null || bundesligaActivity.isFinishing()) return;

            ProgressBar progressBar = bundesligaActivity.findViewById(R.id.progBar_bundesliga);
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Bundesliga bundesliga) {

            Log.i(LOG_TAG,"Bundesliga data loaded successfully");

            // return, if parent activity is not running anymore
            BundesligaActivity activity = activityReference.get();
            if (activity == null || activity.isFinishing()) return;

            // change margin of bundesliga table title
            ConstraintLayout.LayoutParams llp = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
            llp.setMargins(0, 40, 0, 0); // (left, top, right, bottom)

            // check if BundesligaActivity is still active
            BundesligaActivity bundesligaActivity = activityReference.get();
            if (bundesligaActivity == null || bundesligaActivity.isFinishing()) return;

            bundesligaActivity.setBundesliga(bundesliga);

            // get reference to ProgressBar and hide it
            ProgressBar progressBar = bundesligaActivity.findViewById(R.id.progBar_bundesliga);
            progressBar.setVisibility(View.GONE);
        }
    }

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
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }
    }
}