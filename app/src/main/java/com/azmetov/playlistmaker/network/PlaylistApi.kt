package com.azmetov.playlistmaker.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PlaylistApi {

    @GET("search")
    fun search(
        @Query("term") text: String,
        @Query("entity") type: String = ContentType.MUSIC.entity
    ): Call<PlaylistResponse>

    enum class ContentType(val entity: String) {
        MOVIE("movie"),
        PODCAST("podcast"),
        MUSIC("music"),
        MUSICVIDEO("musicvideo"),
        AUDIOBOOK("audiobook"),
        SHORTFILM("shortfilm"),
        TVSHOW("tvshows"),
        SOFTWARE("software"),
        EBOOK("ebook"),
        ALL("all"),
    }

    // или лучше так?
    companion object {
        private const val SEARCH_TYPE_MOVIE = "movie"
        private const val SEARCH_TYPE_PODCAST = "podcast"
        private const val SEARCH_TYPE_MUSIC = "music"
        private const val SEARCH_TYPE_MUSICVIDEO = "musicvideo"
        private const val SEARCH_TYPE_AUDIOBOOK = "audiobook"
        private const val SEARCH_TYPE_SHORTFILM = "shortfilm"
        private const val SEARCH_TYPE_TVSHOW = "tvshows"
        private const val SEARCH_TYPE_SOFTWARE = "software"
        private const val SEARCH_TYPE_EBOOK = "ebook"
        private const val SEARCH_TYPE_ALL = "all"
    }
}