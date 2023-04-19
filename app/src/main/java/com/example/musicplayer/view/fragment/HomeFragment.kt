package com.example.musicplayer.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.listener.ISongClickListener
import com.example.musicplayer.databinding.FragmentHomeBinding
import com.example.musicplayer.model.Song
import com.example.musicplayer.view.activity.PlayMusicActivity
import com.example.musicplayer.view.adapter.ListSongAdapter
import com.example.musicplayer.viewmodel.SongViewModel

class HomeFragment : Fragment() {
    private lateinit var songAdapter: ListSongAdapter
    private lateinit var recyclerViewListSong: RecyclerView
    private lateinit var songViewModel: SongViewModel
    private lateinit var fragmentHomeBinding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        renderListSong()
        return fragmentHomeBinding.root
    }

    private fun renderListSong(){
        recyclerViewListSong = fragmentHomeBinding.recyclerListSongs
        recyclerViewListSong.layoutManager = GridLayoutManager(activity, 2)
        val dividerDecoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        recyclerViewListSong.addItemDecoration(dividerDecoration)
        songViewModel = ViewModelProvider(this)[SongViewModel::class.java]
        songViewModel.allSongs.observe(viewLifecycleOwner, Observer {
            songAdapter = ListSongAdapter(it, object : ISongClickListener {
                override fun onSongClick(song: Song) {
                    playSong(song)
                }
            })
            recyclerViewListSong.adapter = songAdapter
        })
    }

    private fun playSong(song: Song) {
        val intent = Intent(context, PlayMusicActivity::class.java)
        startActivity(intent)
    }
}