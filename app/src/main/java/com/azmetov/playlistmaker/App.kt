package com.azmetov.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.azmetov.playlistmaker.data.network.NetworkDispatcher
import com.azmetov.playlistmaker.data.shared.SingleTrackSharedStore
import com.azmetov.playlistmaker.data.shared.TrackListSharedStore
import com.azmetov.playlistmaker.other.Constants
import com.azmetov.playlistmaker.other.Constants.THEME_SWITCHER_KEY
import com.azmetov.playlistmaker.other.Constants.THEME_SWITCHER_PREFERENCES
import com.google.gson.Gson

class App : Application() {

    private var darkTheme = false

    lateinit var gson: Gson
        private set

    lateinit var singleTrackSharedStore: SingleTrackSharedStore
        private set

    lateinit var trackSharedStore: TrackListSharedStore
        private set

    lateinit var networkDispatcher: NetworkDispatcher
        private set


    override fun onCreate() {
        super.onCreate()

        instance = this
        gson = Gson()
        networkDispatcher = NetworkDispatcher()

        getSharedPreferences(Constants.PLAYER_SHARED_PREFS, MODE_PRIVATE)
            .apply {
                singleTrackSharedStore = SingleTrackSharedStore(this, gson)
            }
        getSharedPreferences(Constants.SEARCH_TRACKS_PREFS, MODE_PRIVATE)
            .apply {
                trackSharedStore = TrackListSharedStore(this, gson)
            }
        getSharedPreferences(THEME_SWITCHER_PREFERENCES, MODE_PRIVATE)
            .apply {
                darkTheme = this.getBoolean(THEME_SWITCHER_KEY, false)
            }

        switchTheme(darkTheme)

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