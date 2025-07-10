package com.jsc.jscmusicapp.di.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "songs")
data class SongEntity(
    @PrimaryKey val id: String,
    val name: String,
    val year: String,
    val duration: Int,
    val label: String,
    val language: String,
    val url: String,

    // Album details
    val albumId: String,
    val albumName: String,
    val albumUrl: String,

    // Song image (optional: store one)
    val imageUrl: String?,

    // Preferred download URL
    val downloadUrl320kbps: String?
)
