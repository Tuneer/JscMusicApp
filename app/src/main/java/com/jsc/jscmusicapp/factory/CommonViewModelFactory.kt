package com.jsc.jscmusicapp.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jsc.jscmusicapp.di.DatabaseModule
import com.jsc.jscmusicapp.ui.YearRangeViewModel
import com.jsc.jscmusicapp.ui.SongViewModel

class CommonViewModelFactory(
    private val container: DatabaseModule
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(YearRangeViewModel::class.java) -> {
                YearRangeViewModel(container.artistRepository) as T
            }
            modelClass.isAssignableFrom(SongViewModel::class.java) -> {
                SongViewModel(container.songRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
