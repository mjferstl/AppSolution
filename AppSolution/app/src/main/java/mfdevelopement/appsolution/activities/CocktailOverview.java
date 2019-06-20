package mfdevelopement.appsolution.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import mfdevelopement.appsolution.R;

public class CocktailOverview extends AppCompatActivity implements View.OnClickListener {

    private String CUBALIBRE = "";
    private String MAITAI = "";
    private String MOJITO = "";
    private String PLANTERSPUNCH = "";
    private String WASTEDTIME = "";
    private String TEQUILASUNRISE = "";
    private String MARGARITA = "";
    private String APHRODITESKISS = "";
    private String BLACKRUSSIAN = "";
    private String BLOODYMARY = "";
    private String HARVEYWALLBANGER = "";
    private String LONGISLANDICETEA = "";
    private String SEXONTHEBEACH = "";
    private String SWIMMINGPOOL = "";
    private String TOUCHDOWN = "";
    private String WHITERUSSIAN = "";
    private String WODKASUNRISE = "";
    private String BLANCO43 = "";
    private String SOUR43 = "";
    private String CAIPIRINHA = "";
    private String GINFIZZ = "";

    private String LogTag = "";
    private String appname = "";

    public final static String COCKTAIL_NAME = "com.mfdevelopement.appsolution.appsolution.MESSAGE";

