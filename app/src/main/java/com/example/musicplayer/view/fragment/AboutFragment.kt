package com.example.musicplayer.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.R
import com.example.musicplayer.adapter.AboutAdapter
import com.example.musicplayer.databinding.FragmentSettingBinding
import com.example.musicplayer.model.About

class AboutFragment : Fragment() {
    private lateinit var about :AboutFragment
    private lateinit var aboutAdapter:AboutAdapter
    private lateinit var fragmentAboutBinding: FragmentSettingBinding
    private lateinit var recyclerabout: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        fragmentAboutBinding=FragmentSettingBinding.inflate(inflater, container, false)
        val aboutList = listOf(
            About("Thông tin ứng dụng", "url_hình_ảnh_1"),
            About("Version", "url_hình_ảnh_2"),
            About("Chính sách và bảo mật", "url_hình_ảnh_3")
        )

        aboutAdapter = AboutAdapter(aboutList)
        recyclerabout=fragmentAboutBinding.recyclerAbout
        recyclerabout.setAdapter(aboutAdapter)
        recyclerabout.layoutManager = GridLayoutManager(activity, 1)

        return fragmentAboutBinding.root
    }
}