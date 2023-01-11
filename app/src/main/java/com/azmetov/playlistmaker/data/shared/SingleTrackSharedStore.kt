package com.azmetov.playlistmaker.data.shared

import android.content.SharedPreferences
import com.azmetov.playlistmaker.data.entities.Track
import com.azmetov.playlistmaker.other.Constants
import com.google.gson.Gson

class SingleTrackSharedStore(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson
) {

    fun saveToSharedPrefs(track: Track) {
        sharedPreferences
            .edit()
            .putString(Constants.TRACK_DATA, gson.toJson(track))
            .apply()
    }

    fun loadFromSharedPrefs(): Track? {
        val trackJson = sharedPreferences.getString(Constants.TRACK_DATA, "") ?: ""
        return gson.fromJson(trackJson, Track::class.java)
    }
}