    private Button btnCubaLibre = null;
    private Button btnMaiTai = null;
    private Button btnMojito = null;
    private Button btnPlantersPunch = null;
    private Button btnWastedTime = null;
    private Button btnTequilaSunrise = null;
    private Button btnMargarita = null;
    private Button btnAphroditesKiss = null;
    private Button btnBlackRussian = null;
    private Button btnBloodyMary = null;
    private Button btnHarveyWallbanger = null;
    private Button btnLongIslandIceTea = null;
    private Button btnSexOnTheBeach = null;
    private Button btnSwimmingPool = null;
    private Button btnTouchDown = null;
    private Button btnWhiteRussian = null;
    private Button btnWodkaSunrise = null;
    private Button btnBlanco43 = null;
    private Button btnSour43 = null;
    private Button btnCaipirinha = null;
    private Button btnGinFizz = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cocktail_overview);

        setStrings();

        initButtons();
    }

    public void setStrings() {

        appname = getString(R.string.app_name);
        LogTag = appname + "/CocktailOverview";

        CUBALIBRE = getString(R.string.cubaLibre);
        MAITAI = getString(R.string.maiTai);
        MOJITO = getString(R.string.mojito);
        PLANTERSPUNCH = getString(R.string.plantersPunch);
        WASTEDTIME = getString(R.string.wastedTime);
        TEQUILASUNRISE = getString(R.string.tequilaSunrise);
        MARGARITA = getString(R.string.margarita);
        APHRODITESKISS = getString(R.string.aphroditesKiss);
        BLACKRUSSIAN = getString(R.string.blackRussian);
        BLOODYMARY = getString(R.string.bloodyMary);
        HARVEYWALLBANGER = getString(R.string.harveyWallbanger);
        LONGISLANDICETEA = getString(R.string.longIslandIceTea);
        SEXONTHEBEACH = getString(R.string.sexOnTheBeach);
        SWIMMINGPOOL = getString(R.string.swimmingPool);
        TOUCHDOWN = getString(R.string.touchdown);
        WHITERUSSIAN = getString(R.string.whiteRussian);
        WODKASUNRISE = getString(R.string.wodkaSunrise);
        BLANCO43 = getString(R.string.blanco43);
        SOUR43 = getString(R.string.sour43);
        CAIPIRINHA = getString(R.string.caipirinha);
        GINFIZZ = getString(R.string.ginFizz);

    }

    /*
    Initialize all buttons
     */
    public void initButtons() {

        btnCubaLibre = findViewById(R.id.cocktail_button_cubaLibre);
        btnMaiTai = findViewById(R.id.cocktail_button_maiTai);
        btnMojito = findViewById(R.id.cocktail_button_mojito);
        btnPlantersPunch = findViewById(R.id.cocktail_button_plantersPunch);
        btnWastedTime = findViewById(R.id.cocktail_button_wastedTime);
        btnTequilaSunrise = findViewById(R.id.cocktail_button_tequilaSunrise);
        btnMargarita = findViewById(R.id.cocktail_button_margarita);
        btnAphroditesKiss = findViewById(R.id.cocktail_button_aphroditesKiss);
        btnBlackRussian = findViewById(R.id.cocktail_button_blackRussian);
        btnBloodyMary = findViewById(R.id.cocktail_button_bloodyMary);
        btnHarveyWallbanger = findViewById(R.id.cocktail_button_harveyWallbanger);
        btnLongIslandIceTea = findViewById(R.id.cocktail_button_longIslandIceTea);
        btnSexOnTheBeach = findViewById(R.id.cocktail_button_sexOnTheBeach);
        btnSwimmingPool = findViewById(R.id.cocktail_button_swimmingPool);
        btnTouchDown = findViewById(R.id.cocktail_button_touchDown);
        btnWhiteRussian = findViewById(R.id.cocktail_button_whiteRussian);
        btnWodkaSunrise = findViewById(R.id.cocktail_button_WodkaSunrise);
        btnBlanco43 = findViewById(R.id.cocktail_button_blanco43);
        btnSour43 = findViewById(R.id.cocktail_button_Sour43);
        btnCaipirinha = findViewById(R.id.cocktail_button_caipirinha);
        btnGinFizz = findViewById(R.id.cocktail_button_ginFizz);

        btnCubaLibre.setOnClickListener(this);
        btnMaiTai.setOnClickListener(this);
        btnMojito.setOnClickListener(this);
        btnPlantersPunch.setOnClickListener(this);
        btnWastedTime.setOnClickListener(this);
        btnTequilaSunrise.setOnClickListener(this);
        btnMargarita.setOnClickListener(this);
        btnAphroditesKiss.setOnClickListener(this);
        btnBlackRussian.setOnClickListener(this);
        btnBloodyMary.setOnClickListener(this);
        btnHarveyWallbanger.setOnClickListener(this);
        btnLongIslandIceTea.setOnClickListener(this);
        btnSexOnTheBeach.setOnClickListener(this);
        btnSwimmingPool.setOnClickListener(this);
        btnTouchDown.setOnClickListener(this);
        btnWhiteRussian.setOnClickListener(this);
        btnWodkaSunrise.setOnClickListener(this);
        btnBlanco43.setOnClickListener(this);
        btnSour43.setOnClickListener(this);
        btnCaipirinha.setOnClickListener(this);
        btnGinFizz.setOnClickListener(this);

    }

    public void onClick(View view) {

        Intent intent = new Intent(this, CocktailRecipe.class);
        String cocktailName = "";

        final int id = view.getId();

        switch (id) {
            case R.id.cocktail_button_cubaLibre:
                cocktailName = CUBALIBRE;
                break;
            case R.id.cocktail_button_maiTai:
                cocktailName = MAITAI;
                break;
            case R.id.cocktail_button_mojito:
                cocktailName = MOJITO;
                break;
            case R.id.cocktail_button_plantersPunch:
                cocktailName = PLANTERSPUNCH;
                break;
            case R.id.cocktail_button_wastedTime:
                cocktailName = WASTEDTIME;
                break;
            case R.id.cocktail_button_tequilaSunrise:
                cocktailName = TEQUILASUNRISE;
                break;
            case R.id.cocktail_button_margarita:
                cocktailName = MARGARITA;
                break;
            case R.id.cocktail_button_aphroditesKiss:
                cocktailName = APHRODITESKISS;
                break;
            case R.id.cocktail_button_blackRussian:
                cocktailName = BLACKRUSSIAN;
                break;
            case R.id.cocktail_button_bloodyMary:
                cocktailName = BLOODYMARY;
                break;
            case R.id.cocktail_button_harveyWallbanger:
                cocktailName = HARVEYWALLBANGER;
                break;
            case R.id.cocktail_button_longIslandIceTea:
                cocktailName = LONGISLANDICETEA;
                break;
            case R.id.cocktail_button_sexOnTheBeach:
                cocktailName = SEXONTHEBEACH;
                break;
            case R.id.cocktail_button_swimmingPool:
                cocktailName = SWIMMINGPOOL;
                break;
            case R.id.cocktail_button_touchDown:
                cocktailName = TOUCHDOWN;
                break;
            case R.id.cocktail_button_whiteRussian:
                cocktailName = WHITERUSSIAN;
                break;
            case R.id.cocktail_button_WodkaSunrise:
                cocktailName = WODKASUNRISE;
                break;
            case R.id.cocktail_button_blanco43:
                cocktailName = BLANCO43;
                break;
            case R.id.cocktail_button_Sour43:
                cocktailName = SOUR43;
                break;
            case R.id.cocktail_button_caipirinha:
                cocktailName = CAIPIRINHA;
                break;
            case R.id.cocktail_button_ginFizz:
                cocktailName = GINFIZZ;
                break;
        }
        intent.putExtra(COCKTAIL_NAME, cocktailName);
        startActivity(intent);
    }

    public void onBackPressed() {
        Intent intent = new Intent(this, Main.class);
        finish();
        startActivity(intent);
    }
}
