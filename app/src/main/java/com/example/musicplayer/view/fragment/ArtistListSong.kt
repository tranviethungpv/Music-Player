package com.example.musicplayer.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.Constant
import com.example.musicplayer.GlobalFunction
import com.example.musicplayer.databinding.FragmentArtistListSongBinding
import com.example.musicplayer.listener.ISongClickListener
import com.example.musicplayer.model.Song
import com.example.musicplayer.service.MusicService
import com.example.musicplayer.view.activity.PlayMusicActivity
import com.example.musicplayer.adapter.SongSearchAdapter
import com.example.musicplayer.view.viewmodel.SongViewModel

class ArtistListSong : Fragment() {
    private lateinit var songSearchAdapter: SongSearchAdapter
    private lateinit var recyclerPlaylist: RecyclerView
    private lateinit var songViewModel: SongViewModel
    private lateinit var fragmentArtistListSong: FragmentArtistListSongBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        fragmentArtistListSong = FragmentArtistListSongBinding.inflate(inflater, container, false)
        fragmentArtistListSong.playlistname.text = Artist.selectedArtistName
        renderListSong()
        fragmentArtistListSong.playallartist.setOnClickListener {
            songViewModel = ViewModelProvider(this)[SongViewModel::class.java]
            songViewModel.getSongByArtist(Artist.selectedArtistName).observe(viewLifecycleOwner) {
                MusicService.clearListSong()
                MusicService.originalListSong.addAll(it)
                MusicService.isPlaying = false
                GlobalFunction.startMusicService(requireContext(), Constant.PLAY, 0)
                GlobalFunction.startActivity(requireActivity(), PlayMusicActivity::class.java)
            }
        }
        return fragmentArtistListSong.root
    }

    private fun renderListSong() {
        recyclerPlaylist = fragmentArtistListSong.recyclerPlaylist
        recyclerPlaylist.layoutManager = LinearLayoutManager(activity)
        songViewModel = ViewModelProvider(this)[SongViewModel::class.java]
        songViewModel.getSongByArtist(Artist.selectedArtistName).observe(viewLifecycleOwner) {
            songSearchAdapter = SongSearchAdapter(it, object : ISongClickListener {
                override fun onSongClick(song: Song) {
                    playSong(song)
                }
            })
            recyclerPlaylist.adapter = songSearchAdapter
        }
    }

    private fun playSong(song: Song) {
        MusicService.originalListSong.clear()
        MusicService.originalListSong.add(song)
        GlobalFunction.startMusicService(requireContext(), Constant.PLAY, 0)
        GlobalFunction.startActivity(requireContext(), PlayMusicActivity::class.java)
    }
}