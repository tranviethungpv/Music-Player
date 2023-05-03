package com.example.musicplayer.repository

import androidx.lifecycle.MutableLiveData
import com.example.musicplayer.model.Song
import com.example.musicplayer.remote.SongRemote

class SongRepository {
    private val songRemote = SongRemote()

    fun getAllSong(): MutableLiveData<ArrayList<Song>> {
        return songRemote.getAllSong()
    }
    fun getSongByHint(hint: String): MutableLiveData<ArrayList<Song>> {
       // val hint = textView.text.toString()
        return songRemote.getSongByHint(hint)
    }
    fun getSongbyArtist(name:String):MutableLiveData<ArrayList<Song>>{
        return songRemote.getSongByArtist(name)
    }
}