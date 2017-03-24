package com.makingiants.android.banjotuner

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.media.MediaPlayer.OnPreparedListener

class SoundPlayer(private val context: Context) : OnPreparedListener, OnCompletionListener {
  private var mediaPlayer: MediaPlayer? = null
  private val audioManager by lazy { context.getSystemService(Context.AUDIO_SERVICE) as AudioManager }
  private val soundsPath = "b_sounds"
  private val sounds = arrayOf("1.mp3", "2.mp3", "3.mp3", "4.mp3")

  /**
   * Play the sound at index stopping any current playback.
   */
  fun playWithLoop(index: Int) {
    if (mediaPlayer?.isPlaying ?: false) {
      stop()
    }

    val afd = context.assets.openFd("$soundsPath/${sounds[index]}")

    mediaPlayer = MediaPlayer().apply {
      setAudioStreamType(AudioManager.STREAM_MUSIC)
      setVolume(1.0f, 1.0f)
      setOnPreparedListener(this@SoundPlayer)
      setOnCompletionListener(this@SoundPlayer)

      setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)

      prepareAsync()
      isLooping = true
    }

    afd.close()
  }

  fun stop() {
    audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true)

    mediaPlayer?.apply {
      stop()
      reset()
      release()
    }

    mediaPlayer = null
    audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false)
  }

  //<editor-fold desc="OnPreparedListener, OnCompletionListener implements">

  override fun onPrepared(player: MediaPlayer) {
    mediaPlayer?.start()
  }

  override fun onCompletion(arg0: MediaPlayer) {
    //        mediaPlayer.reset();
  }
  //</editor-fold>
}
