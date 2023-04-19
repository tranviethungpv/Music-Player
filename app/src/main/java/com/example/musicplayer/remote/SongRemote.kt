package com.example.musicplayer.remote

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.musicplayer.model.Song
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SongRemote {
    private val databaseRef = FirebaseDatabase.getInstance().getReference("/songs")
    fun getAllSong(): MutableLiveData<ArrayList<Song>> {
        val songLiveData = MutableLiveData<ArrayList<Song>>()
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val songs: MutableList<Song> = mutableListOf()

                for (songSnapshot in snapshot.children) {
                    val song = songSnapshot.getValue(Song::class.java)
                    song?.let { songs.add(it) }
                }
                songLiveData.value = ArrayList(songs)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "Error getting songs from database", error.toException())
            }
        })
        return songLiveData
    }
}