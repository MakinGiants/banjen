package com.makingiants.android.banjotuner

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ToggleButton
import com.crashlytics.android.Crashlytics
import com.google.android.gms.ads.AdRequest
import io.fabric.sdk.android.Fabric
import kotlinx.android.synthetic.main.activity_ear_ads.*
import java.io.IOException

class EarActivity : AppCompatActivity(), View.OnClickListener, View.OnTouchListener {
    private var player: SoundPlayer? = null

    //<editor-fold desc="Activity Overrides">
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
        setContentView(R.layout.activity_ear_ads)

        player = SoundPlayer(this)
        val adRequest: AdRequest
        if (BuildConfig.DEBUG) {
            adRequest = AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .addTestDevice("027c6ee5571a8376")
                    .build()
        } else {
            adRequest = AdRequest.Builder().build()
        }
        adsView.loadAd(adRequest)

        soundsRadioGroup.setOnCheckedChangeListener { radioGroup, i ->
            for (j in 0..radioGroup.childCount - 1) {
                val view = radioGroup.getChildAt(j) as ToggleButton
                view.isChecked = view.id == i
            }
        }

        arrayOf(ear1Button, ear2Button, ear3Button, ear4Button).forEach {
            it.setOnClickListener(this)
            it.setOnTouchListener(this)
        }
    }

    override fun onPause() {
        soundsRadioGroup.clearCheck()
        player?.stop()
        super.onPause()
    }

    //</editor-fold>
    //<editor-fold desc="UI Events">
    override fun onClick(view: View?) {
        val button = view as ToggleButton
        soundsRadioGroup.check(button.id)
        mainLayout.shouldPaintTouchBitmap = false

        if (button.isChecked) {
            mainLayout.shouldPaintTouchBitmap = true
            val buttonTag = Integer.parseInt(button.tag.toString())
            try {
                player?.playWithLoop(buttonTag)
            } catch (e: IOException) {
                Log.e("EarActivity", "Playing sound", e)
            }
        } else {
            player?.stop()
        }
    }

    override fun onTouch(view: View?, motionEvent: MotionEvent?): Boolean {
        if (motionEvent == null) {
            return false
        }

        val button = view as Button
        if (motionEvent.action == MotionEvent.ACTION_UP) {
            val yDifference = button.height * (3 - Integer.parseInt(button.tag.toString()))
            mainLayout.setTouch(motionEvent.x, motionEvent.y + yDifference)
        }
        return false
    }
    //</editor-fold>
}

