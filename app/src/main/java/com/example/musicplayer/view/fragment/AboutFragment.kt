package com.example.musicplayer.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.adapter.AboutAdapter
import com.example.musicplayer.databinding.FragmentSettingBinding
import com.example.musicplayer.model.About

class AboutFragment : Fragment() {
    private lateinit var aboutAdapter: AboutAdapter
    private lateinit var fragmentAboutBinding: FragmentSettingBinding
    private lateinit var recyclerAbout: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        fragmentAboutBinding = FragmentSettingBinding.inflate(inflater, container, false)
        val aboutList = listOf(
            About(
                "Music Player - Your music companion",
                "https://img.freepik.com/free-icon/black-music-icon_318-9277.jpg"
            ), About(
                "Version v1.0 06/05/2023",
                "https://static.vecteezy.com/system/resources/thumbnails/016/895/710/small/git-branch-icon-line-isolated-on-white-background-black-flat-thin-icon-on-modern-outline-style-linear-symbol-and-editable-stroke-simple-and-pixel-perfect-stroke-illustration-vector.jpg"
            ), About("Group 06 UTC", "https://cdn-icons-png.flaticon.com/512/4974/4974492.png")
        )

        aboutAdapter = AboutAdapter(aboutList)
        recyclerAbout = fragmentAboutBinding.recyclerAbout
        recyclerAbout.adapter = aboutAdapter
        recyclerAbout.layoutManager = GridLayoutManager(activity, 1)

        return fragmentAboutBinding.root
    }
}