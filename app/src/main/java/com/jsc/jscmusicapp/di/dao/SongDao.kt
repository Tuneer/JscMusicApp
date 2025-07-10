package com.jsc.jscmusicapp.di.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jsc.jscmusicapp.di.entities.AlbumEntity
import com.jsc.jscmusicapp.di.entities.SongEntity

@Dao
interface SongDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSongs(songs: List<SongEntity>)


    @Query("SELECT * FROM songs WHERE albumId = :albumId")
    suspend fun getSongsByAlbum(albumId: String): List<SongEntity>
}
