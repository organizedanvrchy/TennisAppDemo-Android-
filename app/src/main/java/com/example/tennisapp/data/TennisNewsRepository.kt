package com.example.tennisapp.data

import com.example.tennisapp.model.news.NewsArticle
import okhttp3.OkHttpClient
import okhttp3.Request
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.net.URI
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TennisNewsRepository(
    private val client: OkHttpClient = OkHttpClient()
) {
    // Feeds appear in this order for round-robin interleave
    private val feeds: List<String> = listOf(
        "https://feeds.bbci.co.uk/sport/tennis/rss.xml", // BBC Sport Tennis
        "https://feeds.feedburner.com/tennisx"          // Tennis-X (HTTPS)
        // Add more feeds and they'll be included in the cycle:
        // "https://www.wtatennis.com/feeds/rss/some-feed.xml",
        // "https://www.atptour.com/-/rss/rssfeed.xml",
    )

    /** Fetch per-feed, sort newest->oldest, then round-robin: f0[0], f1[0], ..., fN[0], f0[1], ... */
    suspend fun fetchAllRoundRobin(): List<NewsArticle> {
        val perFeedLists: List<List<NewsArticle>> = feeds.map { url ->
            runCatching { fetchOne(url) }
                .getOrElse { emptyList() }
                .sortedByDescending { it.published ?: Date(0) } // newest first within feed
        }

        val merged = roundRobinMerge(perFeedLists)
        return merged.distinctBy { it.link } // de-dup by link
    }

    private fun roundRobinMerge(lists: List<List<NewsArticle>>, limit: Int? = null): List<NewsArticle> {
        if (lists.isEmpty()) return emptyList()
        val indices = IntArray(lists.size) { 0 }
        val out = mutableListOf<NewsArticle>()

        var progressed: Boolean
        do {
            progressed = false
            for (i in lists.indices) {
                val li = lists[i]
                val idx = indices[i]
                if (idx < li.size) {
                    out += li[idx]
                    indices[i] = idx + 1
                    progressed = true
                    if (limit != null && out.size >= limit) return out
                }
            }
        } while (progressed)

        return out
    }

    private fun fetchOne(url: String): List<NewsArticle> {
        val req = Request.Builder()
            .url(url)
            .header("User-Agent", "Mozilla/5.0 (Android) TennisApp/1.0")
            .build()

        client.newCall(req).execute().use { resp ->
            if (!resp.isSuccessful) error("HTTP ${resp.code}")
            val xml = resp.body?.string().orEmpty()
            val host = runCatching { URI(url).host ?: "Unknown" }.getOrDefault("Unknown")
            return parseRss(xml, host)
        }
    }

    // Minimal RSS/Atom parser
    private fun parseRss(xml: String, defaultSource: String): List<NewsArticle> {
        val factory = XmlPullParserFactory.newInstance()
        val parser = factory.newPullParser().apply { setInput(xml.reader()) }

        val out = mutableListOf<NewsArticle>()
        var event = parser.eventType

        var inItem = false
        var title: String? = null
        var link: String? = null
        var pubDate: Date? = null
        var desc: String? = null
        var source = defaultSource

        fun flush() {
            val t = title?.trim().orEmpty()
            val l = link?.trim().orEmpty()
            if (t.isNotEmpty() && l.isNotEmpty()) {
                out += NewsArticle(
                    id = l,
                    title = t,
                    link = l,
                    source = source.trim().ifEmpty { defaultSource },
                    published = pubDate,
                    summary = desc?.trim()
                )
            }
            title = null; link = null; pubDate = null; desc = null; source = defaultSource
        }

        while (event != XmlPullParser.END_DOCUMENT) {
            when (event) {
                XmlPullParser.START_TAG -> {
                    when (parser.name.lowercase(Locale.US)) {
                        "item", "entry" -> {
                            inItem = true
                            title = null; link = null; pubDate = null; desc = null; source = defaultSource
                        }
                        "title" -> if (inItem) title = parser.nextTextOrEmpty()
                        "link" -> if (inItem) {
                            // RSS: <link>url</link>, Atom: <link href="..."/>
                            val href = parser.getAttributeValue(null, "href")
                            link = href ?: parser.nextTextOrEmpty()
                        }
                        "pubdate", "updated" -> if (inItem) {
                            pubDate = parser.nextTextOrEmpty().parseDateOrNull()
                        }
                        "description", "summary", "content" -> if (inItem) {
                            desc = parser.nextTextOrEmpty()
                        }
                        "source" -> if (inItem) {
                            source = parser.nextTextOrEmpty().ifBlank { defaultSource }
                        }
                    }
                }
                XmlPullParser.END_TAG -> {
                    when (parser.name.lowercase(Locale.US)) {
                        "item", "entry" -> {
                            flush()
                            inItem = false
                        }
                    }
                }
            }
            event = parser.next()
        }
        return out
    }
}

// --------- helpers ---------

private fun org.xmlpull.v1.XmlPullParser.nextTextOrEmpty(): String =
    try { nextText() } catch (_: Exception) { "" }

private fun String.parseDateOrNull(): Date? {
    val text = trim()
    if (text.isEmpty()) return null
    val fmts = listOf(
        "EEE, dd MMM yyyy HH:mm:ss Z", // RSS 2.0
        "EEE, dd MMM yyyy HH:mm Z",
        "yyyy-MM-dd'T'HH:mm:ss'Z'",    // Atom Zulu
        "yyyy-MM-dd'T'HH:mm:ssXXX",    // Atom TZ offset
        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
        "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"
    ).map { SimpleDateFormat(it, Locale.US) }
    for (fmt in fmts) {
        runCatching { return fmt.parse(text) }.getOrNull()?.let { return it }
    }
    return null
}
