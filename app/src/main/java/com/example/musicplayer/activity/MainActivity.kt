package com.example.musicplayer.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.musicplayer.R
import com.example.musicplayer.views.Home
import com.example.musicplayer.views.NoticeMusic
import com.example.musicplayer.views.Setting


import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView


class MainActivity : AppCompatActivity() {

    var bottomNavigationView: BottomNavigationView? = null

    val homeFragment = Home()
    val settingsFragment = Setting()
    val myMusicFragment = NoticeMusic()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment)
            .commit()
        val badgeDrawable = bottomNavigationView!!.getOrCreateBadge(R.id.mymusic)
        badgeDrawable.isVisible = true
        badgeDrawable.number = 999
        bottomNavigationView!!.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, homeFragment).commit()
                    return@OnItemSelectedListener true
                }
                R.id.mymusic -> {
                    getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, myMusicFragment).commit()
                    return@OnItemSelectedListener true
                }
                R.id.settings -> {
                    getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, settingsFragment).commit()
                    return@OnItemSelectedListener true
                }
            }
            false
        })
    }

}