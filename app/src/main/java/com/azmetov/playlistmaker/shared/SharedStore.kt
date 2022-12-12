package com.azmetov.playlistmaker.shared


import android.content.SharedPreferences
import com.azmetov.playlistmaker.entities.Track
import com.azmetov.playlistmaker.other.Constants.COUNT_OF_TRACKS
import com.azmetov.playlistmaker.other.Constants.TRACKS_HISTORY_KEY
import com.google.gson.Gson

class SharedStore(
    private val sharedPrefs: SharedPreferences
) {
    private var trackList = mutableListOf<Track>()

    fun addTrack(track: Track) {
        if (trackList.contains(track)) {
            trackList.remove(track)
        }
        trackList.add(track)
        cropList()
        val json = Gson().toJson(trackList)
        sharedPrefs.edit().putString(TRACKS_HISTORY_KEY, json).apply()
    }

    fun getTracks(): List<Track>? {
        val json = sharedPrefs.getString(TRACKS_HISTORY_KEY, null) ?: return null
        return Gson().fromJson(json, Array<Track>::class.java).reversed().toList()
    }

    fun clearList() {
        trackList.clear()
    }

    private fun cropList() {
        trackList = if (trackList.size > 10) {
            trackList.subList(0, COUNT_OF_TRACKS - 1)
        } else {
            trackList
        }
    }
}