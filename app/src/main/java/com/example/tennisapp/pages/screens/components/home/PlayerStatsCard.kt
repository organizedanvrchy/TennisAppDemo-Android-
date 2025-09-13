package com.example.tennisapp.pages.screens.components.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tennisapp.pages.screens.components.ranking.PlayerRankListView
import com.example.tennisapp.viewmodel.TennisViewModel

@Composable
fun PlayerStatsCard(
    modifier: Modifier = Modifier,
    viewModel: TennisViewModel,
) {
    Card(
        modifier = modifier.fillMaxSize(),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        PlayerRankListView(
            modifier = modifier,
            viewModel = viewModel,
        )
    }
}