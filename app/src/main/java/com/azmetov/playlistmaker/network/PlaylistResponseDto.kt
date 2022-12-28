package com.azmetov.playlistmaker.network

import com.google.gson.annotations.SerializedName

data class PlaylistResponseDto(
    @SerializedName("resultCount") val resultCount: Int,
    @SerializedName("results") val results: List<TrackDto>
)

data class TrackDto(
    @SerializedName("artistName") val artistName: String,
    @SerializedName("trackName") val trackName: String,
    @SerializedName("trackTimeMillis") val trackTimeMillis: Int,
    @SerializedName("artworkUrl100") val artworkUrl100: String? = null,
    @SerializedName("collectionName") val collectionName: String? = null,
    @SerializedName("releaseDate") val releaseDate: String? = null,
    @SerializedName("primaryGenreName") val genre: String? = null,
    @SerializedName("country") val country: String? = null,
)