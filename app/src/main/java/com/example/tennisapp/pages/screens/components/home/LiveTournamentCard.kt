package com.example.tennisapp.pages.screens.components.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tennisapp.navigation.GlobalNavigation
import com.example.tennisapp.pages.screens.components.livetournament.LiveTournamentView
import com.example.tennisapp.ui.theme.lexendLight
import com.example.tennisapp.viewmodel.TennisViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun LiveTournamentCard(
    modifier: Modifier = Modifier,
    viewModel: TennisViewModel
) {
    // Trigger loading of live events
    LaunchedEffect(Unit) {
        viewModel.loadLiveEvents()
    }

    val liveEvents by viewModel.liveEvents
    val event = liveEvents.firstOrNull()

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                onClick = {
                    GlobalNavigation.navController.navigate("live")
                }
            ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        if (event == null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No live events currently.",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontFamily = lexendLight
                    )
                )
            }
        } else {
            // Display only the first/top event
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LiveTournamentView(event = event)
            }
        }
    }
}

fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("MMM dd yyyy", Locale.getDefault())
    return sdf.format(Date(timestamp * 1000))
}