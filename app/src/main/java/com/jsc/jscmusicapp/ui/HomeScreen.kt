package com.jsc.jscmusicapp.ui

// ui/HomeScreen.kt

import android.media.JetPlayer
import android.media.MediaPlayer
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.jsc.jscmusicapp.di.DatabaseModule
import com.jsc.jscmusicapp.ui.players.MusicCardPlayer
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: YearRangeViewModel = remember {
    YearRangeViewModel(DatabaseModule.artistRepository)
}, modifier: Modifier) {
    val TAG = "HomeScreen"
    val songviewModel: SongViewModel = remember {
        SongViewModel(DatabaseModule.songRepository)
    }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val yearRanges by viewModel.yearRanges.collectAsState()
    val selectedRange by viewModel.selectedRange.collectAsState()
    val visibleArtists by viewModel.visibleArtists.collectAsState()
    val latestSongs by songviewModel.latestSongs.collectAsState()
    val albumList by viewModel.artistAlbums.collectAsState()
    var showAlbumSection by remember { mutableStateOf(false) }
    var showSongSection by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val selectedArtistDetail by viewModel.selectedArtistDetail.collectAsState()

    var selectedSongUrl by remember { mutableStateOf<String?>(null) }
    var selectedSongTitle by remember { mutableStateOf<String?>(null) }

    val screenWidth = LocalConfiguration.current.screenWidthDp
    val buttonSpacing = if (screenWidth >= 600) 16.dp else 8.dp

    var showBottomSheet by remember { mutableStateOf(false) }

    // Bottom Sheet content
    if (showBottomSheet && selectedSongUrl != null) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState,
            modifier = Modifier.fillMaxWidth()
        ) {

            // Show player only if a song was selected
            selectedSongUrl?.let { url ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF7F8F8))
                        .padding(8.dp)
                ) {
                    MusicCardPlayer(
                        songTitle = selectedSongTitle ?: "Unknown",
                        streamUrl = url
                    )
                }
            }

        }
    }


    LaunchedEffect(latestSongs) {
        if (latestSongs.isNotEmpty()) {
            Log.d(TAG, "LaunchedEffect: New songs received - ${latestSongs.size}")
            Log.d(TAG, "HomeScreen: $latestSongs")
            songviewModel.saveSongs(latestSongs)
           // songviewModel.loadAlbumsByArtist(selectedArtistDetail?.name)
            Log.d(TAG, "HomeScreen: saved and loaded album")
            showSongSection = true
        }else{
            showSongSection = false
        }
    }

    LaunchedEffect(albumList) {
        if (albumList.isNotEmpty()) {
            Log.d(TAG, "Albums ready to display: ${albumList.size}")
            showAlbumSection = true
        }else{
            showAlbumSection = false
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // 1. Years List (from ViewModel)
        item {
            Text("Years", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(buttonSpacing)) {
                items(yearRanges) { range ->
                    val isSelected = range == selectedRange
                    Button(onClick = { viewModel.onYearRangeSelected(range) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                        )
                    ) {
                        Text(range)
                    }
                }
            }
        }

        if (visibleArtists.isNotEmpty()) {
            // 2. Placeholder: Artists
            item {
                Text("Artists", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(horizontalArrangement = Arrangement.spacedBy(buttonSpacing)) {

                    items(visibleArtists) { artist ->
                        OutlinedButton(onClick = {
                            scope.launch {
                               viewModel.onArtistClicked(artist.name)
                            }
                        }) {
                            Text(artist.name)
                        }
                    }
                }
            }
        }

        if (showAlbumSection && albumList.isNotEmpty()) {
            // 3. Placeholder: Albums
            item {
                Text("Albums", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(horizontalArrangement = Arrangement.spacedBy(buttonSpacing)) {
                    items(albumList.map { it }) { item ->
                        Card(
                            modifier = Modifier
                                .width(120.dp)
                                .height(60.dp)
                                .clickable {
                                    scope.launch {
                                        songviewModel.getSongsByArtistId(selectedArtistDetail?.id)
                                    }
                                },
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFE3F2FD) // Light blue background
                            ),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(2.dp),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(item.name, style = MaterialTheme.typography.bodySmall)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(item.year.toString())
                            }
                        }
                    }
                }
            }

        }

        if (showSongSection && latestSongs.isNotEmpty()) {
            // 4. Placeholder: Songs
            item {
                Text("Songs", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(horizontalArrangement = Arrangement.spacedBy(buttonSpacing)) {
                    items(latestSongs.map { it }) { item ->
                        Card(
                            modifier = Modifier
                                .width(120.dp)
                                .height(60.dp)
                                .clickable{
                                    // Set selected song info here
                                    selectedSongTitle = item.name
                                    selectedSongUrl = item.downloadUrl[0].url
                                    showBottomSheet = true

                                    // Optional: animate bottom sheet
                                    scope.launch {
                                        sheetState.show()
                                    }
                                },
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFE4FDE3) // Light blue background
                            ),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(2.dp),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(contentAlignment = androidx.compose.ui.Alignment.Center) {
                                    Text(item.name, style = MaterialTheme.typography.bodySmall)
                                }
                            }
                        }
                    }
                }
            }

        }
    }




}
