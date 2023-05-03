package com.example.musicplayer.Remote

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.musicplayer.model.Artist
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class ArtistRemote {
    private val db = Firebase.firestore
    private val storage = Firebase.storage
    fun getAllArtist(): MutableLiveData<ArrayList<Artist>> {
        val artistliveData = MutableLiveData<ArrayList<Artist>>()
        val docRef = db.collection("artists")
        val artists: MutableList<Artist> = mutableListOf()
        docRef.get()
            .addOnSuccessListener { result ->
                for (snapshot in result) {
                    val artist= snapshot.toObject(Artist::class.java)
                    artists.add(artist)
                }
                artistliveData.value=ArrayList(artists)
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "get failed with ", exception)
            }

        return artistliveData
    }

}