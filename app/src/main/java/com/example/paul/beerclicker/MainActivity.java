package com.example.paul.beerclicker;

import android.content.ComponentCallbacks2;
import android.content.SharedPreferences;
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

        beerCounter = (TextView)findViewById(R.id.beerCounter);

        SharedPreferences sharedPref= getSharedPreferences(PREFS_NAME, 0);

        if (sharedPref.contains("beerCounter"))
        {
            String savedBeerCounter = sharedPref.getString("beerCounter", "");

            if (!savedBeerCounter.equals("0"))
            {
                beerCounter.setText(savedBeerCounter);
            }
        }

        ButterKnife.bind(this);
    }

    @OnClick(R.id.getBeerBtn)
    public void submit(View view) {
        String count = beerCounter.getText().toString();

        int intBeerCounter = Integer.parseInt(count);
        intBeerCounter++;

        beerCounter.setText(String.valueOf(intBeerCounter));
    }

    @Override
    protected void onStop(){
        super.onStop();

        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("beerCounter", beerCounter.getText().toString());

        // Commit the edits!
        editor.commit();
    }
}
