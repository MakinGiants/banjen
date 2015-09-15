package com.makingiants.android.banjotuner;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;

import java.io.IOException;

/**
 * Sound player for android
 */
public class SoundPlayer implements OnPreparedListener, OnCompletionListener {

    //<editor-fold desc="Constants">

    private final static String SOUNDS_PATH = "b_sounds";
    private final static String[] SOUNDS = {"1 - d.mp3", "2 - b.mp3", "3 - g.mp3", "4 - d.mp3"};
    private final static int STREAM_TYPE = AudioManager.STREAM_MUSIC;

    //</editor-fold>

    //<editor-fold desc="Attributes">

    private MediaPlayer mediaPlayer;
    private AudioManager manager;
    private Context context;

    //</editor-fold>

    //<editor-fold desc="Constructor">

    public SoundPlayer(final Context context) {
        this.context = context;
        this.manager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    //</editor-fold>

    //<editor-fold desc="Sound play methods">

    /**
     * Play the sound in path and if there are any sound playing it will stop
     * and play the new one if there are no sounds playing now, else stop the
     * sound
     *
     * @param index file to play
     * @throws java.io.IOException
     */
    public void playWithLoop(int index) throws IOException {

        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            stop();
        }

        String path = SOUNDS_PATH + "/" + SOUNDS[index];

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(STREAM_TYPE);
        mediaPlayer.setVolume(1.0f, 1.0f);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);

        final AssetFileDescriptor afd = context.getAssets().openFd(path);

        mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());

        mediaPlayer.prepareAsync();
        mediaPlayer.setLooping(true);

        afd.close();
    }

    /**
     * Reset the player with a mute to avoid
     * a cut sound on reset.
     */
    public void stop() {
        if (mediaPlayer != null) {
            manager.setStreamMute(STREAM_TYPE, true);


            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;

            manager.setStreamMute(STREAM_TYPE, false);
        }
    }

    //</editor-fold>

    //<editor-fold desc="OnPreparedListener, OnCompletionListener implements">

    public void onPrepared(final MediaPlayer player) {
        mediaPlayer.start();
    }

    public void onCompletion(final MediaPlayer arg0) {
//        mediaPlayer.reset();
    }

    //</editor-fold>

}
