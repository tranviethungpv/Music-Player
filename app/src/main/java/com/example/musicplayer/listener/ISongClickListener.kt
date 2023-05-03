package com.example.musicplayer.listener

import com.example.musicplayer.model.Song

interface ISongClickListener {
    fun onSongClick(song: Song)
}