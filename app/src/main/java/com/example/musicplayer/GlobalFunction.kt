package com.example.musicplayer

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.example.musicplayer.service.MusicService
import java.text.Normalizer
import java.util.regex.Pattern

class GlobalFunction {
    companion object {
        fun startMusicService(ctx: Context, action: Int, songPosition: Int) {
            val musicService = Intent(ctx, MusicService::class.java)
            musicService.putExtra(Constant.MUSIC_ACTION, action)
            musicService.putExtra(Constant.SONG_POSITION, songPosition)
            ContextCompat.startForegroundService(ctx, musicService)
        }
        fun getTextSearch(input: String?): String {
            val nfdNormalizedString = Normalizer.normalize(input, Normalizer.Form.NFD)
            val pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+")
            return pattern.matcher(nfdNormalizedString).replaceAll("")
        }
    }
}