package com.makingiants.banjotuner;

import java.io.IOException;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.util.Log;

/**
 * Sound player for android
 */
public class SoundPlayer implements OnPreparedListener, OnCompletionListener {
    
    // ****************************************************************
    // Attributes
    private final static int STREAM_TYPE = AudioManager.STREAM_MUSIC;
    private final MediaPlayer mediaPlayer;
    AudioManager manager;
    private Context context;
    
    // ****************************************************************
    // Constructor
    // ****************************************************************
    
    public SoundPlayer(final Context context) {
        
        this.context = context;
        
        manager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(STREAM_TYPE);
        mediaPlayer.setVolume(1.0f, 1.0f);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
    }
    
    // ****************************************************************
    // Sound play methods
    // ****************************************************************
    
    /**
     * Play the sound in path synchronized (only one sound at time)
     * 
     * @param path
     *            file to play
     * @throws java.io.IOException
     */
    public void playSynchronized(final String path) throws IOException, InterruptedException {
        
        final AssetFileDescriptor afd = context.getAssets().openFd(path);
        
        synchronized (mediaPlayer) {
            
            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(),
                    afd.getLength());
            
            mediaPlayer.prepare();
            mediaPlayer.start();
            
            // Make the other synchronized calls to wait until the file
            // is
            // played
            Thread.sleep(mediaPlayer.getDuration());
            
            mediaPlayer.stop();
            mediaPlayer.reset();
            
        }
        
        afd.close();
    }
    
    /**
     * Play the sound in path and if there are any sound playing it will stop
     * and play the new one if there are no sounds playing now, else stop the
     * sound
     * 
     * @param path
     *            file to play
     * @throws java.io.IOException
     */
    public void play(final String path) throws IOException, InterruptedException {
        
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.reset();
        }
        
        final AssetFileDescriptor afd = context.getAssets().openFd(path);
        
        mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
        mediaPlayer.prepareAsync();
        
        afd.close();
        
    }
    
    /**
     * Play the sound in path and if there are any sound playing it will stop
     * and play the new one if there are no sounds playing now, else stop the
     * sound
     * 
     * @param path
     *            file to play
     * @throws java.io.IOException
     */
    public void playWithLoop(final String path) throws IOException, InterruptedException {
        
        if (mediaPlayer.isPlaying()) {
            stop();
        }
        
        final AssetFileDescriptor afd = context.getAssets().openFd(path);
        
        mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
        mediaPlayer.setLooping(true);
        mediaPlayer.prepareAsync();
        
        afd.close();
        
    }
    
    /**
     * Reset the player with a mute to avoid 
     * a cut sound on reset.
     */
    public void stop() {
        manager.setStreamMute(STREAM_TYPE, true);
        
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Log.e(context.getString(R.string.app_name), "stop InterruptedException", e);
        }
        
        mediaPlayer.reset();
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
