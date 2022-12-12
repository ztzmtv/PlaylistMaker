package com.azmetov.playlistmaker.network

import com.google.gson.annotations.SerializedName


data class TrackDto(

    @SerializedName("trackTimeMillis")
    val trackTimeMillis: Int? = null,

    @SerializedName("artworkUrl100")
    val artworkUrl100: String? = null,

    @SerializedName("artistName")
    val artistName: String? = null,

    @SerializedName("trackName")
    val trackName: String? = null
)