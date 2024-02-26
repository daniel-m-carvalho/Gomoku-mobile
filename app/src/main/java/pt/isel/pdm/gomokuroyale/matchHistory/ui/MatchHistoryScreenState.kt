package pt.isel.pdm.gomokuroyale.matchHistory.ui

import pt.isel.pdm.gomokuroyale.authentication.domain.UserInfo
import pt.isel.pdm.gomokuroyale.matchHistory.domain.MatchHistoryInfo

/**
 *  Represents the state of the match history screen.
 *  It can be in one of the following states:
 *  - [Idle]
 *  - [FetchingPlayerInfo]
 *  - [FetchedPlayerInfo]
 *  - [FailedToFetch]
 *  - [FetchingMatchHistory]
 *  - [FetchedMatchHistory]
 */

sealed interface MatchHistoryScreenState

data object Idle : MatchHistoryScreenState
data object FetchingPlayerInfo : MatchHistoryScreenState
data class FetchedPlayerInfo(val userInfo: UserInfo?) : MatchHistoryScreenState
data class FailedToFetch(val error: Throwable) : MatchHistoryScreenState
data object FetchingMatchHistory : MatchHistoryScreenState
data class FetchedMatchHistory(val matchHistory: List<MatchHistoryInfo>) : MatchHistoryScreenState