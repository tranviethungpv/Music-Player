package com.example.musicplayer.remote

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.musicplayer.model.Artist
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ArtistRemote {
    private val db = Firebase.firestore
    fun getAllArtist(): MutableLiveData<ArrayList<Artist>> {
        val artistLiveData = MutableLiveData<ArrayList<Artist>>()
        val docRef = db.collection("artists")
        val artists: MutableList<Artist> = mutableListOf()
        docRef.get().addOnSuccessListener { result ->
            for (snapshot in result) {
                val artist = snapshot.toObject(Artist::class.java)
                artists.add(artist)
            }
            artistLiveData.value = ArrayList(artists)
        }.addOnFailureListener { exception ->
            Log.d(ContentValues.TAG, "get failed with ", exception)
        }

        return artistLiveData
    }

}