package com.example.tennisapp.pages.screens.components.ranking

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.tennisapp.navigation.GlobalNavigation
import com.example.tennisapp.ui.theme.lexendBold
import com.example.tennisapp.ui.theme.lexendLight
import com.example.tennisapp.utils.countryAcroMap
import com.example.tennisapp.viewmodel.TennisViewModel

@Composable
fun PlayerRankListView(modifier: Modifier = Modifier, viewModel: TennisViewModel) {
    // Observe rankings state from ViewModel
    val rankings by viewModel.rankings

    // Load rankings when Composable is first launched
    LaunchedEffect(Unit) {
        viewModel.loadRankings()
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // ---------------------------
        // Header row for rankings table
        // ---------------------------
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Rnk", // Rank column
                modifier = Modifier.weight(1f),
                style = TextStyle(fontSize = 12.sp, fontFamily = lexendBold)
            )
            Text(
                " ", // Placeholder for flag column
                modifier = Modifier.weight(1f),
            )
            Text(
                "Name", // Player name column
                modifier = Modifier.weight(2.5f),
                style = TextStyle(fontSize = 12.sp, fontFamily = lexendBold)
            )
            Text(
                "Pts", // Points column
                modifier = Modifier
                    .weight(1.5f)
                    .padding(start = 12.dp),
                style = TextStyle(fontSize = 12.sp, fontFamily = lexendBold)
            )
        }

        // Divider under header
        HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)

        // ---------------------------
        // LazyColumn for player rows
        // ---------------------------
        LazyColumn {
            items(rankings) { player ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Rank column
                    Text(
                        text = "${player.position}",
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 4.dp),
                        style = TextStyle(fontSize = 12.sp, fontFamily = lexendLight)
                    )

                    // Player flag
                    val countryAcro = countryAcroMap[player.player.country.acronym] ?: "unknown"
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data("file:///android_asset/flags/$countryAcro.svg")
                            .decoderFactory(SvgDecoder.Factory())
                            .build(),
                        contentDescription = "${player.player.country.acronym} flag",
                        modifier = Modifier
                            .size(20.dp)
                            .weight(1f)
                    )

                    // Player name column
                    Text(
                        text = player.player.name,
                        modifier = Modifier
                            .weight(2.5f)
                            .padding(2.dp)
                            .basicMarquee() // Scroll long names
                            .clickable {
                                viewModel.selectPlayer(player) // Update selected player in ViewModel
                                GlobalNavigation.navController.navigate("players/${player.player.id}") // Navigate to PlayerScreen
                            },
                        style = TextStyle(fontSize = 12.sp, fontFamily = lexendLight)
                    )

                    // Points column
                    Text(
                        text = "${player.point}",
                        modifier = Modifier
                            .weight(1.5f)
                            .padding(start = 8.dp)
                            .basicMarquee(),
                        style = TextStyle(fontSize = 12.sp, fontFamily = lexendLight)
                    )
                }

                // Divider between rows
                HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
            }
        }
    }
}
