package com.example.musicplayer.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.musicplayer.R
import com.example.musicplayer.view.fragment.HomeFragment
import com.example.musicplayer.view.fragment.NoticeMusic
import com.example.musicplayer.view.fragment.Setting
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity() {
    private var bottomNavigationView: BottomNavigationView? = null

    private val homeFragment = HomeFragment()
    private val settingsFragment = Setting()
    private val myMusicFragment = NoticeMusic()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavigationView = findViewById(R.id.bottom_navigation)
        supportFragmentManager.beginTransaction().replace(R.id.container, homeFragment)
            .commit()
        val badgeDrawable = bottomNavigationView!!.getOrCreateBadge(R.id.mymusic)
        badgeDrawable.isVisible = true
        badgeDrawable.number = 999
        bottomNavigationView!!.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, homeFragment).commit()
                    return@OnItemSelectedListener true
                }
                R.id.mymusic -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, myMusicFragment).commit()
                    return@OnItemSelectedListener true
                }
                R.id.settings -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, settingsFragment).commit()
                    return@OnItemSelectedListener true
                }
            }
            false
        })
    }
}