package com.azmetov.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.azmetov.playlistmaker.data.network.NetworkDispatcher
import com.azmetov.playlistmaker.data.shared.SingleTrackSharedStore
import com.azmetov.playlistmaker.data.shared.TrackListSharedStore
import com.azmetov.playlistmaker.other.Constants
import com.azmetov.playlistmaker.other.Constants.THEME_SWITCHER_KEY
import com.azmetov.playlistmaker.other.Constants.THEME_SWITCHER_PREFERENCES
import com.azmetov.playlistmaker.ui.SearchAdapter
import com.google.gson.Gson

class App : Application() {


    lateinit var singleTrackSharedStore: SingleTrackSharedStore
        private set

    lateinit var trackSharedStore: TrackListSharedStore
        private set

    lateinit var networkDispatcher: NetworkDispatcher
        private set

    lateinit var searchAdapter: SearchAdapter
        private set

    private lateinit var gson: Gson
    private var darkTheme = false

    override fun onCreate() {
        super.onCreate()

        instance = this

        createObjects()
        switchTheme(darkTheme)
    }

    private fun createObjects() {
        gson = Gson()
        networkDispatcher = NetworkDispatcher()
        searchAdapter = SearchAdapter()
        val playerPrefs = getSharedPreferences(Constants.PLAYER_SHARED_PREFS, MODE_PRIVATE)
        singleTrackSharedStore = SingleTrackSharedStore(playerPrefs, gson)
        val searchPrefs = getSharedPreferences(Constants.SEARCH_TRACKS_PREFS, MODE_PRIVATE)
        trackSharedStore = TrackListSharedStore(searchPrefs, gson)
        val themePrefs = getSharedPreferences(THEME_SWITCHER_PREFERENCES, MODE_PRIVATE)
        darkTheme = themePrefs.getBoolean(THEME_SWITCHER_KEY, false)

    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    companion object {
        lateinit var instance: App
            private set
    }
}