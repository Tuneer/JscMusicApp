package com.jsc.jscmusicapp.ui.players

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.jsc.jscmusicapp.utils.formatTime
import kotlinx.coroutines.delay


@Composable
fun StreamAudioPlayer(streamUrl: String) {
    val context = LocalContext.current
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(streamUrl))
            prepare()
        }
    }

    var isPlaying by remember { mutableStateOf(false) }
    var currentPosition by remember { mutableLongStateOf(0L) }
    var totalDuration by remember { mutableLongStateOf(0L) }

    // Update playback position every second
    LaunchedEffect(key1 = isPlaying) {
        while (isPlaying) {
            currentPosition = exoPlayer.currentPosition
            totalDuration = exoPlayer.duration.coerceAtLeast(0L)
            delay(1000)
        }
    }

    // Release player on exit
    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Play/Pause Button
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = {
                isPlaying = !isPlaying
                if (isPlaying) exoPlayer.play() else exoPlayer.pause()
            }) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Refresh else Icons.Default.PlayArrow,
                    contentDescription = "Play/Pause"
                )
            }

            // Stop button
            IconButton(onClick = {
                exoPlayer.pause()
                exoPlayer.seekTo(0)
                isPlaying = false
            }) {
                Icon(Icons.Default.Close, contentDescription = "Stop")
            }
        }

        // SeekBar
        Slider(
            value = currentPosition.toFloat(),
            onValueChange = {
                exoPlayer.seekTo(it.toLong())
                currentPosition = it.toLong()
            },
            valueRange = 0f..(totalDuration.takeIf { it > 0 } ?: 1).toFloat(),
            modifier = Modifier.fillMaxWidth()
        )

        // Time display
        Text(
            text = "${formatTime(currentPosition)} / ${formatTime(totalDuration)}",
            style = MaterialTheme.typography.bodySmall
        )
    }
}
