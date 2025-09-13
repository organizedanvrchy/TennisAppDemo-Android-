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
    var bannerList by remember {
        mutableStateOf<List<String>>(emptyList())
    }

    LaunchedEffect(Unit) {
        Firebase.firestore
            .collection("data")
            .document("banners")
            .get().addOnCompleteListener {
                bannerList = it.result.get("urls") as List<String>
            }
    }

    Column(
        modifier = modifier
    ) {
        val pagerState = rememberPagerState(0) {
            bannerList.size
        }

        HorizontalPager(
            state = pagerState,
            pageSpacing = 24.dp,
            modifier = Modifier.height(125.dp)
        ) {
            AsyncImage(
                model = bannerList[it],
                contentDescription = "banner",
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .clickable(
                        onClick = { }
                    ),
                contentScale = ContentScale.Crop,
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        DotsIndicator(
            dotCount = bannerList.size,
            type = WormIndicatorType(
                dotsGraphic = DotGraphic(
                    size = 6.dp,
                    borderWidth = 1.dp,
                    borderColor = Color.Black,
                    color = Color.Transparent
                ),
                wormDotGraphic = DotGraphic(
                    size = 12.dp,
                    color = Color.Black
                )
            ),
            pagerState = pagerState
        )
    }
}