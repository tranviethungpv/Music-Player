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
import com.example.musicplayer.databinding.FragmentPlaylistBinding
import com.example.musicplayer.listener.ISongClickListener
import com.example.musicplayer.model.Song
import com.example.musicplayer.service.MusicService
import com.example.musicplayer.view.activity.PlayMusicActivity
import com.example.musicplayer.adapter.SongSearchAdapter
import com.example.musicplayer.view.viewmodel.SongViewModel

class PlayListMusic : Fragment() {
    private lateinit var songSearchAdapter: SongSearchAdapter
    private lateinit var recyclerPlaylist: RecyclerView
    private lateinit var songViewModel: SongViewModel
    private lateinit var fragmentPlayListMusic: FragmentPlaylistBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        fragmentPlayListMusic = FragmentPlaylistBinding.inflate(inflater, container, false)
        fragmentPlayListMusic.playlistname.text = Artist.selectedArtistName
        renderListSong()
        fragmentPlayListMusic.playallartist.setOnClickListener {
            songViewModel = ViewModelProvider(this)[SongViewModel::class.java]
            songViewModel.getSongByArtist(Artist.selectedArtistName).observe(viewLifecycleOwner) {
                MusicService.clearListSongPlaying()
                MusicService.listSongPlaying.addAll(it)
                MusicService.isPlaying = false
                GlobalFunction.startMusicService(requireContext(), Constant.PLAY, 0)
                GlobalFunction.startActivity(requireActivity(), PlayMusicActivity::class.java)
            }
        }
        return fragmentPlayListMusic.root
    }

    private fun renderListSong() {
        recyclerPlaylist = fragmentPlayListMusic.recyclerPlaylist
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
        MusicService.listSongPlaying.clear()
        MusicService.listSongPlaying.add(song)

        GlobalFunction.startMusicService(requireContext(), Constant.PLAY, 0)

        GlobalFunction.startActivity(requireContext(), PlayMusicActivity::class.java)
    }
}