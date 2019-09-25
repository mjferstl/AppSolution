package mfdevelopement.appsolution.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import mfdevelopement.appsolution.R;
import mfdevelopement.appsolution.listview_adapters.AboutIconsOverviewAdapter;
import mfdevelopement.appsolution.models.Icon;

public class AboutIconsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_icons);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ListView lv = findViewById(R.id.lv_about_icons);

        /*
        Create a list and add all icons with their description
         */
        List<Icon> icons = new ArrayList<>();
        icons.add(new Icon("Icon made by spovv from www.flaticon.com is licensed by CC 3.0 BY", R.drawable.ic_cocktail_with_fruit_slice));
        icons.add(new Icon("Icon made by Freepik from www.flaticon.com is licensed by CC 3.0 BY", R.drawable.ic_plate_fork_and_knife));
        icons.add(new Icon("Icon made by Eucalyp from www.flaticon.com is licensed by CC 3.0 BY", R.drawable.ic_set_square));
        icons.add(new Icon("Icon made by  from www.flaticon.com is licensed by CC 3.0 BY", R.drawable.ic_mechanical_gears));
        icons.add(new Icon("Icon made by Freepik from www.flaticon.com is licensed by CC 3.0 BY", R.drawable.ic_hand_with_currencies));
        icons.add(new Icon("Icon made by Freepik from www.flaticon.com is licensed by CC 3.0 BY", R.drawable.ic_speedometer));
        icons.add(new Icon("Icon made by Freepik from www.flaticon.com is licensed by CC 3.0 BY", R.drawable.ic_books));
        icons.add(new Icon("Icon made by Freepik from www.flaticon.com is licensed by CC 3.0 BY", R.drawable.ic_news));
        icons.add(new Icon("Icon made by Anton Saputro from www.flaticon.com is licensed by CC 3.0 BY", R.drawable.ic_shirt));
        icons.add(new Icon("Icon made by Freepik from www.flaticon.com is licensed by CC 3.0 BY", R.drawable.ic_img_kangaroo_orange));
        icons.add(new Icon("Icon made by Smashicons from www.flaticon.com is licensed by CC 3.0 BY", R.drawable.ic_coordinates));
        icons.add(new Icon("Icon made by Freepik from www.flaticon.com is licensed by CC 3.0 BY", R.drawable.ic_football));

        /*
        Die ListView befuellen
         */
        lv.setAdapter(new AboutIconsOverviewAdapter(this, icons));

    }

    public void onBackPressed() {
        Intent intent = new Intent(this, AboutActivity.class);
        finish();
        startActivity(intent);
    }

}
