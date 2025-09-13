package com.example.tennisapp.model.tournamentdetails

import com.example.tennisapp.model.playerrankings.PlayerModel
import com.google.gson.annotations.SerializedName

data class TournamentMatchDetails(
    val id: String,
    val date: String,
    val roundId: Int,
    val player1Id: Int,
    val player2Id: Int,
    val tournamentId: Int,
    @SerializedName("match_winner") val matchWinner: String,
    val result: String,
    val player1: PlayerModel,
    val player2: PlayerModel
)
