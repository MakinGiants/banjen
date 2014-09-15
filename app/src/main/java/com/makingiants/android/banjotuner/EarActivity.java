package com.makingiants.android.banjotuner;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioGroup;
import android.widget.ToggleButton;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

//TODO: Manage release of sounds onPause/onResume etc...
public class EarActivity extends Activity implements OnClickListener {

    private RadioGroup radioGroupButtons;
    private SoundPlayer player;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ear);

        setListeners();
        setAds();

        player = new SoundPlayer(this);
    }

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