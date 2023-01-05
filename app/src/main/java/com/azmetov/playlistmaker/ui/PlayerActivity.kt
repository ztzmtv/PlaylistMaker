package com.azmetov.playlistmaker.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.azmetov.playlistmaker.R
import com.azmetov.playlistmaker.entities.Track
import com.azmetov.playlistmaker.other.Constants.PLAYER_SHARED_PREFS
import com.azmetov.playlistmaker.other.Converter.convertTime
import com.azmetov.playlistmaker.shared.SingleTrackSharedStore
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class PlayerActivity : AppCompatActivity() {

    private val artworkView by lazy { findViewById<ImageView>(R.id.player_artwork_iv) }
    private val trackView by lazy { findViewById<TextView>(R.id.player_track_name_tv) }
    private val bandView by lazy { findViewById<TextView>(R.id.player_artist_name_tv) }
    private val durationView by lazy { findViewById<TextView>(R.id.player_duration_tv) }
    private val collectionView by lazy { findViewById<TextView>(R.id.player_album_tv) }
    private val releaseDateView by lazy { findViewById<TextView>(R.id.player_year_tv) }
    private val genreView by lazy { findViewById<TextView>(R.id.player_genre_tv) }
    private val countryView by lazy { findViewById<TextView>(R.id.player_country_tv) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        val arrowBack = findViewById<ImageView>(R.id.iv_player_arrow_back)
        arrowBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        val singleTrackSharedStore =
            SingleTrackSharedStore(getSharedPreferences(PLAYER_SHARED_PREFS, MODE_PRIVATE))
        //Пока не понял, чем  заменить Deprecated
        val track = if (intent.extras != null) {
            (intent.extras!!.getSerializable(EXTRA_TRACK) as Track).apply {
                singleTrackSharedStore.saveToSharedPrefs(this)
            }
        } else {
            singleTrackSharedStore.loadFromSharedPrefs()
                ?: throw RuntimeException("Track can not be null!!!")
        }
        bindTrack(track)
//        setPlayerActivityFlag(true)
    }

    override fun onDestroy() {
        super.onDestroy()
//        setPlayerActivityFlag(false)
    }

    private fun bindTrack(track: Track) {
        with(track) {
            Glide.with(this@PlayerActivity)
                .load(this.getCoverArtwork())
                .centerCrop()
                .transform(RoundedCorners(8))
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


//    private fun setPlayerActivityFlag(isActive: Boolean) {
//        sharedPrefs.edit().putBoolean(IS_PLAYER_ACTIVITY_OPENED, isActive).apply()
//    }

    companion object {
        private const val EXTRA_TRACK = "EXTRA_TRACK"

        fun getIntent(context: Context, track: Track) =
            Intent(context, PlayerActivity::class.java).apply {
                putExtra(EXTRA_TRACK, track)
            }
    }
}