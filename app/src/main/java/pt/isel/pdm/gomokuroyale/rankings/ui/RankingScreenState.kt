package pt.isel.pdm.gomokuroyale.rankings.ui

import pt.isel.pdm.gomokuroyale.http.domain.users.RankingList
import pt.isel.pdm.gomokuroyale.http.domain.users.UserRanking
import pt.isel.pdm.gomokuroyale.rankings.ui.RankingScreenState.FailedToFetch
import pt.isel.pdm.gomokuroyale.rankings.ui.RankingScreenState.FetchedPlayerInfo
import pt.isel.pdm.gomokuroyale.rankings.ui.RankingScreenState.FetchedPlayersBySearch
import pt.isel.pdm.gomokuroyale.rankings.ui.RankingScreenState.FetchedRankingInfo
import pt.isel.pdm.gomokuroyale.rankings.ui.RankingScreenState.FetchingPlayerInfo
import pt.isel.pdm.gomokuroyale.rankings.ui.RankingScreenState.FetchingPlayersBySearch
import pt.isel.pdm.gomokuroyale.rankings.ui.RankingScreenState.FetchingRankingInfo
import pt.isel.pdm.gomokuroyale.rankings.ui.RankingScreenState.WantsToGoToMatchHistory

/**
 * Represents the state of the ranking screen.
 * It can be in one of the following states:
 * - [FetchingRankingInfo]
 * - [FetchedRankingInfo]
 * - [FetchingPlayerInfo]
 * - [FetchedPlayerInfo]
 * - [FetchingPlayersBySearch]
 * - [FetchedPlayersBySearch]
 * - [FailedToFetch]
 * - [WantsToGoToMatchHistory]
 */
sealed interface RankingScreenState {

    data object Idle : RankingScreenState
    data object FetchingRankingInfo : RankingScreenState
    data class FetchedRankingInfo(val rankingInfo: RankingList, val page: Int) : RankingScreenState
    data object FetchingPlayerInfo : RankingScreenState
    data class FetchedPlayerInfo(
        val playerInfo: UserRanking,
        val rankingInfo: RankingList,
        val page: Int
    ) : RankingScreenState

    data object FetchingPlayersBySearch : RankingScreenState
    data class FetchedPlayersBySearch(val players: RankingList, val page: Int) : RankingScreenState
    data class FailedToFetch(val error: Throwable) : RankingScreenState
    data class WantsToGoToMatchHistory(val id: Int, val username: String, val page: Int) :
        RankingScreenState
}