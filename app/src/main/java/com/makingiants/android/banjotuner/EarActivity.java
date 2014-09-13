package com.makingiants.android.banjotuner;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioGroup;
import android.widget.ToggleButton;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.IOException;

public class EarActivity extends Activity implements OnClickListener {
    private RadioGroup radioGroupButtons;

    private SoundPlayer player;
    private String[] tunings = { "sounds/1 - d.mp3", "sounds/2 - b.mp3", "sounds/3 - g.mp3",
            "sounds/4 - d.mp3" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ear);

        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device.
        //AdRequest adRequest = new AdRequest.Builder().addTestDevice("027c6ee5571a8376").build();
        AdRequest adRequest = new AdRequest.Builder().build();

        // Start loading the ad in the background.
        ((AdView) findViewById(R.id.ear_ads)).loadAd(adRequest);

        player = new SoundPlayer(this);

        //
        // Set Listeners
        //
        ((ToggleButton) findViewById(R.id.ear_button_1)).setOnClickListener(this);
        ((ToggleButton) findViewById(R.id.ear_button_2)).setOnClickListener(this);
        ((ToggleButton) findViewById(R.id.ear_button_3)).setOnClickListener(this);
        ((ToggleButton) findViewById(R.id.ear_button_4)).setOnClickListener(this);

        radioGroupButtons = ((RadioGroup) findViewById(R.id.ear_radiogroup_sounds));
        radioGroupButtons.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final RadioGroup radioGroup, final int i) {
                for (int j = 0; j < radioGroup.getChildCount(); j++) {
                    final ToggleButton view = (ToggleButton) radioGroup.getChildAt(j);
                    view.setChecked(view.getId() == i);
                }
            }
        });

    }

    @Override
    protected void onPause() {
        player.stop();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        player.stop();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        radioGroupButtons.check(v.getId());

        ToggleButton button = (ToggleButton) v;

        if (button.isChecked()) {
            final int buttonTag = Integer.parseInt(button.getTag().toString());

            try {
                player.playWithLoop(tunings[buttonTag]);
            } catch (IOException e) {
                Log.e(getString(R.string.app_name), "play IOException", e);
            } catch (InterruptedException e) {
                Log.e(getString(R.string.app_name), "play InterruptedException", e);
            }

        } else {
            player.stop();
        }

    }

}