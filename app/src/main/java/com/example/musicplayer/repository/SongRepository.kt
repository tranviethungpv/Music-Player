package com.example.musicplayer.repository

import androidx.lifecycle.MutableLiveData
import com.example.musicplayer.model.Song
import com.example.musicplayer.remote.SongRemote

class SongRepository {
    private val songRemote = SongRemote()
    fun getAllSong(): MutableLiveData<ArrayList<Song>> {
        return songRemote.getAllSong()
    }
}