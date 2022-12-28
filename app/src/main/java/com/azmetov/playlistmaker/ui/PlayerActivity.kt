package com.azmetov.playlistmaker.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.azmetov.playlistmaker.R
import com.azmetov.playlistmaker.entities.Track
import com.azmetov.playlistmaker.other.Converter
import com.azmetov.playlistmaker.other.Converter.convertTime
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class PlayerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        val arrowBack = findViewById<ImageView>(R.id.iv_player_arrow_back)
        arrowBack.setOnClickListener {
            finish()
        }


        val artworkView = findViewById<ImageView>(R.id.player_artwork_iv)
        val trackView = findViewById<TextView>(R.id.player_track_name_tv)
        val bandView = findViewById<TextView>(R.id.player_artist_name_tv)
        val durationView = findViewById<TextView>(R.id.player_duration_tv)
        val collectionView = findViewById<TextView>(R.id.player_album_tv)
        val releaseDateView = findViewById<TextView>(R.id.player_year_tv)
        val genreView = findViewById<TextView>(R.id.player_genre_tv)
        val countryView = findViewById<TextView>(R.id.player_country_tv)

        //Пока не понял, чем  заменить Deprecated
        with(intent.extras?.getSerializable(EXTRA_TRACK) as Track) {
            Glide.with(this@PlayerActivity)
                .load(this.getCoverArtwork())
                .centerCrop()
                .transform(RoundedCorners(2))
                .placeholder(R.drawable.album_placeholder_with_padding)
                .into(artworkView)
            trackView.text = trackName
            bandView.text = artistName
            durationView.text = convertTime(trackTime)
            collectionView.text = collectionName
            releaseDateView.text = releaseDate
            genreView.text = genre
            countryView.text = country
        }


    }

    companion object {
        private const val EXTRA_TRACK = "EXTRA_TRACK"

        fun getIntent(context: Context, track: Track) =
            Intent(context, PlayerActivity::class.java).apply {
                putExtra(EXTRA_TRACK, track)
            }
    }
}