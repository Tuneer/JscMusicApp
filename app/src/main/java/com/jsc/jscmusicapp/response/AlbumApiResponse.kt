package com.jsc.jscmusicapp.response

import com.jsc.jscmusicapp.di.entities.AlbumEntity

data class AlbumApiResponse(
    val success: Boolean,
    val data: AlbumData
)

data class AlbumData(
    val total: Int,
    val albums: List<Album>
)

data class Album(
    val id: String,
    val name: String,
    val description: String?,
    val type: String,
    val year: Int,
    val playCount: Long?,
    val language: String,
    val explicitContent: Boolean,
    val url: String,
    val songCount: Int,
    val artists: AlbumArtists,
    val image: List<Image>,
    val songs: List<Song>? = null
)

data class AlbumArtists(
    val primary: List<ArtistInfo>,
    val featured: List<ArtistInfo>,
    val all: List<ArtistInfo>
)

data class ArtistInfo(
    val id: String,
    val name: String,
    val role: String,
    val image: List<Image>,
    val type: String,
    val url: String
)

fun Album.toEntity(): AlbumEntity {
    val imageUrl = image.find { it.quality == "500x500" }?.url
    val primaryArtistName = artists.primary.firstOrNull()?.name ?: "Unknown"

    return AlbumEntity(
        id = id,
        name = name,
        year = year,
        language = language,
        url = url,
        imageUrl = imageUrl,
        primaryArtist = primaryArtistName
    )
}

