package mfdevelopement.appsolution;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class About extends AppCompatActivity {

    private String version;
    private final String UNKONWN = "Unknown";
    TextView tv_version;

    private Button btnIcons = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        tv_version = findViewById(R.id.tv_about_versionName);
        btnIcons = findViewById(R.id.btn_about_icons);
        btnIcons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(About.this, AboutIcons.class);
                startActivity(intent);
            }
        });

        version = getVersionName();
        if (version.equals(UNKONWN)) {
            tv_version.setText("Version [error]");
        } else {
            tv_version.setText("Version " + version);
        }
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
        Intent intent = new Intent(this, Main.class);
        finish();
        startActivity(intent);
    }
}
