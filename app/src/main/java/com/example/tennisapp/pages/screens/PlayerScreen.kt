package com.example.tennisapp.pages.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.tennisapp.ui.theme.lexendBold
import com.example.tennisapp.ui.theme.lexendLight
import com.example.tennisapp.utils.countryAcroMap
import com.example.tennisapp.viewmodel.TennisViewModel

@Composable
fun PlayerScreen(
    playerId: Int?,
    tennisViewModel: TennisViewModel,
    modifier: Modifier = Modifier,
) {
    val rankings by tennisViewModel.rankings
    val playerDetails by tennisViewModel.playerDetails

    // Load details when playerId changes
    LaunchedEffect(playerId) {
        playerId?.let {
            Log.d("PlayerScreen", "Fetching details for playerId: $it")
            tennisViewModel.loadPlayerDetails(it)
        }
    }

    val playerRanking = rankings.find { it.player.id == playerId }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Player Details Header Card (Extract Component Later...)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val context = LocalContext.current
                val playerName = playerRanking?.player?.name ?: "Unknown"
                val countryName = playerRanking?.player?.country?.name ?: "Unknown"
                val countryAcro = countryAcroMap[playerRanking?.player?.country?.acronym] ?: "unknown"
                val rank = playerRanking?.position ?: "-"
                val points = playerRanking?.point ?: "-"

                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data("file:///android_asset/flags/$countryAcro.svg")
                        .decoderFactory(SvgDecoder.Factory())
                        .build(),
                    contentDescription = "$countryName flag",
                    modifier = Modifier.size(60.dp)
                )

                Spacer(Modifier.width(16.dp))

                Column {
                    Text(
                        text = playerName,
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontFamily = lexendBold
                        )
                    )
                    Text(
                        text = "Country: $countryName",
                        style = TextStyle(
                            fontFamily = lexendLight
                        )
                    )
                    Text(
                        text = "Rank: $rank | Points: $points",
                        style = TextStyle(
                            fontFamily = lexendLight
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Player Details Body Card (Extract Component Later...)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                if (playerDetails == null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    // Display player details (Format Details Later...)
                    val details = playerDetails!!
                    val info = details.information

                    Text(
                        text = "Status: ${details.playerStatus ?: "N/A"}",
                        style = TextStyle(fontFamily = lexendLight)
                    )
                    Text(
                        text = "Turned Pro: ${info?.turnedPro ?: "N/A"}",
                        style = TextStyle(fontFamily = lexendLight)
                    )
                    Text(
                        text = "Height: ${info?.height ?: "N/A"}",
                        style = TextStyle(fontFamily = lexendLight)
                    )
                    Text(
                        text = "Weight: ${info?.weight ?: "N/A"}",
                        style = TextStyle(fontFamily = lexendLight)
                    )
                    Text(
                        text = "Birthplace: ${info?.birthplace ?: "N/A"}",
                        style = TextStyle(fontFamily = lexendLight)
                    )
                    Text(
                        text = "Residence: ${info?.residence ?: "N/A"}",
                        style = TextStyle(fontFamily = lexendLight)
                    )
                    Text(
                        text = "Plays: ${info?.plays ?: "N/A"}",
                        style = TextStyle(fontFamily = lexendLight)
                    )
                    Text(
                        text = "Coach: ${info?.coach ?: "N/A"}",
                        style = TextStyle(fontFamily = lexendLight)
                    )
                }
            }
        }
    }
}


