package com.example.musicplayer.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicplayer.R
import com.example.musicplayer.databinding.ItemSongPlayingBinding
import com.example.musicplayer.listener.ISongPlayingClickListener
import com.example.musicplayer.model.Song

class ListSongPlayingAdapter(
    private var listSong: ArrayList<Song>,
    private val songPlayingClickListener: ISongPlayingClickListener
) : RecyclerView.Adapter<ListSongPlayingAdapter.SongViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_song_playing, parent, false)
        return SongViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return listSong.size
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val currentItem = listSong[position]
        if (currentItem.isPlaying == true) {
            holder.isPlaying.visibility = View.VISIBLE
        } else {
            holder.isPlaying.visibility = View.GONE
        }
        holder.title.text = currentItem.title
        holder.artist.text = currentItem.artist
        Glide.with(holder.itemView.context).load(currentItem.image).into(holder.image)
        holder.itemView.setOnClickListener { songPlayingClickListener.onSongClick(position) }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setSongList(newSongList: ArrayList<Song>) {
        listSong.clear()
        listSong.addAll(newSongList)
        notifyDataSetChanged()
    }

    class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val itemSongPlayingBinding: ItemSongPlayingBinding =
            ItemSongPlayingBinding.bind(itemView)

        val title: TextView = itemSongPlayingBinding.tvSongName
        val artist: TextView = itemSongPlayingBinding.tvArtist
        val image: ImageView = itemSongPlayingBinding.imgSong
        val isPlaying: ImageView = itemSongPlayingBinding.imgPlaying
    }
}