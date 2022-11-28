package com.azmetov.playlistmaker.network

import com.google.gson.annotations.SerializedName


/*

{
    resultsCount: 2,
    results: [
    {
        trackName: “название трека”,
        artistName: “имя исполнителя”,
        trackTimeMillis: 293000,
        artworkUrl100: “ссылка на обложку”
    },
    {
        trackName: “название трека”,
        artistName: “имя исполнителя”,
        trackTimeMillis: 293000,
        artworkUrl100: “ссылка на обложку”
    }
    ]
}
*/

data class PlaylistResponse(

    @SerializedName("resultsCount")
    val resultsCount: Int,

    @SerializedName("results")
    val results: List<TrackDTO>
)


