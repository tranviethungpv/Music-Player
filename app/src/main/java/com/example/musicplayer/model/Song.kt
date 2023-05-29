package com.example.musicplayer.model

data class Song(
    var artist: String? = null,
    var id: Int? = null,
    var image: String? = null,
    var title: String? = null,
    var url: String? = null,
    var filename: String? = null,
    var isPlaying: Boolean? = null,
    var lyrics: String? = null
) {
    // No-argument constructor required for deserialization
    constructor() : this(null, null, null, null, null, null, null, null)
}
