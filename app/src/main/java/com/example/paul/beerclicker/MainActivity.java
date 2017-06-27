package com.example.paul.beerclicker;

import android.content.ComponentCallbacks2;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "MyPrefsFile";
    @BindView(R.id.beerCounter) TextView beerCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.beerCounter = (TextView) findViewById(R.id.beerCounter);

        SharedPreferences sharedPref = getSharedPreferences(PREFS_NAME, 0);

        if (sharedPref.contains("beerCounter")) {
            String savedBeerCounter = sharedPref.getString("beerCounter", "");

            if (!savedBeerCounter.equals("0")) {
                this.beerCounter.setText(savedBeerCounter);
            }
        }

        final Handler ha=new Handler();
        ha.postDelayed(new Runnable() {

            @Override
            public void run() {
                timer();
                ha.postDelayed(this, 1000);
            }
        }, 1000);
    }

    protected void timer() {
        SharedPreferences sharedPref= getSharedPreferences(PREFS_NAME, 0);

        String savedBeerCounter = sharedPref.getString("beerCounter", "");
        int intBeerCounter = Integer.parseInt(savedBeerCounter);

        String houblonCounter = sharedPref.getString("houblonCounter", "");
        int intHoublonCounter = Integer.parseInt(houblonCounter);

        intBeerCounter += intHoublonCounter;

        beerCounter.setText(String.valueOf(intBeerCounter));

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("beerCounter", String.valueOf(intBeerCounter));

        editor.commit();
    }

    @OnClick(R.id.increButton)
    public void submit(View view) {
        SharedPreferences sharedPref= getSharedPreferences(PREFS_NAME, 0);

        String savedBeerCounter = sharedPref.getString("beerCounter", "");

        int intBeerCounter = Integer.parseInt(savedBeerCounter);
        intBeerCounter++;

        beerCounter.setText(String.valueOf(intBeerCounter));

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("beerCounter", String.valueOf(intBeerCounter));

        editor.commit();
    }

    @OnClick(R.id.upgradeButton)
    protected void loadUpgradeActivity(View v) {
        Intent intent_info = new Intent(MainActivity.this,UpgradeActivity.class);
        startActivity(intent_info);
        overridePendingTransition(R.anim.slide_up,R.anim.no_change);
    }

    @Override
    public void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}
