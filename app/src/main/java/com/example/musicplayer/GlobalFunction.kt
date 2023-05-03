package com.example.musicplayer

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.example.musicplayer.service.MusicService

class GlobalFunction {
    companion object {
        fun startMusicService(ctx: Context, action: Int, songPosition: Int) {
            val musicService = Intent(ctx, MusicService::class.java)
            musicService.putExtra(Constant.MUSIC_ACTION, action)
            musicService.putExtra(Constant.SONG_POSITION, songPosition)
            ContextCompat.startForegroundService(ctx, musicService)
        }

    }
}