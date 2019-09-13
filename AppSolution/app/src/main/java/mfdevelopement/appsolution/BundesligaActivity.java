package mfdevelopement.appsolution;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import mfdevelopement.bundesliga.Bundesliga;
import mfdevelopement.bundesliga.FootballTeam;

public class BundesligaActivity extends AppCompatActivity {

    private final String LOG_TAG = "BundesligaActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bundesliga);
        Toolbar toolbar = findViewById(R.id.toolbar_bundesliga);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        new LoadBundesligaData().execute();
    }


    private class LoadBundesligaData extends AsyncTask<Object, Integer, Bundesliga> {

        protected void onPreExecute() {
            //progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Bundesliga doInBackground(Object ... objects) {

            Bundesliga bl = new Bundesliga();
            bl.updateTable();
            bl.updateMatches();
            return bl;
        }

        protected void onProgressUpdate(Integer... values) {
            //progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Bundesliga bundesliga) {
            //

            FootballTeam ft = bundesliga.getTablePos(18);
            //Log.i(LOG_TAG,ft.toString());
            //weatherOverviewListAdapter =  new WeatherOverviewListAdapter(WeatherOverviewActivity.this, allCitiesWeatherData);
            //listView.setAdapter(weatherOverviewListAdapter);
            //progressBar.setVisibility(View.GONE);
        }

    }

}
