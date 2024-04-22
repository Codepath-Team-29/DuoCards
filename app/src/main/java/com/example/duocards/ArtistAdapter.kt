package com.example.duocards

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ArtistAdapter(private val trackList: ArrayList<String>, private val topURL: ArrayList<String>, private val onPlayButtonClick: (String) -> Unit) : RecyclerView.Adapter<ArtistAdapter.ArtistViewHolder>() {
    inner class ArtistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val artistTrackTextView: TextView = itemView.findViewById(R.id.artistTrackTextView)
        val playButton: Button = itemView.findViewById(R.id.button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.artist_item, parent, false)
        return ArtistViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        val trackName = trackList[position]
        holder.artistTrackTextView.text = trackName

        holder.playButton.setOnClickListener {
            val previewUrl = topURL[position] // Accessing the preview URL directly from trackList
            onPlayButtonClick(previewUrl)
            Log.e("MediaPlayer", previewUrl)
        }
    }

    override fun getItemCount() = trackList.size
}