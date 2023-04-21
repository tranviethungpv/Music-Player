package com.example.musicplayer.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBindings
import com.example.musicplayer.R
import com.example.musicplayer.activity.adapter.SongSearchadapter
import com.example.musicplayer.viewmodels.SongViewModel


class Home : Fragment() {
    private lateinit var songAdapter: SongSearchadapter
    private lateinit var recyclerViewListSong: RecyclerView
    private lateinit var songViewModel: SongViewModel
    private lateinit var songListSeach: SongSearchadapter
    private lateinit var imgSearch:ImageView
    private lateinit var hint:TextView
    private lateinit var layoutSearch:RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_home, container, false)

        recyclerViewListSong = view.findViewById(R.id.recyclerListSongs)
        recyclerViewListSong.layoutManager = GridLayoutManager(activity, 2)
        val dividerDecoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        recyclerViewListSong.addItemDecoration(dividerDecoration)

        songViewModel = ViewModelProvider(this)[SongViewModel::class.java]
        songViewModel.allSongs.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                val songList = it
                songAdapter = SongSearchadapter(songList)
                recyclerViewListSong.adapter = songAdapter
            }
        })

        recyclerViewListSong.setOnClickListener {
            
        }
        return view
    }


}
