package com.example.musicplayer.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.bumptech.glide.Glide
import com.example.musicplayer.Constant
import com.example.musicplayer.GlobalFunction
import com.example.musicplayer.GlobalFunction.Companion.getCircularBitmap
import com.example.musicplayer.R
import com.example.musicplayer.model.Song
import com.example.musicplayer.view.activity.PlayMusicActivity
import java.util.Random

class MusicService : Service(), MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {
    companion object {
        var isPlaying = false
        var currentListSong = mutableListOf<Song>()
        var shuffleListSong = mutableListOf<Song>()
        var originalListSong = mutableListOf<Song>()
        var songPosition = 0
        var mediaPlayer: MediaPlayer? = null
        var lengthSong = 0
        var songAction = -1
        var isShuffle = false
        var repeatMode = Constant.REPEAT_NONE

        fun clearListSong() {
            currentListSong.clear()
            shuffleListSong.clear()
            originalListSong.clear()
        }

        fun shuffleMusic(context: Context) {
            if (!isShuffle) {
                val currentSong = currentListSong[songPosition]
                val random = Random()
                val shuffledList = ArrayList(currentListSong)
                for (i in shuffledList.size - 1 downTo 1) {
                    val j = random.nextInt(i + 1)
                    val temp = shuffledList[i]
                    shuffledList[i] = shuffledList[j]
                    shuffledList[j] = temp
                }
                shuffleListSong.clear()
                shuffleListSong.addAll(shuffledList)
                songPosition = shuffleListSong.indexOf(currentSong)

                isShuffle = true
                val intent = Intent(Constant.SHUFFLE_ACTION)
                intent.putExtra(Constant.SHUFFLE_STATE, isShuffle)
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
            } else {
                val currentSong = currentListSong[songPosition]
                isShuffle = false
                songPosition = originalListSong.indexOf(currentSong)
                val intent = Intent(Constant.SHUFFLE_ACTION)
                intent.putExtra(Constant.SHUFFLE_STATE, isShuffle)
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
            }
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        currentListSong = if (!isShuffle) {
            originalListSong
        } else {
            shuffleListSong
        }
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
        when (repeatMode) {
            Constant.REPEAT_NONE -> {
                isPlaying = false
                pauseSong()
                songAction = Constant.PAUSE
                sendMusicNotification()
                sendBroadcastChangeListener()
            }

            Constant.REPEAT_ALL -> {
                songAction = Constant.NEXT
                GlobalFunction.processForShuffle()
                nextSong()
                sendMusicNotification()
                sendBroadcastChangeListener()
            }

            Constant.REPEAT_ONE -> {
                mp.seekTo(0)
                mp.start()
                songAction = Constant.PLAY
                sendMusicNotification()
                sendBroadcastChangeListener()
            }
        }
    }

    override fun onPrepared(mp: MediaPlayer) {
        lengthSong = mediaPlayer!!.duration
        mp.start()
        isPlaying = true
        songAction = Constant.PLAY
        sendMusicNotification()
        sendBroadcastChangeListener()
    }

    private fun handleActionMusic(action: Int) {
        when (action) {
            Constant.PLAY -> playSong()
            Constant.PREVIOUS -> prevSong()
            Constant.NEXT -> nextSong()
            Constant.PAUSE -> pauseSong()
            Constant.RESUME -> resumeSong()
            Constant.CANCEL_NOTIFICATION -> cancelNotification()
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
        val songUrl = currentListSong[songPosition].url
        if (songUrl?.isNotEmpty() == true) {
            playMediaPlayer(songUrl)
            sendMusicNotification()
            sendBroadcastChangeListener()
        }
    }

    private fun pauseSong() {
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.pause()
            isPlaying = false
            sendMusicNotification()
            sendBroadcastChangeListener()
        }
    }

    private fun resumeSong() {
        if (mediaPlayer != null) {
            mediaPlayer?.start()
            isPlaying = true
            sendMusicNotification()
            sendBroadcastChangeListener()
        }
    }

    private fun prevSong() {
        if (repeatMode == Constant.REPEAT_ONE) {
            mediaPlayer?.seekTo(0)
            mediaPlayer?.start()
            songAction = Constant.PLAY
            sendMusicNotification()
            sendBroadcastChangeListener()
        } else {
            if (currentListSong.size > 1) {
                if (songPosition > 0) {
                    songPosition--
                } else {
                    songPosition = currentListSong.size - 1
                }
            } else {
                songPosition = 0
            }
            sendMusicNotification()
            sendBroadcastChangeListener()
            playSong()
        }
    }

    private fun nextSong() {
        if (repeatMode == Constant.REPEAT_ONE) {
            mediaPlayer?.seekTo(0)
            mediaPlayer?.start()
            songAction = Constant.PLAY
            sendMusicNotification()
            sendBroadcastChangeListener()
        } else {
            if (currentListSong.size > 1 && songPosition < currentListSong.size - 1) {
                songPosition++
            } else {
                songPosition = 0
            }
            sendMusicNotification()
            sendBroadcastChangeListener()
            playSong()
        }
    }

    private fun cancelNotification() {
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.pause()
            isPlaying = false
        }
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()
        sendBroadcastChangeListener()
        stopSelf()
    }

