package com.example.musicplayer.view.fragment

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.musicplayer.databinding.FragmentPlaySongBinding
import com.example.musicplayer.model.Song
import com.example.musicplayer.viewmodel.SongViewModel

class PlaySongFragment : Fragment() {

    private lateinit var songViewModel: SongViewModel
    private var songList: ArrayList<Song> = ArrayList()
    private lateinit var binding: FragmentPlaySongBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaySongBinding.inflate(inflater, container, false)
        showSongInfo(requireContext())
        return binding.root
    }
    private fun showSongInfo(context: Context){
        songViewModel = ViewModelProvider(this)[SongViewModel::class.java]
        songViewModel.allSongs.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {

            }
        })
    }
}