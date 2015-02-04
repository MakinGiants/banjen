package com.makingiants.android.banjotuner;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.widget.RadioGroup;
import android.widget.ToggleButton;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class EarActivity extends ActionBarActivity {

    //<editor-fold desc="Attributes">

    private RadioGroup radioGroupButtons;
    private SoundPlayer player;

    //</editor-fold>

    //<editor-fold desc="Activity Overrides">

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (BuildConfig.ADS_ENABLED) {
            setContentView(R.layout.activity_ear_ads);

            AdRequest adRequest;
            if (BuildConfig.DEBUG) {
                adRequest = new AdRequest.Builder().addTestDevice("027c6ee5571a8376").build();
            } else {
                adRequest = new AdRequest.Builder().build();
            }
            AdView adView = ((AdView) findViewById(R.id.ear_ads));
            adView.loadAd(adRequest);
        } else {
            setContentView(R.layout.activity_ear);
        }

        ButterKnife.inject(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.ear_toolbar);
        toolbar.setLogo(R.drawable.ic_launcher);
        setSupportActionBar(toolbar);

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

        player = new SoundPlayer(this);
    }

    @Override
    protected void onPause() {
        radioGroupButtons.clearCheck();
        player.stop();

        super.onPause();
    }

    //</editor-fold>

    //<editor-fold desc="UI Events">

    @OnClick({R.id.ear_button_1, R.id.ear_button_2, R.id.ear_button_3, R.id.ear_button_4})
    public void onEarButtonClick(ToggleButton button) {
        radioGroupButtons.check(button.getId());

        if (button.isChecked()) {
            int buttonTag = Integer.parseInt(button.getTag().toString());
            try {
                player.playWithLoop(buttonTag);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            player.stop();
        }
    }

    //</editor-fold>
}