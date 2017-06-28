package com.example.paul.beerclicker;

import android.content.ComponentCallbacks2;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
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

        SharedPreferences.Editor editor = sharedPref.edit();
        if (!sharedPref.contains("isRunning")) {
            editor.putString("isRunning", "false");
            editor.commit();
        }

        if (sharedPref.contains("beerCounter")) {
            String savedBeerCounter = sharedPref.getString("beerCounter", "");

            if (!savedBeerCounter.equals("0")) {
                this.beerCounter.setText(savedBeerCounter);
            }
        }
        Log.d("coucou", sharedPref.getString("isRunning", ""));

        if(sharedPref.getString("isRunning", "").equals("false")) {
            editor.putString("isRunning", "true");
            editor.commit();
            final Handler ha = new Handler();
            ha.postDelayed(new Runnable() {

                @Override
                public void run() {
                    timer();
                    ha.postDelayed(this, 1000);
                }
            }, 1000);
        }

        final Handler ha = new Handler();
        ha.postDelayed(new Runnable() {

            @Override
            public void run() {
                refresh();
                ha.postDelayed(this, 1000);
            }
        }, 1000);

        ButterKnife.bind(this);
    }

    protected void timer() {
        SharedPreferences sharedPref= getSharedPreferences(PREFS_NAME, 0);
        int intBeerCounter = 0;
        int intHoublonCounter = 0;

        if (sharedPref.contains("beerCounter") && sharedPref.contains("houblonCounter")) {

            String savedBeerCounter = sharedPref.getString("beerCounter", "");
            intBeerCounter = Integer.parseInt(savedBeerCounter);

            String houblonCounter = sharedPref.getString("houblonCounter", "");
            intHoublonCounter = Integer.parseInt(houblonCounter);

            intBeerCounter += intHoublonCounter;
        }

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("beerCounter", String.valueOf(intBeerCounter));
        editor.putString("houblonCounter", String.valueOf(intHoublonCounter));

        editor.commit();
    }

    private void refresh(){
        SharedPreferences sharedPref= getSharedPreferences(PREFS_NAME, 0);
        int intBeerCounter = 0;

        if (sharedPref.contains("beerCounter")) {
            String savedBeerCounter = sharedPref.getString("beerCounter", "");
            intBeerCounter = Integer.parseInt(savedBeerCounter);
        }

        beerCounter.setText(String.valueOf(intBeerCounter));
    }

    @OnClick(R.id.increButton)
    public void submit(View view) {
        ImageButton btn = (ImageButton) findViewById(R.id.increButton);
        Animation scale = AnimationUtils.loadAnimation(this, R.anim.shake);
        btn.startAnimation(scale);
        SharedPreferences sharedPref = getSharedPreferences(PREFS_NAME, 0);

        String savedBeerCounter = sharedPref.getString("beerCounter", "");

        int intBeerCounter = Integer.parseInt(savedBeerCounter);
        intBeerCounter++;

        beerCounter.setText(String.valueOf(intBeerCounter));

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("beerCounter", String.valueOf(intBeerCounter));

        editor.commit();
    }

    @OnClick(R.id.upgradeButton)
    protected void loadUpgradeActivity(View v) {
        Intent intent_info = new Intent(MainActivity.this,UpgradeActivity.class);
        startActivity(intent_info);
        overridePendingTransition(R.anim.slide_up,R.anim.no_change);
    }

    @OnClick(R.id.reset)
    protected void reset() {
        SharedPreferences sharedPref = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("beerCounter", String.valueOf(0));
        editor.putString("houblonCounter", String.valueOf(0));
        beerCounter.setText(String.valueOf(0));
        editor.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences sharedPref = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("isRunning", "false");
        editor.commit();
    }
}
