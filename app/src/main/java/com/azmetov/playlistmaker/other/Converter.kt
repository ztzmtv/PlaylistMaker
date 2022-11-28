package com.azmetov.playlistmaker.other

import com.azmetov.playlistmaker.entities.Track
import com.azmetov.playlistmaker.network.TrackDTO

class Converter {

    fun dtoToEntity(dto: TrackDTO?) = Track(
        trackName = dto?.trackName,
        artistName = dto?.artistName,
        trackTime = dto?.trackTimeMillis.toString(),
        artworkUrl100 = dto?.artworkUrl100
    )

}