package com.example.musicplayer.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.musicplayer.model.Artist
import com.example.musicplayer.model.Song
import com.example.musicplayer.repository.ArtistRepository

class ArtistViewModel(application: Application): AndroidViewModel(application){
    private val artistRepository =ArtistRepository()
    val allArtists: MutableLiveData<ArrayList<Artist>> = artistRepository.getAllArtist()

}