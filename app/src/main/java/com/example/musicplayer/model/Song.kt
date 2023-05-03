package com.example.musicplayer.model

data class Song(
    val artist: String? = null,
    val id: Int? = null,
    val image: String? = null,
    val title: String? = null,
    var url: String? = null,
    var filename: String? = null,
)