    @SuppressLint("RemoteViewLayout")
    private fun sendMusicNotification() {
        val song = currentListSong[songPosition]

        val pendingFlag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
        val intent = Intent(applicationContext, PlayMusicActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, pendingFlag)

        val remoteViews =
            RemoteViews(applicationContext.packageName, R.layout.layout_push_notification_music)
        if (repeatMode != Constant.REPEAT_NONE) {
            remoteViews.setTextViewText(R.id.tv_song_name, song.title)
            remoteViews.setTextViewText(R.id.tv_artist, song.artist)
            // Load the image using Glide
            try {
                val bitmap: Bitmap =
                    Glide.with(applicationContext).asBitmap().load(song.image.toString())
                        .submit(512, 512).get()
                remoteViews.setImageViewBitmap(R.id.img_song, getCircularBitmap(bitmap))
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }

        // Check if the notification channel exists (needed in Android 8.0 and above)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationManagerCompat.from(this)
                .getNotificationChannel("channel_music_player_id")
            if (channel == null) {
                // Channel does not exist, create it
                val name = "Music Player"
                val descriptionText = "Music Player Notifications"
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channelNotification =
                    NotificationChannel("channel_music_player_id", name, importance).apply {
                        description = descriptionText
                    }
                val notificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channelNotification)
            }
        }

        // Set listeners
        remoteViews.setOnClickPendingIntent(
            R.id.img_previous, GlobalFunction.openMusicReceiver(this, Constant.PREVIOUS)
        )
        remoteViews.setOnClickPendingIntent(
            R.id.img_next, GlobalFunction.openMusicReceiver(this, Constant.NEXT)
        )
        if (isPlaying) {
            remoteViews.setImageViewResource(R.id.img_play, R.drawable.ic_pause_gray)
            remoteViews.setOnClickPendingIntent(
                R.id.img_play, GlobalFunction.openMusicReceiver(this, Constant.PAUSE)
            )
        } else {
            remoteViews.setImageViewResource(R.id.img_play, R.drawable.ic_play_gray)
            remoteViews.setOnClickPendingIntent(
                R.id.img_play, GlobalFunction.openMusicReceiver(this, Constant.RESUME)
            )
        }
        remoteViews.setOnClickPendingIntent(
            R.id.img_close, GlobalFunction.openMusicReceiver(this, Constant.CANCEL_NOTIFICATION)
        )

        val notification = NotificationCompat.Builder(applicationContext, "channel_music_player_id")
            .setSmallIcon(R.drawable.ic_small_push_notification).setContentIntent(pendingIntent)
            .setCustomContentView(remoteViews).setSound(null).build()

        startForeground(1, notification)
    }

    private fun sendBroadcastChangeListener() {
        // Implicit intent
        val intent = Intent(Constant.CHANGE_LISTENER)
        intent.putExtra(Constant.MUSIC_ACTION, songAction)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }
}
