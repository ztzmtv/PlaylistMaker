package com.azmetov.playlistmaker.ui

sealed class NetworkState {
    class Result<T>(val result: T) : NetworkState()
    object NotFound : NetworkState()
    object NetworkError : NetworkState()
}
