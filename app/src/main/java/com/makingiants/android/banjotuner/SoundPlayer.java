package com.makingiants.android.banjotuner;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.util.Log;

import java.io.IOException;

/**
 * Sound player for android
 */
public class SoundPlayer implements OnPreparedListener, OnCompletionListener {

    // ****************************************************************
    // Attributes
    private final static int STREAM_TYPE = AudioManager.STREAM_MUSIC;
    private MediaPlayer mediaPlayer;
    private AudioManager manager;
    private Context context;

    // ****************************************************************
    // Constructor
    // ****************************************************************

    public SoundPlayer(final Context context) {

        this.context = context;

        manager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    // ****************************************************************
    // Sound play methods
    // ****************************************************************

    /**
     * Play the sound in path and if there are any sound playing it will stop
     * and play the new one if there are no sounds playing now, else stop the
     * sound
     *
     * @param path file to play
     * @throws java.io.IOException
     */
    public void playWithLoop(final String path) throws IOException, InterruptedException {

        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            stop();
        }

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setLooping(true);
        mediaPlayer.setAudioStreamType(STREAM_TYPE);
        mediaPlayer.setVolume(1.0f, 1.0f);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);

        final AssetFileDescriptor afd = context.getAssets().openFd(path);

        mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());

        mediaPlayer.prepareAsync();

        afd.close();
    }

    /**
     * Reset the player with a mute to avoid
     * a cut sound on reset.
     */
    public void stop() {
        manager.setStreamMute(STREAM_TYPE, true);

        // TODO: Find a better way to stop the sound without sleep
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Log.e(context.getString(R.string.app_name), "stop InterruptedException", e);
        }

        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;

        manager.setStreamMute(STREAM_TYPE, false);
    }

    // ****************************************************************
    // OnPreparedListener, OnCompletionListener implements
    // ****************************************************************

    public void onPrepared(final MediaPlayer player) {
        mediaPlayer.start();
    }

    public void onCompletion(final MediaPlayer arg0) {
        mediaPlayer.reset();
    }

}
