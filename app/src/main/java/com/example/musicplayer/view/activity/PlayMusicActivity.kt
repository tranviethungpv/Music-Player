package com.example.musicplayer.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.musicplayer.R
import com.example.musicplayer.view.fragment.HomeFragment
import com.example.musicplayer.view.fragment.PlaySongFragment

class PlayMusicActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_music)
        openPlaySongScreen()
    }
    private fun openPlaySongScreen() {
        replaceFragment(PlaySongFragment())
    }
    private fun replaceFragment(fragment: Fragment?) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.content_frame, fragment!!).commitAllowingStateLoss()
    }
}