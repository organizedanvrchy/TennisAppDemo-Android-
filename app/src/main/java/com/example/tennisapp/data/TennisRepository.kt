package com.example.tennisapp.data

import android.util.Log
import com.example.tennisapp.model.eventdetails.Event
import com.example.tennisapp.model.eventdetails.EventApiResponse
import com.example.tennisapp.model.playerdetails.PlayerDetailApiResponse
import com.example.tennisapp.model.playerdetails.PlayerDetails
import com.example.tennisapp.model.playerrankings.PlayerRankingApiResponse
import com.example.tennisapp.model.playerrankings.PlayerRanking
import com.example.tennisapp.model.tournamentdetails.TournamentApiResponse
import com.example.tennisapp.model.tournamentdetails.TournamentDetails
import com.example.tennisapp.model.tournamentdetails.TournamentDetailsApiResponse
import com.example.tennisapp.model.tournamentdetails.TournamentInfo
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.remoteConfig
import com.google.gson.Gson
import kotlinx.coroutines.tasks.await
import okhttp3.OkHttpClient
import okhttp3.Request

class TennisRepository {
    private val client = OkHttpClient()
    private val gson = Gson()

    private val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig.apply {
        // Configure Remote Config
        setConfigSettingsAsync(
            remoteConfigSettings {
                minimumFetchIntervalInSeconds = 3600
            }
        )
        // Set default value to avoid missing key errors
        setDefaultsAsync(mapOf(
            "API_KEY" to "",
            "API_KEY_TWO" to "",
            "API_KEY_THREE" to ""
        ))
    }

    private var cachedApiKey: String? = null

    // Helper function to fetch and return API key
    private suspend fun fetchApiKey(): String {
        return cachedApiKey ?: run {
            try {
                remoteConfig.fetchAndActivate().await()
                remoteConfig.getValue("API_KEY_THREE").asString().trim().also {
                    cachedApiKey = it
                }
            } catch (e: Exception) {
                e.printStackTrace()
                ""
            }
        }
    }
    suspend fun getPlayerRankings(): List<PlayerRanking> {
        val apiKey = fetchApiKey()

        val request = Request.Builder()
            .url("https://tennis-api-atp-wta-itf.p.rapidapi.com/tennis/v2/atp/ranking/singles/")
            .get()
            .addHeader("x-rapidapi-key", apiKey)
            .addHeader("x-rapidapi-host", "tennis-api-atp-wta-itf.p.rapidapi.com")
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw Exception("API call failed: ${response.code}")
            val body = response.body?.string() ?: "{}"
            val playerRankingApiResponse = gson.fromJson(body, PlayerRankingApiResponse::class.java)
            return playerRankingApiResponse.data
        }
    }

    suspend fun getPlayerDetails(playerId: Int): PlayerDetails {
        val apiKey = fetchApiKey()

        val request = Request.Builder()
            .url("https://tennis-api-atp-wta-itf.p.rapidapi.com/tennis/v2/atp/player/profile/$playerId")
            .get()
            .addHeader("x-rapidapi-key", apiKey)
            .addHeader("x-rapidapi-host", "tennis-api-atp-wta-itf.p.rapidapi.com")
            .build()

        client.newCall(request).execute().use { response ->
            val body = response.body?.string() ?: "{}"
            if (!response.isSuccessful) {
                Log.e("TennisRepository", "API failed: ${response.code} Body: $body")
                throw Exception("API call failed: ${response.code}")
            }
            val playerDetailApiResponse = gson.fromJson(body, PlayerDetailApiResponse::class.java)
            return playerDetailApiResponse.data
        }
    }

    // Get Live Tennis Event
    suspend fun getRecentEventDetails(): List<Event> {
        val apiKey = fetchApiKey()

        val request = Request.Builder()
            .url("https://tennisapi1.p.rapidapi.com/api/tennis/events/live")
            .get()
            .addHeader("x-rapidapi-key", apiKey)
            .addHeader("x-rapidapi-host", "tennisapi1.p.rapidapi.com")
            .build()

        client.newCall(request).execute().use { response ->
            val body = response.body?.string() ?: "{}"
            if (!response.isSuccessful) {
                Log.e("TennisRepository", "API failed: ${response.code} Body: $body")
                throw Exception("API call failed: ${response.code}")
            }

            val liveEventResponse = gson.fromJson(body, EventApiResponse::class.java)
            return liveEventResponse.events
        }
    }

    suspend fun getTournamentInfo1(): List<TournamentInfo> {
        val apiKey = fetchApiKey()

        val request = Request.Builder()
            .url("https://tennis-api-atp-wta-itf.p.rapidapi.com/tennis/v2/atp/tournament/calendar/2024")
            .get()
            .addHeader("x-rapidapi-key", apiKey)
            .addHeader("x-rapidapi-host", "tennis-api-atp-wta-itf.p.rapidapi.com")
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw Exception("API call failed: ${response.code}")
            val body = response.body?.string() ?: "{}"
            val tournamentApiResponse = gson.fromJson(body, TournamentApiResponse::class.java)
            return tournamentApiResponse.data
        }
    }

    suspend fun getTournamentInfo2(): List<TournamentInfo>{
        val apiKey = fetchApiKey()

        val request = Request.Builder()
            .url("https://tennis-api-atp-wta-itf.p.rapidapi.com/tennis/v2/atp/tournament/seasons/344")
            .get()
            .addHeader("x-rapidapi-key", apiKey)
            .addHeader("x-rapidapi-host", "tennis-api-atp-wta-itf.p.rapidapi.com")
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw Exception("API call failed: ${response.code}")
            val body = response.body?.string() ?: "{}"
            val tournamentApiResponse = gson.fromJson(body, TournamentApiResponse::class.java)
            return tournamentApiResponse.data
        }
    }

    suspend fun getTournamentDetails(tournamentId: Int): TournamentDetails {
        val apiKey = fetchApiKey()

        val request = Request.Builder()
            .url("https://tennis-api-atp-wta-itf.p.rapidapi.com/tennis/v2/atp/tournament/results/$tournamentId")
            .get()
            .addHeader("x-rapidapi-key", apiKey)
            .addHeader("x-rapidapi-host", "tennis-api-atp-wta-itf.p.rapidapi.com")
            .build()

        client.newCall(request).execute().use { response ->
            val body = response.body?.string() ?: "{}"
            if (!response.isSuccessful) {
                Log.e("TennisRepository", "API failed: ${response.code} Body: $body")
                throw Exception("API call failed: ${response.code}")
            }
            val tournamentDetailsApiResponse = gson.fromJson(body, TournamentDetailsApiResponse::class.java)
            return tournamentDetailsApiResponse.data
        }
    }
}