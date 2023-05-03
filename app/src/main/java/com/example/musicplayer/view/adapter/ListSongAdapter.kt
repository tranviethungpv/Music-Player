package com.example.musicplayer.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicplayer.listener.ISongClickListener
import com.example.musicplayer.R
import com.example.musicplayer.databinding.ItemSongGridBinding
import com.example.musicplayer.model.Song

class ListSongAdapter(private var listSong: ArrayList<Song>, private val songClickListener: ISongClickListener) :
    RecyclerView.Adapter<ListSongAdapter.SongViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_song_grid, parent, false)
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
        holder.itemView.setOnClickListener { songClickListener.onSongClick(currentItem) }
    }

    class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val itemSongGridBinding: ItemSongGridBinding = ItemSongGridBinding.bind(itemView)

        val title: TextView = itemSongGridBinding.texttitle
        val artist: TextView = itemSongGridBinding.textartist
        val image: ImageView = itemSongGridBinding.imageThumbnail
    }
}