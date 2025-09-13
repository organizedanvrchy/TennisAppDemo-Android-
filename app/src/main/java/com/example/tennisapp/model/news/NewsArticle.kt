package com.example.tennisapp.model.news

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class NewsArticle(
    val id: String,
    val title: String,
    val link: String,
    val source: String,
    val published: Date?,
    val summary: String?
) {
    val publishedDisplay: String?
        get() = published?.let { SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(it) }
}
