package com.example.tennisapp.pages.screens

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tennisapp.ui.theme.lexend
import com.example.tennisapp.ui.theme.lexendBold
import com.example.tennisapp.ui.theme.lexendLight
import com.example.tennisapp.ui.theme.lexendSemiBold
import com.example.tennisapp.viewmodel.TennisViewModel

@Composable
fun TournamentScreen(
    tournamentId: Int?,
    tennisViewModel: TennisViewModel,
    modifier: Modifier = Modifier
) {
    val tournaments by tennisViewModel.tournaments
    val tournamentDetails by tennisViewModel.tournamentDetails

    val roundNames = mapOf(
        6 to "Third Round", 9 to "Quarterfinal", 7 to "Fourth Round",
        14 to "Rubber 2", 13 to "Rubber 1", 11 to "Bronze", 0 to "Pre-Q",
        16 to "Rubber 4", 10 to "Semifinal", 15 to "Rubber 3", 3 to "Q3",
        20 to "N/A", 12 to "Final", 2 to "Q2", 5 to "Second Round",
        1 to "Q1", 8 to "Robin", 4 to "First Round", 17 to "Rubber 5",
        21 to "ER", 22 to "Q4", 23 to "CR", 24 to "Quarterfinal",
        25 to "Q5", -1 to "N/A"
    )

    LaunchedEffect(tournamentId) {
        tournamentId?.let {
            Log.d("Tournament", "Fetching details for tournamentId: $it")
            tennisViewModel.loadTournamentDetails(it)
        }
    }

    // Build unique round IDs
    val availableRounds: List<Int> = remember(tournamentDetails) {
        tournamentDetails?.singles
            ?.map { it.roundId }
            ?.distinct()
            ?.sorted()
            ?: emptyList()
    }

    // State for each dropdown
    val expandedStates = remember { mutableStateMapOf<Int, Boolean>() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Tournament info card
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            val index = tournaments.indexOfFirst { it.id == tournamentId }
            val tournamentName = if (index != -1) tournaments[index].name else "No Name"
            val tournamentDate = if (index != -1) tournaments[index].date else "Unknown"

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = tournamentName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    fontFamily = lexendBold
                )
                Text(
                    text = "Date: " + tournamentDate.take(10),
                    fontFamily = lexend
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        // One dropdown per round
        availableRounds.forEach { roundId ->
            val roundName = roundNames[roundId] ?: "Unknown Round"
            val expanded = expandedStates[roundId] ?: false

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expandedStates[roundId] = !expanded },
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = roundName,
                        fontWeight = FontWeight.Bold,
                        fontFamily = lexendBold
                    )

                    // Inline expandable content
                    AnimatedVisibility(
                        visible = expanded,
                        enter = expandVertically() + fadeIn(),
                        exit = shrinkVertically() + fadeOut()
                    ) {
                        val matchesForRound = tournamentDetails?.singles
                            ?.filter { it.roundId == roundId }
                            ?: emptyList()

                        HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            matchesForRound.forEach { match ->
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    var winner = "Unknown"

                                    winner = if(match.matchWinner == match.player1Id.toString()){
                                        match.player1.name
                                    } else{
                                        match.player2.name
                                    }

                                    Row {
                                        Text(
                                            modifier = Modifier.basicMarquee(),
                                            text = match.player1.name,
                                            fontFamily = lexendBold
                                        )
                                        Text(
                                            text = " vs ",
                                            fontFamily = lexendBold
                                        )
                                        Text(
                                            modifier = Modifier.basicMarquee(),
                                            text = match.player2.name,
                                            fontFamily = lexendBold
                                        )
                                    }
                                    Text(
                                        text = "Results: ${match.result}",
                                        fontFamily = lexendLight
                                    )
                                    Text(
                                        text = "Winner: $winner",
                                        fontFamily = lexendSemiBold
                                    )
                                }
                                HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
                            }
                        }
                    }
                }
            }
        }
    }
}

