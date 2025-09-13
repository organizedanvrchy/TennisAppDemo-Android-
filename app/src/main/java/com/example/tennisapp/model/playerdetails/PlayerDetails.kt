package com.example.tennisapp.model.playerdetails

import com.example.tennisapp.model.playerrankings.PlayerCountry

data class PlayerDetails(
    val playerStatus: String,
    val id: Int,
    val name: String,
    val countryAcr: String,
    val country: PlayerCountry,
    val information: PlayerInformation
)
