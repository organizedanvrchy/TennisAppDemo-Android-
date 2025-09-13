package com.example.tennisapp.pages.screens.components.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tennisapp.pages.screens.components.tournament.TournamentListView
import com.example.tennisapp.viewmodel.TennisViewModel

@Composable
fun TournamentStatsCard(
    modifier: Modifier = Modifier,
    viewModel: TennisViewModel,
) {
    Card(
        modifier = modifier.fillMaxSize(),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        TournamentListView(
            modifier = modifier,
            viewModel = viewModel,
        )
    }
}

