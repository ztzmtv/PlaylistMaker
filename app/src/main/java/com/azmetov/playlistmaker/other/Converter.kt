package com.azmetov.playlistmaker.other

import com.azmetov.playlistmaker.entities.Track
import com.azmetov.playlistmaker.network.TrackDto
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

object Converter {

    fun dtoToEntity(dto: TrackDto) = Track(
        trackName = dto.trackName,
        artistName = dto.artistName,
        trackTime = dto.trackTimeMillis,
        artworkUrl100 = dto.artworkUrl100,
        collectionName = dto.collectionName,
        releaseDate = getYearFromTimestamp(dto.releaseDate),
        genre = dto.genre,
        country = dto.country,
    )

    fun convertTime(time: Int?): String =
        SimpleDateFormat(TRACK_TIME_PATTERN, Locale.getDefault())
            .format(time?.toLong() ?: TRACK_TIME_IF_NULL)

    private fun getYearFromTimestamp(timestamp: String?): String {
        return if (timestamp != null) {
            LocalDateTime
                .parse(timestamp, DateTimeFormatter.ofPattern(DATETIME_PATTERN))
                .year
                .toString()
        } else DATETIME_IF_NULL
    }

    private const val DATETIME_IF_NULL = ""
    private const val TRACK_TIME_IF_NULL = 0
    private const val DATETIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ssz"
    private const val TRACK_TIME_PATTERN = "mm:ss"
}