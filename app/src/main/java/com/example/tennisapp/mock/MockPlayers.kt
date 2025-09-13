package com.example.tennisapp.mock

import com.example.tennisapp.model.playerrankings.PlayerCountry
import com.example.tennisapp.model.playerrankings.PlayerModel
import com.example.tennisapp.model.playerrankings.PlayerRanking

object MockPlayers {
    val players: List<PlayerModel> = listOf(
        PlayerModel(101, "Novak Djokovic", PlayerCountry("Serbia", "SRB")),
        PlayerModel(102, "Carlos Alcaraz", PlayerCountry("Spain", "ESP")),
        PlayerModel(103, "Jannik Sinner", PlayerCountry("Italy", "ITA")),
        PlayerModel(104, "Daniil Medvedev", PlayerCountry("Russia", "RUS")),
        PlayerModel(105, "Alexander Zverev", PlayerCountry("Germany", "GER")),
        PlayerModel(106, "Stefanos Tsitsipas", PlayerCountry("Greece", "GRE")),
        PlayerModel(107, "Holger Rune", PlayerCountry("Denmark", "DEN")),
        PlayerModel(108, "Casper Ruud", PlayerCountry("Norway", "NOR"))
    )

    val rankings: List<PlayerRanking> =
        players.mapIndexed { idx, p -> PlayerRanking(point = 9000 - idx * 120, position = idx + 1, player = p) }
}
