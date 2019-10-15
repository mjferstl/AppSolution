package mfdevelopement.appsolution.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import mfdevelopement.appsolution.R;
import mfdevelopement.appsolution.tabs.TabBundesligaGoalGetters;
import mfdevelopement.appsolution.tabs.TabBundesligaMatches;
import mfdevelopement.appsolution.tabs.TabBundesligaTable;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

public class BundesligaActivity extends AppCompatActivity {

    private static final String LOG_TAG = "BundesligaActivity";
    public static final String SHARED_PREF_STRING_BUNDESLIGA = "bundesliga";
    public static final long RELOAD_INTERVALL_SEC = 90;
    public static final String SHARED_PREFS_ENTRY_NAME_TIMESTAMP = "timestamp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d(LOG_TAG, "starting " + LOG_TAG);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bundesliga);

        // set up the ViewPager
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

        // Log start of current Activity
        Log.i(LOG_TAG, LOG_TAG + " startet successfully");
    }


    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);
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