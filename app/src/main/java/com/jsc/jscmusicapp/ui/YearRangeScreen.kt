package com.jsc.jscmusicapp.ui

// ui/YearRangeScreen.kt

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun YearRangeScreen(viewModel: YearRangeViewModel = viewModel()) {
    val yearRanges by viewModel.yearRanges.collectAsState()

    // Get screen width to adjust padding/margins for responsiveness
    val screenWidth = LocalConfiguration.current.screenWidthDp

    val buttonSpacing = if (screenWidth >= 600) 16.dp else 8.dp // Tablet vs phone

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Select Year Range")

        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(buttonSpacing),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(yearRanges) { range ->
                Button(onClick = { /* handle click */ }) {
                    Text(range)
                }
            }
        }
    }
}
