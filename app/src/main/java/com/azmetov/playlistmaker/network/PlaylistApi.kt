package com.azmetov.playlistmaker.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PlaylistApi {

    @GET("search")
    fun search(
        @Query("term") text: String,
        @Query("entity") type: String = "song"
    ): Call<PlaylistResponse>

}