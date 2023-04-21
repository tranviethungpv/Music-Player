package com.example.musicplayer.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.musicplayer.model.Song
import com.example.musicplayer.repository.SongRepository

class SongViewModel(application: Application): AndroidViewModel(application) {
    private val songRepository = SongRepository()
    private lateinit var hint :String
    val allSongs: MutableLiveData<ArrayList<Song>> = songRepository.getAllSong()
    val songSearch:MutableLiveData<ArrayList<Song>> = songRepository.getSongByHint(hint)
}
