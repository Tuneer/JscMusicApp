package com.jsc.jscmusicapp.di.entities

// model/ArtistEntity.kt

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "artists")
data class ArtistEntity(
    @PrimaryKey val id: String,
    val name: String,
    val imageUrl: String,  // We'll save the 150x150 image
    val url: String
)

