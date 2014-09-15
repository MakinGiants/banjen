package com.makingiants.android.banjotuner;

import android.content.Context;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import java.io.IOException;

/**
 * Sound player for android
 */
public class SoundPlayer {

    // ****************************************************************
    // Constants
    // ****************************************************************

    private final static int STREAM_TYPE = AudioManager.STREAM_MUSIC;
    private final static String SOUNDS_PATH = "b_sounds";
    private final static String[] SOUNDS = {"1 - d.mp3", "2 - b.mp3", "3 - g.mp3", "4 - d.mp3"};


    // ****************************************************************
    // Attributes
    // ****************************************************************

    private Context context;
    private SoundPool soundPool;
    private int[] soundsId;
    private int lastStream;

    // ****************************************************************
    // Constructor
    // ****************************************************************

    public SoundPlayer(final Context context) {
        this.context = context;
        lastStream = -1;

        loadSoundsId();
    }

    private void loadSoundsId() {
        this.soundPool = new SoundPool(1, STREAM_TYPE, 0);
        this.soundsId = new int[SOUNDS.length];

        AssetManager am = context.getAssets();

        for (int i = 0; i < SOUNDS.length; i++) {
            try {
                soundsId[i] = soundPool.load(am.openFd(SOUNDS_PATH + '/' + SOUNDS[i]), 1);
            } catch (IOException e) {
                Log.e(context.getString(R.string.app_name), "IOException load asset", e);
            }
        }
    }

    public void release() {
        for (int i = 0; i < soundsId.length; i++) {
            soundPool.unload(soundsId[i]);
        }

        soundsId = null;
        soundPool.release();
    }

    // ****************************************************************
    // Sound play methods
    // ****************************************************************

    public void playWithLoop(int soundIndex) {
        stop();
        lastStream = soundPool.play(soundsId[soundIndex], 1f, 1f, 1, -1, 1f);
    }

    public void stop() {
        if (lastStream != -1) {
            soundPool.stop(lastStream);
            lastStream = -1;
        }
    }
}
