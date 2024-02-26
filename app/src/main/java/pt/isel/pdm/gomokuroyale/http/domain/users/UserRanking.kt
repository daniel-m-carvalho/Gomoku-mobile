package pt.isel.pdm.gomokuroyale.http.domain.users

import pt.isel.pdm.gomokuroyale.http.domain.PaginationLinks

data class RankingList(
    val rankingTable: List<UserRanking>,
    val paginationLinks: PaginationLinks
)

data class UserRanking(
    val id: Int,
    val username: String,
    val gamesPlayed: Int,
    val wins: Int,
    val losses: Int,
    val draws: Int,
    val rank: Int,
    val points: Int
)