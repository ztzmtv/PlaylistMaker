package com.azmetov.playlistmaker.network

import com.google.gson.annotations.SerializedName

data class PlaylistResponseDto(

    @SerializedName("resultCount")
    val resultCount: Int,

    @SerializedName("results")
    val results: List<TrackDto>
)


