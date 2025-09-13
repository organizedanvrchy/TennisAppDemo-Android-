package com.example.tennisapp.model.eventdetails

data class SubTeam(
    val id: Int,
    val name: String,
    val slug: String? = null,
    val shortName: String? = null,
    val gender: String? = null,
    val country: EventCountry? = null,
    val sport: Sport? = null,
    val userCount: Int? = null,
    val nameCode: String? = null,
    val ranking: Int? = null,
    val national: Boolean? = null,
    val type: Int? = null,
    val subTeams: List<SubTeam> = emptyList(),
    val teamColors: TeamColors? = null,
    val fieldTranslations: FieldTranslations? = null
)