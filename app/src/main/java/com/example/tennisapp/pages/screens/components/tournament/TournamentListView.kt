package com.example.tennisapp.pages.screens.components.tournament

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tennisapp.navigation.GlobalNavigation
import com.example.tennisapp.ui.theme.lexendBold
import com.example.tennisapp.ui.theme.lexendLight
import com.example.tennisapp.viewmodel.TennisViewModel

@Composable
fun TournamentListView(
    modifier: Modifier = Modifier,
    viewModel: TennisViewModel
) {
    val tournaments by viewModel.tournaments

    LaunchedEffect(Unit) {
        viewModel.loadTournaments()
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Date",
                modifier = Modifier.weight(1f),
                fontSize = 12.sp,
                fontFamily = lexendBold
            )
            Text(
                "Tournament Name",
                modifier = Modifier.weight(2.5f),
                fontSize = 12.sp,
                fontFamily = lexendBold
            )
        }

        HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)

        // Tournament Rows
        LazyColumn {
            items(tournaments) { tournament ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Date
                    Text(
                        text = tournament.date.take(4),
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 4.dp),
                        fontSize = 12.sp,
                        fontFamily = lexendLight
                    )
                    // Name
                    Text(
                        text = tournament.name,
                        modifier = Modifier
                            .weight(2.5f)
                            .padding(2.dp)
                            .basicMarquee()
                            .clickable {
                                viewModel.selectTournament(tournament)
                                GlobalNavigation.navController.navigate("tournament/${tournament.id}")
                            },
                        fontSize = 12.sp,
                        fontFamily = lexendLight
                    )
                }

                HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
            }
        }
    }
}