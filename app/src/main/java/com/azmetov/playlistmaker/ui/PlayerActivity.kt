package com.azmetov.playlistmaker.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.azmetov.playlistmaker.R
import com.azmetov.playlistmaker.entities.Track

class PlayerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        val arrowBack = findViewById<ImageView>(R.id.iv_player_arrow_back)
        arrowBack.setOnClickListener {
            finish()
        }
        //Пока не понял, чем это заменить
        val track = intent.extras?.getSerializable(EXTRA_TRACK) as Track
        Toast.makeText(this, "$track", Toast.LENGTH_SHORT).show()


    }

    companion object {
        private const val EXTRA_TRACK = "EXTRA_TRACK"

        fun getIntent(context: Context, track: Track) =
            Intent(context, PlayerActivity::class.java).apply {
                putExtra(EXTRA_TRACK, track)
            }
    }
}