package com.example.musicplayer.view.fragment

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.Constant
import com.example.musicplayer.GlobalFunction
import com.example.musicplayer.adapter.ListSongPlayingAdapter
import com.example.musicplayer.databinding.FragmentListSongPlayingBinding
import com.example.musicplayer.listener.ISongPlayingClickListener
import com.example.musicplayer.model.Song
import com.example.musicplayer.service.MusicService

class ListSongPlayingFragment : Fragment() {
    private lateinit var listSongPlayingAdapter: ListSongPlayingAdapter
    private lateinit var recyclerListSongPlaying: RecyclerView
    private lateinit var fragmentListSongPlayingBinding: FragmentListSongPlayingBinding
    private lateinit var listSong: ArrayList<Song>
    private val mBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            updateStatusListSongPlaying()
        }
    }
    private val shuffleStateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val isShuffle = intent.getBooleanExtra(Constant.SHUFFLE_STATE, false)
            if (isShuffle) {
                updateSongList(ArrayList(MusicService.shuffleListSong))
            } else {
                updateSongList(ArrayList(MusicService.originalListSong))
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        fragmentListSongPlayingBinding =
            FragmentListSongPlayingBinding.inflate(inflater, container, false)
        if (activity != null) {
            LocalBroadcastManager.getInstance(requireActivity()).registerReceiver(
                mBroadcastReceiver, IntentFilter(Constant.CHANGE_LISTENER)
            )
            LocalBroadcastManager.getInstance(requireActivity()).registerReceiver(
                shuffleStateReceiver, IntentFilter(Constant.SHUFFLE_ACTION)
            )
        }

        listSong = if (MusicService.isShuffle) {
            val list: ArrayList<Song> = ArrayList(MusicService.shuffleListSong)
            list
        } else {
            val list: ArrayList<Song> = ArrayList(MusicService.originalListSong)
            list
        }

        renderListSongPlaying()
        updateStatusListSongPlaying()
        return fragmentListSongPlayingBinding.root
    }

    private fun renderListSongPlaying() {
        recyclerListSongPlaying = fragmentListSongPlayingBinding.rcvData
        recyclerListSongPlaying.layoutManager = LinearLayoutManager(activity)
        listSongPlayingAdapter =
            ListSongPlayingAdapter(listSong, object : ISongPlayingClickListener {
                override fun onSongClick(position: Int) {
                    playSong(position)
                }
            })
        recyclerListSongPlaying.adapter = listSongPlayingAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateStatusListSongPlaying() {
        for (i in 0 until listSong.size) {
            listSong[i].isPlaying = i == MusicService.songPosition
        }
        listSongPlayingAdapter.notifyDataSetChanged()
    }

    private fun playSong(position: Int) {
        MusicService.isPlaying = false
        if (!MusicService.isShuffle) {
            MusicService.currentListSong = ArrayList(MusicService.originalListSong)
        } else {
            MusicService.currentListSong = ArrayList(MusicService.shuffleListSong)
        }
        GlobalFunction.startMusicService(requireContext(), Constant.PLAY, position)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateSongList(songList: ArrayList<Song>) {
        listSongPlayingAdapter.setSongList(songList)
    }

}