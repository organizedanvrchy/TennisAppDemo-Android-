package com.example.tennisapp.pages.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tennisapp.pages.screens.components.livetournament.LiveTournamentView
import com.example.tennisapp.ui.theme.lexendLight
import com.example.tennisapp.viewmodel.TennisViewModel

@Composable
fun LiveTournamentScreen(modifier: Modifier = Modifier, tennisViewModel: TennisViewModel) {
    // Trigger loading of live events
    LaunchedEffect(Unit) {
        tennisViewModel.loadLiveEvents()
    }

    val liveEvents by tennisViewModel.liveEvents

    Column(modifier = modifier.fillMaxWidth()) {
        if (liveEvents.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No live events currently.",
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontFamily = lexendLight
                    )
                )
            }
        } else {
            // Scrollable column in case there are multiple events
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(8.dp)
            ) {
                liveEvents.forEach { event ->
                    Card {
                        LiveTournamentView(event = event)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}