package com.example.musicplayer.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.musicplayer.R
import com.example.musicplayer.view.fragment.HomeFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        openHomeScreen()
    }
    private fun openHomeScreen() {
        replaceFragment(HomeFragment())
    }
    private fun replaceFragment(fragment: Fragment?) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.content_frame, fragment!!).commitAllowingStateLoss()
    }
}