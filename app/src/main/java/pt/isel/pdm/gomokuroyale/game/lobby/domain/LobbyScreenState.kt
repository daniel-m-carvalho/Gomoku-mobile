package pt.isel.pdm.gomokuroyale.game.lobby.domain

import pt.isel.pdm.gomokuroyale.authentication.domain.UserInfo
import pt.isel.pdm.gomokuroyale.game.play.domain.variants.Variant

/**
 * Represents the state of the lobby screen.
 * It can be in one of the following states:
 * - [Idle]
 * - [FetchingPlayerInfo]
 * - [FetchedPlayerInfo]
 * - [FailedToFetch]
 * - [FetchingVariants]
 * - [FetchedVariants]
 * - [FetchingMatchInfo]
 * - [FetchedMatchInfo]
 */
sealed interface LobbyScreenState
data object Idle : LobbyScreenState
data object FetchingPlayerInfo : LobbyScreenState
data class FetchedPlayerInfo(val userInfo: UserInfo?) : LobbyScreenState
data class FailedToFetch(val error: Throwable) : LobbyScreenState
data class FetchingVariants(val userInfo: UserInfo) : LobbyScreenState
data class FetchedVariants(val variants: List<Variant>, val userInfo: UserInfo) : LobbyScreenState
data class FetchingMatchInfo(val userInfo: UserInfo) : LobbyScreenState
data class FetchedMatchInfo(val matchInfo: MatchInfo) : LobbyScreenState


