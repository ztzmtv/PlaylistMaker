package com.azmetov.playlistmaker.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.azmetov.playlistmaker.R
import com.azmetov.playlistmaker.entities.Track
import com.azmetov.playlistmaker.other.Converter.convertTime
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.util.*

class SearchAdapter : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    private var tracks = listOf<Track>()
    private var trackClickListener: ((Track) -> Unit)? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setTrackList(tracks: List<Track>) {
        this.tracks = tracks
        notifyDataSetChanged()
    }

    fun setTrackClickListener(callback: ((Track) -> Unit)?) {
        trackClickListener = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.search_item, parent, false)
        return SearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            val track = tracks[position]
            trackClickListener?.invoke(track)
        }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    class SearchViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        private val artwork: ImageView =
            view.findViewById(R.id.iv_item_artwork)
        private val artistNameAndTrackTime: TextView =
            view.findViewById(R.id.tv_artist_name_and_track_time)
        private val trackName: TextView =
            view.findViewById(R.id.tv_track_name)

        fun bind(track: Track) {
            trackName.text = track.trackName
            Glide.with(view.context)
                .load(track.artworkUrl100)
                .centerCrop()
                .transform(RoundedCorners(8))
                .placeholder(R.drawable.album_placeholder)
                .into(artwork)
            val template = view.context.getString(R.string.album_title_and_track_time_template)
            artistNameAndTrackTime.text =
                String.format(template, track.artistName, convertTime(track.trackTime))
        }
    }
}