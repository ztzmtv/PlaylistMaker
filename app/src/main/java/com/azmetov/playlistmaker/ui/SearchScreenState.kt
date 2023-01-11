package com.azmetov.playlistmaker.ui

import com.azmetov.playlistmaker.data.entities.Track

sealed class SearchScreenState {
    class Result(val result: List<Track>) : SearchScreenState()
    class History(val list: List<Track>) : SearchScreenState()
    object NotFound : SearchScreenState()
    object SearchError : SearchScreenState()
    object Loading : SearchScreenState()
}
