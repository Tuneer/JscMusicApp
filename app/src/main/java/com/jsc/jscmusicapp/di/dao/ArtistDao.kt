package com.jsc.jscmusicapp.di.dao

// data/ArtistDao.kt

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jsc.jscmusicapp.di.entities.ArtistEntity

@Dao
interface ArtistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArtist(artist: ArtistEntity)

    @Query("SELECT * FROM artists WHERE id = :id")
    suspend fun getArtistById(id: String): ArtistEntity?

    @Query("SELECT * FROM artists")
    suspend fun getAllArtists(): List<ArtistEntity>



}
