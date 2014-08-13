package com.makingiants.android.banjotuner;

import java.io.IOException;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;

/**
 * Sound player for android
 */
public class SoundPlayer implements OnPreparedListener, OnCompletionListener {

	// ****************************************************************
	// Attributes
	// ****************************************************************

	private final MediaPlayer mediaPlayer;
	private Context context;

	// ****************************************************************
	// Constructor
	// ****************************************************************

	public SoundPlayer(final Context context) {

		this.context = context;

		mediaPlayer = new MediaPlayer();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
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
	 * @throws IOException
	 */
	public void playSynchronized(final String path) throws IOException,
			InterruptedException {

		final AssetFileDescriptor afd = context.getAssets().openFd(path);

		synchronized (mediaPlayer) {

			mediaPlayer.setDataSource(afd.getFileDescriptor(),
					afd.getStartOffset(), afd.getLength());

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
	 * @throws IOException
	 */
	public void play(final String path) throws IOException,
			InterruptedException {

		if (mediaPlayer.isPlaying()) {
			mediaPlayer.reset();
		}

		final AssetFileDescriptor afd = context.getAssets().openFd(path);

		mediaPlayer.setDataSource(afd.getFileDescriptor(),
				afd.getStartOffset(), afd.getLength());

		mediaPlayer.prepareAsync();

		afd.close();

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
