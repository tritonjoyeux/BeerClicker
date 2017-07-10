package com.example.paul.beerclicker;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
    @BindView(R.id.houblonPrice)
    TextView houblonPrice;
    @BindView(R.id.pubPrice)
    TextView pubPrice;
    @BindView(R.id.productionPub)
    TextView productionPub;
    @BindView(R.id.productionHoublon)
    TextView productionHoublon;

    final Handler ha = new Handler();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upgrade_activity);

        this.beerCounter = (TextView) findViewById(R.id.beerCounter);
        this.houblonCounter = (TextView) findViewById(R.id.houblonCounter);
        this.houblonPrice = (TextView) findViewById(R.id.houblonPrice);
        this.pubCounter = (TextView) findViewById(R.id.pubCounter);
        this.pubPrice = (TextView) findViewById(R.id.pubPrice);

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
                this.houblonCounter.setText(savedHoublonCounter + " fields");
            }
        }

        if (sharedPref.contains("houblonPrice")) {
            String savedHoublonPrice = sharedPref.getString("houblonPrice", "");

            if (!savedHoublonPrice.equals("0")) {
                this.houblonPrice.setText(savedHoublonPrice + " beers");
            }
        }

        if (sharedPref.contains("pubCounter")) {
            String savedPubCounter = sharedPref.getString("pubCounter", "");

            if (!savedPubCounter.equals("0")) {
                this.pubCounter.setText(savedPubCounter + " pubs");
            }
        }

        if (sharedPref.contains("pubPrice")) {
            String savedPubPrice = sharedPref.getString("pubPrice", "");

            if (!savedPubPrice.equals("0")) {
                this.pubPrice.setText(savedPubPrice + " beers");
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
        int intHoublonCounter = 0;
        int intPubCounter = 0;

        if (sharedPref.contains("beerCounter")) {
            String savedBeerCounter = sharedPref.getString("beerCounter", "");
            intBeerCounter = Integer.parseInt(savedBeerCounter);
        }

        if (sharedPref.contains("houblonCounter")) {
            String savedHoublonCounter = sharedPref.getString("houblonCounter", "");
            intHoublonCounter = Integer.parseInt(savedHoublonCounter);
        }

        if (sharedPref.contains("pubCounter")) {
            String savedPubCounter = sharedPref.getString("pubCounter", "");
            intPubCounter = Integer.parseInt(savedPubCounter);
        }

        beerCounter.setText("You have : " + String.valueOf(intBeerCounter) + " beers");
        productionHoublon.setText(String.valueOf(intHoublonCounter + " beers / seconds"));
        productionPub.setText(String.valueOf((intPubCounter * 10) + " beers / seconds"));
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

        String savedHoublonPrice = sharedPref.getString("houblonPrice", "");
        int intHoublonPrice = Integer.parseInt(savedHoublonPrice);

        if (beers >= intHoublonPrice) {
            beers = beers - intHoublonPrice;
            int intHoublonCounter = Integer.parseInt(houblonCounter);
            intHoublonCounter++;

            this.houblonCounter.setText(String.valueOf(intHoublonCounter + " fields"));
            this.beerCounter.setText(String.valueOf("You have : " + beers + " beers"));

            intHoublonPrice += intHoublonPrice * 0.3;
            houblonPrice.setText(String.valueOf(intHoublonPrice + " beers"));

            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("houblonCounter", String.valueOf(intHoublonCounter));
            editor.putString("houblonPrice", String.valueOf(intHoublonPrice));
            editor.putString("beerCounter", String.valueOf(beers));

            editor.commit();
        } else {
            Context context = getApplicationContext();
            CharSequence text = "You can't purchase this yet";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }

    @OnClick(R.id.buyPubBtn)
    protected void buyPub(View v) {
        SharedPreferences sharedPref = getSharedPreferences(PREFS_NAME, 0);

        String pubCounter = sharedPref.getString("pubCounter", "");

        String savedBeerCounter = sharedPref.getString("beerCounter", "");

        Integer beers = Integer.parseInt(savedBeerCounter);

        String savedPubPrice = sharedPref.getString("pubPrice", "");
        int intPubPrice = Integer.parseInt(savedPubPrice);

        if (beers >= intPubPrice) {
            beers = beers - intPubPrice;
            int intPubCounter = Integer.parseInt(pubCounter);
            intPubCounter++;

            this.pubCounter.setText(String.valueOf(intPubCounter + " pubs"));
            this.beerCounter.setText(String.valueOf("You have : " + beers + " beers"));

            intPubPrice += intPubPrice * 0.3;

            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("pubCounter", String.valueOf(intPubCounter));
            editor.putString("pubPrice", String.valueOf(intPubPrice));
            editor.putString("beerCounter", String.valueOf(beers));

            editor.commit();
        }else {
            Context context = getApplicationContext();
            CharSequence text = "You can't purchase this yet";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ha.removeCallbacksAndMessages(null);
    }
}
