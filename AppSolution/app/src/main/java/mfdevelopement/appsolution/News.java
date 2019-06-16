package mfdevelopement.appsolution;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.pkmmte.pkrss.Article;
import com.pkmmte.pkrss.Callback;
import com.pkmmte.pkrss.PkRSS;

import java.util.List;

public class News extends AppCompatActivity {

    private String SPON_URL = "http://www.spiegel.de/schlagzeilen/index.rss";

    private String appname = "";
    private String LogTag = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appname = getString(R.string.app_name);
        LogTag = appname + "/News";

        PkRSS.with(this).clearCache();

        Log.i(LogTag,"Nachrichten werden geladen...");
        //PkRSS.with(this).load(SPON_URL).async();
        //Log.i(LogTag,"Nachrichten fertig geladen!");
        Callback callback = new Callback() {
            @Override
            public void onPreload() {
                Log.i(LogTag,"onPreLoad");
            }

            @Override
            public void onLoaded(List<Article> newArticles) {
                Log.i(LogTag,"onLoaded");
            }

            @Override
            public void onLoadFailed() {
                Log.i(LogTag,"onLoadedFailed");
            }
        };


        //PkRSS.with(this).load(SPON_URL).callback(callback).async();


        loadArticle();

        //Log.i(LogTag,"Nachrichten umspeichern....");
        //List<Article> articleList = PkRSS.with(this).get(SPON_URL);

        //Log.i("xxx",articleList.get(0).toString());
    }



    private void loadArticle() {
        new AsyncTask<Void, Void, Void>() {

            private Article article1 = null;
            @Override
            protected void onPreExecute() {

            }

            @Override
            protected Void doInBackground(Void... params) {
                Log.i(LogTag,SPON_URL);
                Log.i(LogTag,"Nachrichten async laden ... ");
                article1 = PkRSS.with(News.this).load(SPON_URL).individual().getFirst();
                return null;
            }

            @Override
            protected void onPostExecute(Void p) {
                //List<Article> article = PkRSS.with(News.this).get(SPON_URL);
                Log.i(LogTag,"Nachrichten geladen");
                Log.i(LogTag,article1.getContent());
                //Log.i(LogTag, String.valueOf(article.get().getDate()));
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

/*    // Implementation of AsyncTask used to download XML feed from stackoverflow.com.
    private class loadNews extends AsyncTask<String, Void, List<Nachricht>> {

        Activity activity;


        @Override
        protected List<Nachricht> doInBackground(String... urls) {
            try {
                return loadXmlFromNetwork(urls[0]);
            } catch (IOException e) {
                return null; //getResources().getString(R.string.connection_error);
            } catch (XmlPullParserException e) {
                return null; // getResources().getString(R.string.xml_error);
            }
        }

        @Override
        protected void onPostExecute(List<Nachricht> nachrichten) {
            ListView listView = findViewById(R.id.lv_news_overview);
            listView.setAdapter(new NachrichtenOverviewListAdapter(activity, nachrichten));

        }

        private List<Nachricht> loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
            InputStream stream = null;
            // Instantiate the parser
            SpiegelOnlineParser spiegelOnlineParser = new SpiegelOnlineParser();
            List<Nachricht> Nachrichten = new ArrayList<>();
            String title = null;
            String url = null;
            String summary = null;

            try {
                stream = downloadUrl(urlString);
                Nachrichten = spiegelOnlineParser.parse(stream);
                // Makes sure that the InputStream is closed after the app is
                // finished using it.
            } finally {
                if (stream != null) {
                    stream.close();
                }
            }
            return Nachrichten;
        }

        // Given a string representation of a URL, sets up a connection and gets
        // an input stream.
        private InputStream downloadUrl(String urlString) throws IOException {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 *//* milliseconds *//*);
            conn.setConnectTimeout(15000 *//* milliseconds *//*);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            return conn.getInputStream();
        }
    }*/
}
