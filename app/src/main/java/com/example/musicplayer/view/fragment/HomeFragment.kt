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
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicplayer.Constant
import com.example.musicplayer.GlobalFunction
import com.example.musicplayer.adapter.SongSearchAdapter
import com.example.musicplayer.databinding.FragmentHomeBinding
import com.example.musicplayer.listener.ISongClickListener
import com.example.musicplayer.model.Song
import com.example.musicplayer.service.MusicService
import com.example.musicplayer.view.activity.PlayMusicActivity
import com.example.musicplayer.adapter.ListSongAdapter
import com.example.musicplayer.view.viewmodel.SongViewModel

class HomeFragment : Fragment() {
    private lateinit var songAdapter: ListSongAdapter
    private lateinit var recyclerViewListSong: RecyclerView
    private lateinit var songViewModel: SongViewModel
    private lateinit var fragmentHomeBinding: FragmentHomeBinding
    private lateinit var imgSearch: ImageView
    private lateinit var hint: TextView
    private lateinit var layoutSearch: RecyclerView
    private lateinit var viewFlipper: ViewFlipper
    private lateinit var songListSearch: SongSearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        renderListSong()

        hint = fragmentHomeBinding.edtSearchName
        imgSearch = fragmentHomeBinding.imgSearch
        layoutSearch = fragmentHomeBinding.layoutSearch
        layoutSearch.layoutManager = GridLayoutManager(activity, 1)
        val dividerDecoration =
            DividerItemDecoration(requireContext(), DividerItemDecoration.HORIZONTAL)
        layoutSearch.addItemDecoration(dividerDecoration)
        viewFlipper = fragmentHomeBinding.viewflipper
        actionViewFlipper()

        imgSearch.setOnClickListener {

            searchSongByHint(hint.text.toString())
        }
        fragmentHomeBinding.playall.setOnClickListener {
            songViewModel = ViewModelProvider(this)[SongViewModel::class.java]
            songViewModel.allSongs.observe(viewLifecycleOwner) {
                MusicService.clearListSong()
                MusicService.originalListSong.addAll(it)
                MusicService.isPlaying = false
                GlobalFunction.startMusicService(requireContext(), Constant.PLAY, 0)
                GlobalFunction.startActivity(requireActivity(), PlayMusicActivity::class.java)
            }
        }
        return fragmentHomeBinding.root
    }

    private fun renderListSong() {
        recyclerViewListSong = fragmentHomeBinding.recyclerListSongs
        recyclerViewListSong.layoutManager = GridLayoutManager(activity, 2)

        songViewModel = ViewModelProvider(this)[SongViewModel::class.java]
        songViewModel.getHotSongs().observe(viewLifecycleOwner) {
            songAdapter = ListSongAdapter(it, object : ISongClickListener {
                override fun onSongClick(song: Song) {
                    playSong(song)
                }
            })
            recyclerViewListSong.adapter = songAdapter
        }
    }

    private fun playSong(song: Song) {
        MusicService.originalListSong.clear()
        MusicService.originalListSong.add(song)

        val musicService = Intent(requireContext(), MusicService::class.java)
        musicService.putExtra(Constant.MUSIC_ACTION, Constant.PLAY)
        musicService.putExtra(Constant.SONG_POSITION, 0)
        requireContext().startService(musicService)

        val intent = Intent(requireContext(), PlayMusicActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        requireContext().startActivity(intent)
    }

    private fun actionViewFlipper() {
        songViewModel = ViewModelProvider(this)[SongViewModel::class.java]
        songViewModel.getHotSongs().observe(viewLifecycleOwner) {
            for (song in it) {
                val imageView = ImageView(requireContext())
                Glide.with(requireContext()).load(song.image.toString()).into(imageView)
                imageView.scaleType = ImageView.ScaleType.FIT_XY
                imageView.setOnClickListener {
                    playSong(song)
                }
                viewFlipper.addView(imageView)
            }
        }
        viewFlipper.flipInterval = 5000
        viewFlipper.isAutoStart = true
        viewFlipper.setInAnimation(requireContext(), android.R.anim.fade_in)
        viewFlipper.setOutAnimation(requireContext(), android.R.anim.fade_out)
    }

    private fun searchSongByHint(strKey: String) {
        if (strKey.isEmpty() || strKey.trim() == "") {
            layoutSearch.visibility = View.GONE
            //Toast.makeText(requireContext(), "Không có bài hát tìm kiếm", Toast.LENGTH_SHORT).show()
        } else {
            songViewModel = ViewModelProvider(this)[SongViewModel::class.java]
            songViewModel.getSongByHint(strKey).observe(viewLifecycleOwner) {
                if (it.isNotEmpty()) {
                    layoutSearch.visibility = View.VISIBLE
                    songListSearch = SongSearchAdapter(it, object : ISongClickListener {
                        override fun onSongClick(song: Song) {
                            playSong(song)
                        }
                    })
                    layoutSearch.adapter = songListSearch
                } else {
                    layoutSearch.visibility = View.GONE
                    // Phần toast này đang bị dính lỗi nhỏ chưa debug được nên tạm thời comment nó lại
                    //Toast.makeText(requireContext(), "Không có bài hát tìm kiếm", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}