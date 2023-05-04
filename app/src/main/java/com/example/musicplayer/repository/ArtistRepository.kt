package com.example.musicplayer.repository

import androidx.lifecycle.MutableLiveData
import com.example.musicplayer.remote.ArtistRemote
import com.example.musicplayer.model.Artist

class ArtistRepository {
    private val artistRemote = ArtistRemote()
    fun getAllArtist(): MutableLiveData<ArrayList<Artist>> {
        return artistRemote.getAllArtist()
    }
}