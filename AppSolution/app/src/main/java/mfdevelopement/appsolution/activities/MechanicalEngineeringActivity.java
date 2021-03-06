package mfdevelopement.appsolution.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import mfdevelopement.appsolution.R;

public class MechanicalEngineeringActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanical_engineering);

        Button btdReynolds = findViewById(R.id.meng_btd_reynolds);
        btdReynolds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Start", "Reynolds");
                showReynolds();
            }
        });

        ImageButton btnRheolocicalModels = findViewById(R.id.meng_btn_rheological_models);
        btnRheolocicalModels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRheolocialModels();
            }
        });
    }

    private void showReynolds() {
        Intent intent = new Intent(this, Reynoldszahl.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    private void showRheolocialModels() {
        Intent intent = new Intent(this, RheologicalModelsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);
    }
}
