package com.example.tennisapp.model.eventdetails

data class Tournament(
    val name: String,
    val uniqueTournament: UniqueTournament?,
    val category: EventCategory?
)