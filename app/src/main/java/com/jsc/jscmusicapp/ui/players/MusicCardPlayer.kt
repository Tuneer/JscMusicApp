package com.jsc.jscmusicapp.ui.players

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun MusicCardPlayer(songTitle: String, streamUrl: String) {
    var isPlayerVisible by remember { mutableStateOf(false) }

    Column {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable { isPlayerVisible = true },
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F8F7)),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(songTitle, style = MaterialTheme.typography.titleMedium)
                Text("Tap to play", style = MaterialTheme.typography.bodyMedium)
                if (isPlayerVisible) {
                    StreamAudioPlayer(streamUrl)
                }
            }
        }


    }
}

