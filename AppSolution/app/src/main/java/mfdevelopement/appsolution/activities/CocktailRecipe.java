package mfdevelopement.appsolution.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import mfdevelopement.appsolution.R;

public class CocktailRecipe extends AppCompatActivity {

    private TextView txtvIngredientOne = null;
    private TextView txtvIngredientTwo = null;
    private TextView txtvIngredientThree = null;
    private TextView txtvIngredientFour = null;
    private TextView txtvIngredientFive = null;
    private TextView txtvIngredientSix = null;
    private TextView txtvIngredientSeven = null;
    private TextView txtvIngredientEight = null;

    private TextView txtvAmountOne = null;
    private TextView txtvAmountTwo = null;
    private TextView txtvAmountThree = null;
    private TextView txtvAmountFour = null;
    private TextView txtvAmountFive = null;
    private TextView txtvAmountSix = null;
    private TextView txtvAmountSeven = null;
    private TextView txtvAmountEight = null;

    private TextView txtvTitle = null;

    private TextView txtvPreparation = null;

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

    private String CUBALIBRE_PREP = "";
    private String MAITAI_PREP = "";
    private String MOJITO_PREP = "";
    private String PLANTERSPUNCH_PREP = "";
    private String WASTEDTIME_PREP = "";
    private String TEQUILASUNRISE_PREP = "";
    private String MARGARITA_PREP = "";
    private String APHRODITESKISS_PREP = "";
    private String BLACKRUSSIAN_PREP = "";
    private String BLOODYMARY_PREP = "";
    private String HARVEYWALLBANGER_PREP = "";
    private String LONGISLANDICETEA_PREP = "";
    private String SEXONTHEBEACH_PREP = "";
    private String SWIMMINGPOOL_PREP = "";
    private String TOUCHDOWN_PREP = "";
    private String WHITERUSSIAN_PREP = "";
    private String WODKASUNRISE_PREP = "";
    private String BLANCO43_PREP = "";
    private String SOUR43_PREP = "";
    private String CAIPIRINHA_PREP = "";
    private String GINFIZZ_PREP = "";

    private String LIMETTE = "";
    private String LIMETTEN = "";
    private String ROHRZUCKER = "";
    private String WEISSERRUM = "";
    private String COLA = "";
    private String WEISSERROHRZUCKER;
    private String MINZBLAETTER = "";
    private String ZUCKERSIRUP = "";
    private String SODAWASSER = "";
    private String ORANGENSAFT = "";
    private String ZITRONENSAFT = "";
    private String ANANASSAFT = "";
    private String BRAUNERRUM = "";
    private String GRENADINESIRUP = "";
    private String LIMETTENSAFT = "";
    private String MARACUJASAFT = "";
    private String MALIBU = "";
    private String WEISSERTEQUILA = "";
    private String COINTREAU = "";
    private String TEQUILA = "";
    private String SCHWARZERJOHANNISBEERSAFT = "";
    private String PFIRSICHSAFT = "";
    private String WODKA = "";
    private String KAHLUA = "";
    private String TOMATENSAFT = "";
    private String WORCESTERSAUCE = "";
    private String SPRITZERTABASCO = "";
    private String PRISESALZ = "";
    private String PRISEPFEFFER = "";
    private String GALLIANO = "";
    private String GIN = "";
    private String TRIPLESEC = "";
    private String PFIRSICHLIKOER = "";
    private String CRANBERRYSIRUP = "";
    private String KOKOSNUSSCREME = "";
    private String SAHNE = "";
    private String BLUECURACAO = "";
    private String APRICOTBRANDY = "";
    private String MILCH = "";
    private String LICOR43 = "";
    private String BRAUNERROHRZUCKER = "";
    private String CACHACA = "";
    private String LIMETTENSIRUP = "";
    private String MANDELSIRUP = "";
    private String GINGERALE = "";
    private String SPRITZERWORCESTERSHIRE = "";

    private String LogTag = "";
    private String appname = "";
    private String COCKTAILERROR = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        appname = getString(R.string.app_name);
        LogTag = appname + "/CocktailRecipe";

        Intent intent = getIntent();
        String cocktailName = intent.getStringExtra(CocktailOverviewActivity.COCKTAIL_NAME);
        Log.i(LogTag, cocktailName);
        setContentView(R.layout.activity_cocktail_recipe);

        initTextViews();
        initStrings();

