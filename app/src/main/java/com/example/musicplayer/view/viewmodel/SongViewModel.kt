package com.example.musicplayer.view.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.musicplayer.model.Song
import com.example.musicplayer.repository.SongRepository

class SongViewModel(application: Application) : AndroidViewModel(application) {
    private val songRepository = SongRepository()
    val allSongs: MutableLiveData<ArrayList<Song>> = songRepository.getAllSong()
    fun getSongByHint(hint: String): MutableLiveData<ArrayList<Song>> {
        return songRepository.getSongByHint(hint)
    }

    fun getSongByArtist(name: String): MutableLiveData<ArrayList<Song>> {
        return songRepository.getSongByArtist(name)
    }

    fun getHotSongs(): MutableLiveData<ArrayList<Song>> {
        return songRepository.getHotSongs()
    }
}