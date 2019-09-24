package mfdevelopement.appsolution.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
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
import mfdevelopement.bundesliga.Bundesliga;
import mfdevelopement.bundesliga.FootballTeam;

public class BundesligaActivity extends AppCompatActivity {

    private static final String LOG_TAG = "BundesligaActivity";

    private static ListView lv_bundesliga;

    private static ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d(LOG_TAG,"starting " + LOG_TAG);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bundesliga);
        //Toolbar toolbar = findViewById(R.id.toolbar_bundesliga);
        //setSupportActionBar(toolbar);

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException npe) {
            Log.wtf(LOG_TAG,"setDisplayHomeAsUpEnabled(true) could not be excecuted... ");
        }

        // get reference to layout elements
        lv_bundesliga = findViewById(R.id.lv_bundesliga);
        progressBar = findViewById(R.id.progBar_bundesliga);

        Log.d(LOG_TAG,LOG_TAG + " startet successfully");

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

            // update listview and hide progress bar
            List<FootballTeam> footballTeamsList = bundesliga.getTable();
            BundesligaTableListAdapter bundesligaTableListAdapter = new BundesligaTableListAdapter(activity, footballTeamsList);
            lv_bundesliga.setAdapter(bundesligaTableListAdapter);
            progressBar.setVisibility(View.GONE);
        }
    }
}