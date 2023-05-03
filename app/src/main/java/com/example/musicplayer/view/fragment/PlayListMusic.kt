package com.example.musicplayer.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.ViewFlipper
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.Constant
import com.example.musicplayer.databinding.FragmentPlaylistBinding
import com.example.musicplayer.listener.ISongClickListener
import com.example.musicplayer.model.Song
import com.example.musicplayer.service.MusicService
import com.example.musicplayer.view.activity.PlayMusicActivity
import com.example.musicplayer.view.adapter.SongSearchAdapter
import com.example.musicplayer.viewmodel.SongViewModel

class PlayListMusic : Fragment() {
    private lateinit var songSearchAdapter: SongSearchAdapter
    private lateinit var recyclerPlaylist: RecyclerView
    private lateinit var songViewModel: SongViewModel
    private lateinit var fragmentPlayListMusic:FragmentPlaylistBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentPlayListMusic = FragmentPlaylistBinding.inflate(inflater, container, false)
        fragmentPlayListMusic.playlistname.text = Artist.selectedArtistName
        renderListSong()
        return fragmentPlayListMusic.root
    }

    private fun renderListSong(){
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

        val musicService = Intent(requireContext(), MusicService::class.java)
        musicService.putExtra(Constant.MUSIC_ACTION, Constant.PLAY)
        musicService.putExtra(Constant.SONG_POSITION, 0)
        requireContext().startService(musicService)

        val intent = Intent(requireContext(), PlayMusicActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        requireContext().startActivity(intent)
    }
}