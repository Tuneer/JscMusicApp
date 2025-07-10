package com.jsc.jscmusicapp.di.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "albums")
data class AlbumEntity(
    @PrimaryKey val id: String,
    val name: String,
    val year: Int,
    val language: String,
    val url: String,
    val imageUrl: String?,
    val primaryArtist: String
)

