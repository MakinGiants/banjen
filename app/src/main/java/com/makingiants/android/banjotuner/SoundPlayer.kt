package com.makingiants.android.banjotuner

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.media.MediaPlayer.OnPreparedListener

/**
 * Sound player for android
 */
class SoundPlayer(private val context: Context) : OnPreparedListener, OnCompletionListener {
    private var mediaPlayer: MediaPlayer? = null
    private val manager: AudioManager
    private val soundsPath = "b_sounds"
    private val sounds = arrayOf("1 - d.mp3", "2 - b.mp3", "3 - g.mp3", "4 - d.mp3")

    init {
        manager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    }

    /**
     * Play the sound in path and if there are any sound playing it will stop
     * and play the new one if there are no sounds playing now, else stop the
     * sound

     * @param index file to play
     * *
     * @throws java.io.IOException
     */
    fun playWithLoop(index: Int) {
        if (mediaPlayer != null && mediaPlayer?.isPlaying ?: false) {
            stop()
        }

        mediaPlayer = MediaPlayer()
        mediaPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC)
        mediaPlayer?.setVolume(1.0f, 1.0f)
        mediaPlayer?.setOnPreparedListener(this)
        mediaPlayer?.setOnCompletionListener(this)

        val afd = context.assets.openFd("$soundsPath/${sounds[index]}")

        mediaPlayer?.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)

        mediaPlayer?.prepareAsync()
        mediaPlayer?.isLooping = true

        afd.close()
    }

    /**
     * Reset the player with a mute to avoid
     * a cut sound on reset.
     */
    fun stop() {
        if (mediaPlayer != null) {
            manager.setStreamMute(AudioManager.STREAM_MUSIC, true)

            mediaPlayer?.stop()
            mediaPlayer?.reset()
            mediaPlayer?.release()
            mediaPlayer = null

            manager.setStreamMute(AudioManager.STREAM_MUSIC, false)
        }
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
