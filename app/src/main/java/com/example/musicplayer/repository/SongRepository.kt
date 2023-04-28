package com.example.musicplayer.repository

import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import com.example.musicplayer.R
import com.example.musicplayer.model.Song
import com.example.musicplayer.remote.SongService

class SongRepository {
    private val songService = SongService()

    fun getAllSong(): MutableLiveData<ArrayList<Song>> {
        return songService.getAllSong()
    }
    fun getSongByHint(textView: TextView): MutableLiveData<ArrayList<Song>> {
        val hint = textView.text.toString()
        return songService.GetSongBYHint(hint)
    }



}