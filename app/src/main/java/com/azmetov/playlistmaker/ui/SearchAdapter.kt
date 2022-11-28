package com.azmetov.playlistmaker.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.azmetov.playlistmaker.R
import com.azmetov.playlistmaker.entities.Track
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class SearchAdapter(
    private val tracks: List<Track>
) : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.search_item, parent, false)
        return SearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(tracks[position])
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    class SearchViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        private val artwork: ImageView =
            view.findViewById(R.id.iv_artwork)
        private val artistNameAndTrackTime: TextView =
            view.findViewById(R.id.tv_artist_name_and_track_time)
        private val trackName: TextView =
            view.findViewById(R.id.tv_track_name)

        fun bind(track: Track) {
            trackName.text = track.trackName
            Glide.with(view.context)
                .load(track.artworkUrl100)
                .centerCrop()
                .transform(RoundedCorners(2))
                .placeholder(R.drawable.album_placeholder)
                .into(artwork)
            val template = view.context.getString(R.string.album_title_and_track_time_template)
            artistNameAndTrackTime.text = String.format(template, track.artistName, track.trackTime)
        }
    }
}