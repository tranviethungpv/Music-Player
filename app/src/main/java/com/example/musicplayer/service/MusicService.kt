package com.example.musicplayer.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.musicplayer.Constant
import com.example.musicplayer.model.Song
import kotlin.properties.Delegates

class MusicService : Service(), MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {
    companion object {
        @JvmStatic
        var mediaPlayer: MediaPlayer? = null
        @JvmStatic
        var songAction by Delegates.notNull<Int>()
        @JvmStatic
        var songPosition by Delegates.notNull<Int>()
        @JvmStatic
        var listSongPlaying: ArrayList<Song> = ArrayList()
        @JvmStatic
        var isPlaying: Boolean? = null
        @JvmStatic
        var lengthSong by Delegates.notNull<Int>()
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val bundle = intent!!.extras
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

    override fun onCompletion(mp: MediaPlayer?) {
        songAction = Constant.NEXT
        nextSong()
    }

    override fun onPrepared(mp: MediaPlayer?) {
        lengthSong = mediaPlayer?.duration ?: 0
        mp!!.start()
        isPlaying = true
        songAction = Constant.PLAY
        sendMusicNotification()
        sendBroadcastChangeListener()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mediaPlayer != null) {
            mediaPlayer!!.release()
            mediaPlayer = null
        }
    }


    private fun handleActionMusic(action: Int){
        when (action){
            Constant.PLAY -> playSong()
            Constant.PREVIOUS -> prevSong()
            Constant.NEXT -> nextSong()
            Constant.PAUSE -> pauseSong()
            Constant.RESUME -> resumeSong()
            Constant.CANCEL_NOTIFICATION -> cancelNotification()
        }
    }

    private fun playMediaPlayer(url: String){
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer!!.stop()
        }
        mediaPlayer?.reset()
        mediaPlayer?.setDataSource(url)
        mediaPlayer?.prepareAsync()
        mediaPlayer?.setOnPreparedListener(this)
        mediaPlayer?.setOnCompletionListener(this)
    }

    private fun playSong(){
        val songUrl: String = listSongPlaying[songPosition].url.toString()
        if (songUrl.isNotBlank() || songUrl.isNotEmpty()) {
            playMediaPlayer(songUrl)
        }
    }
    private fun pauseSong(){
        if (mediaPlayer != null && mediaPlayer!!.isPlaying) {
            mediaPlayer!!.pause()
            isPlaying = false
            sendMusicNotification()
            sendBroadcastChangeListener()
        }
    }
    private fun resumeSong(){
        if (mediaPlayer != null) {
            mediaPlayer!!.start()
            isPlaying = true
            sendMusicNotification()
            sendBroadcastChangeListener()
        }
    }
    private fun prevSong(){
        if (listSongPlaying.size > 1) {
            if (songPosition > 0) {
                songPosition--
            } else {
                songPosition = listSongPlaying.size - 1
            }
        } else {
            songPosition = 0
        }
        sendMusicNotification()
        sendBroadcastChangeListener()
        playSong()
    }
    private fun nextSong(){
        if (listSongPlaying.size > 1 && songPosition < listSongPlaying.size - 1) {
            songPosition++
        } else {
            songPosition = 0
        }
        sendMusicNotification()
        sendBroadcastChangeListener()
        playSong()
    }


    private fun cancelNotification(){}
    private fun sendMusicNotification(){}
    private fun sendBroadcastChangeListener(){
        val intent = Intent(Constant.CHANGE_LISTENER)
        intent.putExtra(Constant.MUSIC_ACTION, songAction)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }
}
