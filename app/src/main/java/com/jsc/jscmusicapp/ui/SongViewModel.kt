package com.jsc.jscmusicapp.ui

import android.adservices.adid.AdId
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jsc.jscmusicapp.di.NetworkModule
import com.jsc.jscmusicapp.repository.SongRepository
import com.jsc.jscmusicapp.response.Song
import com.jsc.jscmusicapp.response.toEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.jsc.jscmusicapp.di.entities.AlbumEntity
import com.jsc.jscmusicapp.di.entities.SongEntity

class SongViewModel(
    private val repository: SongRepository
) : ViewModel() {

    private val TAG = "SongViewModel"

    private val _songs = MutableStateFlow<List<SongEntity>>(emptyList())
    val songs = _songs.asStateFlow()

    private val _albums = MutableStateFlow<List<AlbumEntity>>(emptyList())
    val albums = _albums.asStateFlow()

    private val _latestSongs = MutableStateFlow<List<Song>>(emptyList())
    val latestSongs = _latestSongs.asStateFlow()

    fun saveSongs(songList: List<Song>) {
        val entities = songList.map { it.toEntity() }
        viewModelScope.launch {
            repository.insertSongs(entities)
            _songs.value = entities
        }
    }


    fun loadSongsByAlbum(albumId: String) {
        viewModelScope.launch {
            _songs.value = repository.getSongsByAlbum(albumId)
        }
    }

    fun getSongsByArtistId(artistId: String?) {
        viewModelScope.launch {
            if (artistId != null) {
                    val songsResponse = NetworkModule.apiService.getSongsByArtist(artistId)
                    if (songsResponse.success) {
                        Log.d(TAG,"Songs Response: $songsResponse")
                        // Handle the songs response
                        val songs = songsResponse.data.songs
                        _latestSongs.value = songs
                    } else {
                        Log.d(TAG, "Songs Response Error")
                        // Handle the error
                        _latestSongs.value = emptyList()
                    }
            }
        }

    }

}
