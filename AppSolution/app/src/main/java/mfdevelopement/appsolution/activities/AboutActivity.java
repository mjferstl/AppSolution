package mfdevelopement.appsolution.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import mfdevelopement.appsolution.R;

public class AboutActivity extends AppCompatActivity {

    private final String UNKONWN = "Unknown";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        TextView tv_version = findViewById(R.id.tv_about_versionName);
        Button btnIcons = findViewById(R.id.btn_about_icons);
        btnIcons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutActivity.this, AboutIconsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });

        String version = getVersionName();
        if (version.equals(UNKONWN)) {
            tv_version.setText("Version [error]");
        } else {
            tv_version.setText("Version " + version);
        }

        // make TextView with attribution to DarkSkyAPI clickable
        TextView txtv_darkSkyAPI_attribution = findViewById(R.id.txtv_about_darkSkyAPI_attribution);
        txtv_darkSkyAPI_attribution.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public String getVersionName() {
        // get version name
        try {
            return this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return UNKONWN;
        }
    }

    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);
    }
}
