package com.example.musicplayer.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.musicplayer.adapter.MusicViewPagerAdapter
import com.example.musicplayer.databinding.ActivityPlayMusicBinding
import com.example.musicplayer.view.fragment.ListSongPlayingFragment
import com.example.musicplayer.view.fragment.LyricsFragment
import com.example.musicplayer.view.fragment.PlaySongFragment

class PlayMusicActivity : AppCompatActivity() {
    private lateinit var activityPlayMusicBinding: ActivityPlayMusicBinding
    private lateinit var musicViewPagerAdapter: MusicViewPagerAdapter
    private val fragments = listOf(
        ListSongPlayingFragment(),
        PlaySongFragment(),
        LyricsFragment(),
        // Add more fragments as needed
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityPlayMusicBinding = ActivityPlayMusicBinding.inflate(layoutInflater)
        setContentView(activityPlayMusicBinding.root)
        musicViewPagerAdapter = MusicViewPagerAdapter(this, fragments)
        activityPlayMusicBinding.viewpager2.adapter = musicViewPagerAdapter
        activityPlayMusicBinding.indicator3.setViewPager(activityPlayMusicBinding.viewpager2)
        activityPlayMusicBinding.viewpager2.currentItem = 1
    }
}