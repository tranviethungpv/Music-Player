package com.example.musicplayer.listener

import com.example.musicplayer.model.Artist
import com.example.musicplayer.model.Song

interface IArtistClickListener {
    fun onArtistClick(artist: Artist)
}