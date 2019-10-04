package mfdevelopement.appsolution.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import mfdevelopement.appsolution.R;
import mfdevelopement.appsolution.dialogs.DialogAboutIconCreditsLinks;
import mfdevelopement.appsolution.listview_adapters.AboutIconsOverviewAdapter;
import mfdevelopement.appsolution.models.Author;
import mfdevelopement.appsolution.models.IconCredit;
import mfdevelopement.appsolution.models.License;
import mfdevelopement.appsolution.models.TitleCredits;

public class AboutIconsActivity extends AppCompatActivity {

    public static final String FLATICON_BASE_LICENSE = "Flaticon Base License";

    private List<IconCredit> iconCredits = new ArrayList<>();

    // imageIDs for all weather icons
    private final int[] weatherIcon = {R.drawable.ic_wi_cloudy,R.drawable.ic_wi_cloudy_gusts,
            R.drawable.ic_wi_day_haze,R.drawable.ic_wi_day_sunny,R.drawable.ic_wi_day_windy,
            R.drawable.ic_wi_dust,R.drawable.ic_wi_fog,R.drawable.ic_wi_hail,R.drawable.ic_wi_hot,
            R.drawable.ic_wi_hurricane,R.drawable.ic_wi_lightning,R.drawable.ic_wi_rain,
            R.drawable.ic_wi_rain_mix,R.drawable.ic_wi_showers,R.drawable.ic_wi_sleet,
            R.drawable.ic_wi_smoke,R.drawable.ic_wi_snow,R.drawable.ic_wi_snowflake_cold,
            R.drawable.ic_wi_sprinkle,R.drawable.ic_wi_storm_showers,R.drawable.ic_wi_strong_wind,
            R.drawable.ic_wi_thunderstorm,R.drawable.ic_wi_tornado,R.drawable.ic_wi_windy};

    // all title credits
    private final TitleCredits titleEmpty = new TitleCredits();
    private final TitleCredits weatherIcons = new TitleCredits("Weather Icons","https://erikflowers.github.io/weather-icons/");
    private final TitleCredits plate_fork_and_knife = new TitleCredits("Plate Fork And Knife free icon","https://www.flaticon.com/free-icon/plate-fork-and-knife_35194#term=plate%20and%20fork%20knife&page=1&position=2");
    private final TitleCredits set_square = new TitleCredits("Set Square free icon","https://www.flaticon.com/free-icon/set-square_250315#term=set%20square&page=1&position=43");
    private final TitleCredits speedometer = new TitleCredits("Speedometer free icon","https://www.flaticon.com/free-icon/speedometer_2204#term=speedometer&page=1&position=9");
    private final TitleCredits shirt = new TitleCredits("Shirt free icon","https://www.flaticon.com/free-icon/shirt_286964#term=shirt&page=1&position=5");
    private final TitleCredits kangaroo = new TitleCredits("Kangaroo Looking Right free icon","https://www.flaticon.com/free-icon/kangaroo-looking-right_84452#term=kangaroo&page=1&position=5");
    private final TitleCredits coordinates = new TitleCredits("Coordinates free icon","https://www.flaticon.com/free-icon/coordinates_136810#term=coordinates&page=1&position=19");
    private final TitleCredits football = new TitleCredits("Football free icon","https://www.flaticon.com/free-icon/football_1165187#term=football&page=1&position=1");
    private final TitleCredits cocktailShaker = new TitleCredits("Shaker free icon","https://www.flaticon.com/free-icon/shaker_493551#term=lineal%20cocktail%20shaker&page=1&position=17");
    private final TitleCredits currencyChange = new TitleCredits("Change free icon","https://www.flaticon.com/free-icon/change_417107#term=currency%20change%20lineal&page=1&position=22");
    //private final TitleCredits laminarAndTurbulentFlow = new TitleCredits("Laminar and turbulent flows.svg","https://commons.wikimedia.org/wiki/File:Laminar_and_turbulent_flows.svg");
    //private final TitleCredits phoneAxis = new TitleCredits("axis device","https://developer.android.com/images/axis_device.png");

    // all licenses
    private final License unknownLicense = new License();
    private final License sil_ofl_1_1 = new License("SIL OFL 1.1","http://scripts.sil.org/cms/scripts/page.php?site_id=nrsi&id=OFL");
    private final License flaticonBaseLicense = new License(FLATICON_BASE_LICENSE,"https://file000.flaticon.com/downloads/license/license.pdf");

    // all authors
    private final Author erikFlowers = new Author("Erik Flowers","https://twitter.com/erik_flowers");
    private final Author freepik = new Author("Freepik","https://www.flaticon.com/");
    private final Author eucalyp = new Author("Eucalyp","https://creativemarket.com/eucalyp");
    private final Author nikitaGolubev = new Author("Nikita Golubev","https://www.flaticon.com/authors/nikita-golubev");
    private final Author smashicons = new Author("Smashicons","https://www.flaticon.com/authors/smashicons");
    private final Author mavadee = new Author("mavadee","https://www.flaticon.com/authors/mavadee");
    //private final Author guillom = new Author("Guillom","https://commons.wikimedia.org/wiki/User:Guillom");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_icons);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ListView listView = findViewById(R.id.lv_about_icons);

        // all icons to the list
        iconCredits.add(new IconCredit(cocktailShaker,freepik,flaticonBaseLicense,R.drawable.ic_shaker));
        iconCredits.add(new IconCredit(plate_fork_and_knife,freepik,flaticonBaseLicense,R.drawable.ic_plate_fork_and_knife));
        iconCredits.add(new IconCredit(set_square,eucalyp,flaticonBaseLicense,R.drawable.ic_set_square));
        iconCredits.add(new IconCredit(titleEmpty,freepik,flaticonBaseLicense,R.drawable.ic_mechanical_gears));
        iconCredits.add(new IconCredit(currencyChange,freepik,flaticonBaseLicense,R.drawable.ic_currency_change));
        iconCredits.add(new IconCredit(speedometer,freepik,flaticonBaseLicense,R.drawable.ic_speedometer));
        iconCredits.add(new IconCredit(shirt,nikitaGolubev,flaticonBaseLicense,R.drawable.ic_shirt));
        iconCredits.add(new IconCredit(kangaroo,freepik,flaticonBaseLicense,R.drawable.ic_img_kangaroo_orange));
        iconCredits.add(new IconCredit(coordinates,smashicons,flaticonBaseLicense,R.drawable.ic_coordinates));
        iconCredits.add(new IconCredit(football,mavadee,flaticonBaseLicense,R.drawable.ic_football));
        // add all weather icons
        for (Integer iconID : weatherIcon) {
            iconCredits.add(new IconCredit(weatherIcons, erikFlowers, sil_ofl_1_1, iconID));
        }

        // fill content of listView
        listView.setAdapter(new AboutIconsOverviewAdapter(this, iconCredits));

        // set OnClickListener on ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                IconCredit selectedIconCredit = iconCredits.get(position);
                DialogAboutIconCreditsLinks dia = new DialogAboutIconCreditsLinks(AboutIconsActivity.this,selectedIconCredit);
                dia.show();
            }
        });
    }

    public void onBackPressed() {
        Intent intent = new Intent(this, AboutActivity.class);
        finish();
        startActivity(intent);
    }
}