        if (cocktailName.equals(CUBALIBRE)) {
            // checked
            Log.i(LogTag, "load " + CUBALIBRE);
            setTitle(CUBALIBRE);
            setIngredientOne("1/2", LIMETTE);
            setIngredientTwo("6 cl", WEISSERRUM);
            setIngredientThree("100 ml", COLA);
            setPreparation(CUBALIBRE_PREP);
            Log.i(LogTag, CUBALIBRE + " loaded");
        } else if (cocktailName.equals(MAITAI)) {
            // checked
            Log.i(LogTag, "load " + MAITAI);
            setTitle(MAITAI);
            setIngredientOne("3 cl", WEISSERRUM);
            setIngredientTwo("3 cl", BRAUNERRUM);
            setIngredientThree("2 cl", TRIPLESEC);
            setIngredientFour("2 cl", LIMETTENSIRUP);
            setIngredientFive("1 cl", MANDELSIRUP);
            setIngredientSix("1 cl", LIMETTENSAFT);
            setIngredientSeven("3 cl", ORANGENSAFT);
            setIngredientEight("3 cl", ANANASSAFT);
            setPreparation(MAITAI_PREP);
            Log.i(LogTag, MAITAI + " loaded");
        } else if (cocktailName.equals(MOJITO)) {
            // checked
            Log.i(LogTag, "load " + MOJITO);
            setTitle(MOJITO);
            setIngredientOne("1/2", LIMETTE);
            setIngredientTwo("2 TL", ROHRZUCKER);
            setIngredientThree("6", MINZBLAETTER);
            setIngredientFour("6 cl", SODAWASSER);
            setIngredientFive("6 cl", WEISSERRUM);
            setIngredientSix("3 cl", LIMETTENSAFT);
            setPreparation(MOJITO_PREP);
            Log.i(LogTag, MOJITO + " loaded");
        } else if (cocktailName.equals(PLANTERSPUNCH)) {
            // checked
            Log.i(LogTag, "load " + PLANTERSPUNCH);
            setTitle(PLANTERSPUNCH);
            setIngredientOne("1 cl", LIMETTENSAFT);
            setIngredientTwo("10 cl", ORANGENSAFT);
            setIngredientThree("0.5 cl", ZUCKERSIRUP);
            setIngredientFour("4 cl", BRAUNERRUM);
            setIngredientFive("2 cl", SODAWASSER);
            setPreparation(PLANTERSPUNCH_PREP);
            Log.i(LogTag, PLANTERSPUNCH + " loaded");
        } else if (cocktailName.equals(WASTEDTIME)) {
            // checked
            Log.i(LogTag, "load " + WASTEDTIME);
            setTitle(WASTEDTIME);
            setIngredientOne("8 cl", MARACUJASAFT);
            setIngredientTwo("1 cl", LIMETTENSAFT);
            setIngredientThree("3 cl", MALIBU);
            setIngredientFour("2 cl", WEISSERRUM);
            setPreparation(WASTEDTIME_PREP);
            Log.i(LogTag, WASTEDTIME + " loaded");
        } else if (cocktailName.equals(TEQUILASUNRISE)) {
            // checked
            Log.i(LogTag, "load " + TEQUILASUNRISE);
            setTitle(TEQUILASUNRISE);
            setIngredientOne("10 cl", ORANGENSAFT);
            setIngredientTwo("6 cl", WEISSERTEQUILA);
            setIngredientThree("1 cl", ZITRONENSAFT);
            setIngredientFour("2 cl", GRENADINESIRUP);
            setPreparation(TEQUILASUNRISE_PREP);
            Log.i(LogTag, TEQUILASUNRISE + " loaded");
        } else if (cocktailName.equals(MARGARITA)) {
            Log.i(LogTag, "load " + MARGARITA);
            setTitle(MARGARITA);
            setIngredientOne("1.5 cl", LIMETTENSAFT);
            setIngredientTwo("2 cl", COINTREAU);
            setIngredientThree("3.5 cl", TEQUILA);
            setPreparation(MARGARITA_PREP);
            Log.i(LogTag, MARGARITA + " loaded");
        } else if (cocktailName.equals(APHRODITESKISS)) {
            Log.i(LogTag, "load " + APHRODITESKISS);
            setTitle(APHRODITESKISS);
            setIngredientOne("5 cl", SCHWARZERJOHANNISBEERSAFT);
            setIngredientTwo("5 cl", PFIRSICHSAFT);
            setIngredientThree("6 cl", MARACUJASAFT);
            setIngredientFour("6 cl", ANANASSAFT);
            setIngredientFive("2 cl", WEISSERRUM);
            setIngredientSix("2 cl", WODKA);
            setIngredientSeven("1 cl", GRENADINESIRUP);
            setPreparation(APHRODITESKISS_PREP);
            Log.i(LogTag, APHRODITESKISS + " loaded");
        } else if (cocktailName.equals(BLACKRUSSIAN)) {
            Log.i(LogTag, "load " + BLACKRUSSIAN);
            setTitle(BLACKRUSSIAN);
            setIngredientOne("3 cl", KAHLUA);
            setIngredientTwo("6 cl", WODKA);
            setPreparation(BLACKRUSSIAN_PREP);
            Log.i(LogTag, BLACKRUSSIAN + " loaded");
        } else if (cocktailName.equals(BLOODYMARY)) {
            // checked
            Log.i(LogTag, "load " + BLOODYMARY);
            setTitle(BLOODYMARY);
            setIngredientOne("5 cl", WODKA);
            setIngredientTwo("10 cl", TOMATENSAFT);
            setIngredientThree("1 ", SPRITZERWORCESTERSHIRE);
            setIngredientFour("1", SPRITZERTABASCO);
            setIngredientFive("1", PRISESALZ);
            setIngredientSix("1", PRISEPFEFFER);
            setPreparation(BLOODYMARY_PREP);
            Log.i(LogTag, BLOODYMARY + " loaded");
        } else if (cocktailName.equals(HARVEYWALLBANGER)) {
            Log.i(LogTag, "load " + HARVEYWALLBANGER);
            setTitle(HARVEYWALLBANGER);
            setIngredientOne("12 cl", ORANGENSAFT);
            setIngredientTwo("2 cl", GALLIANO);
            setIngredientThree("4 cl", WODKA);
            setPreparation(HARVEYWALLBANGER_PREP);
            Log.i(LogTag, HARVEYWALLBANGER + " loaded");
        } else if (cocktailName.equals(LONGISLANDICETEA)) {
            // checked
            Log.i(LogTag, "load " + LONGISLANDICETEA);
            setTitle(LONGISLANDICETEA);
            setIngredientOne("2 cl", WEISSERRUM);
            setIngredientTwo("2 cl", WODKA);
            setIngredientThree("2 cl", GIN);
            setIngredientFour("2 cl", COINTREAU);
            setIngredientFive("2 cl", ZITRONENSAFT);
            setIngredientSix("2 cl", ZUCKERSIRUP);
            setIngredientSeven("4 cl", GINGERALE);
            setIngredientEight("2 cl", COLA);
            setPreparation(LONGISLANDICETEA_PREP);
            Log.i(LogTag, LONGISLANDICETEA + " loaded");
        } else if (cocktailName.equals(SEXONTHEBEACH)) {
            Log.i(LogTag, "load " + SEXONTHEBEACH);
            setTitle(SEXONTHEBEACH);
            setIngredientOne("12 cl", ORANGENSAFT);
            setIngredientTwo("2 cl", PFIRSICHLIKOER);
            setIngredientThree("1 cl", CRANBERRYSIRUP);
            setIngredientFour("4 cl", WODKA);
            setPreparation(SEXONTHEBEACH_PREP);
            Log.i(LogTag, SEXONTHEBEACH + " loaded");
        } else if (cocktailName.equals(SWIMMINGPOOL)) {
            // checked
            Log.i(LogTag, "load " + SWIMMINGPOOL);
            setTitle(SWIMMINGPOOL);
            setIngredientOne("4 cl", WEISSERRUM);
            setIngredientTwo("2 cl", WODKA);
            setIngredientThree("1 cl", BLUECURACAO);
            setIngredientFour("6 cl", ANANASSAFT);
            setIngredientFive("2 cl", KOKOSNUSSCREME);
            setIngredientSix("1 cl", SAHNE);
            setPreparation(SWIMMINGPOOL_PREP);
            Log.i(LogTag, SWIMMINGPOOL + " loaded");
        } else if (cocktailName.equals(TOUCHDOWN)) {
            Log.i(LogTag, "load " + TOUCHDOWN);
            setTitle(TOUCHDOWN);
            setIngredientOne("15 cl", MARACUJASAFT);
            setIngredientTwo("2 cl", ZITRONENSAFT);
            setIngredientThree("2 cl", APRICOTBRANDY);
            setIngredientFour("4 cl", WODKA);
            setIngredientFive("2 cl", GRENADINESIRUP);
            setPreparation(TOUCHDOWN_PREP);
            Log.i(LogTag, TOUCHDOWN + " loaded");
        } else if (cocktailName.equals(WHITERUSSIAN)) {
            // checked
            Log.i(LogTag, "load " + WHITERUSSIAN);
            setTitle(WHITERUSSIAN);
            setIngredientOne("3 cl", KAHLUA);
            setIngredientTwo("3 cl", WODKA);
            setIngredientThree("5 cl", SAHNE);
            setPreparation(WHITERUSSIAN_PREP);
            Log.i(LogTag, WHITERUSSIAN + " loaded");
        } else if (cocktailName.equals(WODKASUNRISE)) {
            Log.i(LogTag, "load " + WODKASUNRISE);
            setTitle(WODKASUNRISE);
            setIngredientOne("10 cl", ORANGENSAFT);
            setIngredientTwo("4 cl", WODKA);
            setIngredientThree("1 cl", GRENADINESIRUP);
            setPreparation(WODKASUNRISE_PREP);
            Log.i(LogTag, WODKASUNRISE + " loaded");
        } else if (cocktailName.equals(BLANCO43)) {
            Log.i(LogTag, "load " + BLANCO43);
            setTitle(BLANCO43);
            setIngredientOne("8 cl", MILCH);
            setIngredientTwo("2 cl", LICOR43);
            setPreparation(BLANCO43_PREP);
            Log.i(LogTag, BLANCO43 + " loaded");
        } else if (cocktailName.equals(SOUR43)) {
            Log.i(LogTag, "load " + SOUR43);
            setTitle(SOUR43);
            setIngredientOne("3 cl", ORANGENSAFT);
            setIngredientTwo("3 cl", ZITRONENSAFT);
            setIngredientThree("6 cl", LICOR43);
            setPreparation(SOUR43_PREP);
            Log.i(LogTag, SOUR43 + " loaded");
        } else if (cocktailName.equals(CAIPIRINHA)) {
            Log.i(LogTag, "load " + CAIPIRINHA);
            setTitle(CAIPIRINHA);
            setIngredientOne("1", LIMETTE);
            setIngredientTwo("3 EL", BRAUNERROHRZUCKER);
            setIngredientThree("5 cl", CACHACA);
            setPreparation(CAIPIRINHA_PREP);
            Log.i(LogTag, CAIPIRINHA + " loaded");
        } else if (cocktailName.equals(GINFIZZ)) {
            Log.i(LogTag, "load " + GINFIZZ);
            setTitle(GINFIZZ);
            setIngredientOne("5 cl", GIN);
            setIngredientTwo("3 cl", ZITRONENSAFT);
            setIngredientThree("2 cl", ZUCKERSIRUP);
            setIngredientFour("10cl", SODAWASSER);
            setPreparation(GINFIZZ_PREP);
            Log.i(LogTag, GINFIZZ + " loaded");
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(COCKTAILERROR);
            builder.setPositiveButton(getString(R.string.btd_ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onBackPressed();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }

    }

    private void initTextViews() {

        txtvAmountOne = findViewById(R.id.txtv_cocktail_recipe_amount_1);
        txtvAmountTwo = findViewById(R.id.txtv_cocktail_recipe_amount_2);
        txtvAmountThree = findViewById(R.id.txtv_cocktail_recipe_amount_3);
        txtvAmountFour = findViewById(R.id.txtv_cocktail_recipe_amount_4);
        txtvAmountFive = findViewById(R.id.txtv_cocktail_recipe_amount_5);
        txtvAmountSix = findViewById(R.id.txtv_cocktail_recipe_amount_6);
        txtvAmountSeven = findViewById(R.id.txtv_cocktail_recipe_amount_7);
        txtvAmountEight = findViewById(R.id.txtv_cocktail_recipe_amount_8);

        txtvIngredientOne = findViewById(R.id.txtv_cocktail_recipe_ingredient_1);
        txtvIngredientTwo = findViewById(R.id.txtv_cocktail_recipe_ingredient_2);
        txtvIngredientThree = findViewById(R.id.txtv_cocktail_recipe_ingredient_3);
        txtvIngredientFour = findViewById(R.id.txtv_cocktail_recipe_ingredient_4);
        txtvIngredientFive = findViewById(R.id.txtv_cocktail_recipe_ingredient_5);
        txtvIngredientSix = findViewById(R.id.txtv_cocktail_recipe_ingredient_6);
        txtvIngredientSeven = findViewById(R.id.txtv_cocktail_recipe_ingredient_7);
        txtvIngredientEight = findViewById(R.id.txtv_cocktail_recipe_ingredient_8);

        txtvTitle = findViewById(R.id.cocktail_recipe_title);

        txtvPreparation = findViewById(R.id.txtv_cocktail_recipe_preparation);

        txtvAmountOne.setVisibility(View.GONE);
        txtvAmountTwo.setVisibility(View.GONE);
        txtvAmountThree.setVisibility(View.GONE);
        txtvAmountFour.setVisibility(View.GONE);
        txtvAmountFive.setVisibility(View.GONE);
        txtvAmountSix.setVisibility(View.GONE);
        txtvAmountSeven.setVisibility(View.GONE);
        txtvAmountEight.setVisibility(View.GONE);

        txtvIngredientOne.setVisibility(View.GONE);
        txtvIngredientTwo.setVisibility(View.GONE);
        txtvIngredientThree.setVisibility(View.GONE);
        txtvIngredientFour.setVisibility(View.GONE);
        txtvIngredientFive.setVisibility(View.GONE);
        txtvIngredientSix.setVisibility(View.GONE);
        txtvIngredientSeven.setVisibility(View.GONE);
        txtvIngredientEight.setVisibility(View.GONE);

    }

    public void initStrings() {

        appname = getString(R.string.app_name);
        LogTag = appname + "/CocktailOverviewActivity";

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

        CUBALIBRE_PREP = getString(R.string.txt_cocktail_cuba_libre_prep);
        MAITAI_PREP = getString(R.string.txt_cocktail_mai_tai_prep);
        MOJITO_PREP = getString(R.string.txt_cocktail_mojito_prep);
        PLANTERSPUNCH_PREP = getString(R.string.txt_cocktail_planterspunch_prep);
        WASTEDTIME_PREP = getString(R.string.txt_cocktail_wasted_time_prep);
        TEQUILASUNRISE_PREP = getString(R.string.txt_cocktail_tequila_sunrise_prep);
        MARGARITA_PREP = getString(R.string.txt_cocktail_margarita_prep);
        APHRODITESKISS_PREP = getString(R.string.txt_cocktail_aphrodites_kiss_prep);
        BLACKRUSSIAN_PREP = getString(R.string.txt_cocktail_black_russian_prep);
        BLOODYMARY_PREP = getString(R.string.txt_cocktail_bloody_mary_prep);
        HARVEYWALLBANGER_PREP = getString(R.string.txt_cocktail_harvey_wallbanger_prep);
        LONGISLANDICETEA_PREP = getString(R.string.txt_cocktail_long_island_ice_tea_prep);
        SEXONTHEBEACH_PREP = getString(R.string.txt_cocktail_sex_on_the_beach_prep);
        SWIMMINGPOOL_PREP = getString(R.string.txt_cocktail_swimming_pool_prep);
        TOUCHDOWN_PREP = getString(R.string.txt_cocktail_touch_down_prep);
        WHITERUSSIAN_PREP = getString(R.string.txt_cocktail_white_russian_prep);
        WODKASUNRISE_PREP = getString(R.string.txt_cocktail_wodka_sunrise_prep);
        BLANCO43_PREP = getString(R.string.txt_cocktail_blanco_43_prep);
        SOUR43_PREP = getString(R.string.txt_cocktail_sour_43_prep);
        CAIPIRINHA_PREP = getString(R.string.txt_cocktail_caipirinha_prep);
        GINFIZZ_PREP = getString(R.string.txt_cocktail_gin_fizz_prep);

        LIMETTE = getString(R.string.txt_limette);
        ROHRZUCKER = getString(R.string.txt_rohrzucker);
        WEISSERRUM = getString(R.string.txt_weisser_rum);
        COLA = getString(R.string.txt_cola);
        WEISSERROHRZUCKER = getString(R.string.txt_weisser_rohrzucker);
        MINZBLAETTER = getString(R.string.txt_minzblaetter);
        ZUCKERSIRUP = getString(R.string.txt_zuckersirup);
        SODAWASSER = getString(R.string.txt_sodawasser);
        ORANGENSAFT = getString(R.string.txt_orangensaft);
        ZITRONENSAFT = getString(R.string.txt_zitronensaft);
        ANANASSAFT = getString(R.string.txt_ananassaft);
        BRAUNERRUM = getString(R.string.txt_brauner_Rum);
        GRENADINESIRUP = getString(R.string.txt_grenadinesirup);
        MARACUJASAFT = getString(R.string.txt_maracujsaft);
        LIMETTENSAFT = getString(R.string.txt_limettensaft);
        MALIBU = getString(R.string.txt_malibu);
        WEISSERTEQUILA = getString(R.string.txt_weisser_tequila);
        COINTREAU = getString(R.string.txt_cointreau);
        TEQUILA = getString(R.string.txt_tequila);
        SCHWARZERJOHANNISBEERSAFT = getString(R.string.txt_johannisbeersaft_schwarz);
        PFIRSICHSAFT = getString(R.string.txt_pfirsichsaft);
        WODKA = getString(R.string.txt_wodka);
        KAHLUA = getString(R.string.txt_kahlua);
        TOMATENSAFT = getString(R.string.txt_tomatensaft);
        WORCESTERSAUCE = getString(R.string.txt_worcestersauce);
        SPRITZERTABASCO = getString(R.string.txt_spritzer_tabasco);
        PRISESALZ = getString(R.string.txt_prise_salz);
        PRISEPFEFFER = getString(R.string.txt_prise_pfeffer);
        GALLIANO = getString(R.string.txt_galliano);
        GIN = getString(R.string.txt_gin);
        TRIPLESEC = getString(R.string.txt_triple_sec);
        PFIRSICHLIKOER = getString(R.string.txt_pfirsichlikoer);
        CRANBERRYSIRUP = getString(R.string.txt_cranberrysirup);
        KOKOSNUSSCREME = getString(R.string.txt_kokosnusscreme);
        SAHNE = getString(R.string.txt_sahne);
        BLUECURACAO = getString(R.string.txt_blue_curacao);
        APRICOTBRANDY = getString(R.string.txt_apricot_brandy);
        MILCH = getString(R.string.txt_milch);
        LICOR43 = getString(R.string.txt_licor_43);
        BRAUNERROHRZUCKER = getString(R.string.txt_brauner_Rohrzucker);
        CACHACA = getString(R.string.txt_cachaca);
        LIMETTENSIRUP = getString(R.string.txt_limettensirup);
        MANDELSIRUP = getString(R.string.txt_mandelsirup);
        GINGERALE = getString(R.string.txt_ginger_ale);
        SPRITZERWORCESTERSHIRE = getString(R.string.txt_spritzer_worcestershire);

        COCKTAILERROR = getString(R.string.txt_cocktail_alert_msg_not_found);

    }

    public void setTitle(String string) {
        Log.i(LogTag, txtvTitle.toString());
        txtvTitle.setText(string);
    }

    public void setIngredientOne(String amount, String ingredient) {
        setIngredient(txtvAmountOne, txtvIngredientOne, amount, ingredient);
    }

    public void setIngredientTwo(String amount, String ingredient) {
        setIngredient(txtvAmountTwo, txtvIngredientTwo, amount, ingredient);
    }

    public void setIngredientThree(String amount, String ingredient) {
        setIngredient(txtvAmountThree, txtvIngredientThree, amount, ingredient);
    }

    public void setIngredientFour(String amount, String ingredient) {
        setIngredient(txtvAmountFour, txtvIngredientFour, amount, ingredient);
    }

    public void setIngredientFive(String amount, String ingredient) {
        setIngredient(txtvAmountFive, txtvIngredientFive, amount, ingredient);
    }

    public void setIngredientSix(String amount, String ingredient) {
        setIngredient(txtvAmountSix, txtvIngredientSix, amount, ingredient);
    }

    public void setIngredientSeven(String amount, String ingredient) {
        setIngredient(txtvAmountSeven, txtvIngredientSeven, amount, ingredient);
    }

    public void setIngredientEight(String amount, String ingredient) {
        setIngredient(txtvAmountEight, txtvIngredientEight, amount, ingredient);
    }

    public void setPreparation(String string) {
        txtvPreparation.setText(string);
    }

    private void setIngredient(TextView tv1, TextView tv2, String a, String i) {
        if (!a.equals("") && !i.equals("")) {
            // set text
            tv1.setText(a);
            tv2.setText(i);
            // set size
            tv1.setVisibility(View.VISIBLE);
            tv2.setVisibility(View.VISIBLE);
        }
    }

    public void onBackPressed() {
        Intent intent = new Intent(this, CocktailOverviewActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);
    }
}