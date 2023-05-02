package com.example.musicplayer.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.ViewFlipper
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.R
import com.example.musicplayer.activity.adapter.SongSearchAdapter
import com.example.musicplayer.activity.adapter.Songadapter
import com.example.musicplayer.viewmodels.SongViewModel


class Home : Fragment() {
    private lateinit var songAdapter: Songadapter
    private lateinit var recyclerViewListSong: RecyclerView
    private lateinit var songViewModel: SongViewModel
    private var songListSearch = SongSearchAdapter(mutableListOf())
    private lateinit var imgSearch:ImageView
    private lateinit var hint:TextView
    private lateinit var layoutSearch:RecyclerView
    private lateinit  var viewFlipper :ViewFlipper

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
        songViewModel.allSongs.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                val songList = it
                songAdapter = Songadapter(songList)
                recyclerViewListSong.adapter = songAdapter
            }
        }
        //searchSong = view.findViewById(R.id.searchView)
        hint=view.findViewById(R.id.edt_search_name)
        imgSearch=view.findViewById(R.id.img_search)
        layoutSearch = view.findViewById(R.id.layout_search)
        layoutSearch.layoutManager = GridLayoutManager(activity, 1)
        layoutSearch.addItemDecoration(dividerDecoration)
        viewFlipper = view.findViewById(R.id.viewflipper)
        actionViewFlipper()

        imgSearch.setOnClickListener {

            searchSongByHint(hint.text.toString())
        }

        return view
    }

    private fun actionViewFlipper() {
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
            songListSearch.clear()
            songViewModel.getSongByHint(strKey).observe(viewLifecycleOwner) { songs ->
                if (songs.isNotEmpty()) {
                    layoutSearch.visibility = View.VISIBLE
                    val searchAdapter = SongSearchAdapter(songs)
                    layoutSearch.adapter = searchAdapter
                } else {
                    layoutSearch.visibility = View.GONE
                    // Phần toast này đang bị dính lỗi nhỏ chưa debug được nên tạm thời comment nó lại
                    //Toast.makeText(requireContext(), "Không có bài hát tìm kiếm", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}


