package com.example.musicplayer.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.musicplayer.Constant
import com.example.musicplayer.GlobalFunction
import com.example.musicplayer.service.MusicService

class MusicReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.extras!!.getInt(Constant.MUSIC_ACTION)
        GlobalFunction.processForShuffle()
        GlobalFunction.startMusicService(context, action, MusicService.songPosition)
    }
}