package com.example.musicplayer.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.ViewFlipper
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.R
import com.example.musicplayer.activity.adapter.SongSearchadapter
import com.example.musicplayer.activity.adapter.Songadapter
import com.example.musicplayer.viewmodels.SongViewModel
import com.squareup.picasso.Picasso


class Home : Fragment() {
    private lateinit var songAdapter: Songadapter
    private lateinit var recyclerViewListSong: RecyclerView
    private lateinit var songViewModel: SongViewModel
    private lateinit var songListSeach: SongSearchadapter
    private lateinit var imgSearch:ImageView
    private lateinit var hint:TextView
    private lateinit var layoutSearch:RecyclerView
    private lateinit  var viewFlipper :ViewFlipper
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

        recyclerViewListSong.setOnClickListener {

        }
        viewFlipper = view.findViewById(R.id.viewflipper)
        ActionViewFlipper()
       // SearchByHint()
        return view
    }

    private fun ActionViewFlipper() {
       //var viewFlipper = getView()?.findViewById<ViewFlipper>(R.id.viewflipper)
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
            viewFlipper?.addView(imageView)
        }
        viewFlipper?.setFlipInterval(5000)
        viewFlipper?.setAutoStart(true)
        viewFlipper?.setInAnimation(requireContext(), android.R.anim.fade_in)
        viewFlipper?.setOutAnimation(requireContext(), android.R.anim.fade_out)
    }


}
