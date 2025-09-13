package com.example.tennisapp.pages.screens.components.livetournament

import android.util.Log
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.tennisapp.model.eventdetails.Event
import com.example.tennisapp.pages.screens.components.home.formatDate
import com.example.tennisapp.ui.theme.lexend
import com.example.tennisapp.ui.theme.lexendBold
import com.example.tennisapp.ui.theme.lexendLight
import com.example.tennisapp.ui.theme.lexendSemiBold
import com.example.tennisapp.utils.countryAcroMap

@Composable
fun LiveTournamentView(modifier: Modifier = Modifier, event: Event) {
    // Main column layout for the live tournament card
    Column(
        modifier = modifier.padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // ---------------------------
        // Tournament Name
        // ---------------------------
        Text(
            modifier = modifier.basicMarquee(), // marquee effect for long names
            text = shortenTournamentName(event.tournament.uniqueTournament?.name ?: event.tournament.name),
            style = TextStyle(
                fontSize = 24.sp,
                fontFamily = lexendBold
            )
        )

        Spacer(modifier = Modifier.height(4.dp))

        // ---------------------------
        // Season, Location, and Match Date
        // ---------------------------
        val location = event.tournament.name.substringAfter(", ") // Extract location from tournament name

        Text(
            modifier = modifier.basicMarquee(),
            text = "${event.season.name} | $location | ${formatDate(event.startTimestamp)}",
            style = TextStyle(
                fontSize = 12.sp,
                fontFamily = lexendLight
            )
        )

        // ---------------------------
        // Player Names and Flags
        // ---------------------------
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Home player/team
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
                    .basicMarquee()
                    .weight(1f)
            ) {
                val homeCountryCode = if (event.homeTeam.subTeams.isNotEmpty()) {
                    event.homeTeam.subTeams.first().country?.alpha3
                } else {
                    event.homeTeam.country?.alpha3
                } ?: "unknown"

                // Log for debugging flag loading
                Log.d("LiveTournamentView", "Loading flag: $homeCountryCode")

                val homeFlag = countryAcroMap[homeCountryCode] ?: "unknown"
                Log.d("LiveTournamentView", "Loading flag: $homeFlag.svg")

                // Load country flag image from assets
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("file:///android_asset/flags/$homeFlag.svg")
                        .decoderFactory(SvgDecoder.Factory())
                        .build(),
                    contentDescription = "$homeCountryCode flag",
                    modifier = Modifier
                        .size(20.dp)
                        .padding(4.dp)
                )

                Text(
                    text = event.homeTeam.name,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = lexendSemiBold
                    )
                )
            }

            // VS separator
            Text(
                text = "VS",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = lexendSemiBold
                ),
                modifier = modifier.padding(8.dp)
            )

            // Away player/team
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier.basicMarquee().weight(1f)
            ) {
                val awayCountryCode = if (event.awayTeam.subTeams.isNotEmpty()) {
                    event.awayTeam.subTeams.first().country?.alpha3
                } else {
                    event.awayTeam.country?.alpha3
                } ?: "unknown"

                Log.d("LiveTournamentView", "Loading flag: $awayCountryCode")

                val awayFlag = countryAcroMap[awayCountryCode] ?: "unknown"
                Log.d("LiveTournamentView", "Loading flag: $awayFlag.svg")

                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("file:///android_asset/flags/$awayFlag.svg")
                        .decoderFactory(SvgDecoder.Factory())
                        .build(),
                    contentDescription = "$awayCountryCode flag",
                    modifier = Modifier
                        .size(20.dp)
                        .padding(4.dp)
                )

                Text(
                    text = event.awayTeam.name,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = lexendSemiBold
                    )
                )
            }
        }

        // ---------------------------
        // Match Progress / Scores
        // ---------------------------
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Column headers for sets
            Text(text = "Set 1", style = TextStyle(fontFamily = lexend))
            Text(text = "Set 2", style = TextStyle(fontFamily = lexend))
            Text(text = "Set 3", style = TextStyle(fontFamily = lexend))
        }

        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Scores for each set
            Text(text = "${event.homeScore?.period1 ?: 0} - ${event.awayScore?.period1 ?: 0}", style = TextStyle(fontFamily = lexendLight))
            Text(text = "${event.homeScore?.period2 ?: 0} - ${event.awayScore?.period2 ?: 0}", style = TextStyle(fontFamily = lexendLight))
            Text(text = "${event.homeScore?.period3 ?: 0} - ${event.awayScore?.period3 ?: 0}", style = TextStyle(fontFamily = lexendLight))
        }
    }
}

// ---------------------------
// Utility function to shorten tournament names by removing years, countries, and redundant words
// ---------------------------
fun shortenTournamentName(fullName: String): String {
    val tennisCountries = listOf(
        "USA", "Canada", "Spain", "France", "Italy", "Germany",
        "United Kingdom", "Australia", "Austria", "Argentina", "Brazil",
        "Switzerland", "Serbia", "Russia", "Croatia", "Slovenia",
        "Czech Republic", "Poland", "Greece", "Japan", "China", "Netherlands"
    )

    val tennisCities = listOf(
        "Punta Cana"
    )

    // Patterns to remove from tournament names
    val patterns = listOf(
        "Women", "Men", "Boys", "Girls", "Doubles", "Singles", "Quad", "Wheelchair",
        "\\d{4}",                  // years like 2024
        "(?<=\\s)\\d+(?=\\s|$)",  // standalone numbers
        "\\+H"                     // hospitality symbols
    ) + tennisCountries + tennisCities

    val regex = ("[ ,\\-]*(" + patterns.joinToString("|") + ")[ ,\\-]*")
        .toRegex(RegexOption.IGNORE_CASE)

    return fullName
        .replace(regex, "")          // remove matching patterns
        .replace(Regex("\\s+"), " ") // normalize spaces
        .trim()                       // trim leading/trailing spaces
}
