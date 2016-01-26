package com.makingiants.android.banjotuner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.ToggleButton;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import io.fabric.sdk.android.Fabric;
import java.io.IOException;

public class EarActivity extends AppCompatActivity {
  @Bind(R.id.ear_radiogroup_sounds) RadioGroup radioGroupButtons;
  @Bind(R.id.ear_layout_main) TouchDrawLayout touchDrawLayout;
  @Bind(R.id.ear_ads) AdView adView;
  private SoundPlayer player;

  //<editor-fold desc="Activity Overrides">
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Fabric.with(this, new Crashlytics());
    setContentView(R.layout.activity_ear_ads);
    ButterKnife.bind(this);

    AdRequest adRequest;
    if (BuildConfig.DEBUG) {
      adRequest = new AdRequest.Builder().addTestDevice("027c6ee5571a8376").build();
    } else {
      adRequest = new AdRequest.Builder().build();
    }
    adView.loadAd(adRequest);

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

  @OnClick({ R.id.ear_button_1, R.id.ear_button_2, R.id.ear_button_3, R.id.ear_button_4 })
  public void onEarButtonClick(ToggleButton button) {
    touchDrawLayout.setShouldPaintTouchBitmap(false);
    radioGroupButtons.check(button.getId());

    if (button.isChecked()) {
      touchDrawLayout.setShouldPaintTouchBitmap(true);
      int buttonTag = Integer.parseInt(button.getTag().toString());
      try {
        player.playWithLoop(buttonTag);
      } catch (IOException e) {
        e.printStackTrace();
      }
    } else {
      touchDrawLayout.setShouldPaintTouchBitmap(false);
      player.stop();
    }
  }

  // Draw the hand on touch
  @OnTouch({ R.id.ear_button_1, R.id.ear_button_2, R.id.ear_button_3, R.id.ear_button_4 })
  public boolean OnTouch(Button button, MotionEvent event) {
    if (event.getAction() == MotionEvent.ACTION_UP) {
      int yDifference = button.getHeight() * (3 - Integer.parseInt(button.getTag().toString()));
      touchDrawLayout.setTouch(event.getX(), event.getY() + yDifference);
    }
    return false;
  }
  //</editor-fold>
}

