package com.example.musicplayer.activity.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicplayer.R
import com.example.musicplayer.model.Song

class SongSearchAdapter(private var listSong: MutableList<Song>) :
    RecyclerView.Adapter<SongSearchAdapter.SongViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.adapter_listsongsearch, parent, false)
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
    @SuppressLint("NotifyDataSetChanged")
    fun clear() {
        listSong.clear()
        notifyDataSetChanged()
    }
    class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.tv_song_name)
        val artist: TextView = itemView.findViewById(R.id.tv_artist)
        val image: ImageView = itemView.findViewById(R.id.img_song)
    }
}