package com.example.musicplayer.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.R
import com.example.musicplayer.activity.adapter.SongSearchAdapter
import com.example.musicplayer.viewmodels.SongViewModel

class NoticeMusic : Fragment() {
    private lateinit var recyclerViewListSong: RecyclerView
    private lateinit var songViewModel: SongViewModel
    private lateinit var songListSearch: SongSearchAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_new, container, false)
        recyclerViewListSong = view.findViewById(R.id.recyclerListSongs)
        recyclerViewListSong.layoutManager = GridLayoutManager(activity, 1)
        val dividerDecoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        recyclerViewListSong.addItemDecoration(dividerDecoration)
        songViewModel = ViewModelProvider(this)[SongViewModel::class.java]
        songViewModel.allSongs.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                val songList = it
                songListSearch = SongSearchAdapter(songList)
                recyclerViewListSong.adapter = songListSearch
            }
        }

        recyclerViewListSong.setOnClickListener {

        }
        // SearchByHint()
        return view
    }
}