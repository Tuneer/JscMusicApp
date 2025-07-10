package com.jsc.jscmusicapp.ui

// ui/YearRangeViewModel.kt

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jsc.jscmusicapp.di.NetworkModule
import com.jsc.jscmusicapp.model.Artist
import com.jsc.jscmusicapp.repository.ArtistRepository
import com.jsc.jscmusicapp.response.Album
import com.jsc.jscmusicapp.response.ArtistResult
import com.jsc.jscmusicapp.response.Song
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar



class YearRangeViewModel(
    private val repository: ArtistRepository
) : ViewModel() {

    private val TAG = "YearRangeViewModel"

    private val _yearRanges = MutableStateFlow<List<String>>(emptyList())
    val yearRanges = _yearRanges.asStateFlow()

    private val _selectedRange = MutableStateFlow<String?>(null)
    val selectedRange = _selectedRange.asStateFlow()

    private val _visibleArtists = MutableStateFlow<List<Artist>>(emptyList())
    val visibleArtists = _visibleArtists.asStateFlow()

    private val _selectedArtistDetail = MutableStateFlow<ArtistResult?>(null)
    val selectedArtistDetail = _selectedArtistDetail.asStateFlow()


    private val artistRepo = ArtistRepository(repository.artistDao)

    private val _artistAlbums = MutableStateFlow<List<Album>>(emptyList())
    val artistAlbums = _artistAlbums.asStateFlow()



    init {
        generateYearRanges()
    }

    private fun generateYearRanges() {
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val startYear = 1900
        val ranges = mutableListOf<String>()

        var year = startYear
        while (year <= currentYear) {
            val end = (year + 4).coerceAtMost(currentYear)
            ranges.add("$year–$end")
            year += 5
        }

        _yearRanges.value = ranges
    }

    fun onYearRangeSelected(range: String) {
        _selectedRange.value = range

        val (start, end) = range.split("–").map { it.toInt() }
        val matchingArtists = artistRepo.getIndianLegends().filter {
            it.careerStart <= end && it.careerEnd >= start
        }

        _visibleArtists.value = matchingArtists
    }

//    suspend fun onArtistClicked(name: String) {
//        try {
//            val response = NetworkModule.apiService.searchArtist(query = name)
//            if (response.success && response.data.results.isNotEmpty()) {
//                _selectedArtistDetail.value = response.data.results.first()
//            } else {
//                _selectedArtistDetail.value = null
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//            _selectedArtistDetail.value = null
//        }
//    }

    suspend fun onArtistClicked(name: String) {
        viewModelScope.launch {
            try {
                Log.d(TAG,"Artist clicked: $name")
                val response = NetworkModule.apiService.searchArtist(name)
                if (response.success && response.data.results.isNotEmpty()) {
                    val artist = response.data.results.first()
                    _selectedArtistDetail.value = artist


                    // ✅ Check if artist already exists in local DB
                    val existingArtist = repository.artistDao.getArtistById(artist.id)
                    if (existingArtist == null) {
                        repository.saveArtistToRoom(artist)
                        Log.d(TAG, "Artist saved: $artist")
                    } else {
                        Log.d(TAG, "Artist already exists in database: $artist")
                    }


                    delay(2000)

                    // 👉 Now fetch albums instead of songs
                    val albumResponse = NetworkModule.apiService.getAlbumsByArtist(artist.id)
                    if (albumResponse.success) {
                        val albums = albumResponse.data.albums
                        _artistAlbums.value = albums
                        Log.d(TAG, "Albums: $albums")
                    } else {
                        Log.d(TAG, "Album fetch failed")
                        _artistAlbums.value = emptyList()
                    }



                }else{
                    _selectedArtistDetail.value = null
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }


}
