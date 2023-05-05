package com.example.musicplayer.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicplayer.R
import com.example.musicplayer.databinding.ItemGridAboutBinding
import com.example.musicplayer.model.About

class AboutAdapter(
    private var listSong: List<About>
) : RecyclerView.Adapter<AboutAdapter.AboutViewHolder>() {
    class AboutViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val itemAboutGridBinding: ItemGridAboutBinding =
            ItemGridAboutBinding.bind(itemView)


        val infor: TextView = itemAboutGridBinding.information
        val image: ImageView = itemAboutGridBinding.imgInfo
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AboutViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_grid_about, parent, false)
        return AboutViewHolder(itemView)
    }


    override fun getItemCount(): Int {
        return listSong.size
    }

    override fun onBindViewHolder(holder: AboutViewHolder, position: Int) {
        val currentItem = listSong[position]

        holder.infor.text = currentItem.infor
        Glide.with(holder.itemView.context).load(currentItem.image).into(holder.image)

    }
}
