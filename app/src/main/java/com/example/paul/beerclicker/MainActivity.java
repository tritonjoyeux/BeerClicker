package com.example.paul.beerclicker;

import java.util.Calendar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    final Handler ha2 = new Handler();
    final Handler ha = new Handler();
    public static final String PREFS_NAME = "MyPrefsFile";
    @BindView(R.id.beerCounter)
    TextView beerCounter;
    @BindView(R.id.productionText)
    TextView production;

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

        if (sharedPref.contains("stopTime")) {
            int incre = 0;
            String secondsText = sharedPref.getString("stopTime", "");
            int secondStop = Integer.parseInt(secondsText);

            Calendar c = Calendar.getInstance();
            int seconds = c.get(Calendar.SECOND);

            if (seconds - secondStop > 3600) {
                incre = welcomeBack(3600);
            } else if (seconds - secondStop <= 3600 && seconds - secondStop >= 0) {
                incre = welcomeBack(seconds - secondStop);
            }

            Context context = getApplicationContext();
            CharSequence text = "You have win " + String.valueOf(incre) + " beers";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }

        ha.postDelayed(new Runnable() {
            @Override
            public void run() {
                timer();
                ha.postDelayed(this, 1000);
            }
        }, 1000);

        ha2.postDelayed(new Runnable() {
            @Override
            public void run() {
                refresh();
                ha2.postDelayed(this, 1000);
            }
        }, 1000);

        ButterKnife.bind(this);
    }

    protected int welcomeBack(int seconds) {
        SharedPreferences sharedPref = getSharedPreferences(PREFS_NAME, 0);
        int intHoublonCounter = 0;
        int intBeerCounter = 0;
        int intPubCounter = 0;

        if (sharedPref.contains("beerCounter") && sharedPref.contains("houblonCounter") && sharedPref.contains("pubCounter")) {

            String savedBeerCounter = sharedPref.getString("beerCounter", "");
            intBeerCounter = Integer.parseInt(savedBeerCounter);

            String houblonCounter = sharedPref.getString("houblonCounter", "");
            intHoublonCounter = Integer.parseInt(houblonCounter);

            String pubCounter = sharedPref.getString("pubCounter", "");
            intPubCounter = Integer.parseInt(pubCounter);

            intBeerCounter += (intHoublonCounter * seconds) + (intPubCounter * 10 * seconds);
        }

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("beerCounter", String.valueOf(intBeerCounter));

        return (intHoublonCounter * seconds) + (intPubCounter * 10 * seconds);
    }

    protected void timer() {
        SharedPreferences sharedPref = getSharedPreferences(PREFS_NAME, 0);
        int intBeerCounter = 0;
        int intHoublonCounter = 0;
        int intPubCounter = 0;

        if (sharedPref.contains("beerCounter") && sharedPref.contains("houblonCounter")) {

            String savedBeerCounter = sharedPref.getString("beerCounter", "");
            intBeerCounter = Integer.parseInt(savedBeerCounter);

            String houblonCounter = sharedPref.getString("houblonCounter", "");
            intHoublonCounter = Integer.parseInt(houblonCounter);

            String pubCounter = sharedPref.getString("pubCounter", "");
            intPubCounter = Integer.parseInt(pubCounter);

            production.setText(String.valueOf(intHoublonCounter + (intPubCounter * 10) + " beers / second"));

            intBeerCounter += intHoublonCounter + (intPubCounter * 10);
        }

        Calendar c = Calendar.getInstance();
        int seconds = c.get(Calendar.SECOND);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("beerCounter", String.valueOf(intBeerCounter));
        editor.putString("houblonCounter", String.valueOf(intHoublonCounter));
        editor.putString("pubCounter", String.valueOf(intPubCounter));
        editor.putString("stopTime", String.valueOf(seconds));

        editor.commit();
    }

    private void refresh() {
        SharedPreferences sharedPref = getSharedPreferences(PREFS_NAME, 0);
        int intBeerCounter = 0;

        if (sharedPref.contains("beerCounter")) {
            String savedBeerCounter = sharedPref.getString("beerCounter", "");
            intBeerCounter = Integer.parseInt(savedBeerCounter);
        }

        beerCounter.setText(String.valueOf(intBeerCounter + " beers"));
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

        beerCounter.setText(String.valueOf(intBeerCounter + " beers"));

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("beerCounter", String.valueOf(intBeerCounter));

        editor.commit();
    }

    @OnClick(R.id.upgradeButton)
    protected void loadUpgradeActivity(View v) {
        Intent intent_info = new Intent(MainActivity.this, UpgradeActivity.class);
        intent_info.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent_info);
        overridePendingTransition(R.anim.slide_up, R.anim.no_change);
    }

    @OnClick(R.id.reset)
    protected void reset() {
        SharedPreferences sharedPref = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("beerCounter", String.valueOf(0));
        editor.putString("houblonCounter", String.valueOf(0));
        editor.putString("pubCounter", String.valueOf(0));
        editor.putString("houblonPrice", String.valueOf(50));
        editor.putString("pubPrice", String.valueOf(500));
        beerCounter.setText(String.valueOf(0 + " beers"));
        editor.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ha.removeCallbacksAndMessages(null);
        ha2.removeCallbacksAndMessages(null);
    }
}
