package com.example.musicplayer.remote

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.musicplayer.model.Song
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

class SongRemote {
    private val db = Firebase.firestore
    private val storage = Firebase.storage
    fun getAllSong(): MutableLiveData<ArrayList<Song>> {
        val songLiveData = MutableLiveData<ArrayList<Song>>()
        val docRef = db.collection("songs")
        val songs: MutableList<Song> = mutableListOf()
        docRef.get()
            .addOnSuccessListener { result ->
                for (snapshot in result) {
                    val song = snapshot.toObject(Song::class.java)

                    val fileRef = storage.reference.child("songs/"+song.filename.toString())

                    fileRef.downloadUrl
                        .addOnSuccessListener { uri ->
                            song.url = uri.toString()
                            songs.add(song)
                            songLiveData.value = ArrayList(songs)
                        }
                        .addOnFailureListener { exception ->
                            Log.d(TAG, "getDownloadUrl failed with ", exception)
                        }
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }
        return songLiveData
    }
}