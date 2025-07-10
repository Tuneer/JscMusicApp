package com.jsc.jscmusicapp.repository

import com.jsc.jscmusicapp.di.dao.SongDao
import com.jsc.jscmusicapp.di.entities.SongEntity

class SongRepository(val songDao: SongDao) {

        suspend fun insertSongs(songs: List<SongEntity>) {
            songDao.insertSongs(songs)
        }

        suspend fun getSongsByAlbum(albumId: String) = songDao.getSongsByAlbum(albumId)




}