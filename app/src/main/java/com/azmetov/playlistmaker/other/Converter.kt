package com.azmetov.playlistmaker.other

import com.azmetov.playlistmaker.entities.Track
import com.azmetov.playlistmaker.network.TrackDto
import java.text.SimpleDateFormat
import java.util.*

object Converter {

    fun dtoToEntity(dto: TrackDto) = Track(
        trackName = dto.trackName,
        artistName = dto.artistName,
        trackTime = dto.trackTimeMillis,
        artworkUrl100 = dto.artworkUrl100,
        collectionName = dto.collectionName,
        releaseDate = dto.releaseDate,//TODO()
        genre = dto.genre,
        country = dto.country,
    )

    fun convertTime(time: String?): String =
        SimpleDateFormat("mm:ss", Locale.getDefault()).format(time?.toLong() ?: "")

    fun convertTime(time: Int?): String =
        SimpleDateFormat("mm:ss", Locale.getDefault()).format(time?.toLong() ?: 0)
}