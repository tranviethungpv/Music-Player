package com.example.musicplayer.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.musicplayer.Constant
import com.example.musicplayer.model.Song
import kotlin.properties.Delegates

class MusicService : Service(), MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {
    companion object {
        var isPlaying = false
        var listSongPlaying = mutableListOf<Song>()
        var songPosition = 0
        var mediaPlayer: MediaPlayer? = null
        var lengthSong = 0
        var songAction = -1
        fun clearListSongPlaying() {
            listSongPlaying.clear()
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer()
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val bundle = intent.extras
        if (bundle != null) {
            if (bundle.containsKey(Constant.MUSIC_ACTION)) {
                songAction = bundle.getInt(Constant.MUSIC_ACTION)
            }
            if (bundle.containsKey(Constant.SONG_POSITION)) {
                songPosition = bundle.getInt(Constant.SONG_POSITION)
            }

            handleActionMusic(songAction)
        }

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mediaPlayer != null) {
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

    override fun onCompletion(mp: MediaPlayer) {
        songAction = Constant.NEXT
        nextSong()
        sendBroadcastChangeListener()
    }

    override fun onPrepared(mp: MediaPlayer) {
        lengthSong = mediaPlayer!!.duration
        mp.start()
        isPlaying = true
        songAction = Constant.PLAY
        sendBroadcastChangeListener()
    }

    private fun handleActionMusic(action: Int) {
        when (action) {
            Constant.PLAY -> playSong()
            Constant.PREVIOUS -> prevSong()
            Constant.NEXT -> nextSong()
            Constant.PAUSE -> pauseSong()
            Constant.RESUME -> resumeSong()
            //Constant.CANCEL_NOTIFICATION -> cancelNotification()
            else -> Unit
        }
    }

    private fun initControl() {
        mediaPlayer?.setOnPreparedListener(this)
        mediaPlayer?.setOnCompletionListener(this)
    }

    private fun playMediaPlayer(songUrl: String) {
        try {
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.stop()
            }
            mediaPlayer?.reset()
            mediaPlayer?.setDataSource(songUrl)
            mediaPlayer?.prepareAsync()
            initControl()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun playSong() {
        val songUrl = listSongPlaying[songPosition].url
        if (songUrl?.isNotEmpty() == true) {
            playMediaPlayer(songUrl)
            sendBroadcastChangeListener()
        }
    }

    private fun pauseSong() {
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.pause()
            isPlaying = false
            sendBroadcastChangeListener()
        }
    }

    private fun resumeSong() {
        if (mediaPlayer != null) {
            mediaPlayer?.start()
            isPlaying = true
            sendBroadcastChangeListener()
        }
    }

    private fun prevSong() {
        if (listSongPlaying.size > 1) {
            if (songPosition > 0) {
                songPosition--
            } else {
                songPosition = listSongPlaying.size - 1
            }
        } else {
            songPosition = 0
        }
        sendBroadcastChangeListener()
        playSong()
    }

    private fun nextSong() {
        if (listSongPlaying.size > 1 && songPosition < listSongPlaying.size - 1) {
            songPosition++
        } else {
            songPosition = 0
        }
        sendBroadcastChangeListener()
        playSong()
    }

    private fun sendBroadcastChangeListener() {
        //implicit intent
        val intent = Intent(Constant.CHANGE_LISTENER)
        intent.putExtra(Constant.MUSIC_ACTION, songAction)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }
}
