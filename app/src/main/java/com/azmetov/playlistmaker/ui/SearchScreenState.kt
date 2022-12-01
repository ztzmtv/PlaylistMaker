package com.azmetov.playlistmaker.ui

import com.azmetov.playlistmaker.entities.Track

sealed class SearchScreenState {
    class Result(val list: List<Track>) : SearchScreenState()
    object NothingFound : SearchScreenState()
    object NetworkError : SearchScreenState()
}
