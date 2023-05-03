package com.example.musicplayer.view.fragment

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
import com.example.musicplayer.databinding.FragmentHomeBinding
import com.example.musicplayer.databinding.FragmentNewBinding
import com.example.musicplayer.listener.IArtistClickListener
import com.example.musicplayer.model.Artist
import com.example.musicplayer.view.adapter.ArtistAdapter
import com.example.musicplayer.viewmodel.ArtistViewModel


class Artist : Fragment() {
    private lateinit var recyclerViewArtist: RecyclerView
    private lateinit var artistViewModel: ArtistViewModel
    private lateinit var artistAdapter: ArtistAdapter
    private lateinit var fragmentArtist: FragmentNewBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_new, container, false)
        fragmentArtist = FragmentNewBinding.inflate(inflater, container, false)
        recyclerViewArtist = fragmentArtist.recyclerListArtist
        recyclerViewArtist.layoutManager = GridLayoutManager(activity, 2)
        val dividerDecoration =
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        recyclerViewArtist.addItemDecoration(dividerDecoration)
        artistViewModel = ViewModelProvider(this)[ArtistViewModel::class.java]
        artistViewModel.allArtists.observe(viewLifecycleOwner) {
            artistAdapter = ArtistAdapter(it, object : IArtistClickListener {
                override fun onArtistClick(artist: Artist) {
                    TODO("Not yet implemented")
                }
            })
            recyclerViewArtist.adapter = artistAdapter
        }

        return view
    }
}