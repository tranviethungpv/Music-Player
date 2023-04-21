package com.example.musicplayer.viewmodels

import android.app.Application
import android.widget.TextView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.viewbinding.ViewBindings
import com.example.musicplayer.R
import com.example.musicplayer.model.Song
import com.example.musicplayer.repository.SongRepository

class SongViewModel(application: Application): AndroidViewModel(application) {
    private val songRepository = SongRepository()
    private lateinit var textSearch:TextView

    val allSongs: MutableLiveData<ArrayList<Song>> = songRepository.getAllSong()
   // val songSearch:MutableLiveData<ArrayList<Song>> = songRepository.getSongByHint( textSearch.findViewById<TextView?>(R.id.edt_search_name))
}
