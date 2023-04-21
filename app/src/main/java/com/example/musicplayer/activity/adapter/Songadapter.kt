package com.example.musicplayer.activity.adapter


import com.example.musicplayer.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.example.musicplayer.model.Song

class Songadapter(private var listSong: ArrayList<Song>) :
    RecyclerView.Adapter<SongSearchadapter.SongViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.song_grid, parent, false)
        return SongViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return listSong.size
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val currentItem = listSong[position]
        holder.title.text = currentItem.title
        holder.artist.text = currentItem.artist
        Glide.with(holder.itemView.context).load(currentItem.image).into(holder.image)
    }

    class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.texttitle)
        val artist: TextView = itemView.findViewById(R.id.textartist)
        val image: ImageView = itemView.findViewById(R.id.imageThumbnail)
    }
}