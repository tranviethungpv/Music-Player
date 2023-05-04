package com.example.musicplayer.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicplayer.R
import com.example.musicplayer.databinding.ItemArtistGridBinding
import com.example.musicplayer.listener.IArtistClickListener
import com.example.musicplayer.model.Artist


class ArtistAdapter(
    private var listArtist: ArrayList<Artist>, private val artistClickListener: IArtistClickListener
) : RecyclerView.Adapter<ArtistAdapter.ArtistViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_artist_grid, parent, false)
        return ArtistViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return listArtist.size
    }

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        val currentItem = listArtist[position]
        holder.artist.text = currentItem.name
        Glide.with(holder.itemView.context).load(currentItem.image.toString()).into(holder.image)
        holder.itemView.setOnClickListener { artistClickListener.onArtistClick(currentItem) }
    }

    class ArtistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val itemArtistGridBinding: ItemArtistGridBinding =
            ItemArtistGridBinding.bind(itemView)

        val artist: TextView = itemArtistGridBinding.textartist
        val image: ImageView = itemArtistGridBinding.imageThumbnail
    }
}