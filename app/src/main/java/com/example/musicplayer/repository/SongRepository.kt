package com.example.musicplayer.repository

import androidx.lifecycle.MutableLiveData
import com.example.musicplayer.model.Song
import com.example.musicplayer.remote.SongService

class SongRepository {
    private val songService = SongService()
    fun getAllSong(): MutableLiveData<ArrayList<Song>> {
        return songService.getAllSong()
    }
    fun gatSongByHint(hint:String):MutableLiveData<ArrayList<Song>>
    {
        return songService.getAllSong();
    }


}