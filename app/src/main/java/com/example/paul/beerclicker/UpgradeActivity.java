package com.example.paul.beerclicker;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UpgradeActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "MyPrefsFile";
    @BindView(R.id.beerCounter)
    TextView beerCounter;
    @BindView(R.id.houblonCounter)
    TextView houblonCounter;
    @BindView(R.id.pubCounter)
    TextView pubCounter;

    final Handler ha = new Handler();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upgrade_activity);

        this.beerCounter = (TextView) findViewById(R.id.beerCounter);
        this.houblonCounter = (TextView) findViewById(R.id.houblonCounter);
        this.pubCounter = (TextView) findViewById(R.id.pubCounter);

        SharedPreferences sharedPref = getSharedPreferences(PREFS_NAME, 0);

        if (sharedPref.contains("beerCounter")) {
            String savedBeerCounter = sharedPref.getString("beerCounter", "");

            if (!savedBeerCounter.equals("0")) {
                this.beerCounter.setText("You have : " + savedBeerCounter + " beers");
            }
        }

        if (sharedPref.contains("houblonCounter")) {
            String savedHoublonCounter = sharedPref.getString("houblonCounter", "");

            if (!savedHoublonCounter.equals("0")) {
                this.houblonCounter.setText(savedHoublonCounter);
            }
        }

        if (sharedPref.contains("pubCounter")) {
            String savedPubCounter = sharedPref.getString("pubCounter", "");

            if (!savedPubCounter.equals("0")) {
                this.pubCounter.setText(savedPubCounter);
            }
        }

        ha.postDelayed(new Runnable() {
            @Override
            public void run() {
                refresh();
                ha.postDelayed(this, 1000);
            }
        }, 1000);

        ButterKnife.bind(this);
    }

    private void refresh() {
        SharedPreferences sharedPref = getSharedPreferences(PREFS_NAME, 0);
        int intBeerCounter = 0;

        if (sharedPref.contains("beerCounter")) {
            String savedBeerCounter = sharedPref.getString("beerCounter", "");
            intBeerCounter = Integer.parseInt(savedBeerCounter);
        }

        beerCounter.setText("You have : " + String.valueOf(intBeerCounter) + " beers");
    }

    @OnClick(R.id.backBtn)
    protected void backToMainActivity(View v) {
        finish();
        overridePendingTransition(R.anim.no_change, R.anim.slide_down);
    }

    @OnClick(R.id.buyHoublonBtn)
    protected void buyHoublon(View v) {
        SharedPreferences sharedPref = getSharedPreferences(PREFS_NAME, 0);

        String houblonCounter = sharedPref.getString("houblonCounter", "");

        String savedBeerCounter = sharedPref.getString("beerCounter", "");

        Integer beers = Integer.parseInt(savedBeerCounter);

        if (beers >= 50) {
            beers = beers - 50;
            int intHoublonCounter = Integer.parseInt(houblonCounter);
            intHoublonCounter++;

            this.houblonCounter.setText(String.valueOf(intHoublonCounter));
            this.beerCounter.setText(String.valueOf("You have : " + beers + " beers"));

            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("houblonCounter", String.valueOf(intHoublonCounter));
            editor.putString("beerCounter", String.valueOf(beers));

            editor.commit();
        }
    }

    @OnClick(R.id.buyPubBtn)
    protected void buyPub(View v) {
        SharedPreferences sharedPref = getSharedPreferences(PREFS_NAME, 0);

        String pubCounter = sharedPref.getString("pubCounter", "");

        String savedBeerCounter = sharedPref.getString("beerCounter", "");

        Integer beers = Integer.parseInt(savedBeerCounter);

        if (beers >= 500) {
            beers = beers - 500;
            int intPubCounter = Integer.parseInt(pubCounter);
            intPubCounter++;

            this.pubCounter.setText(String.valueOf(intPubCounter));
            this.beerCounter.setText(String.valueOf("You have : " + beers + " beers"));

            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("pubCounter", String.valueOf(intPubCounter));
            editor.putString("beerCounter", String.valueOf(beers));

            editor.commit();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ha.removeCallbacksAndMessages(null);
    }
}
