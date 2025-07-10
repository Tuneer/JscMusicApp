package com.jsc.jscmusicapp.response

import com.jsc.jscmusicapp.di.entities.SongEntity

data class SongResponse(
    val success: Boolean,
    val data: SongData
)

data class SongData(
    val total: Int,
    val songs: List<Song>
)

data class Song(
    val id: String,
    val name: String,
    val year: String,
    val duration: Int,
    val label: String,
    val language: String,
    val url: String,
    val album: Album,
    val image: List<Image>,
    val downloadUrl: List<DownloadUrl>,
)



data class Image(
    val quality: String,
    val url: String
)

data class DownloadUrl(
    val quality: String,
    val url: String
)

fun Song.toEntity(): SongEntity {
    val bestImage = image.find { it.quality == "500x500" }?.url
    val bestDownload = downloadUrl.find { it.quality == "320kbps" }?.url


    return SongEntity(
        id = id,
        name = name,
        year = year,
        duration = duration,
        label = label,
        language = language,
        url = url,
        albumId = album.id,
        albumName = album.name,
        albumUrl = album.url,
        imageUrl = bestImage,
        downloadUrl320kbps = bestDownload
    )
}


