package com.example.musicplayer.view.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.bumptech.glide.Glide
import com.example.musicplayer.Constant
import com.example.musicplayer.GlobalFunction
import com.example.musicplayer.R
import com.example.musicplayer.databinding.ActivityMainBinding
import com.example.musicplayer.model.Song
import com.example.musicplayer.service.MusicService
import com.example.musicplayer.view.fragment.HomeFragment

class MainActivity : AppCompatActivity() {
    class MainActivity : AppCompatActivity(), View.OnClickListener{
        private lateinit var mActivityMainBinding: ActivityMainBinding

        private val mBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                MusicService.songAction = intent.getIntExtra(Constant.MUSIC_ACTION, 0)
                showLayoutBottom()
            }
        }
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)
            mActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(mActivityMainBinding.root)
            LocalBroadcastManager.getInstance(this).registerReceiver(
                mBroadcastReceiver,
                IntentFilter(Constant.CHANGE_LISTENER)
            )
            openHomeScreen()
            initListener()
            displayLayoutBottom()
        }
        private fun openHomeScreen() {
            replaceFragment(HomeFragment())
            @@ -19, 4+46, 73 @@ class MainActivity : AppCompatActivity() {
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.content_frame, fragment!!).commitAllowingStateLoss()
            }
            private fun showLayoutBottom() {
                if (Constant.CANCEL_NOTIFICATION == MusicService.songAction) {
                    mActivityMainBinding.layoutBottom.layoutItem.visibility = View.GONE
                    return
                }
                mActivityMainBinding.layoutBottom.layoutItem.visibility = View.VISIBLE
                showInfoSongToLayoutBottom()
                showStatusButtonPlay()
            }

            private fun displayLayoutBottom() {
                if (MusicService.mediaPlayer == null) {
                    mActivityMainBinding.layoutBottom.layoutItem.visibility = View.GONE
                    return
                }
                mActivityMainBinding.layoutBottom.layoutItem.visibility = View.VISIBLE
                showInfoSongToLayoutBottom()
                showStatusButtonPlay()
            }

            private fun showInfoSongToLayoutBottom() {
                if (MusicService.listSongPlaying.isEmpty()) {
                    return
                }
                val currentSong: Song = MusicService.listSongPlaying[MusicService.songPosition]
                mActivityMainBinding.layoutBottom.tvSongName.text = currentSong.title.toString()
                mActivityMainBinding.layoutBottom.tvArtist.text = currentSong.artist.toString()
                Glide.with(applicationContext).load(currentSong.image.toString())
                    .into(mActivityMainBinding.layoutBottom.imgSong)
            }

            private fun showStatusButtonPlay() {
                if (MusicService.isPlaying) {
                    mActivityMainBinding.layoutBottom.imgPlay.setImageResource(R.drawable.ic_pause_black)
                } else {
                    mActivityMainBinding.layoutBottom.imgPlay.setImageResource(R.drawable.ic_play_black)
                }
            }

            private fun initListener() {
                mActivityMainBinding.layoutBottom.imgPrevious.setOnClickListener(this)
                mActivityMainBinding.layoutBottom.imgPlay.setOnClickListener(this)
                mActivityMainBinding.layoutBottom.imgNext.setOnClickListener(this)
                mActivityMainBinding.layoutBottom.imgClose.setOnClickListener(this)
                mActivityMainBinding.layoutBottom.layoutText.setOnClickListener(this)
                mActivityMainBinding.layoutBottom.imgSong.setOnClickListener(this)
            }

            override fun onClick(v: View?) {
                when (v?.id) {
                    R.id.img_previous -> clickOnPrevButton()
                    R.id.img_play -> clickOnPlayButton()
                    R.id.img_next -> clickOnNextButton()
                    R.id.img_close -> clickOnCloseButton()
                    R.id.layout_text, R.id.img_song -> {
                        val intent = Intent(applicationContext, PlayMusicActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        applicationContext.startActivity(intent)
                    }
                }
            }

            private fun clickOnPrevButton() {
                GlobalFunction.startMusicService(
                    applicationContext,
                    Constant.PREVIOUS,
                    MusicService.songPosition
                )
            }

            private fun clickOnNextButton() {
                GlobalFunction.startMusicService(
                    applicationContext,
                    Constant.NEXT,
                    MusicService.songPosition
                )
            }

            private fun clickOnPlayButton() {
                if (MusicService.mediaPlayer?.isPlaying == true) {
                    GlobalFunction.startMusicService(
                        applicationContext,
                        Constant.PAUSE,
                        MusicService.songPosition
                    )
                } else {
                    GlobalFunction.startMusicService(
                        applicationContext,
                        Constant.RESUME,
                        MusicService.songPosition
                    )
                }
            }

            private fun clickOnCloseButton() {
                GlobalFunction.startMusicService(
                    this,
                    Constant.CANCEL_NOTIFICATION,
                    MusicService.songPosition
                )
            }
        }