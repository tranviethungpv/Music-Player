package com.example.musicplayer

class Constant {
    companion object {
        const val MUSIC_ACTION: String = "musicAction"
        const val PLAY: Int = 0
        const val PAUSE: Int = 1
        const val NEXT: Int = 2
        const val PREVIOUS: Int = 3
        const val RESUME: Int = 4
        const val CANCEL_NOTIFICATION = 5
        const val SONG_POSITION = "songPosition"
        const val CHANGE_LISTENER = "change_listener"

        //Repeat mode
        const val REPEAT_NONE = 0
        const val REPEAT_ALL = 1
        const val REPEAT_ONE = 2

        //Shuffle state
        const val SHUFFLE_ACTION: String = "Shuffle_Action"
        const val SHUFFLE_STATE: String = "Shuffle_State"
    }
}