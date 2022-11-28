package com.azmetov.playlistmaker.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.azmetov.playlistmaker.R

class MediaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)
    }

    companion object {
        fun getIntent(context: Context) = Intent(context, MediaActivity::class.java)
    }
}