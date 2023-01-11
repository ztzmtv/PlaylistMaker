package com.azmetov.playlistmaker.data.entities

import java.io.Serializable

data class Track(
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    val trackTime: Int, // Продолжительность трека
    val artworkUrl100: String?, // Ссылка на изображение обложки
    val collectionName: String?,
    val releaseDate: String?,
    val genre: String?,
    val country: String?,
) : Serializable {

    fun getCoverArtwork() =
        artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg")

}