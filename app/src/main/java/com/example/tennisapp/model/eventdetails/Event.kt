package com.example.tennisapp.model.eventdetails

data class Event(
    val tournament: Tournament,
    val season: Season,
    val homeTeam: Team,
    val awayTeam: Team,
    val homeScore: Score?,
    val awayScore: Score?,
    val startTimestamp: Long
)
