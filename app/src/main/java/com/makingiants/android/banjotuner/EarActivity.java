package com.makingiants.android.banjotuner;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioGroup;
import android.widget.ToggleButton;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class EarActivity extends Activity implements OnClickListener {

    // ****************************************************************
    // Attributes
    // ****************************************************************

    private RadioGroup radioGroupButtons;
    private SoundPlayer player;

    // ****************************************************************
    // Activity
    // ****************************************************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ear);

        setListeners();
        setAds();

        initSoundPlayer();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (player == null) {
            initSoundPlayer();
        }
    }

    @Override
    protected void onPause() {
        radioGroupButtons.clearCheck();

        player.release();
        player = null;

        super.onPause();
    }

    // ****************************************************************
    // Initializer
    // ****************************************************************

    private void setAds() {
        //AdRequest adRequest = new AdRequest.Builder().addTestDevice("027c6ee5571a8376").build();
        AdRequest adRequest = new AdRequest.Builder().build();
        ((AdView) findViewById(R.id.ear_ads)).loadAd(adRequest);
    }

    private void setListeners() {
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

    private void initSoundPlayer() {
        player = new SoundPlayer(this);
    }

    // ****************************************************************
    // UI Events
    // ****************************************************************

    @Override
    public void onClick(View clickView) {
        radioGroupButtons.check(clickView.getId());
        ToggleButton button = (ToggleButton) clickView;

        if (button.isChecked()) {
            final int buttonTag = Integer.parseInt(button.getTag().toString());
            player.playWithLoop(buttonTag);
        } else {
            player.stop();
        }

    }
}