package com.example.tennisapp.pages.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tennisapp.pages.screens.components.home.TournamentStatsCard
import com.example.tennisapp.pages.screens.components.home.BannerView
import com.example.tennisapp.pages.screens.components.home.LiveTournamentCard
import com.example.tennisapp.pages.screens.components.home.PlayerStatsCard
import com.example.tennisapp.viewmodel.TennisViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    tennisViewModel: TennisViewModel
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp)
            .verticalScroll(scrollState)
    ) {
        BannerView(modifier = Modifier.height(150.dp))

        Spacer(modifier = Modifier.height(10.dp))

        LiveTournamentCard(
            modifier = Modifier.height(150.dp),
            viewModel = tennisViewModel
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.height(300.dp)
        ) {
            PlayerStatsCard(
                modifier = Modifier.weight(1f),
                viewModel = tennisViewModel
            )
            Spacer(modifier = Modifier.width(10.dp))
            TournamentStatsCard(
                modifier = Modifier.weight(1f),
                viewModel = tennisViewModel
            )
        }
    }
}