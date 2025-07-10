package com.jsc.jscmusicapp.di

import android.content.Context
import androidx.room.Room
import com.jsc.jscmusicapp.AppApplication
import com.jsc.jscmusicapp.di.db.AppDatabase
import com.jsc.jscmusicapp.repository.ArtistRepository
import com.jsc.jscmusicapp.repository.SongRepository

object DatabaseModule {

    lateinit var database: AppDatabase
    lateinit var artistRepository: ArtistRepository
    lateinit var songRepository: SongRepository

    fun init(context: Context) {
        database = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_db"
        ).build()

        artistRepository = ArtistRepository(database.artistDao())
        songRepository = SongRepository(database.songDao())
    }
}
