package com.example.duocards

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ArtistAdapter(private val artistList: List<String>) : RecyclerView.Adapter<ArtistAdapter.ArtistViewHolder>() {
    inner class ArtistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val artistTrackTextView: TextView = itemView.findViewById(R.id.artistTrackTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.artist_item, parent, false)
        return ArtistViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        val currentItem = artistList[position]
        holder.artistTrackTextView.text = currentItem

    }

    override fun getItemCount() = artistList.size
}