package com.azmetov.playlistmaker.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.azmetov.playlistmaker.R

class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnSearch = findViewById<Button>(R.id.btn_search)
        val btnMedia = findViewById<Button>(R.id.btn_media)
        val btnSettings = findViewById<Button>(R.id.btn_settings)

        btnSearch.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                val intent = SearchActivity.getIntent(this@MainActivity)
                startActivity(intent)
            }
        })

        btnMedia.setOnClickListener {
            val intent = MediaActivity.getIntent(this@MainActivity)
            startActivity(intent)
        }
        btnSettings.setOnClickListener(this@MainActivity)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btn_settings -> {
                val intent = SettingsActivity.getIntent(this@MainActivity)
                startActivity(intent)
            }
        }
    }
}