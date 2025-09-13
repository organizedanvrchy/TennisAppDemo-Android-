package com.example.tennisapp.pages.screens.components.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.tbuonomo.viewpagerdotsindicator.compose.DotsIndicator
import com.tbuonomo.viewpagerdotsindicator.compose.model.DotGraphic
import com.tbuonomo.viewpagerdotsindicator.compose.type.WormIndicatorType

@Composable
fun BannerView(modifier: Modifier = Modifier) {
    // State to hold the list of banner image URLs
    var bannerList by remember {
        mutableStateOf<List<String>>(emptyList())
    }

    // Fetch banner URLs from Firestore when the Composable is first composed
    LaunchedEffect(Unit) {
        Firebase.firestore
            .collection("data")
            .document("banners")
            .get().addOnCompleteListener {
                // Cast the "urls" field to a List<String> and update state
                bannerList = it.result.get("urls") as List<String>
            }
    }

    // Main layout column
    Column(
        modifier = modifier
    ) {
        // Pager state for horizontal scrolling banners, starting at index 0
        val pagerState = rememberPagerState(0) {
            bannerList.size // total pages = number of banners
        }

        // Horizontal pager for banners
        HorizontalPager(
            state = pagerState,
            pageSpacing = 24.dp, // space between pages
            modifier = Modifier.height(125.dp) // fixed banner height
        ) {
            // Each page shows an image loaded asynchronously
            AsyncImage(
                model = bannerList[it], // load image from bannerList
                contentDescription = "banner",
                modifier = Modifier
                    .fillMaxWidth() // take full width
                    .clip(RoundedCornerShape(16.dp)) // rounded corners
                    .clickable(
                        onClick = { } // placeholder for click action
                    ),
                contentScale = ContentScale.Crop, // crop image to fill bounds
            )
        }

        // Spacer between pager and dots indicator
        Spacer(modifier = Modifier.height(10.dp))

        // Dots indicator showing current page
        DotsIndicator(
            dotCount = bannerList.size, // number of dots = number of banners
            type = WormIndicatorType(
                dotsGraphic = DotGraphic(
                    size = 6.dp,
                    borderWidth = 1.dp,
                    borderColor = Color.Black,
                    color = Color.Transparent
                ),
                wormDotGraphic = DotGraphic(
                    size = 12.dp,
                    color = Color.Black // active dot color
                )
            ),
            pagerState = pagerState // link dots to pager state
        )
    }
}