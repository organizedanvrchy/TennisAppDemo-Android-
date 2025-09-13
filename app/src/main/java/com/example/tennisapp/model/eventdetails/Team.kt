package com.example.tennisapp.model.eventdetails

data class Team(
    val id: Int,
    val name: String,
    val slug: String? = null,
    val shortName: String? = null,
    val gender: String? = null,
    val country: EventCountry? = null,
    val subTeams: List<SubTeam> = emptyList(),
    val teamColors: TeamColors? = null,
    val fieldTranslations: FieldTranslations? = null
)