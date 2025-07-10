package com.jsc.jscmusicapp.di.db

// data/AppDatabase.kt

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jsc.jscmusicapp.di.dao.ArtistDao
import com.jsc.jscmusicapp.di.dao.SongDao
import com.jsc.jscmusicapp.di.entities.ArtistEntity
import com.jsc.jscmusicapp.di.entities.SongEntity

@Database(entities = [ArtistEntity::class,SongEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun artistDao(): ArtistDao
    abstract fun songDao(): SongDao
}
