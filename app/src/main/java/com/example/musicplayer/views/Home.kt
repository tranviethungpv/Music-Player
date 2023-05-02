package com.example.musicplayer.views

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import android.widget.ViewFlipper
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.R
import com.example.musicplayer.activity.adapter.SongSearchadapter
import com.example.musicplayer.activity.adapter.Songadapter

import com.example.musicplayer.model.Song
import com.example.musicplayer.viewmodels.SongViewModel


class Home : Fragment() {
    private lateinit var songAdapter: Songadapter
    private lateinit var recyclerViewListSong: RecyclerView
    private lateinit var songViewModel: SongViewModel
    private var songListSeach = SongSearchadapter(mutableListOf<Song>())
    private lateinit var imgSearch:ImageView
    private lateinit var hint:TextView
    private lateinit var layoutSearch:RecyclerView
    private lateinit  var viewFlipper :ViewFlipper

    private lateinit  var searchSong:SearchView
    private lateinit var song:Song
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(com.example.musicplayer.R.layout.fragment_home, container, false)
        recyclerViewListSong = view.findViewById(com.example.musicplayer.R.id.recyclerListSongs)
        recyclerViewListSong.layoutManager = GridLayoutManager(activity, 2)
        val dividerDecoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        recyclerViewListSong.addItemDecoration(dividerDecoration)
        songViewModel = ViewModelProvider(this)[SongViewModel::class.java]
        songViewModel.allSongs.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                val songList = it
                songAdapter = Songadapter(songList)
                recyclerViewListSong.adapter = songAdapter
            }
        })
        //searchSong = view.findViewById(R.id.searchView)
        hint=view.findViewById(R.id.edt_search_name)
        imgSearch=view.findViewById(R.id.img_search)
        layoutSearch = view.findViewById<RecyclerView>(R.id.layout_search)
        layoutSearch.layoutManager = GridLayoutManager(activity, 1)
        layoutSearch.addItemDecoration(dividerDecoration)
        viewFlipper = view.findViewById(R.id.viewflipper)
        ActionViewFlipper()

        imgSearch.setOnClickListener(View.OnClickListener {

            searchSongByHint(hint.text.toString())}
        )

        return view
    }

    private fun ActionViewFlipper() {
        val imageList = mutableListOf(
            R.drawable.banner01,
            R.drawable.banner02,
            R.drawable.banner03
        )

        // Thêm các ảnh vào ViewFlipper
        imageList.forEach { image ->
            val imageView = ImageView(requireContext())
            imageView.setImageResource(image)
            imageView.scaleType = ImageView.ScaleType.FIT_XY
            viewFlipper.addView(imageView)
        }
        viewFlipper.setFlipInterval(5000)
        viewFlipper.setAutoStart(true)
        viewFlipper.setInAnimation(requireContext(), android.R.anim.fade_in)
        viewFlipper.setOutAnimation(requireContext(), android.R.anim.fade_out)
    }
    private fun searchSongByHint(strKey: String?) {
        if (strKey != null) {
            songListSeach.clear()
            songViewModel.getSongByHint(strKey).observe(viewLifecycleOwner, Observer { songs ->
                if (songs.isNotEmpty()) {
                    layoutSearch.visibility = View.VISIBLE

                    val searchAdapter = SongSearchadapter(songs)

                    layoutSearch.adapter = searchAdapter // set the adapter to the layoutSearch RecyclerView
                }
                else {
                    Toast.makeText(requireContext(), "Không có bài hát tìm kiếm", Toast.LENGTH_SHORT).apply {
                        setGravity(Gravity.BOTTOM, 200, 500)
                    }.show()
                }
            })
        }
        else {
            Toast.makeText(requireContext(),"Không có bài hát tìm kiếm", Toast.LENGTH_SHORT).apply { setGravity(
                Gravity.BOTTOM, 0, 0) }.show()
        }
    }


}


