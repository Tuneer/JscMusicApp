package com.jsc.jscmusicapp.repository

import com.jsc.jscmusicapp.di.dao.ArtistDao
import com.jsc.jscmusicapp.di.entities.ArtistEntity
import com.jsc.jscmusicapp.model.Artist
import com.jsc.jscmusicapp.response.ArtistResult


// data/ArtistRepository.kt


class ArtistRepository(
    val artistDao: ArtistDao
) {

    suspend fun saveArtistToRoom(apiArtist: ArtistResult) {
        val entity = ArtistEntity(
            id = apiArtist.id,
            name = apiArtist.name,
            imageUrl = apiArtist.image.firstOrNull { it.quality == "150x150" }?.url ?: "",
            url = apiArtist.url
        )
        artistDao.insertArtist(entity)
    }

    fun getIndianLegends(): List<Artist> {
        return listOf(
            Artist("Mohammed Rafi", 1944, 1980),
            Artist("Kishore Kumar", 1946, 1987),
            Artist("Lata Mangeshkar", 1942, 2015)
        )
    }
}