package com.azmetov.playlistmaker.shared

import android.content.SharedPreferences
import com.azmetov.playlistmaker.entities.Track
import com.azmetov.playlistmaker.other.Constants
import com.google.gson.Gson

class SingleTrackSharedStore(
    private val sharedPreferences: SharedPreferences
) {

    fun saveToSharedPrefs(track: Track) {
        sharedPreferences
            .edit()
            .putString(Constants.TRACK_DATA, Gson().toJson(track))
            .apply()
    }

    fun loadFromSharedPrefs(): Track? {
        val trackJson = sharedPreferences.getString(Constants.TRACK_DATA, "") ?: ""
        return Gson().fromJson(trackJson, Track::class.java)
    }
}